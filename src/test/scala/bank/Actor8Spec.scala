package bank

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.cluster.sharding.{ClusterSharding, ClusterShardingSettings}
import akka.persistence.query.journal.leveldb.scaladsl.LeveldbReadJournal
import akka.persistence.query.{EventEnvelope, PersistenceQuery}
import akka.stream.ActorMaterializer
import akka.util.Timeout
import bank.PersistentAccount.{CreateAccount, Deposit}
import org.scalatest._

import scala.concurrent.duration.DurationLong

class Actor8Spec extends FlatSpec with Matchers {

  implicit val system = ActorSystem("demo")
  val account = Account("ac123", Customer("cust1", "mushtaq"), 0)
  implicit val mat = ActorMaterializer()
  implicit val timeout = Timeout(1.second)

  val readJournal = PersistenceQuery(system)
    .readJournalFor[LeveldbReadJournal](LeveldbReadJournal.Identifier)

  private val persistenceIds = readJournal.allPersistenceIds()
  private val events = readJournal.eventsByPersistenceId("account-manager-1")

  "Hello" should "have tests" in {

//    val actorRef = system.actorOf(Props(new PersistentAccount), "account-manager-1")

    val actorRef: ActorRef = ClusterSharding(system).start(
      typeName = "accountRegion",
      entityProps = Props[PersistentAccount],
      settings = ClusterShardingSettings(system),
      extractEntityId = PersistentAccount.extractEntityId,
      extractShardId = PersistentAccount.extractShardId
    )

    persistenceIds.runForeach(println)
//    events.runForeach {
//      case env@EventEnvelope(offset, persistenceId, sequenceNr, event) =>
//        println(env)
//    }

    actorRef ! CreateAccount(account)
    actorRef ! Deposit(account.id, 100)

    Thread.sleep(10000)

    system.terminate()
  }
}
