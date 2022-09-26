# reactiveApiDemo

## Set-up

### Cassandra + Docker

Back-end database for this demo is Reactive Cassandra NoSQL DB integrated with Spring Boot, running on a local docker container.

Installing Docker Engine: [Here](https://docs.docker.com/engine/install/)

Once docker is installed, pull latest Cassandra image in terminal window:
``` docker pull cassandra:latest```  
Next, navigate to project repo and run this command to instantiate container with cassandra image locally ```docker-compose up -d```  

If you have docker engine UI available, the container should appear within it. 

```docker exec -it cassandra-dev``` to start up the cql shell for Cassandra.

Once in the shell, run this command to make sure the table is instantiated properly.  
```use demo;```  
```create table if not exists productentity( id int , name text, price double, quantity int, PRIMARY KEY ((id));```

### Swagger-UI

Use your favorite Java IDE to start up the project (I used IDEA Ultimate). Run configs should already exist in the project.

Once started, go to ```localhost:8080/swagger-ui/index.html``` to test the different endpoints.

Postman should also work to test the varying endpoints.

## Exposed endpoints

POST `/product` - Pass product object to create new object in db  
GET `/listProducts` - Returns list of all products in db  
PUT `/product/{id}` - Pass product object to update product with associated id in header (you can pass different id in the object itself)  
DEL `/product/{id}` - Delete product object with associated id  
DEL `/clearProducts` - Clears the database

