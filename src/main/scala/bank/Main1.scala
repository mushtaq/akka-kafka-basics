package bank

import akka.actor.{ActorIdentity, ActorRef, ActorSystem, Identify, Props}
import akka.cluster.Cluster
import akka.cluster.sharding.{ClusterSharding, ClusterShardingSettings}
import akka.pattern.ask
import akka.util.Timeout
import bank.AccountActor.{Balance, GetBalance}
import bank.PersistentAccount.{CreateAccount, Deposit}

import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}

object Main1 extends App {
  val system = ActorSystem("demo")
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
