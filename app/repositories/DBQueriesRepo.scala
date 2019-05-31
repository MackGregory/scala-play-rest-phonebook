package repositories

import java.sql.{Connection, DriverManager, ResultSet}

import configs.DBParameters._
import models.Phone

import scala.concurrent.{ExecutionContext, Future}

trait DBQueriesRepo {
  def all: Future[List[Phone]]

  def create(phone: Phone): Future[Phone]

  def delete(number: String): Future[String]

  def update(number: String, phone: Phone): Future[Phone]

  def searchByNumber(number: String): Future[List[Phone]]

  def searchByName(name: String): Future[List[Phone]]

  def getPhone(number: String): Future[Option[Phone]]
}

class DBQueriesRepoImpl(implicit ec: ExecutionContext) extends DBQueriesRepo {
  def getConnection: Connection = {
    Class.forName(driver)
    DriverManager.getConnection(url, username, password)
  }

  override def all: Future[List[Phone]] = Future {
    val connection = getConnection
    val resultSet: ResultSet = connection.createStatement.executeQuery(
      "SELECT * FROM phone")
    val phones = resultSetToPhonesList(resultSet)
    connection.close()
    phones
  }

  override def getPhone(number: String): Future[Option[Phone]] = Future{
    val connection = getConnection
    val resultSet: ResultSet = connection.createStatement.executeQuery(
      s"SELECT * FROM phone WHERE number = '$number'")
    val phones = resultSetToPhonesList(resultSet)
    connection.close()
    phones.headOption
  }

  override def create(phone: Phone): Future[Phone] = Future {
    val connection = getConnection
    connection.createStatement.executeUpdate(
      s"INSERT INTO phone (number, name) VALUES ('${phone.number}', '${phone.name}')")
    connection.close()
    phone
  }

  override def delete(number: String): Future[String] = Future {
    val connection = getConnection
    connection.createStatement.executeUpdate(
      s"DELETE FROM phone WHERE (number = '$number')")
    connection.close()
    s"Phone number $number deleted"
  }

  override def update(number: String, phone: Phone): Future[Phone] = Future {
    val connection = getConnection
    connection.createStatement.executeUpdate(
      s"UPDATE phone SET number = '${phone.number}', name = '${phone.name}' WHERE (number = '$number')")
    connection.close()
    phone
  }

  override def searchByNumber(number: String): Future[List[Phone]] = Future {
    val connection = getConnection
    val resultSet: ResultSet = connection.createStatement.executeQuery(
      s"SELECT * FROM phone WHERE number LIKE '%$number%'")
    val phones = resultSetToPhonesList(resultSet)
    connection.close()
    phones
  }

  override def searchByName(name: String): Future[List[Phone]] = Future {
    val connection = getConnection
    val resultSet: ResultSet = connection.createStatement.executeQuery(
      s"SELECT * FROM phone WHERE name LIKE '%$name%'")
    val phones = resultSetToPhonesList(resultSet)
    connection.close()
    phones
  }

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
