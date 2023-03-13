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

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/smaato")
@Slf4j
public class AdController {

    @Autowired
    private Map<Integer, Integer> requestMap;

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping(value = { "/accept/{id}/{endpoint}" }, method = { RequestMethod.GET })
    public ResponseEntity<String> pushAdvert(
            @PathVariable(name = "id", required = true) Integer id,
            @PathVariable(name = "endpoint") Optional<String> endpoint) {

        try {

            Integer retrievedId = requestMap.get(id);
            if (retrievedId != null) {
                requestMap.put(id, retrievedId + 1);
            } else {
                requestMap.put(id, 1);
            }

            log.info(null);

            if (endpoint.isPresent()) {

                ResponseEntity<String> responseEntity = restTemplate.getForEntity(
                        endpoint.get() + "/" + requestMap.size(), String.class,
                        Map.of("id", "1"));
                log.info("The HttpStatus Code is {}", responseEntity.getStatusCode());

                return new ResponseEntity("ok", HttpStatus.OK);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity("failed", HttpStatus.EXPECTATION_FAILED);
        }

        return ResponseEntity.ok("ok");
    }

}