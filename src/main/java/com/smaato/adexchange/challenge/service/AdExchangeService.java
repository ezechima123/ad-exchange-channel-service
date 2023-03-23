package com.smaato.adexchange.challenge.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger log = LoggerFactory.getLogger(AdExchangeService.class);

    public String handleOperation(Integer id, Optional<String> endpoint) {

        // About to acquire Lock on redis to avoid duplication across instances
        try {
            if (redisManagerService.acquireLock(id.toString())) {
                // insert id and Endpoint to Database (with Unique Contraint) or
                // ConcurrentHashMap(Test purposes)
                String retrievedId = requestMap.get(id);
                if (retrievedId != null) {
                    return "duplicate";
                } else {
                    requestMap.put(id, endpoint.orElse("No EndPoint Provided"));
                }
            } else {
                return "duplicate";
            }
        } finally {
            redisManagerService.unlock(id.toString());
        }

        if (endpoint.isPresent()) {
            if (extenstionHttpGetOrPost.equalsIgnoreCase("GET")) {
                return handleGetRequest(endpoint.get());
            } else if (extenstionHttpGetOrPost.equalsIgnoreCase("POST")) {
                return handlePostRequest(endpoint.get());
            } else {
                String response = handleGetRequest(endpoint.get());
                if (response.equals("ok")) {
                    return handlePostRequest(endpoint.get());
                } else {
                    return response;
                }
            }
        }

        return "ok";
    }

    private String handleGetRequest(String endpoint) {
        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(
                    endpoint + "?uniqueRequestCount=" + requestMap.size(), String.class);
            log.info("GET HttpStatus Code => {}", responseEntity.getStatusCode());
        } catch (Exception ex) {
            return "failed";
        }
        return "ok";
    }

    private String handlePostRequest(String endpoint) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            // create a map for post parameters
            Map<String, Integer> map = new HashMap<>();
            map.put("uniqueRequestCount", requestMap.size());

            // build the request
            HttpEntity<Map<String, Integer>> entity = new HttpEntity<>(map, headers);
            // send POST request
            ResponseEntity<String> response = restTemplate.postForEntity(endpoint, entity, String.class);
            log.info("POST HttpStatus Code => {}", response.getStatusCode());
        } catch (Exception ex) {
            return "failed";
        }
        return "ok";
    }

}
