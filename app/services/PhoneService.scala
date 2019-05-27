package services


import models.Phone
import repositories.DBQueriesRepo

import scala.concurrent.{ExecutionContext, Future}
import scala.util.matching.Regex


trait PhoneService {
  def getAllPhones: Future[List[Phone]]
  def addNewPhone(phone: Phone): Future[Phone]
  def deletePhone(number: String): Future[String]
  def updatePhoneData(number: String, phone: Phone): Future[Phone]
  def searchPhoneByName(name: String): Future[List[Phone]]
  def searchPhoneByNumber(number: String): Future[List[Phone]]
}

class PhoneServiceImpl(dbQueriesRepo: DBQueriesRepo, cacheImpl: CacheImpl)(implicit ec: ExecutionContext) extends PhoneService {
  private def validatePhoneNumber(number: String): Boolean = {
    val reg: Regex = """^([+])(\d)(\d{10})$""".r
    reg.pattern.matcher(number).matches
  }

  override def getAllPhones: Future[List[Phone]] = {
    val result = dbQueriesRepo.all
    cacheImpl.cache.clear
    result.foreach(cacheImpl.add)
    result
  }

  override def addNewPhone(phone: Phone): Future[Phone] = {
    val result = dbQueriesRepo.create(phone)
    result.foreach(cacheImpl.add)
    result
  }

  override def deletePhone(number: String): Future[String] = {
    val result = dbQueriesRepo.delete(number)
    cacheImpl.cache.remove(number)
    result
  }

  override def updatePhoneData(number: String, phone: Phone): Future[Phone] = {
    val result = dbQueriesRepo.update(number, phone)
    result.foreach(ph => {
      cacheImpl.cache.remove(number)
      cacheImpl.add(ph)
    })
    result
  }

  override def searchPhoneByName(name: String): Future[List[Phone]] = {
    if(cacheImpl.cache.exists(_._2.name.contains(name))){
      Future(cacheImpl.cache.filter(_._2.name.contains(name)).values.toList)
    }
    else
      dbQueriesRepo.searchByName(name)
  }

  override def searchPhoneByNumber(number: String): Future[List[Phone]] = {
    if(cacheImpl.cache.contains(number)){
      Future(cacheImpl.cache.filterKeys(_.contains(number)).values.toList)
    }
    else
      dbQueriesRepo.searchByNumber(number)
  }
}
