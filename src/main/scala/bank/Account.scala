package bank

import java.time.temporal.TemporalAmount

import akka.actor.{Actor, ActorSystem}
import akka.actor.Actor.Receive
import bank.AccountActor.{Deposit, GetBalance}

case class Customer(id: String, name: String)

case class Account(id: String, customer: Customer, balance: Double) {
  def deposit(amount: Double) = copy(balance = balance + amount)
}

class AccountManager(init: Account) {
  private var account = init

  def deposit(amount: Double): Unit = {
    account = account.deposit(amount)
  }

  def getBalance() = account.balance

}

class AccountActor(init: Account) extends Actor {

  var account = init

  override def receive: Receive = {
    case Deposit(amount) =>
      account = account.deposit(amount)
    case GetBalance =>
      println(account.balance)
  }

  def deposit(amount: Double): Unit = {
    account = account.deposit(amount)
  }
}


object AccountActor {
  case class Deposit(amount: Double)
  case object GetBalance
}
