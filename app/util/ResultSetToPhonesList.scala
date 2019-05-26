package util

import java.sql.ResultSet
import models.Phone

object ResultSetToPhonesList {
  def resultSetToPhonesList(resultSet: ResultSet): List[Phone] = {
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