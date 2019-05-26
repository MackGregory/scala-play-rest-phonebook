package services


import models.Phone
import repositories.DBQueriesRepo

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

trait PhoneService {
  def getAllPhones: Future[List[Phone]]
  def addNewPhone(phone: Phone): Future[Phone]
  def deletePhone(id: Int): Future[Unit]
  def updatePhoneData(id: Int, phone: Phone): Future[Phone]
  def searchPhoneByName(name: String): Future[List[Phone]]
  def searchPhoneByNumber(number: String): Future[List[Phone]]
}

class PhoneServiceImpl(dbQueriesRepo: DBQueriesRepo)(implicit ec: ExecutionContext) extends PhoneService {
  override def getAllPhones: Future[List[Phone]] = {
    val result = dbQueriesRepo.all
    result
  }

  override def addNewPhone(phone: Phone): Future[Phone] = {
    dbQueriesRepo.create(phone)
  }

  override def deletePhone(id: Int): Future[Unit] = {
    dbQueriesRepo.delete(id)
  }

  override def updatePhoneData(id: Int, phone: Phone): Future[Phone] = {
    dbQueriesRepo.update(id, phone)
  }

  override def searchPhoneByName(name: String): Future[List[Phone]] = {
    dbQueriesRepo.searchByName(name)
  }

  override def searchPhoneByNumber(number: String): Future[List[Phone]] = {
    dbQueriesRepo.searchByNumber(number)
  }
}
