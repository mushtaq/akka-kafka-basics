package bank

import akka.actor.{ActorRef, ActorSystem, PoisonPill, Props}
import akka.pattern.ask
import akka.util.Timeout
import bank.AccountActor.{Balance, Deposit, GetBalance}
import org.scalatest._

import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}
import async.Async._

class Actor4Spec extends FlatSpec with Matchers {

  val system = ActorSystem("demo")
  val account = Account("ac123", Customer("cust1", "mushtaq"), 0)
  implicit val timeout = Timeout(1.second)
  import system.dispatcher

  "Hello" should "have tests" in {

    val actorRef: ActorRef = system.actorOf(Props(new AccountActor(account)), "account-manager-1")

    def future(): Future[Balance] = (actorRef ? GetBalance).mapTo[Balance]

    val resultF: Future[List[Balance]] = async {
      val balance = await(future())
      (1 to 50).par.foreach(x => actorRef ! Deposit(100))
      val balance2 = await(future())
      List(balance, balance2)
    }

    val balances: List[Balance] = Await.result(resultF, 2.seconds)

    println(balances)

    println(actorRef)

    actorRef ! PoisonPill
    system.stop(actorRef)

    system.terminate()

  }
}
