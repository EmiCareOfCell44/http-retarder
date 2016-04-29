package io.ecocell44

import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.actor.{ ActorRef, ActorSystem, Props}
import akka.stream.ActorMaterializer

import scala.util.{ Failure, Success }
import scala.concurrent.{ Await, Future }

class Main {
  
  implicit val system = ActorSystem("settlements")
  implicit val flowMaterializer = ActorMaterializer()
  
  val ref = system.actorOf(Props[RetarderActor])
  val prov = new HeartbeatRoutes(ref, system.dispatcher)
  
  def startServer(): (Future[ServerBinding], ActorSystem) = {
    val service = (Http().bindAndHandle(prov.route, "0.0.0.0", 9091))
    (service, system)
  }
  
}

object Main extends App {
  
  val (service, system) = new Main().startServer() 
  implicit val ec = system.dispatcher
  
  service.onComplete {
    case Success(binding) => { 
      sys.addShutdownHook {
        binding.unbind()
        shutdown()
      }
    }
    case Failure(e) => {
      sys.addShutdownHook {
        shutdown()
      }
    }
  }
  
  private def shutdown(): Unit = {
    println("\nShutting down Akka...")
    val timeout = system.settings.CreationTimeout
    system.terminate()
    Await.ready(system.whenTerminated, timeout.duration)
    println("Successfully shut down Akka")
  }
  
}
  