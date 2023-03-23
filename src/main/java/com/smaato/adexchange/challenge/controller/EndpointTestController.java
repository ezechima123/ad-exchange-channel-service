package com.smaato.adexchange.challenge.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class EndpointTestController {

    @GetMapping("/ping")
    public ResponseEntity<String> replyMessage(
            @RequestParam(value = "uniqueRequestCount", required = true) String uniqueRequestCount) {
        return ResponseEntity.status(HttpStatus.OK).body("UniqueId Request Count is " + uniqueRequestCount);

    }

    @PostMapping("/ping")
    public ResponseEntity<String> processMessage(@RequestBody Map<String, String> requestDto) {

        String uniqueIdCount = requestDto.get("uniqueRequestCount");
        if (uniqueIdCount != null) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body("UniqueId Request Count is " + requestDto.get("uniqueRequestCount"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("UniqueId Request Parameter Not found ");
        }
    }

}
