package repositories

import DBQueriesRepo._

object PhoneRepo {
  def getAllPhones = {
    all
  }

  def addNewPhone(number: String, name: String) = {
    create(number, name)
  }

  def deletePhone(id: Int) = {
    delete(id)
  }

  def updatePhoneData(id: Int, number: String, name: String) = {
    update(id, number, name)
  }

  def searchPhoneByName(name: String) = {

  }
}