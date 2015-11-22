package com.dunacom.emailvas.app

import akka.actor.Actor
import spray.routing._
import spray.http._
import MediaTypes._
import spray.routing.directives.LogEntry
import akka.event.Logging.InfoLevel
import com.dunacom.emailvas.utils.APIresponse
import com.dunacom.emailvas.logic.UserMgmtLOGIC
import scala.collection.mutable.Map
import spray.http.HttpHeaders.RawHeader
import com.dunacom.emailvas.logic.Username
import com.dunacom.emailvas.logic.Username
import com.dunacom.emailvas.logic.Username
import com.dunacom.emailvas.logic.Username
import com.dunacom.emailvas.logic.Password
import com.dunacom.emailvas.logic.Email

class MyServiceActor extends Actor with MyService {
  def actorRefFactory = context
  def requestMethodAndResponseStatusAsInfo(req: HttpRequest): Any => Option[LogEntry] = {
    case res: HttpResponse => Some(LogEntry(req.method + ":" + req.uri + ":" + res.message.status, InfoLevel))
    case _ => None // other kind of responses
  }
  def routeWithLogging = logRequestResponse(requestMethodAndResponseStatusAsInfo _)(myRoute ~ testRoute)
  def receive = runRoute(routeWithLogging)
}

trait MyService extends HttpService {
    
  implicit val rejectHandler = RejectionHandler {
    case MissingQueryParamRejection(paramName) :: _ => ctx 
      => ctx.complete(APIresponse.errorResponse(s"Parameter ' $paramName ' is missing", 404)) 
    case MissingFormFieldRejection(paramName) :: _ => ctx 
      => ctx.complete(APIresponse.errorResponse(s"Parameter ' $paramName ' is missing", 404))
  }
  
  val AccessControlAllowAll = HttpHeaders.RawHeader(
    "Access-Control-Allow-Origin", "*"
  )
  val AccessControlAllowHeadersAll = HttpHeaders.RawHeader(
    "Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Content-Encoding"
  )
  
  val userLogic = new UserMgmtLOGIC
  var response = new String
  
  val myRoute =
    path("api" / "user-service") {
      get {
        ctx => ctx.complete(APIresponse.successResponese(null, "Welcome to User Management Service"))
      }
    } ~ path("api" / "user-service" / "create-user") {
          respondWithHeaders(AccessControlAllowAll, AccessControlAllowHeadersAll){
           options {
             ctx => ctx.complete(StatusCodes.OK)
             } ~ post {
                formFields('email, 'user_name, 'password) {(email, user_name, password) =>
                  if(user_name.isEmpty()){
                   ctx => ctx.complete(APIresponse.errorResponse("Username field is empty"))
                  }else if(password.isEmpty()){
                    ctx => ctx.complete(APIresponse.errorResponse("Password field is empty"))
                  }else{
                    val userData = Map("email" -> email, "user_name" -> user_name, "password" -> password)
                    var payload = userData.asInstanceOf[Map[String, Any]]
                    response = userLogic.createUserRecord(payload)
                    ctx => ctx.complete(response)
                  }
               }
            }
          }
    } ~ path("api" / "user-service" / "delete-user") {
      get {
        parameter('email){(email) =>
          val pEmail = Email.apply(email)
          response = userLogic.deleteUserRecord(pEmail)
          ctx => ctx.complete(response)
        }
      }
    } ~ path("api" / "user-service" / "all-users") {
      get {
        response = userLogic.getAllUsers()
        ctx => ctx.complete(response) 
      }
    } ~ path("api" / "user-service" / "login") {
         respondWithHeaders(AccessControlAllowAll, AccessControlAllowHeadersAll){
           options{
             ctx => ctx.complete(StatusCodes.OK)
           } ~ post {
              formFields ('username, 'password) {(username, password) => 
                if(username.isEmpty()){
                   ctx => ctx.complete(APIresponse.errorResponse("Email field is empty"))
                  }else if(password.isEmpty()){
                    ctx => ctx.complete(APIresponse.errorResponse("Password field is empty"))
                  }else{
                    val pUname = Username.apply(username)
                    val pPassword = Password.apply(password)
                    response = userLogic.userLogin(pUname, pPassword)
                    ctx => ctx.complete(response) 
                  }
                }
              }
           }
         } 

    val testRoute = 
      path("test") {
        get {
          respondWithMediaType(`application/json`) {
            complete {
            <html>
              <body>
                <h1>Say hello to <i>spray-routing</i> on <i>spray-can</i>!</h1>
              </body>
            </html>

            }
          }
        }
      }
}