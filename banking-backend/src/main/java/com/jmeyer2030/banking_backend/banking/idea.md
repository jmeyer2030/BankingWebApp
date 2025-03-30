
### Transaction/banking features

Funtions to define:
Deposit (no verification, just adds money to an account)
Withdraw (no verification, just removes money from an account)
Transfer (Given username, sends an amount <= balance to that user)

These are all "transactions"
Transactions should have:
 - id
 - Type (Deposit, Withdraw, Transfer)
 - Time of transaction 
 - Amount
 - description

Controller:
 -banking/transaction, form
  - verify non-negative amount
  - fields non-null
 
 

Service:
 - Handle transaction:
  - validate auth token
    - if invalid, LoginExpiredException
  - if withdraw, 
    - amount less than balance (InsufficientFundsException)
  - if transaction
    - amount less than balance (InsufficientFundsException)
    - other person exists (InvalidRecepientException)
  - if deposit

return success, change current displayed balance?



## How do we actually store transactions in the database???

Don't worry about it, primary key transaction id, postgresql
will handle everything
