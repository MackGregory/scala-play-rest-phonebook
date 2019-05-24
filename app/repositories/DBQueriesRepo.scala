package repositories

import models.Phone
import java.sql.{Connection, DriverManager, ResultSet}
import configs.DBParameters._
import util.ResultSetToPhonesList._

object DBQueriesRepo {
  //============================
  val connection: Connection = {
    Class.forName(driver)
    DriverManager.getConnection(url, username, password)
  }

  //==================================================
  def all: Either[Exception, List[Phone]] = {
    try {
      val resultSet: ResultSet = connection.createStatement.executeQuery(
        "SELECT * FROM phone")
      val phonesList = resultSetToPhonesList(resultSet)
      Right(phonesList)
    }
    catch {
      case e: Exception => Left(e)
    }
    finally {
      connection.close()
    }
  }

  //====================================================================
  def create(number: String, name: String): Either[Exception, Phone] = {
    try {
      connection.createStatement.executeUpdate(
        s"INSERT INTO phonebookdb.phone (number, name) VALUES ('$number', '$name')")
      Right(Phone(number, name))
    }
    catch {
      case e: Exception => Left(e)
    }
    finally {
      connection.close()
    }
  }

  //===========================
  def delete(id: Int): Unit = {
    try {
      connection.createStatement.executeUpdate(
        s"DELETE FROM phonebookdb.phone WHERE (id = '$id')")
    }
    catch {
      case e: Exception => e.printStackTrace()
    }
    finally {
      connection.close()
    }
  }

  //=============================================================================
  def update(id: Int, number: String, name: String): Either[Exception, Phone] = {
    try {
      connection.createStatement.executeUpdate(
        s"UPDATE phonebookdb.phone SET number = '$number', name = '$name' WHERE (id = '$id')")
      Right(Phone(number, name))
    }
    catch {
      case e: Exception => Left(e)
    }
    finally {
      connection.close()
    }
  }

  //==========================================================
  def searchByNumber(number: String): Either[Exception, List[Phone]] = {
    try {
      val resultSet: ResultSet = connection.createStatement.executeQuery(
        s"SELECT * FROM phone WHERE number LIKE '%$number%'")
      val phonesList = resultSetToPhonesList(resultSet)
      Right(phonesList)
    }
    catch {
      case e: Exception => Left(e)
    }
    finally {
      connection.close()
    }
  }

  //======================================================
  def searchByName(name: String): Either[Exception, List[Phone]] = {
    try {
      val resultSet: ResultSet = connection.createStatement.executeQuery(
        s"SELECT * FROM phone WHERE name LIKE '%$name%'")
      val phonesList = resultSetToPhonesList(resultSet)
      Right(phonesList)
    }
    catch {
      case e: Exception => Left(e)
    }
    finally {
      connection.close()
    }
  }
}
