### Network audio transmission using TCP and UDP

* TCP Transmitter
```
java -jar audio.jar -t TRANSMITTER_PORT
```
* TCP Receiver
```
java -jar audio.jar -r TRANSMITTER_PORT TRANSMITTER_IP
```
---
* UDP Transmitter (if RECEIVER_IP is not present localhost will be used)
```
java -jar audio.jar -tu TRANSMITTER_PORT
java -jar audio.jar -tu TRANSMITTER_PORT RECEIVER_IP
```
* UDP Receiver
```
java -jar audio.jar -ru TRANSMITTER_PORT
```
