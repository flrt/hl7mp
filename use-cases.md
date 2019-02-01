Use Cases
=========
# Test your integration engine

## Simulate MLLP Server, with errors or delay
The tool can be used to simulate a MLLP Server that might have some troubles :

- delay for processing
- internal error 

With different strategies, different kind of MLLP server (aka HL7 applications) can be simulate

## Starts tens of mock application to simulate yout system

With only 1 configuration file, tens of applications can be started in order to test the behavior of the integration engine

# Backup, log messages

In order to simply backup HL7 messages, an application can be started and messages stored locally.

## Proxy with delay or log

You wan to put a proxy between two actors, and log or backup messages, or even put a delay between us. 

# Test your HL7 application

## Quick send to MLLP Server (application)

You can want to test your application that have to deal with HL7 messages. Your application plays the role of HL7 Server by receiving HL7 messages. In order to quickly test your application, start hl7mp, which will read files locally and send them to your application. 
No need of complex tool like an integration engine. Just 1 configuration file and voila.

