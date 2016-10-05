package bank

import akka.cluster.sharding.ShardRegion
import akka.persistence.{PersistentActor, SnapshotOffer}
import bank.PersistentAccount._

class PersistentAccount extends PersistentActor {
  override def persistenceId: String = getClass.getSimpleName + "-" + self.path.name

  var account: Account = null

  override def receiveCommand: Receive = {
    case CreateAccount(init) => persist(AccountCreated(init)) { event =>
      account = init
//      println(event)
      sender() ! "done"
    }
    case Deposit(accountId, amount) =>
      persist(Deposited(accountId, amount)) { event =>
        account = account.deposit(amount)
//        println(event)
        sender() ! "done"
      }
    case Snapshot =>
      saveSnapshot(account)
  }

  override def receiveRecover: Receive = {
    case SnapshotOffer(_, state: Account) =>
      account = state
    case AccountCreated(init) =>
      account = init
    case Deposited(accountId, amount) =>
      account = account.deposit(amount)
  }
}


object PersistentAccount {
  //commands
  trait Command {
    def accountId: String
  }
  case class CreateAccount(init: Account) extends Command {
    override def accountId: String = init.id
  }
  case object Snapshot
  case class Deposit(accountId: String, amount: Double) extends Command

  //events
  trait Event
  case class Deposited(accountId: String, amount: Double) extends Event
  case class AccountCreated(init: Account) extends Event


  def extractEntityId: ShardRegion.ExtractEntityId = {
    case command: Command => (command.accountId, command)
  }

  def extractShardId: ShardRegion.ExtractShardId = {
    case x: Command => (x.accountId.hashCode.abs % 30).toString
    case x          => null
  }
}
