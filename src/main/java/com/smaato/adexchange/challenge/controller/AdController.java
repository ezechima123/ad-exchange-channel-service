package com.smaato.adexchange.challenge.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.smaato.adexchange.challenge.service.RedisManagerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@Tag(name = "advert", description = "Tech Challenge Advert API")
public class AdController {

    @Autowired
    private String getFireType;
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    RedisManagerService redisManagerService;

    @Autowired
    private ConcurrentHashMap<Integer, Integer> requestMap;

    @GetMapping("/api/smaato/accept")
    @Operation(summary = "Find book by ID", description = "Returns ok or failed response depending on the processing", tags = {
            "advert" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Book not found", content = @Content) })
    public @ResponseBody String pushAdvert(
            @Parameter(description = "Id of the Advert,Cannot be empty") @RequestParam(value = "id", required = true) Integer id,
            @Parameter(description = "HTTP Endpoint, its Optional") @RequestParam(value = "endpoint") Optional<String> endpoint) {

        try {

            log.info("Got Advert Request from API with id {} and Endpoint {}", id, endpoint);

            Integer retrievedId = requestMap.get(id);
            if (retrievedId != null) {
                requestMap.put(id, retrievedId + 1);
            } else {
                requestMap.put(id, 1);
            }

            if (endpoint.isPresent()) {

                if (getFireType.equalsIgnoreCase("GET")) {
                    ResponseEntity<String> responseEntity = restTemplate.getForEntity(
                            endpoint.get() + "?uniqueRequestIdCount=" + requestMap.size(), String.class);

                    log.info("GET HttpStatus Code => {}", responseEntity.getStatusCode());
                } else {

                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

                    // create a map for post parameters
                    Map<String, Integer> map = new HashMap<>();
                    map.put("uniqueRequestIdCount", requestMap.size());

                    // build the request
                    HttpEntity<Map<String, Integer>> entity = new HttpEntity<>(map, headers);
                    // send POST request
                    ResponseEntity<String> response = restTemplate.postForEntity(endpoint.get(), entity, String.class);
                    log.info("POST HttpStatus Code => {}", response.getStatusCode());
                }

            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return "failed";
        }

        return "ok";
    }

}