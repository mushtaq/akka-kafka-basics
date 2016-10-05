package bank

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.cluster.sharding.{ClusterSharding, ClusterShardingSettings}
import akka.persistence.query.PersistenceQuery
import akka.persistence.query.journal.leveldb.scaladsl.LeveldbReadJournal
import akka.stream.ActorMaterializer
import akka.util.Timeout
import bank.PersistentAccount.{CreateAccount, Deposit}
import org.scalatest._

import scala.concurrent.duration.DurationLong

class Actor9Spec extends FlatSpec with Matchers {

  implicit val system = ActorSystem("demo")
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

    (1 to 10) foreach { x =>
      val account = Account(x.toString, Customer("cust1", "mushtaq"), 0)
      actorRef ! CreateAccount(account)
      actorRef ! Deposit(account.id, 100)
    }

    Thread.sleep(10000)

    system.terminate()
  }
}
