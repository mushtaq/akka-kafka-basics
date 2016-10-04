package bank

import akka.actor.{ActorIdentity, ActorRef, ActorSelection, ActorSystem, Identify, Props}
import akka.util.Timeout
import bank.AccountActor.{Balance, GetBalance}
import org.scalatest._

import scala.concurrent.{Await, Future}
import akka.pattern.ask

import scala.concurrent.duration.DurationLong

class Actor5Spec extends FlatSpec with Matchers {

  val system = ActorSystem("demo")
  val account = Account("ac123", Customer("cust1", "mushtaq"), 0)
  import system.dispatcher
  implicit val timeout = Timeout(1.second)

  "Hello" should "have tests" in {

    val actorRef: ActorRef = system.actorOf(Props(new AccountActor(account)), "account-manager-1")
    def future(): Future[Balance] = (actorRef ? GetBalance).mapTo[Balance]

    println(Await.result(future(), 1.second))

    println(actorRef)

    actorRef ! "error"

    Thread.sleep(1000)

    val future1 = (system.actorSelection("/user/account-manager-1") ? Identify()).mapTo[ActorIdentity]

    println(Await.result(future1, 1.second))

    system.terminate()
  }
}
