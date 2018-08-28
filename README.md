![Architecture](https://github.com/kurartur/rb/blob/master/arch.png?raw=true)

**rb-importer** - Spring Boot application with a REST controller which receives messages from providers (hotels). Received messages are put into *rb-importer-queue*.

**rb-sync** - Spring Boot application with a listener that listens to *rb-importer-queue* for messages. Assume that providers message include provider ID and message's sequence number which is unique for that provider. By using sequence number rb-sync will put messages into *rb-consumer-queue* in correct order.

![Flow](https://github.com/kurartur/rb/blob/master/flow.png?raw=true)

**rb-consumer** - Spring Boot application that listens to *rb-consumer-queue* and processes received messages.

**JMS broker** - JMS provider with *rb-impoter-queue* and *rb-consumer-queue*. [RabbitMQ](https://www.rabbitmq.com/) is used in this example.

**Message Store** - Database where rb-sync can store messages. In this example messages are simply stored in application's memory ([InMemoryMessageRepository](https://github.com/kurartur/rb/blob/master/rb-sync/src/main/java/com/rb/sync/InMemoryMessageRepository.java))

# Running
To run the project using commands below *docker* and *docker-compose* must be installed on the system.
```sh
$ cd <project root dir>
$ ./rb-importer/gradlew build -p rb-importer
$ ./rb-sync/gradlew build -p rb-sync
$ ./rb-consumer/gradlew build -p rb-consumer
$ docker-compose build
$ docker-compose up
```
It will launch RabbitMQ (172.20.199.2), rb-importer (172.20.199.3), rb-sync (172.20.199.4) and rb-consumer (172.20.199.5) and output to the console.

# Testing
Message REST endpoint should be accessible on http://172.20.199.3:8080/message  
It accepts POST requests with following JSON structure in request body:
```json
{
   "providerId": "MegaHotel",
   "sequenceNumber": 1,
   "payload": "my payload"
}
```
rb-importer and rb-consumer will log received messages to the console.  
Sequence numbers and provider IDs can be varied to check how systems reacts.

# Q&A
##### What are the advantages of this setup?
- System is decoupled. Changes can be made to any part of the system without worrying about other parts.
- System is durable. Messages can only be lost in case of JMS Broker or Message Store fatal failure.
- UI/monitoring can be easily attached to message store to track/troubleshoot stuck messages.

##### What are the disadvantages of this setup?
- External message provider (hotel) must implement some sort of sequence generator on his side.
- 3 additional components in system - JMS Broker, Message Store and rb-sync app that need to be maintained (*but since large projects most probably already have JMS Broker and some kind of database, that leaves us only with additional rb-sync app, so this disadvantage is questionable*)

##### What kind of data does the message by the sender NEED to contain to ensure ordered processing?
- Sender must include his ID and sequence number of the message he sends. Sequences should start from 1.

##### What hasn't been done and needs to be done?
- Transaction management.
- "In memory" message store needs to be replaced with normal high-performance database (Hazelcast, MongoDB ot similar), because now messages are lost on rb-sync restart.
- Archive old messages.
- ??? It's never perfect :)

