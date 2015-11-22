package com.dunacom.emailvas.utils

import org.json4s.native.Json
import org.json4s.DefaultFormats

/**
 * @author olalekanelesin
 */
object APIresponse {
  
  implicit val formats = org.json4s.DefaultFormats
  
  def successResponese(data: Any, message: String, code: Int = 200): String = {
    val payload = List(data)
    val response = Map("error_code" -> code, "error_status" -> false, "data" -> payload, "message" -> message)
    return (Json(DefaultFormats).write(response))
  }
  
  def errorResponse(message: String, code: Int = 200) : String = {
    val response = Map("error_code" -> code, "message" -> message, "error_status" -> true)
    return (Json(DefaultFormats).write(response))
  }
  
   def bulkResponse(data: Any, message: String, code: Int = 200): String = {
    val response = Map("error_code" -> code, "error_status" -> false, "data" -> data, "message" -> message)
    return (Json(DefaultFormats).write(response))
  }
}