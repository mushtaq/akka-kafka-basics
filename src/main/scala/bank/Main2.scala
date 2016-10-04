package bank

import akka.actor.{ActorIdentity, ActorRef, ActorSystem, Identify, Props}
import akka.pattern.ask
import akka.util.Timeout
import bank.AccountActor.{Balance, GetBalance}
import com.typesafe.config.ConfigFactory

import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}

object Main2 extends App {
  val config = ConfigFactory.load("application2.conf")
  val system = ActorSystem("demo", config)
  val account = Account("ac123", Customer("cust1", "mushtaq"), 0)
  implicit val timeout = Timeout(1.second)
}
