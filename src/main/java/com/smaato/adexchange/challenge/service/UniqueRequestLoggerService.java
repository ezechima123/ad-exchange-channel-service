package com.smaato.adexchange.challenge.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class UniqueRequestLoggerService {

    private static final Logger log = LoggerFactory.getLogger(UniqueRequestLoggerService.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    private Map<Integer, Integer> requestMap;

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.MINUTES)
    public void reportCurrentTime() {
        log.info("The Number of unique Request at {} is {}", dateFormat.format(new Date()), requestMap.size());
        log.info("The Map Result is {}", requestMap);
        // requestMap.clear();
    }
}