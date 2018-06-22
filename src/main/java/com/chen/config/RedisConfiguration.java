package com.chen.config;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

import java.util.ArrayList;

/**
 * RedisConfiguration
 * @Author LeifChen
 * @Date 2018-06-11
 */
@Configuration
@Slf4j
public class RedisConfiguration {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    public RedisConfiguration() {
    }

    @Bean(name = "shardedJedisPool")
    public ShardedJedisPool shardedJedisPool() {
        log.info("Redis：" + host + ":" + port);
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        ArrayList<JedisShardInfo> jedisShardInfos = Lists.newArrayList();
        jedisShardInfos.add(new JedisShardInfo(host, port));
        return new ShardedJedisPool(jedisPoolConfig, jedisShardInfos);
    }
}
