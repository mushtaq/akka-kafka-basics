package bank

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import bank.AccountActor.{Balance, Deposit, GetBalance}
import org.scalatest._

import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}

class Actor3Spec extends FlatSpec with Matchers {

  val system = ActorSystem("demo")
  val account = Account("ac123", Customer("cust1", "mushtaq"), 0)
  implicit val timeout = Timeout(1.second)
  import system.dispatcher

  "Hello" should "have tests" in {

    val actorRef: ActorRef = system.actorOf(Props(new AccountActor(account)))

    def future: Future[Balance] = (actorRef ? GetBalance).mapTo[Balance]

    val resultF: Future[List[Balance]] = future.flatMap { balance =>
      (1 to 50).par.foreach(x => actorRef ! Deposit(100))
      future.map { balance2 =>
        List(balance, balance2)
      }
    }

    val balances: List[Balance] = Await.result(resultF, 2.seconds)

    println(balances)

  }
}
