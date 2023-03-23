package com.smaato.adexchange.challenge.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtils {

    public static ResponseEntity<String> getResponse(String response) {
        switch (response) {
            case "duplicate":
                return new ResponseEntity<>("failed", HttpStatus.CONFLICT);
            case "ok":
                return new ResponseEntity<>("ok", HttpStatus.OK);
            default:
                return new ResponseEntity<>("failed", HttpStatus.BAD_REQUEST);
        }
    }

}
