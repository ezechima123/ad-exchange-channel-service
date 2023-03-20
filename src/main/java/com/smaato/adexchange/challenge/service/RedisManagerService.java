package com.smaato.adexchange.challenge.service;

import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RedisManagerService {
    @Autowired
    RedissonClient redissonClient;

}
