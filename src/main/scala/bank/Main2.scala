package bank

import akka.actor.{ActorIdentity, ActorRef, ActorSystem, Identify, Props}
import akka.cluster.sharding.{ClusterSharding, ClusterShardingSettings}
import akka.pattern.ask
import akka.util.Timeout
import bank.AccountActor.{Balance, GetBalance}
import bank.PersistentAccount.{CreateAccount, Deposit}
import com.typesafe.config.ConfigFactory

import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}

object Main2 extends App {
  val config = ConfigFactory.load("application2.conf")
  val system = ActorSystem("demo", config)
  val account = Account("ac123", Customer("cust1", "mushtaq"), 0)
  implicit val timeout = Timeout(1.second)

  val actorRef: ActorRef = ClusterSharding(system).start(
    typeName = "accountRegion",
    entityProps = Props[PersistentAccount],
    settings = ClusterShardingSettings(system),
    extractEntityId = PersistentAccount.extractEntityId,
    extractShardId = PersistentAccount.extractShardId
  )

}
