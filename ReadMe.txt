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
        d. updateOnRemove: false

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
    Ex: prder-processor/logs/processor.log
    You can analyze these logs and verify that the application is working properly.


Pre-requisites Additional Details:

    1. Need to have Java installed on your machine
    2. Need to have maven installed on your machine
    3. Test cases are written and the main application 'order-processor' has a code coverage of around 80%
    4. You can test with adjusting the variables in application.yml file (in 'order-processor' and 'order-consumer') and see the output accordingly
    5. The log format is controlled via logback.xml file present in each of those projects
    6. 'order-processor' application has 3 APIs using which you can test some cases where its difficult for the human eye to just look at the logs and verify if everything si working as expected
    7.  You can use the below CURL commands to test the application and check the output of the contents
    8.  To submit the order run the below command in terminal or postman
            curl --location --request POST 'http://localhost:8080/orderprocessor/order' \
            --header 'Content-Type: application/json' \
            --data-raw '{
                "name": "Kale Salad",
                "temp": "cold",
                "shelfLife": 250,
                "decayRate": 0.25
            }'
    9.  To view the contents of the shelves
            curl --location --request GET 'http://localhost:8080/orderprocessor/kitchen/displayContents'
    10. To remove the order from the shelf
            curl --location --request POST 'http://localhost:8090/orderconsumer/driver' \
            --header 'Content-Type: application/json' \
            --data-raw '{
                "name": "Kale Salad",
                "temp": "cold",
                "shelfLife": 250,
                "decayRate": 0.25
            }'



How and why you chose to handle moving orders to and from the overflow shelf?

    1.  The task was to if the shelves free up, you need to move an order back from the overflow shelf
    2.  There are 2 ways the shelf can free up 
        a. if a driver picks the order
        b. if the order value becomes zero
    3.  In order to achieve this I am first checking if there is any space available on either hot, cold or frozen shelf
        and if there is any space available then I will check to see if there is any order of that shelf type (hot, cold or frozen) available on overflow shelf. If there is any such order then I will pick the order with the lower value (the order decay value calculated using the given formula) and move it back to the actual shelf
    4.  I perform the above step 3 at two steps
            a.  When a new order comes in we want to make sure that before placing this order we first check if there are any older orders that needs to be pushed from overflow shelf since the order is decaying at a faster rate and it will lose value if we chose to keep it on the overflow shelf for a long period of time before the order is picked
            b.  (Conditional) When an existing order is removed we want to perform the check to move the orders from overflow shelf because decaying is faster on that shelf. Lets say that new orders stopped coming in and may be the drivers are also struck in traffic for a longer period then it may so happen that the order is decayed and wasted. To avoid this I am call this move operation after removing an order
    5.  However, in real world situation the above step cannot be decided without knowing the actual production and consumption rate. Hence, I chose to have a configuration parameter which can decide whether to call the move operation or not on removal of an order. 
    6.  For the purpose of this task I chose to keep the value 'updateOnRemove' as false and will move the orders from overflow shelf only when a new order request comes in. 






