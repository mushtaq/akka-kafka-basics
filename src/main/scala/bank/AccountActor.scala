package bank

import akka.actor.Actor
import bank.AccountActor.{Balance, Deposit, GetBalance}

class AccountActor(init: Account) extends Actor {

  var account = init

  override def receive: Receive = {
    case Deposit(amount) =>
      account = account.deposit(amount)
    case GetBalance =>
      sender() ! Balance(account.balance)
    case "error" =>
      throw new RuntimeException("error")
  }
}

object AccountActor {
  case class Deposit(amount: Double)
  case object GetBalance
  case class Balance(amount: Double)
}
