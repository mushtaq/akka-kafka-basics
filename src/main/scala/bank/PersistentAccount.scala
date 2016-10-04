package bank

import akka.actor.ReceiveTimeout
import akka.persistence.{PersistentActor, SnapshotOffer}
import bank.AccountActor.Deposit
import bank.PersistentAccount.{AccountCreated, CreateAccount, Deposited, Snapshot}

class PersistentAccount extends PersistentActor {
  override def persistenceId: String = self.path.name

  var account: Account = null

  self ! ReceiveTimeout

  override def receiveCommand: Receive = {
    case x => persistAsync(x) { event =>

    }
    case CreateAccount(init) => persist(AccountCreated(init)) { event =>
      account = init
      sender() ! "done"
    }
    case Deposit(amount) =>
      persist(Deposited(amount)) { event =>
        account = account.deposit(amount)
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
    case Deposited(amount) =>
      account = account.deposit(amount)
  }
}


object PersistentAccount {
  //commands
  case class CreateAccount(init: Account)
  case object Snapshot

  //events
  case class Deposited(amount: Double)
  case class AccountCreated(init: Account)
}
