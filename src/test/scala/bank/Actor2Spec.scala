package bank

import akka.actor.{ActorRef, ActorSystem, Props}
import bank.AccountActor.{Balance, Deposit, GetBalance}
import org.scalatest._
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
import scala.util.{Failure, Success}

class Actor2Spec extends FlatSpec with Matchers {

  val system = ActorSystem("demo")
  val account = Account("ac123", Customer("cust1", "mushtaq"), 0)
  implicit val timeout = Timeout(1.second)
  import system.dispatcher

  "Hello" should "have tests" in {

    val actorRef: ActorRef = system.actorOf(Props(new AccountActor(account)))

    def future: Future[Balance] = (actorRef ? GetBalance).mapTo[Balance]

    future.onComplete {
      case Success(balance) =>
        println(balance)
        (1 to 50).par.foreach(x => actorRef ! Deposit(100))
        future.onComplete {
          case Success(balance2) =>
            println(balance2)
          case Failure(ex) =>
        }
      case Failure(ex) =>
    }

    Thread.sleep(1000)
  }
}
