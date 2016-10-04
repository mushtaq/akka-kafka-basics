package bank

class AccountManager(init: Account) {
  private var account = init

  def deposit(amount: Double): Unit = {
    account = account.deposit(amount)
  }

  def getBalance() = account.balance
}
