package io.ecocell44

import akka.actor.Actor
import akka.pattern.after
import scala.concurrent.Future
import scala.concurrent.duration._

object RetarderActor {
  case object GetHeartBeat
  case object Increase100
  case object Decrease100
}

class RetarderActor extends Actor {

  import RetarderActor._
  implicit val ec = context.dispatcher
  
  private var waitFor : Option[Int] = None
  
  private def html(amount: Int) : String = s"""<html><center><blockquote>
<pre><small>

   _  _   _ _____ ___      ___  ___   _   _    ___ 
  /_\\| | | |_   _/ _ \\ ___/ __|/ __| /_\\ | |  | __|
 / _ \\ |_| | | || (_) |___\\__ \\ (__ / _ \\| |__| _| 
/_/ \\_\\___/  |_| \\___/    |___/\\___/_/ \\_\\____|___|
                                                   
</small><br>

<center><font face="Comic sans MS" size="10">I have a delay of
<br>
$amount ms
<center>

</pre></blockquote></center><html>"""
  
  def receive: Receive =  {
    case GetHeartBeat => {
      waitFor match {
        case None => sender ! html(0)
        case Some(ms) => {
          val origin = sender
          after[Unit](new FiniteDuration(waitFor.get, java.util.concurrent.TimeUnit.MILLISECONDS), context.system.scheduler) (Future[Unit] { origin ! html(waitFor.get) }) 
        }
      }
    }
    
    case Increase100 => {
      waitFor match {
        case None => waitFor = Some(100)
        case _ => waitFor = waitFor map (_ + 100) 
      }
      sender ! "OK"
    }
    
    case Decrease100 => {
      waitFor match {
        case None =>
        case _ => waitFor = waitFor map { a =>  if(a > 0) (a - 100) else a }  
      }
      sender ! "OK"
    }
  }
  
}