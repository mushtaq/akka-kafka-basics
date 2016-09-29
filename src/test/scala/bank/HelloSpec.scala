package bank

import akka.actor.{ActorRef, ActorSystem, Props}
import bank.AccountActor.{Deposit, GetBalance}
import org.scalatest._

class HelloSpec extends FlatSpec with Matchers {

  val system = ActorSystem("demo")
  val account = Account("ac123", Customer("cust1", "mushtaq"), 0)

  "Hello" should "have tests" in {

    val actorRef: ActorRef = system.actorOf(Props(new AccountActor(account)))

    actorRef ! GetBalance
    (1 to 50).par.foreach(x => actorRef ! Deposit(100))
    actorRef ! GetBalance
    Thread.sleep(1000)
  }
}
