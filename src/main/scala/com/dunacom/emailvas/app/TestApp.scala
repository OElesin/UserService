package com.dunacom.emailvas.app

import com.dunacom.emailvas.utils.RedisDB
import scalaj.http.HttpResponse
import scalaj.http.Http
import scala.collection.mutable.ArrayBuffer
import concurrent.Future
import concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Promise
import com.dunacom.emailvas.utils.MySQLDriver
import scala.util.Success
import scala.util.Failure
import scala.util.Success
import scala.util.Failure
import com.dunacom.emailvas.utils.Utility
import org.json4s.DefaultFormats
import org.json4s.jackson.Json

/**
 * @author olalekanelesin
 */
object TestApp extends App {
  
//  val keys = RedisDB.get_keys("user_match:*")
  
  val client = RedisDB.client
  
  def get_keys(pattern: String): Any ={
    var keys :ArrayBuffer[Any] = new ArrayBuffer
    val data = client.keys(pattern).get
    data.foreach { 
      x =>  
       val userinfo = get_all(x.get) 
       val oldid = userinfo("user_id")
       if(userinfo.contains("user_id")){
         val userdata = this.fetch_msisdn(userinfo("msisdn"))
         userdata.onComplete { 
           case Success(data) => {
            val temp = ((Json(DefaultFormats).parse(data)).values).asInstanceOf[Map[String, String]]
            MySQLDriver.update_data("UPDATE cms_subscription SET new_userid = ? where user_id = ?", temp("response"), oldid)
            println(userinfo)
           }
           case Failure(error) => println("notice :" + error)
         }
       }
     }
  }
  
  def get_all(key: String): scala.collection.immutable.Map[String, String] ={
    return client.hgetall(key).get
  }
  
  def fetch_msisdn(msisdn: String): Future[String] ={
    val p = Promise[String]()
    Future {
      Thread.sleep(1000)
       val getResponse: HttpResponse[String] = Http("http://localhost/etisalat-core/manual-get-user-identity")
                                              .param("msisdn", msisdn)
                                              .asString
        p.success(getResponse.body)
    } 
    p.future
  }
  
//  val data = MySQLDriver.fetch_data("SELECT * FROM cms_subscription WHERE user_id = 'f8UThcgPAWdX5GRVcNM2' limit 5")
//  MySQLDriver.update_data("ALTER TABLE cms_subscription ADD COLUMN new_userid VARCHAR(50)")
  
  this.get_keys("user_match:*") 
}