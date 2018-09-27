package model2

import Tables.EventRow
import scala.reflect.{ClassTag, classTag}

//case class WithID(id:Int)

class DBBuffer[T <: WithID:ClassTag] {

  private val buff = collection.mutable.ArrayBuffer[T]()

  def updateBuffer(items:Array[T]) = {
    buff ++= items
  }

  def items:Array[T] = {
    buff.toArray
  }

  def getEvent(id:Int):T = {
    items.filter(_.id == id).head
  }

}