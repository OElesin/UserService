package com.dunacom.emailvas.logic

import scala.collection.mutable.Map
import com.dunacom.emailvas.dao.UserDAO
import com.dunacom.emailvas.utils.APIresponse
import com.dunacom.emailvas.utils.Utility
import com.sksamuel.elastic4s.ElasticDsl._
import scala.collection.mutable.ArrayBuffer
import com.sksamuel.elastic4s.QueryDefinition
import com.roundeights.hasher.Algo

/**
 * @author olalekanelesin
 */

case class Email(name: String) { override def toString = s"'$name'"}
case class Username(name: String) {override def toString = s"'$name'"}
case class Password(name: String) {override def toString = Utility.hashUsing(Algo.sha256, name) }

class UserMgmtLOGIC extends UserDAO {
  
  private val userPrefix: String = "user_id_"
  private var response = new String
  private var responseArray: ArrayBuffer[Any] = new ArrayBuffer
  private val passwordHasher = Algo.sha256
  
  def createUserRecord(record: Map[String, Any]): String ={
    val key = userPrefix + record("email")
    val email = record("email")
    record("status") = 1
    record("passkey") = Utility.hashUsing(passwordHasher, record("password").toString)
    var check = this.checkUserKey(key)
    if(check == false){
      this.addeUserRecordRedis(key, record)
      this.addUser(key, record)
      response = APIresponse.successResponese(s"""User with: $email has been added""", "User created successfully")
    }else{
      var userData = Map("email" -> record("email"), "status" -> record("status"))
      this.updateUser(key, userData)
      response = APIresponse.errorResponse(s"""User with $email already exists on this platform""", 302)
    }
    return response
  }
  
  def updateUserRecord(record: Map[String, Any]): String ={
    val key = userPrefix + record("email")
    val email = record("email")
    var check = this.checkUserKey(key)
    if(check == false){
      this.updateUser(key, record)
      response = APIresponse.successResponese(s"""User with: $email has been updated""", "User record updated successfully")
    }else{
      response = APIresponse.errorResponse(s"""User with:  $email does not exists on this platform""", 302)
    }
    return response
  }
  
  def deleteUserRecord(email: Email): String ={
    val key = userPrefix + email
    var userData = Map("email" -> email, "status" -> 0)
    var check = this.checkUserKey(key)
    if(check == true){
      this.updateUser(key, userData)
      response = APIresponse.successResponese(s"""User with: $email has been deleted""", "User record deleted successfully")
    }else{
      response = APIresponse.errorResponse(s"""User with:  $email cannot be deleted because he/she does not exists on this platform""", 302)
    }
    return response
  }
  
  def getAllUsers(): String ={ 
    val conditions: Array[QueryDefinition] = Array(matchall)
    val payload = this.fetchUsers(conditions)
    payload.getHits.getHits.foreach { data => 
     responseArray += Utility.toScalaMap(data.getSource)   
    }
    response = APIresponse.bulkResponse(responseArray, "Data found")
    return response
  }
  
  def getUsersByRole(role: String): String ={
    val conditions: Array[QueryDefinition] = Array(matches("role", role))
    val payload = this.fetchUsers(conditions)
    payload.getHits.getHits.foreach { data => 
     responseArray += Utility.toScalaMap(data.getSource)   
    }
    response = APIresponse.bulkResponse(responseArray, "Data found")
    return response
  }
  
  def userLogin(username: Username, password: Password): String ={
    var responseData: ArrayBuffer[Any] = new ArrayBuffer
    val conditions: Array[QueryDefinition] = Array(matches("user_name", username), matches("passkey",  password))
    val payload = this.fetchUsers(conditions)
    if(payload.getHits.getTotalHits == 0){
      response = APIresponse.errorResponse("Username and password do not match. Please try again")
    }else {
      payload.getHits.getHits.foreach { 
      data => responseData += (Utility.toScalaMap(data.getSource)).-("password", "passkey")   
      }
      response = APIresponse.bulkResponse(responseData, "Data found")
    }

    return response
  }
  
  def getUsersByStatus(status: Int): String = {
    val conditions: Array[QueryDefinition] = Array(matches("status", status))
    val payload = this.fetchUsers(conditions)
    payload.getHits.getHits.foreach { data => 
     responseArray += Utility.toScalaMap(data.getSource)   
    }
    response = APIresponse.bulkResponse(responseArray, "Data found")
    return response
  }
  
}