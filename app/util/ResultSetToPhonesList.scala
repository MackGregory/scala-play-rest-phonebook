package util

import java.sql.ResultSet

import models.Phone

object ResultSetToPhonesList {
  def resultSetToPhonesList(resultSet: ResultSet): List[Phone] = {
    val phones = new Iterator[String] {
      def hasNext: Boolean = resultSet.next()
      def next(): String = {
        val number = resultSet.getString("number")
        val name = resultSet.getString("name")
        s"$number $name"
      }
    }.toList.map(_.split(" ") match {
      case (s1, s2) => Phone(s1, s2)
    })
    phones
  }
}
