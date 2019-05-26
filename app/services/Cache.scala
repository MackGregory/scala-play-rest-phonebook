package services

import models.Phone

import scala.collection.mutable

trait Cache{
  def add(phone: Phone): Unit
  def add(phones: List[Phone]): Unit
  def isCached(phone: Phone): Boolean
}

class CacheImpl extends Cache{
  val cache: mutable.Map[String, Phone] = mutable.Map.empty

  override def add(phone: Phone): Unit = {
    cache += (phone.number -> phone)
  }

  override def add(phones: List[Phone]): Unit = {
    phones.foreach(add)
  }

  override def isCached(phone: Phone): Boolean = {
    cache.contains(phone.number)
  }
}
