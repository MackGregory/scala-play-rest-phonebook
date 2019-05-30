package services


import models.Phone
import repositories.DBQueriesRepo
import utils.PhoneNotFound

import scala.concurrent.{ExecutionContext, Future}
import scala.util.matching.Regex
import scala.util.{Failure, Success}

trait PhoneService {
  def getAllPhones: Future[List[Phone]]
  def addNewPhone(phone: Phone): Future[Phone]
  def deletePhone(number: String): Future[String]
  def updatePhoneData(number: String, phone: Phone): Future[Unit]
  def searchPhoneByName(name: String): Future[List[Phone]]
  def searchPhoneByNumber(number: String): Future[List[Phone]]
  def getPhone(number: String): Future[Phone]
}

class PhoneServiceImpl(dbQueriesRepo: DBQueriesRepo, cache: Cache)(implicit ec: ExecutionContext) extends PhoneService {
  private def validatePhoneNumber(number: String): Boolean = {
    val reg: Regex = """^([+])(\d)(\d{10})$""".r
    reg.pattern.matcher(number).matches
  }

  override def getAllPhones: Future[List[Phone]] = {
    dbQueriesRepo.all
  }

  override def addNewPhone(phone: Phone): Future[Phone] = {
    dbQueriesRepo.create(phone)
  }

  override def deletePhone(number: String): Future[String] = {
    val result = dbQueriesRepo.delete(number)
    cache.remove(number)
    result
  }

  override def getPhone(number: String): Future[Phone] =
    cache.get(number) match {
      case Some(phone) => Future.successful(phone)
      case None => dbQueriesRepo.getPhone(number).transform{
        case Success(Some(phone)) => Success(phone)
        case _ => Failure(new PhoneNotFound)
      }
    }

  override def updatePhoneData(number: String, phone: Phone): Future[Unit] = {
    for {
      _ <- dbQueriesRepo.getPhone(number)
      _ <- dbQueriesRepo.update(number, phone)
      _ = cache.remove(number)
    } yield ()
  }

  override def searchPhoneByName(name: String): Future[List[Phone]] = {
    dbQueriesRepo.searchByName(name)
  }

  override def searchPhoneByNumber(number: String): Future[List[Phone]] = {
    dbQueriesRepo.searchByNumber(number)
  }
}
