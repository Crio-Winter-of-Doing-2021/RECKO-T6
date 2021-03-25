# Discussions

## API Details

|End Point|Request Body|Response Body|Response Status|Method Type|
|-------|-----------|-----------|-------------|---------|
|/api/auth/adminLogin|{username, password}|{message}|401 - Failure, 200 - Success|POST
|/api/auth/moderatorLogin|{username, password}|{message}|401 - Failure, 200 - Success|POST
|/api/auth/adminChangePassword/{adminName}|{oldPassword,newPassword}|{message}|401 - Failure, 200 - Success|PATCH
|/api/auth/moderatorChangePassword/{moderatorName}|{oldPassword,newPassword}|{message}|401 - Failure, 200 - Success|PATCH
|/api/auth/adminForgotPassword/{adminName}||{message}|401 - Failure, 200 - Success|GET
|/api/auth/moderatorForgotPassword/{moderatorName}||{message}|401 - Failure, 200 - Success|GET
/api/accounts||[{id,name,amount,date,type,{name,description}}]|500 - Failure, 200 - Success| GET|
|/api/accounts/{partner}||[{id,name,amount,date,type,{name,description}}]|500 - Failure, 200 - Success| GET|
|/api/accounts/create/{partner}|{name,type}|{id,name,amount,date,type,{name,description}}|500 - Failure, 200 - Succcess | POST
|/api/accounts/update/{partner}/{customerId}|{name,type}|{id,name,amount,date,type,{name,description}}|500 - Failure, 200 - Success | PATCH
|/api/accounts/delete/{serviceId}/{customerId}||{message}|500 - Failure, 200 - Success | DELETE
|/api/services||[{name,description}]|500 - Failure, 200 Success| GET
|/api/services/{partner}||{name,description}|404 - Failure, 200 Success| GET

## Database Schema 

* admin(username, password) 
* service(service_id,name,client_id,client_secret,access_token,refresh_token,application_id)
* account(id,name,amount,date,type,service_id,active)
* user(username,password)