package com.dunacom.emailvas.dao

import scala.collection.mutable.Map
import com.dunacom.emailvas.utils.RedisDB
import com.dunacom.emailvas.utils.Elastic
import org.elasticsearch.action.index.IndexResponse
import scala.concurrent.Future
import org.elasticsearch.action.update.UpdateResponse
import com.sksamuel.elastic4s.QueryDefinition
import org.elasticsearch.action.search.SearchResponse

/**
 * @author olalekanelesin
 */
class UserDAO {
  
  def addeUserRecordRedis(key: String, data: Map[String, Any], expiry_time: Int=0): Boolean ={
    return RedisDB.add_set(key, data, expiry_time)
  }
  
  def checkUserKey(key: String): Boolean = {
   return RedisDB.check_set(key) 
  }
  
  def addUser(id: String, params: Map[String, Any]): Future[IndexResponse] ={
    return Elastic.insert_document(id, params)
  }
  
  def updateUser(id: String, params: Map[String, Any]): Future[UpdateResponse] ={
    return Elastic.update_document(id, params)
  }
  
  def deleteUser(){
    
  }
  
  def fetchUsers(conditions: Array[QueryDefinition]): SearchResponse ={
    return Elastic.get_all(conditions)
  }
  
}