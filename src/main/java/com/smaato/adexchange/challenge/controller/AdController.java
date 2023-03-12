package com.smaato.adexchange.challenge.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/smaato")
public class AdController {

    @Autowired
    private Map<Integer, Integer> requestMap;

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping(value = { "/accept/{id}/{endpoint}" }, method = { RequestMethod.POST, RequestMethod.GET })
    public ResponseEntity<String> pushAd(@PathVariable(name = "id", required = true) Integer id,
            @PathVariable(name = "endpoint") Optional<String> endpoint) {

        Integer retrievedId = requestMap.get(id);
        if (retrievedId != null) {
            requestMap.put(id, retrievedId + 1);
        } else {
            requestMap.put(id, 1);
        }

        if (endpoint.isPresent()) {
            // return new ResponseEntity(id.get(), HttpStatus.OK);
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(endpoint.get(), String.class,
                    Map.of("id", "1"));

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                return responseEntity.getBody();
            } else {
                return null;
            }

            return new ResponseEntity("ok", HttpStatus.OK);
        } else {
            return new ResponseEntity("failed", HttpStatus.OK);
        }

        // return ResponseEntity.ok("ok");

    }

}