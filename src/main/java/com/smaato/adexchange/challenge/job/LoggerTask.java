package com.smaato.adexchange.challenge.job;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class LoggerTask {

    private static final Logger log = LoggerFactory.getLogger(LoggerTask.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    private Map<Integer, Integer> requestMap;

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.MINUTES)
    public void reportCurrentTime() {
        log.info("The time is now {}", dateFormat.format(new Date()));
        log.info(requestMap.toString());
        requestMap.clear();
    }
}