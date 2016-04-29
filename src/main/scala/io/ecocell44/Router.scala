package io.ecocell44

import akka.actor.ActorRef
import akka.pattern._
import akka.http.scaladsl.model._

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport

import akka.util.Timeout

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

import akka.http.scaladsl.model.{ HttpEntity, HttpResponse }
import akka.http.scaladsl.model.MediaTypes.`text/html`

import akka.stream.scaladsl.Flow

import io.ecocell44.RetarderActor._

trait ActorProvider {
  val ref: ActorRef
}


class HeartbeatRoutes(ref: ActorRef, implicit val ec: ExecutionContext) extends SprayJsonSupport {
  
  implicit val _timeout : Timeout = Timeout(3 seconds)  
    
  val route =
    path("heartbeat") {
      get {
        complete {
          { (ref ? GetHeartBeat).mapTo[String] }.map { s =>  
              HttpResponse(entity = HttpEntity(ContentTypes.`text/html(UTF-8)`, s))
            }   
          }
        }
    } ~ 
    path("increase100ms") {
      post {
        complete {
          (ref ? Increase100).mapTo[String] 
        }
      }
    } ~ 
    path("decrease100ms") {
      post {
        complete{
          (ref ? Decrease100).mapTo[String] 
        }
      }
    }

}