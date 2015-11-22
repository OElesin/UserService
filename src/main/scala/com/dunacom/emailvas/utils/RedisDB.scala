package com.dunacom.emailvas.utils

import scala.collection.mutable.Map
import scala.collection.mutable.HashMap
import com.redis.RedisClient
import com.typesafe.config.ConfigFactory
import scala.collection.mutable.ArrayBuffer

/**
 * @author olalekanelesin, fatolutoye
 */
object RedisDB {
 
  val config = ConfigFactory.load()
  val redis = config.getConfig("redis") 
  val redisHost = redis.getString("host")
  val redisPort = redis.getInt("port")
  val redisDB = redis.getInt("db")
  
  val client = new RedisClient("176.34.130.167", redisPort, redisDB)
  
  def check_set(key: String): Boolean = {
    return client.exists(key)
  }
  
  def get_keys(pattern: String): Any ={
    var keys :ArrayBuffer[Any] = new ArrayBuffer
    val data = client.keys(pattern).get
    data.foreach { 
      x =>  println(get_all(x.get)) }
//    println(get_all(x.get))
  }
  
  def get_all(key: String): scala.collection.immutable.Map[String, String] ={
    return client.hgetall(key).get
  }
  
  def check_set_fields(key: Any, field: Any): Boolean = {
    return client.hexists(key, field)
  }
  
  def add_set(key: String,data: Map[String, Any], expiry_time: Int=0): Boolean = {
    var status: Boolean = true
    if(expiry_time != 0){
      status = client.hmset(key, data)
               client.expire(key, expiry_time)
    }else{
      status = client.hmset(key, data)
    }    
    return status
  }
  
  def get_set(key: String, field: String) : Any = {
    if(!field.isEmpty()){
       return client.hmget(key, field)
    } else {
       return  client.hgetall(key)
    }
  }
  
  def rename_set(oldkey: String, newkey: String) : Boolean = {
    return client.rename(oldkey, newkey)
  }
  
  def delete_set(key: String) : Any = {
    return client.del(key, "")
  }
}