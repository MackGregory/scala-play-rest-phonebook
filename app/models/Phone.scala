package models

import java.sql.{Connection, DriverManager}

case class Phone(number: String, name: String)

object Phone {
  //Connection parameters
  val url = "jdbc:mysql://localhost:3306/phonebookdb?useUnicode=true&serverTimezone=UTC"
  val driver = "com.mysql.cj.jdbc.Driver"
  val username = "root"
  val password = "admin"
  var connection: Connection = _

  def apply(number: String, name: String): Phone = new Phone(number, name)

  //Get all phones from DB
  def all: List[Phone] = {
    var phones: List[Phone] = Nil
    try {
      Class.forName(driver)
      connection = DriverManager.getConnection(url, username, password)
      val statement = connection.createStatement()
      val resultSet = statement.executeQuery("SELECT * FROM phone")
      while (resultSet.next) {
        val id = resultSet.getInt("id")
        val number = resultSet.getString("number")
        val name = resultSet.getString("name")
        phones = phones :+ Phone(number, name)
      }
    }
    catch {
      case e: Exception => e.printStackTrace()
    }
    connection.close()
    phones
  }

  //Add phone to DB
  def create(number: String, name: String): Unit = {
    try {
      Class.forName(driver)
      connection = DriverManager.getConnection(url, username, password)
      connection.createStatement.executeUpdate(
        "INSERT INTO phonebookdb.phone (number, name) " +
          s"VALUES ('$number', '$name')")
    }
    catch {
      case e: Exception => e.printStackTrace()
    }
    finally {
      connection.close()
    }
  }

  //Delete phone from DB
  def delete(id: Int): Unit = {
    try {
      Class.forName(driver)
      connection = DriverManager.getConnection(url, username, password)
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

  //Update phone data in DB
  def update(id: Int, number: String, name: String): Unit = {
    try {
      Class.forName(driver)
      connection = DriverManager.getConnection(url, username, password)
      connection.createStatement.executeUpdate(
        s"UPDATE phonebookdb.phone SET number = '$number', name = '$name' WHERE (id = '$id')")
    }
    catch {
      case e: Exception => e.printStackTrace()
    }
    finally {
      connection.close()
    }
  }

}
