**SIMPLE Non blocking AKKA-HTTP**

Simple service that publish one heartbeat in port 9091 whose response time can be configured. 

*** Routes ***

```
/heartbeat //get a html text response that indicates how much time in milliseconds is the delayed the response

POST /increase100ms // increase the delay in 100 millis

POST /decrease100ms // decrease the delay in 100 millis
```

Docker 

docker pull emicareofcell44/http-retarder:0.1.6
