package repositories

import models.Phone
import java.sql.{Connection, DriverManager, ResultSet}

import configs.DBParameters._

import scala.concurrent.{ExecutionContext, Future}

trait DBQueriesRepo {
  def all: Future[List[Phone]]

  def create(phone: Phone): Future[Phone]

  def delete(id: Int): Future[Unit]

  def update(id: Int, phone: Phone): Future[Phone]

  def searchByNumber(number: String): Future[List[Phone]]

  def searchByName(name: String): Future[List[Phone]]
}

class DBQueriesRepoImpl(implicit ec: ExecutionContext) extends DBQueriesRepo {
  //============================
  val connection: Connection = {
    Class.forName(driver)
    DriverManager.getConnection(url, username, password)
  }

  //==============================================
  override def all: Future[List[Phone]] = Future {
    val resultSet: ResultSet = connection.createStatement.executeQuery(
      "SELECT * FROM phone")
    val result = resultSetToPhonesList(resultSet)
    connection.close()
    Thread.sleep(100)
    result
  }

  //=========================================================
  override def create(phone: Phone): Future[Phone] = Future {
    connection.createStatement.executeUpdate(
      s"INSERT INTO phonebookdb.phone (number, name) VALUES ('${phone.number}', '${phone.name}')")
    //TODO: FIX SQL QUERIES (prepared statement)
    connection.close()
    Thread.sleep(100)
    phone
  }

  //===================================================
  override def delete(id: Int): Future[Unit] = Future {
    connection.createStatement.executeUpdate(
      s"DELETE FROM phonebookdb.phone WHERE (id = '$id')")
    connection.close()
  }

  //==================================================================
  override def update(id: Int, phone: Phone): Future[Phone] = Future {
    connection.createStatement.executeUpdate(
      s"UPDATE phonebookdb.phone SET number = '${phone.number}', name = '${phone.name}' WHERE (id = '$id')")
    connection.close()
    phone
  }

  //=========================================================================
  override def searchByNumber(number: String): Future[List[Phone]] = Future {
    val resultSet: ResultSet = connection.createStatement.executeQuery(
      s"SELECT * FROM phone WHERE number LIKE '%$number%'")
    val result = resultSetToPhonesList(resultSet)
    connection.close()
    result
  }

  //=====================================================================
  override def searchByName(name: String): Future[List[Phone]] = Future {
    val resultSet: ResultSet = connection.createStatement.executeQuery(
      s"SELECT * FROM phone WHERE name LIKE '%$name%'")
    val result = resultSetToPhonesList(resultSet)
    connection.close()
    result
  }

  //======================================================================
  private def resultSetToPhonesList(resultSet: ResultSet): List[Phone] = {
    new Iterator[Phone] {
      def hasNext: Boolean = resultSet.next()

      def next(): Phone = {
        val number = resultSet.getString("number")
        val name = resultSet.getString("name")
        Phone(number, name)
      }
    }.toList
  }
}
