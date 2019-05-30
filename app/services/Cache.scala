package services

import models.Phone

import scala.collection.mutable

trait Cache{
  def add(phone: Phone): Unit
  def add(phones: List[Phone]): Unit
  def isCached(phone: Phone): Boolean
  def remove(number: String): Unit
  def get(number: String): Option[Phone]
}

class CacheImpl extends Cache{
  @volatile private var cache: mutable.Map[String, Phone] = mutable.Map.empty

  override def add(phone: Phone): Unit =
    cache += (phone.number -> phone)

  override def add(phones: List[Phone]): Unit =
    phones.foreach(add)

  override def isCached(phone: Phone): Boolean =
    cache.contains(phone.number)

  override def remove(number: String): Unit =
    cache.remove(number)

  override def get(number: String): Option[Phone] =
    cache.get(number)
}
