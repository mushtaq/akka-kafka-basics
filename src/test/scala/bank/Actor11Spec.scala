package bank

import akka.actor.{ActorSystem, Props}
import akka.stream.ActorMaterializer
import akka.util.Timeout
import bank.AccountActor.{Deposit, GetBalance}
import bank.PersistentAccount.CreateAccount
import org.scalatest._

import scala.concurrent.duration.DurationLong

class Actor11Spec extends FlatSpec with Matchers {

  implicit val system = ActorSystem("demo")
  val account = Account("ac123", Customer("cust1", "mushtaq"), 0)
  implicit val mat = ActorMaterializer()
  implicit val timeout = Timeout(1.second)

  "Hello" should "have tests" in {

    val actorOf = system.actorOf(Props(new AccountBecomeActor))

    actorOf ! Deposit(100)
    actorOf ! CreateAccount(account)
    actorOf ! GetBalance

    Thread.sleep(1000)
    system.terminate()
  }
}
