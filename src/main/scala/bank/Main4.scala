package bank

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.cluster.sharding.{ClusterSharding, ClusterShardingSettings}
import akka.util.Timeout
import bank.PersistentAccount.{CreateAccount, Deposit}
import com.typesafe.config.ConfigFactory

import scala.concurrent.duration.DurationInt

object Main4 extends App {
  val config = ConfigFactory.load("application4.conf")
  val system = ActorSystem("demo", config)
  val account = Account("ac123", Customer("cust1", "mushtaq"), 0)
  implicit val timeout = Timeout(1.second)

  ClusterSharding(system).startProxy(
    typeName = "accountRegion",
    role = None,
    extractEntityId = PersistentAccount.extractEntityId,
    extractShardId = PersistentAccount.extractShardId
  )

  val actorRef = ClusterSharding(system).shardRegion("accountRegion")

  (1 to 100) foreach { x =>
    val account = Account(x.toString, Customer("cust1", "mushtaq"), 0)
    Thread.sleep(10)
    actorRef ! CreateAccount(account)
    actorRef ! Deposit(account.id, 100)
  }

}
