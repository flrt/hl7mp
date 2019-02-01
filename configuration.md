Configuration syntax
====================

# Messages consumer
## MLLP Server
A MLLP server is started and listens to incoming messages.

- `listen-port`: port on which the server listen
- `listen-address`: address of the server

Example:

    mllp-server:
      listen-port: 8888
      listen-address: localhost

## Directory client
Messages are read from a local directory.

- `dir-path`: path where the files are stored
- `dir-filename-pattern`: pattern of the file names

Example:

	dir-client:
	  dir-path: files/HL7
	  dir-filename-pattern: ".*\\.hl7"

## Directory client
Messages are read from an archive file stored locally.

- `filename`: name of the file containing messages

Example:

	archive-client:
	  filename: files/archive.msg


# Message Handler
## AckHandler
An handler that generate an HL7 acknowledge as defined in the chapter 2 of the HL7 specification.
A strategy can be set : what type of ACK is produced.
3 different can be produced : AA, AE, AR.

Set for each the pourcent of generated ACK, by setting a value between 0 and 1 : 0.33 means 33%, e.g. 1 in 3 will be generated like configured.

By setting a filename, logs will be saved.


	ack-handler:
	  ack-aa: 0.33
	  ack-ae: 0.33
	  ack-ar: 0.33
	  filename: /tmp/files/log/ack.log

## BackupHandler
A handler that can backup received messages in a directory.

	backup-handler:
	  directory: /tmp/files/backup-adt
  
## LoggerHandler
A simple logger, that log every received message
Specify the filename of the log file. 
 
	logger-handler:
	  filename: /tmp/files/log/tests.log
	  
## TerserLoggerHandler
A handler that logs informations about messages by extracting special values.

Specify the filename of the log file. 


	terser-logger-handler:
	  filename: /tmp/files/log/terser-tests.log
	  
## WaitHandler
A handler that waits for a delay. The `scope` allows to define if the handler have to wait for all the messages or for a pourcent of processed messages.

- `delay`: wait for x seconds
- `scope`: pourcent (0 to 1) of delayed messages 


	wait-handler:
	  delay: 3
	  scope: 0.5
	  filename: /tmp/files/log/wait.log

## MLLP handler
A handler that sends HL7 message to a HL7 consumer (server). 

- `listen-port`: port on which the server listen
- `listen-address`: address of the server

	mllp-client-handler:
	  listen-address: localhost
	  listen-port: 8901