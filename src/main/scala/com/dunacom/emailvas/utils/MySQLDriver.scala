package com.dunacom.emailvas.utils

import java.sql.{Connection,DriverManager}
import com.typesafe.config.ConfigFactory
import concurrent.Future
import concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Promise

/**
 * @author olalekanelesin
 */
object MySQLDriver {
  
  val config = ConfigFactory.load()
  val mysql_config = config.getConfig("mysql")
  val uri = mysql_config.getString("url")
  val driver = mysql_config.getString("driver")
  val database = mysql_config.getString("database")
  val url = uri + database
  val username = mysql_config.getString("username")
  val password = mysql_config.getString("password")
  
  def connector(): Connection ={
    Class.forName(driver)
      val con: Connection = DriverManager.getConnection(url, username, password)
      return con
  }
  
  def fetch_data(sql: String){
    val statement = this.connector().createStatement()
    val dataSet = statement.executeQuery(sql)
      while(dataSet.next()){
        println(dataSet.getString("service_id"))
      }
  }
  
  def update_data(sql: String, old_id: String, user_id: String){
    val statement = this.connector().prepareStatement(sql)
    statement.setString(1, user_id)
    statement.setString(2, old_id)
    statement.executeUpdate()
    println(statement.getUpdateCount)
    statement.closeOnCompletion()
  }
}