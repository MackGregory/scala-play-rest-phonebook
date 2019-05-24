package repositories

import models.Phone

object Cache{
  @volatile val cache: scala.collection.mutable.Map[String, Phone] = scala.collection.mutable.Map.empty

  def add(phones: List[Phone]) = {
    phones.foreach(p => cache += (p.number -> p))
  }

  def add(phone: Phone) = {
    cache += (phone.number -> phone)
  }

  def isCached(phone: Phone): Boolean = {
    cache.contains(phone.number)
  }
}
