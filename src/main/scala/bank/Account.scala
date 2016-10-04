package bank

case class Customer(id: String, name: String)

case class Account(id: String, customer: Customer, balance: Double) {
  def deposit(amount: Double) = copy(balance = balance + amount)
}

