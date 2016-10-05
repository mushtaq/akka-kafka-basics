package bank

import akka.actor.{ActorSystem, Props}
import akka.persistence.inmemory.query.scaladsl.InMemoryReadJournal
import akka.persistence.query.{EventEnvelope, PersistenceQuery}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink
import akka.util.Timeout
import bank.PersistentAccount.{AccountCreated, CreateAccount, Deposit, Deposited}
import org.scalatest._

import scala.concurrent.duration.DurationLong

class Actor10Spec extends FlatSpec with Matchers {

  implicit val system = ActorSystem("demo")
  val account = Account("ac123", Customer("cust1", "mushtaq"), 0)
  implicit val mat = ActorMaterializer()
  implicit val timeout = Timeout(1.second)

  var readModel = Map.empty[String, Double]

  val readJournal = PersistenceQuery(system)
    .readJournalFor[InMemoryReadJournal](InMemoryReadJournal.Identifier)

  private val persistenceIds = readJournal.allPersistenceIds()
  private val events = readJournal.eventsByPersistenceId("PersistentAccount-1", 0, Long.MaxValue)

  "Hello" should "have tests" in {

    val actorRef = system.actorOf(Props(new PersistentAccount), "1")

    events
      .collect {
        case EventEnvelope(offset, persistenceId, sequenceNr, AccountCreated(acc))          =>
          readModel += (acc.id -> acc.balance)
        case EventEnvelope(offset, persistenceId, sequenceNr, Deposited(accountId, amount)) =>
          readModel += (accountId -> (readModel(accountId) + amount))
      }.runWith(Sink.ignore)

    actorRef ! CreateAccount(account)
    actorRef ! Deposit(account.id, 100)

    Thread.sleep(10000)

    println(readModel)

    system.terminate()
  }
}
