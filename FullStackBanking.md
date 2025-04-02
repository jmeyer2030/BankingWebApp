# Banking Web-App Design document 

Joshua Meyer 2025

## Database Design:

### Tables:

users:
 - Long id
 - String passwordHash
 - String firstname
 - String lastname
 - String email

accounts:
 - Long id
 - Long userID
 - Integer balance (in pennies)
 - String accountType
 - Date creationData

transactions:
 - Long id
 - Long senderID (always signed in user)
 - Long recepientID (either recepient or null)
 - String transactionType (transfer, deposit, withdraw)
 - String description
 - Date timestamp