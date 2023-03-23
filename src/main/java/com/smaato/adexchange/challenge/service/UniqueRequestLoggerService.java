package com.smaato.adexchange.challenge.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class UniqueRequestLoggerService {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    private ConcurrentHashMap<Integer, String> requestMap;
    private static final Logger log = LogManager.getLogger(UniqueRequestLoggerService.class);

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.MINUTES)
    public void printNumberOfUniqueId() {
        log.info("The Number of unique Request at {} is {}", dateFormat.format(new Date()), requestMap.size());
        requestMap.clear();
    }
}