package com.dunacom.emailvas.utils

import org.elasticsearch.action.delete.DeleteResponse
import scala.concurrent.Future
import com.sksamuel.elastic4s.mappings.FieldType._
import org.elasticsearch.action.update.UpdateResponse
import org.elasticsearch.action.index.IndexResponse
import com.sksamuel.elastic4s.ElasticDsl._
import com.sksamuel.elastic4s.QueryDefinition
import org.elasticsearch.action.search.SearchResponse
import org.elasticsearch.action.get.GetResponse
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse
import org.elasticsearch.common.settings.ImmutableSettings
import com.sksamuel.elastic4s.ElasticClient
import com.sksamuel.elastic4s.ElasticsearchClientUri
import com.sksamuel.elastic4s.FuzzyQueryDefinition
import com.sksamuel.elastic4s.BoolQueryDefinition

/**
 * @author olalekanelesin
 */

object Elastic  {
  
  val uri = ElasticsearchClientUri("elasticsearch://localhost:9300")
  val settings = ImmutableSettings.settingsBuilder().put("cluster.name", "elasticsearch").build()
  val client = ElasticClient.remote(settings, uri)
    val elasticIndex = "users"
    val elasticType = "userdata"
  
  def add_mappings(params: Map[String, Object]) {
    
  }
  
  def create_index(params: Map[String, Any]): CreateIndexResponse = {
    return client.execute {
      create index elasticIndex mappings (
        elasticType as (
          params.keys.toString typed StringType
        )
      )
    }.await
  }
  
  def get_document(docid: String) : GetResponse ={
    val data = client.execute {
            get id docid from elasticIndex/elasticType
       }.await
    return data
  }
  
  def return_document(docid: String): Future[GetResponse] = {
    val data = client.execute {
            get id docid from elasticIndex/elasticType
       }
    return data
  }
  
  def get_all(params: Array[QueryDefinition]): SearchResponse = {
   return client.execute {
      search in elasticIndex / elasticType query {
        bool {
          must(
            params  
          )
        }
      }
    }.await
  }
  
  def insert_document(id: String, params: scala.collection.mutable.Map[String, Any]): Future[IndexResponse] = {
    //note plain old objects used
    return client.execute {
      index into elasticIndex/elasticType id id fields (params) 
    }
  }
  
  def delete_document(id: String) : DeleteResponse = {
    return client.execute {
       delete id id from elasticIndex / elasticType
    }.await
  }
  
  def update_document(docid: String, params: scala.collection.mutable.Map[String, Any]): Future[UpdateResponse] = {
    return client.execute {
      update(docid).in(elasticIndex/ elasticType).doc(params)
    }
  }
}