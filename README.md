# Solution Overview and Requirement definitions
This document serves as a high-level description of our API Design Guidelines and imlementation approach for the technical challenge at Smaato


## Functional Requirements
The following are the proposed list of functional requirement to be implemented on the System:

1. The service has one GET endpoint - /api/smaato/accept which is able to accept an integer id as a mandatory query parameter and an optional string HTTP endpoint query parameter. It should return String “ok” if there were no errors processing the request and “failed” in case of any errors.
2. Every minute, the application should write the count of unique requests your application received in that minute to a log file - please use a standard logger. Uniqueness of request is based on the id parameter provided.
3. When the endpoint is provided, the service should fire an HTTP GET request to the provided endpoint with count of unique requests in the current minute as a query parameter. 
4. Instead of firing an HTTP GET request to the endpoint, fire a POST request. The data structure of the content can be freely decided.  
5. Instead of writing the count of unique received ids to a log file, send the count of unique received ids to a distributed streaming service of your choice. 

## Non Functional Requirements
The following are the proposed list of Non-functional requirement to be implemented on the System:

1. Ability to process 10K requests per Second
2. Implement deduplication to avoid duplication
3. Implement Tracing - observability
4. High Availbility


# Capacity Estimation and Asumptions
I have to determine the capacity estimation so as to know the type of  bandwith and memory I can allocate for the system architecture and also to know what is needed when i want to scale the system. Also,Since the Query will be integer and String Endpoint, I assumed the maximum value of Integer(2147483647) or 4bytes and 1000 length for the endpoint url at 2bytes for a character which gives us 2000bytes.
therefore, i  estimated total byte to be 2100 byte and at 10k request gives us 21MB. I will go for bandwith 30-50 MBPS


# System interface definition
Based on the assignment, the api needs a mandatory Integer and optional endpoint and should return a string of "ok" or "failed" based on the execution and http GET method
```bash
processId(Integer id, Optional<String> endpoint);

http://127.0.0.1:8080/api/smaato/accept?id=102772&endpoint=http://localhost:8080/api/v1/ping
http://127.0.0.1:8080/api/smaato/accept?id=402772
```
Secondly, there is a also a timer service that runs every minute printing the distinct values of a concurrent map.


# High-level design
This section explains the architecture of the design

<img src="images/hld1.jpg"
     alt="Solution Architecture"
     style="float: left; margin-right: 10px;" />

In the design, I introduce kafka as our streaming platform for log which also logs on a file and console and can be edited from the log4j2.xml as we used log4j logger.


## Deployment and Testing

For the Source to build and deployed successfully, it requires the following Software requirments

* [Java SE Development Kit 8 installed](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [Maven](https://maven.apache.org/install.html)
* [Docker](https://www.docker.com/)



Firstly,you have to download project with GIT then run docker compose to starup Kafka,Zookeper and Redis
```bash
git clone https://github.com/ezechima123/ad-exchange-channel-service.git
cd ad-exchange-channel-service

To Start Docker 
docker-compose up -d 

To Stop docker
docker-compose stop
```

We can then run the application with `maven`  or directly with the jar file
```bash
mvn spring-boot:run

java -jar target/ad-exchange-channel-service-1.0.jar
```

This endpoint can be tested with query parameters either from browser or postman:
```bash
http://127.0.0.1:8080/api/smaato/accept?id=102772&endpoint=http://localhost:8080/api/v1/ping
http://127.0.0.1:8080/api/smaato/accept?id=402772
```


## Design considerations
The following are other enhancements that can be done on the system to improve the production ability of the process:

1. I used kafka as its on of the best matured platform in terms of logging and can easily integrate with ElasticSearch and Kibana.
2. I used SpringBoot framework to develop the service for ease of support and large community and following
3. I also use Redis for distributed lock which can also be extended for cache 


## Further Enhancements
The following are other enhancements that can be done on the system to improve the production ability of the process:

1. Implementation of circuit breaker pattern for the endpoint service
2. Implemementing the ELK (Elastic Search,LogStash and Kibana) stack on Log management and monitoring with Kafka
3. Cache can be added for to improve performance 
4. The deployment can be done on kubernetes/ECS Cloud for more performance,availability and scalabilty



