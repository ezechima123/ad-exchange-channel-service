package com.smaato.adexchange.challenge.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smaato.adexchange.challenge.service.AdExchangeService;
import com.smaato.adexchange.challenge.util.ResponseUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RestController
@RequestMapping("/api")
public class AdExchangeController {

    @Autowired
    AdExchangeService adExchangeService;

    private static final Logger log = LogManager.getLogger(AdExchangeController.class);

    @GetMapping("/smaato/accept")
    @Operation(summary = "Handle Operation by Id and Optional Endpoint", description = "Return ok or failed values depending on the operation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content),
            @ApiResponse(responseCode = "409", description = "Duplicate Id supplied", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })

    public ResponseEntity<String> processId(
            @Parameter(description = "Request Id") @RequestParam(value = "id", required = true) Integer id,
            @Parameter(description = "Optional Http Endpoint") @RequestParam(value = "endpoint") Optional<String> endpoint) {

        log.debug("Query  with Id {} and Endpoint {} parameters received from client", id, endpoint);

        return ResponseUtils.getResponse(adExchangeService.handleOperation(id, endpoint));
    }

}