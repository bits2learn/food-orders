This file contains details on what components are included in this project and how to run it

Contents:

    1. order-producer: This is a simple java application which reads the orders from the input orders.json file and submits the order to order-processor

    2. order-processor: This is a spring-boot application which processes the incoming order requests, sends a notification to the drivers and places the order on one of the shelf. Below parameters can be configured in application.yml file
        a. port: 8080
        b. size of the shelved
            i. hot: 15
            ii. cold: 15
            iii. frozen: 15
            iv. overflow: 20
        c. driverAppUrl: 'order-consumer' application URL

    3. order-consumer: This is a spring-boot application which consumes the orders. Below parameters can be configured in application.yml file
        a. port: 8090
        b. min and max wait time for the drivers
            i. minDriveTime: 2
            ii. maxDriveTime: 10
        c. kitchenUrl: 'order-processor' application URL


Steps to start the applications: 

We need to open 3 terminals and follow the below steps in order of execution

    A. In terminal one run the following commands for starting 'order-processor' application
        1. > cd order-processor
        2. > mvn clean install
        3. > mvn spring-boot:run

    B. In terminal two run the following commands for starting 'order-consumer' application
        1. > cd order-consumer
        2. > mvn clean install
        3. > mvn spring-boot:run

    C. In terminal three run the following commands for starting 'order-producer' application
        1. > cd order-producer
        2. > mvn clean install
        3. > java -jar target/order-producer-1.0.0-jar-with-dependencies.jar -kitchenUrl http://localhost:8080/orderprocessor/order -orders ./orders.json


Testing the applications:

    1. We need to focus mainly on the 'order-processor' application terminal. This is where we can see the contents of the shelf including the normalized value. The contents are displayed as json string

    2. In the 'order-producer' terminal we can see that it keeps submitting the orders at Poisson Distribution with an average 
    of 3.25 deliveries per second (lambda). In this terminal here we can see the order contents which are being submitted

    3. If we look at the 'order-consumer' terminal we can see the contents of the order which is being picked up by the driver

    Since its difficult to just observe the terminals and see if everything is working as expected, you can look at
    the contents (or the logs) for each application in a log file present inside 'logs' folder.
    You can analyze these logs and verify that the application is working properly.


Pre-requisites Additional Details:

    1. Need to have Java installed on your machine
    2. Need to have maven installed on your machine
    3. Test cases are written and the main application 'order-processor' has a code coverage of 80%
    4. You can test with adjusting the variables in application.yml file (in 'order-processor' and 'order-consumer') and see the output accordingly
    5. The log format is controlled via logback.xml file present in each of those projects









1. 