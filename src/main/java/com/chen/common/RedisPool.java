package com.chen.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import javax.annotation.Resource;

/**
 * RedisPool
 * @Author LeifChen
 * @Date 2018-06-11
 */
@Service("redisPool")
@Slf4j
public class RedisPool {

    @Resource(name = "shardedJedisPool")
    private ShardedJedisPool shardedJedisPool;

    /**
     * 获取Redis实例
     * @return
     */
    public ShardedJedis instance() {
        return shardedJedisPool.getResource();
    }

    /**
     * 关闭Redis连接
     * @param shardedJedis
     */
    public void safeClose(ShardedJedis shardedJedis) {
        try {
            if (shardedJedis != null) {
                shardedJedis.close();
            }
        } catch (Exception e) {
            log.error("redis resource exception", e);
        }
    }
}
