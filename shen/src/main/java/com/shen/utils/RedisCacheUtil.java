package com.shen.utils;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 操作 hash 的基本操作
 * 
 * @author sy
 * 有额外需求 可以在这个网站  http://blog.csdn.net/u011911084/article/details/53435172
 */
@Component("redisCache")
public class RedisCacheUtil {

	@Resource
	private StringRedisTemplate redisTemplate;

	/**
	 * 获取缓存的地址
	 * 
	 * @param cacheKey
	 * @return
	 */
	public String getCacheValue(String cacheKey) {
		String cacheValue = (String) redisTemplate.opsForValue().get(cacheKey);
		return cacheValue;
	}

	/**
	 * 设置缓存值
	 * 
	 * @param key
	 * @param value
	 */
	public void setCacheValue(String key, String value) {
		redisTemplate.opsForValue().set(key, value);
	}

	/**
	 * 设置缓存值并设置有效期
	 * 
	 * @param key
	 * @param value
	 */
	public void setCacheValueForTime(String key, String value, long time) {
		redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
	}

	/**
	 * 删除key值
	 * 
	 * @param key
	 */
	public void delCacheByKey(String key) {
		redisTemplate.opsForValue().getOperations().delete(key);
		redisTemplate.opsForHash().delete("");
	}

	/**
	 * 获取token的有效期
	 * 
	 * @param key
	 */
	public long getExpireTime(String key) {
		long time = redisTemplate.getExpire(key);
		return time;
	}

	/**
	 * 指定时间类型---秒
	 * 
	 * @param key
	 * @return
	 */
	public long getExpireTimeType(String key) {
		long time = redisTemplate.getExpire(key, TimeUnit.SECONDS);
		return time;
	}

	/**
	 * 
	 * @param key---分
	 * @return
	 */
	public long getExpireTimeTypeForMin(String key) {
		long time = redisTemplate.getExpire(key, TimeUnit.MINUTES);
		return time;
	}

	/**
	 * 设置一个自增的数据
	 * 
	 * @param key
	 * @param growthLength
	 */
	public void testInc(String key, Long growthLength) {
		redisTemplate.opsForValue().increment(key, growthLength);
	}

}