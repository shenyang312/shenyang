package com.shen.utils;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 操作 hash 的基本操作
 * 
 * @author sy
 */
@Component("redisCacheSurfaceUtil")
public class RedisCacheSurfaceUtil {

	@Resource
	private StringRedisTemplate redisTemplate;

	/**
	 * 向Hash中添加值
	 * 
	 * @param key
	 *            可以对应数据库中的表名
	 * @param field
	 *            可以对应数据库表中的唯一索引
	 * @param value
	 *            存入redis中的值
	 */
	public void hset(String key, String field, String value, long time) {
		if (key == null || "".equals(key)) {
			return;
		}
		// String versionVal = (String)
		// redisTemplate.boundHashOps(key).get(field);
		Long expire = redisTemplate.boundHashOps(key).getExpire();
		System.out.println("redis有效时间：" + expire + "S");
		// 将结果放入缓存，注意先后顺序（先设置值，再设置过期时间），否则过期时间不生效
		redisTemplate.opsForHash().put(key, field, value);
		if (time != 0) {
			System.out.println("redis缓存没有数据，重新获取后设置缓存");
			// 设置该库的到期时间
			redisTemplate.expire(key, time, TimeUnit.SECONDS);
		}
	}

	/**
	 * 从redis中取出值
	 * 
	 * @param key
	 * @param field
	 * @return
	 */
	public String hget(String key, String field) {
		if (key == null || "".equals(key)) {
			return null;
		}
		return (String) redisTemplate.opsForHash().get(key, field);
	}

	/**
	 * 判断 是否存在 key 以及 hash key
	 * 
	 * @param key
	 * @param field
	 * @return
	 */
	public boolean hexists(String key, String field) {
		if (key == null || "".equals(key)) {
			return false;
		}
		return redisTemplate.opsForHash().hasKey(key, field);
	}

	/**
	 * 查询 key中对应多少条数据
	 * 
	 * @param key
	 * @return
	 */
	public long hsize(String key) {
		if (key == null || "".equals(key)) {
			return 0L;
		}
		return redisTemplate.opsForHash().size(key);
	}

	/**
	 * 删除
	 * 
	 * @param key
	 * @param field
	 */
	public void hdel(String key, String field) {
		if (key == null || "".equals(key)) {
			return;
		}
		redisTemplate.opsForHash().delete(key, field);
	}
}