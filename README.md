# Event-Driven-Fuel-Cost-Calculator

## Technologies Used

 1. Programming Language: Java8
 2. Backend Framework: SpringBoot2
 3. Database: NA
 4. Caching: Spring Cache, Ehcache
 5. Messaging: Kafka
 6. Miscellaneous: Spring Schedulers

## Modules Available
 **1. **Event-Receiving-API****
 
|URL| http://localhost:8082/producer/eventTrigger/action/trigger-event |
|--|--|
|Reqyest body | {  "eventFlag”: true, "cityName": "Bangalore" }  |

 **2. **Event-Producer-API****
 
|URL| http://localhost:8081/consumer/eventTrigger/action/trigger-event |
|--|--|
|Reqyest body | {"eventFlag": false, "timeStamp": "2021-02-15T12:44:00.014", "cityName": "Mumbai"}  |

 **3. Fuel-Price-API**
|URL| http://localhost:8083/fuelPrice/v1/{cityName} |
|--|--|

# Setting Up Kafka

<details><summary>Mac</summary>
<p>

- Make sure you are navigated inside the bin directory.

## Start Zookeeper and Kafka Broker

-   Start up the Zookeeper.

```
./zookeeper-server-start.sh ../config/zookeeper.properties
```

- Add the below properties in the server.properties

```
listeners=PLAINTEXT://localhost:9092
auto.create.topics.enable=false
```

-   Start up the Kafka Broker

```
./kafka-server-start.sh ../config/server.properties
```
</details>
<details><summary>Windows</summary>
<p>

- Make sure you are inside the **bin/windows** directory.

## Start Zookeeper and Kafka Broker

-   Start up the Zookeeper.

```
zookeeper-server-start.bat ..\..\config\zookeeper.properties
```

-   Start up the Kafka Broker.

```
kafka-server-start.bat ..\..\config\server.properties
```

</p>

</details>
