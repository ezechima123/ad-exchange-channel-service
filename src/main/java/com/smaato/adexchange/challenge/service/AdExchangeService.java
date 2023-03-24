package com.smaato.adexchange.challenge.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.MDC;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AdExchangeService {

    @Value("${extenstion.http.get.or.post}")
    String extenstionHttpGetOrPost;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    RedisManagerService redisManagerService;
    @Autowired
    private ConcurrentHashMap<Integer, String> requestMap;
    private static final Logger log = LogManager.getLogger(AdExchangeService.class);

    public String handleOperation(Integer id, Optional<String> endpoint) {

        log.debug("About to acquire Lock on Redis to avoid duplication across instances");
        try {
            if (redisManagerService.acquireLock(id.toString())) {
                log.debug("Lock acquired for Id {}", id);
                // insert id and Endpoint to Database (with Unique Contraint)
                String retrievedId = requestMap.get(id);
                log.debug("Retrieved Value {}", retrievedId);
                if (retrievedId != null) {
                    return "duplicate";
                } else {
                    log.debug("Perform other operations");
                    requestMap.put(id, endpoint.orElse("No EndPoint Provided"));
                }
            } else {
                log.debug("Failed acquire for Id {}, possible ducplicate", id);
                return "duplicate";
            }
        } finally {
            log.debug("finally unlock for Id {}", id);
            redisManagerService.unlock(id.toString());
        }

        log.debug("Done with RedisLock on Id {}", id);
        if (endpoint.isPresent()) {
            log.debug("Endpoint url is detected and with configured Http method => {}", extenstionHttpGetOrPost);
            if (extenstionHttpGetOrPost.equalsIgnoreCase("GET")) {
                return handleGetRequest(id, endpoint.get());
            } else if (extenstionHttpGetOrPost.equalsIgnoreCase("POST")) {
                return handlePostRequest(id, endpoint.get());
            } else {
                String response = handleGetRequest(id, endpoint.get());
                if (response.equals("ok")) {
                    return handlePostRequest(id, endpoint.get());
                } else {
                    return response;
                }
            }
        } else {
            log.debug("No endpoint was passed, so about to return to client for Id {}", id);
        }

        return "ok";
    }

    private String handleGetRequest(Integer id, String endpoint) {
        try {
            log.debug("Coming to process GET request with id {} and {}", id, endpoint);
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(
                    endpoint + "?uniqueRequestCount=" + requestMap.size(), String.class);
            log.info("Final GET HttpStatus Code => {}", responseEntity.getStatusCode());
        } catch (Exception ex) {
            ex.printStackTrace();
            return "servererror";
        }
        return "ok";
    }

    private String handlePostRequest(Integer id, String endpoint) {
        try {
            log.debug("Coming to process POST request with id {} and {}", id, endpoint);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("transactionId", MDC.get("tracingId").toString());
            // create a map for post parameters
            Map<String, Integer> map = new HashMap<>();
            map.put("uniqueRequestCount", requestMap.size());

            // build the request
            HttpEntity<Map<String, Integer>> entity = new HttpEntity<>(map, headers);
            // send POST request
            ResponseEntity<String> response = restTemplate.postForEntity(endpoint, entity, String.class);
            log.info("Final POST HttpStatus Code => {}", response.getStatusCode());
        } catch (Exception ex) {
            ex.printStackTrace();
            return "servererror";
        }
        return "ok";
    }

}
