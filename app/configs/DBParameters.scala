package configs

import java.sql.{Connection, DriverManager}

object DBParameters {
  val url = "jdbc:mysql://localhost:3306/phonebookdb?useUnicode=true&serverTimezone=UTC"
  val driver = "com.mysql.cj.jdbc.Driver"
  val username = "root"
  val password = "admin"
}
