package bank

import akka.actor.{ActorIdentity, ActorRef, ActorSystem, Identify, Props}
import akka.cluster.Cluster
import akka.pattern.ask
import akka.util.Timeout
import bank.AccountActor.{Balance, GetBalance}

import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}

object Main1 extends App {
  val system = ActorSystem("demo")
  val account = Account("ac123", Customer("cust1", "mushtaq"), 0)
  implicit val timeout = Timeout(1.second)
  import system.dispatcher

  val cluster = Cluster(system)

  system.scheduler.schedule(1.second, 1.second) {
    println(cluster.state.members)
  }
}
