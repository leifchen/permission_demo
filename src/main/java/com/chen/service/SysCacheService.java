package com.chen.service;

import com.chen.common.RedisPool;
import com.chen.constant.CacheKeyConstant;
import com.chen.util.JsonMapperUtil;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import redis.clients.jedis.ShardedJedis;

import javax.annotation.Resource;

/**
 * 缓存Service
 * @Author LeifChen
 * @Date 2018-06-11
 */
@Service
@Slf4j
public class SysCacheService {

    @Resource(name = "redisPool")
    private RedisPool redisPool;

    /**
     * 保存单值
     * @param toSaveValue
     * @param timeoutSeconds
     * @param prefix
     */
    public void saveCache(String toSaveValue, int timeoutSeconds, CacheKeyConstant prefix) {
        saveCache(toSaveValue, timeoutSeconds, prefix,"");
    }

    /**
     * 保存多值
     * @param toSaveValue
     * @param timeoutSeconds
     * @param prefix
     * @param keys
     */
    public void saveCache(String toSaveValue, int timeoutSeconds, CacheKeyConstant prefix, String... keys) {
        if (toSaveValue == null) {
            return;
        }

        ShardedJedis shardedJedis = null;
        try {
            String cacheKey = generateCacheKey(prefix, keys);
            shardedJedis = redisPool.instance();
            shardedJedis.setex(cacheKey, timeoutSeconds, toSaveValue);
        } catch (Exception e) {
            log.error("save cache exception, prefix:{}, keys:{}", prefix.name(), JsonMapperUtil.obj2String(keys));
        } finally {
            redisPool.safeClose(shardedJedis);
        }
    }

    /**
     * 取值
     * @param prefix
     * @param keys
     * @return
     */
    public String getFromCache(CacheKeyConstant prefix, String... keys) {
        ShardedJedis shardedJedis = null;
        String cacheKey = generateCacheKey(prefix, keys);
        try {
            shardedJedis = redisPool.instance();
            return shardedJedis.get(cacheKey);
        } catch (Exception e) {
            log.error("get from cache exception, prefix:{}, keys:{}", prefix.name(), JsonMapperUtil.obj2String(keys));
            return null;
        } finally {
            redisPool.safeClose(shardedJedis);
        }
    }

    /**
     * 自动生成CacheKey
     * @param prefix
     * @param keys
     * @return
     */
    private String generateCacheKey(CacheKeyConstant prefix, String... keys) {
        String key = prefix.name();
        if (keys != null && keys.length > 0) {
            key += "_" + Joiner.on("_").join(keys);
        }
        return key;
    }
}
