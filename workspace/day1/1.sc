import bank._


val account = Account("ac123", Customer("cust1", "mushtaq"), 0)
val manager = new AccountManager(account)

manager.getBalance()
(1 to 50).par.foreach(x => manager.deposit(100))
manager.getBalance()


