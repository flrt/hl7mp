HL7 v2 message processor
========================

This project allows to define mock applications that handle HL7 messages.

The main goal is to handle messages as a piece of data, not to deal with the content itself.

The concept is very simple:

1. define how the messages will be consume (consumer)
2. define how the messages will be handled

The aim is to have a very small piece of software: 1 jar file and 1 configuration file. With them, you can start, at once, on your local machine or a server these application without installation or complex stuff. Goal: Keep it VERY simple.

See [configuration page](configuration.md) for options available for each consumer/handler.

# Typical use cases

See [samples](samples.md) for detailed description.

## Test my HL7 producer
I've got an HL7 producer and I want to measure, test, challenge how it will work in different context:

- with a fast HL7 consumer with no error
- with a slow HL7 consumer (set the delay) with or without error
- with a HL7 consumer that returns error acks

With just a configuration file, a HL7 consumer can be easily started.

## Test my integration engine
By starting several applications with several strategies, an whole system can be mocked. You can simulate fast consumers, slow consumers, breaking consumers, etc.

Each consumer only needs one simple configuration file.

## Test my HL7 consumer
I want to test my HL7 consumer so I need some HL7 messages to send. Put messages in a directory and start the application that will send HL7 messages. 

A simple jar file is enough to send messages, no need of a complex piece of software, like integration engine or so.

## Log messages
Two loggers can be useful to log information about a producer: a simple logger and a terser logger.

## Backup messages
Start the application with a backup handler, and all the received messages are stored in the configured directory.

## Proxy with delay or log
You wan to put a proxy between two actors, and log or backup messages, or even put a delay between them. 

## Test my HL7 application

You can want to test your application that have to deal with HL7 messages. Your application plays the role of HL7 Server by receiving HL7 messages. In order to quickly test your application, start hl7mp, which will read files locally and send them to your application. 
No need of complex tool like an integration engine. Just 1 configuration file and voila.

# Capacities
## Consumer
The consumer is the first part of the application. It can consume HL7 messages from

- a MLLP Client (receives HL7 messages) that will receive messages on a configurated port
- a local directory: all messages stored in files are read and computed
- an archive of HL7 messages: 1 file containing x HL7 message. The archive has a specific format

## Handlers
Messages sent by the consumer can be computed by handlers:

- delay handler: wait for a delay before transmit the message to the next handler
- log handler: log messages into a log file 
- terser log handler: log specific data contented in the messages into a log file
- bacckup handler: backup messages in HL7 files, in an output directory
- ACK handler: respond to a MLLP client (consumer) by sending AA, AE or AR ACK messages.
- MLLP handler: send the messages to a MLLP server 

# Run
You can run the application with 1 command line. Simply call the main with the configuration file.

## without docker
You can clone the repo and build your own jar with maven:

    $ mvn clean compile assembly:single
    $ ls target/*.jar
    target/hl7mp-1.0-jar-with-dependencies.jar    

Or get the jar from github.

Then launch the main class `org.opikanoba.hl7mp.App` with the configuration file. And that's it !

    $ java -jar hl7mp-1.0-jar-with-dependencies.jar \
    org.opikanoba.hl7mp.App -cfg conf/config-app01.yaml
    
## with docker
Use the prebuild image available on docker hub: `flrt/hl7mp`.
Be aware of the used ports in MLLP connections.
For convenience, the ports in range `8900-8999` are forwarded. That means that if you defined a server/client using a port in these range, nothing to do with the docker forwarding port parameter.
Otherwise, you have to define the forwarding explicitly.

If you have defined a MLLP server as a consumer with a port, for instance:

    mllp-server:
       listen-port: 8901
       
Ok, nothing to do. Start the container with

    $ docker run -ti --rm -v {$PWD}:/home hl7mp conf/config-app01.yaml

If you have defined a port outside the range, for instance: 

    mllp-server:
       listen-port: 9533

Declare it:

    $ docker run -ti --rm -p 9533:9533 -v {$PWD}:/home hl7mp conf/config-app01.yaml

### Default Configuration file
If your configuration file is `conf/config.yaml` you can omit it in the docker run command, as it's the default filename.

    $ docker run -ti --rm -v {$PWD}:/home hl7mp 


# Licence 

[MIT](LICENSE) 
