package bank

import akka.actor.{Actor, Stash}
import bank.AccountActor.{Balance, Deposit, GetBalance}
import bank.PersistentAccount.CreateAccount

class AccountBecomeActor extends Actor with Stash {

  var account: Account = null

  override def receive: Receive = uninitialized

  def uninitialized: Receive = {
    case CreateAccount(acc) =>
      account = acc
      println(s"account created $acc")
      context.become(initialized)
      unstashAll()
    case x => stash()
  }

  def initialized: Receive = {
    case Deposit(amount) =>
      account = account.deposit(amount)
      println(s"deposited $amount")
    case GetBalance =>
      sender() ! Balance(account.balance)
      println(s"balance is ${account.balance}")
    case "error" =>
      throw new RuntimeException("error")
  }

  override def unhandled(message: Any): Unit = {
    println(s"unhandled messages $message")
  }
}
