Samples
========================

Here are some samples with configuration files. 
To run application, just set the config file :

    java -jar hl7mp-1.0-jar-with-dependencies.jar org.opikanoba.hl7mp.App -cfg conf/config-app1.yaml

# Consumer: MLLP Server
## Normal application
Description: application that consumes messages, log them.

- consumer app1 MLLP server (port 8901)
- handlers:

| Category      | Parameter               |
| ------------- |:------------------------|
| ACK           | AA=100% AE=0% AR=0%     |
| WAIT          | not configured          | 
| LOG           | Y                       |

## Few errors
Description: some application errors (20%)

- consumer app2 MLLP server (port 8902)
- handlers:

| Category      | Parameter               |
| ------------- |:------------------------|
| ACK           | AA=80% AE=20% AR=0%     |
| WAIT          | not configured          | 
| LOG           | Y                       |

## Application errors and technical errors
Description: some application errors ACK (20%) and some technical errors ACK (20%)

- consumer app3 MLLP server (port 8903)
- handlers:

| Category      | Parameter               |
| ------------- |:------------------------|
| ACK           | AA=60% AE=20% AR=20%    |
| WAIT          | not configured          | 
| LOG           | Y                       |

## All ACK = errors
Description: The application only responds with errors (AE 100%)

- consumer app4 MLLP server (port 8904)
- handlers:

| Category      | Parameter               |
| ------------- |:------------------------|
| ACK           | AA=0% AE=100% AR=0%     |
| WAIT          | not configured          | 
| LOG           | Y                       |

## Slow application
Description: the application is very slow, 10s for processing each message 

- consumer app5 MLLP server (port 8905)
- handlers:

| Category      | Parameter               |
| ------------- |:------------------------|
| ACK           | AA=100% AE=0% AR=0%     |
| WAIT          | 10s for 100%            | 
| LOG           | Y                       |

## Some messages are slow to be processed
Description: the application processes some message (20%) with a lot of delay (20s)

- consumer app6 MLLP server (port 8906)
- handlers:

| Category      | Parameter               |
| ------------- |:------------------------|
| ACK           | AA=100% AE=0% AR=0%     |
| WAIT          | 20s for 20%             | 
| LOG           | Y                       |

## all messages are rejected
Description: No message processed due to errors: application (50%) or technical (50%)

- consumer app7 MLLP server (port 8907)
- handlers:

| Category      | Parameter               |
| ------------- |:------------------------|
| ACK           | AA=0% AE=50% AR=50%     |
| WAIT          | not configured          | 
| LOG           | Y                       |


## Some messages are processed but with delay, some are rejected
Description: The application processes several messages (20%) with a long delay (20s). 60% are OK, others are KO.

- consumer app8 MLLP server (port 8908)
- handlers:

| Category      | Parameter               |
| ------------- |:------------------------|
| ACK           | AA=60% AE=20% AR=20%    |
| WAIT          | 20s for 20%             | 
| LOG           | Y                       |

# Consumer: local files
## Messages in local directory
Description: The application reads all HL7 files which follow the regex name `*.hl7`, written in java, e.g. `.*?[.]hl7`.

- consumer app10 directory reader
- handlers:

| Category      | Parameter               |
| ------------- |:------------------------|
| ACK           | not configured          |
| WAIT          | not configured          | 
| LOG           | Y                       |

## Messages in local directory, add a terser logger
Description: The application reads all HL7 files which follow the regex name `*.hl7`, written in java, e.g. `.*?[.]hl7`.

- consumer app11 directory reader
- handlers:

| Category      | Parameter                         |
| ------------- |:----------------------------------|
| ACK           | not configured                    |
| WAIT          | not configured                    | 
| LOG           | Y                                 |
| LOG           | logs values of MSH-9-2 and PID-18 |                       

## Messages in a local directory, sent via MLLP
Description: The application reads all HL7 files which follow the regex name `*.hl7`, written in java, e.g. `.*?[.]hl7`. and sends the messages to a MLLP server

- consumer app12 directory reader
- handlers:

| Category      | Parameter                         |
| ------------- |:----------------------------------|
| ACK           | not configured                    |
| WAIT          | not configured                    | 
| LOG           | Y                                 |
| MLLP          | sends to localhost:8901           |                       
