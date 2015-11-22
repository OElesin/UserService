package com.dunacom.emailvas.utils

import collection.JavaConversions._
import scala.collection.immutable.Map
import com.roundeights.hasher.Algo

/**
 * @author olalekanelesin
 */
object Utility {
  
  def toScalaMap(javaMap: java.util.Map[String, Object]): Map[String, String] = {
    return javaMap.toMap.asInstanceOf[Map[String, String]]
  }
  
  def hashUsing(algo: Algo, value: String): String = {
    val hashed = algo(value)
    return hashed.hex
  }
}