# Discussions

## API Details

|End Point|Request Body|Response Body|Response Status|Method Type|
|-------|-----------|-----------|-------------|---------|
|/api/adminLogin|{username, password}|{message}|401 - Failure, 200 - Success|POST
|/api/userLogin|{username, password}|{message}|401 - Failure, 200 - Success|POST
|/api/accounts/{serviceId}||[{id,name,amount,date,type,serviceId}]|500 - Failure, 200 - Success| GET|
|/api/accounts||[{id,name,amount,date,type,serviceId}]|500 - Failure, 200 - Success| GET|
|/api/services||[{id,name}]|500 - Failure, 200 Success| GET
|/api/create/{serviceId}|{name,type}|{id,name,amount,date,type,serviceId}|500 - Failure, 200 - Succcess | POST
|/api/update/{serviceId}|{accountId,name}|{id,name,amount,date,type,serviceId}|500 - Failure, 200 - Success | PATCH
|/api/delete/{serviceId}|{accountId}|{message}|500 - Failure, 200 - Success | DELETE

## Database Schema 

* admin(username, password) 
* service(service_id,name,client_id,client_secret,access_token,refresh_token,application_id)
* account(id,name,amount,date,type,service_id,active)
* user(username,password)