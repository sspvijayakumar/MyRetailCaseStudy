# MyRetailCaseStudy
myRetail RESTful service
RESTful service to retrieve product and update price details by Product ID

Technical Stack and IDE used:

Spring Boot
Mongo DB
Gradle
Mokito/Junit
IntelliJ
Postman
OpenJDK
Robo 3T

Setup instructions: 
1. Java 1.8 
2. Install Mongo DB(Client and Server): https://docs.mongodb.com/manual/tutorial/install-mongodb-on-windows/ 
3. Install any IDE(IntelliJ, eclipse, etc) 4. Install Gradle:https://gradle.org/install/ 5. Install Robo 3T:https://robomongo.org/download 6. Install Postman 7. Clone the Project from git https://github.com/sspvijayakumar/MyRetailCaseStudy.git 8. Import the project in IDE

To run the application:

Run mongo DB from the command prompt. And test --- http://localhost:27017/
Run the spring boot application from IDE

Command to create database: 
use test

Command to create collection/Table: 
db.products.insert

Command to insert records in Collection: 
db.products.insertMany([ {product_id: 13860428, price: 1, currency_code: "USD"}, {product_id: 13860428, price: 10.99, currency_code: "USD"}, 
{product_id: 16696652, price: 0.99, currency_code: "USD"}])

Command to view products: 
db.products.find().pretty()

GET and POST endpoints can be tested with below link GET : http://localhost:8102/products/13860428 
{
    "id": 13860428,
    "name": "The Big Lebowski (Blu-ray)",
    "current_price": {
        "value": 1 ,
        "currency_code": "USD"
    }
}

PUT : http://localhost:8102/products/13860428
{
    "id": 13860428,
    "name": "The Big Lebowski (Blu-ray)",
    "current_price": {
        "value": 1 ,
        "currency_code": "USD"
    }
}