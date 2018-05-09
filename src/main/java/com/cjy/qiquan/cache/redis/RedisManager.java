package com.cjy.qiquan.cache.redis;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.cjy.qiquan.cache.CacheManager;
import com.cjy.qiquan.utils.Constant;
import com.cjy.qiquan.utils.Serializer;
import com.cjy.qiquan.utils.properties.Prop;
import com.cjy.qiquan.utils.properties.Proper;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisManager extends CacheManager {

	private static JedisPool pool = null;
	static {
		destory();
		Prop redisConfig = Proper.use("redis.properties");
		System.out.println("redis配置文件加载:" + redisConfig);
		// 连接池配置
		JedisPoolConfig config = new JedisPoolConfig();
		config.setTestOnBorrow(true);
		config.setTestWhileIdle(true);
		// config.setMaxActive(redisConfig.maxConn());
		// config.setMaxIdle(redisConfig.maxIdle());
		// config.setMaxWait(1000);
		// config.setTestOnBorrow(true);
		// config.setTestWhileIdle(true);

		pool = new JedisPool(config, redisConfig.get("address"), redisConfig.getInt("port"),
				redisConfig.getInt("timeout"), redisConfig.get("password"), redisConfig.getInt("database"));
		//

	}

	private String getRedisKey(String group, String key) {
		return group + Constant.CONNECTOR + key;
	}

	@SuppressWarnings("unchecked")
	public <T> T getCache(String group, String key) {
		String jkey = getRedisKey(group, key);
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			T object = null;
			object = (T) Serializer.unserialize(jedis.get(jkey.getBytes()));
			return object;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnJedis(jedis);
		}
		return null;
	}

	public <T> void addCache(String group, String key, T t) {
		if (t == null) {
			return;
		}
		String jkey = getRedisKey(group, key);
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			byte[] date = Serializer.serialize(t);
			jedis.set(jkey.getBytes(), date);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnJedis(jedis);
		}
	}

	public long incrKey(String group, String key, int sec) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			String jkey = getRedisKey(group, key);
			long r = jedis.incr(jkey);
			jedis.expire(jkey, sec);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnJedis(jedis);
		}
		return 0l;
	}

	public void removeCache(String group, String key) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			String jkey = getRedisKey(group, key);
			jedis.del(jkey.getBytes());
		} catch (Exception e) {

		} finally {
			returnJedis(jedis);
		}
	}

	public Set<String> getCacheKeyLikes(String group, String key) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			Set<String> keySet = jedis.keys(group + Constant.CONNECTOR + key);
			return keySet;
		} catch (Exception e) {

		} finally {
			returnJedis(jedis);
		}
		return null;
	}

	public long setzAdd(String key, double score, String member) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			long re = jedis.zadd(key, score, member);
			return re;
		} catch (Exception e) {

		} finally {
			returnJedis(jedis);
		}
		return -1l;
	}

	public Set<String> zrevrange(String key, int limit) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			Set<String> list = jedis.zrevrange(key, 0, limit);
			return list;
		} catch (Exception e) {
		} finally {
			returnJedis(jedis);
		}
		return new HashSet<>();
	}

	public long zrevrank(String key, String member) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			long re = jedis.zrevrank(key, member);
			return re;
		} catch (Exception e) {

		} finally {
			returnJedis(jedis);
		}
		return -1l;
	}

	public long setAdd(String key, String members) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			long re = jedis.sadd(key, members);
			return re;
		} catch (Exception e) {

		} finally {
			returnJedis(jedis);
		}
		return -1l;
	}

	public long hashSet(String key, String field, String value) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.hset(key, field, value);
		} catch (Exception e) {
		} finally {
			returnJedis(jedis);
		}

		return -1l;
	}

	/**
	 * 获取HASH表的值 不反序列化
	 */
	public String hashGet(String key, String field) {
		String result = null;
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			result = jedis.hget(key, field);

		} catch (Exception e) {
		} finally {
			returnJedis(jedis);
		}
		return result;
	}

	public Map<String, String> hashGetAll(String key) {
		Map<String, String> result = new HashMap<>();
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			result = jedis.hgetAll(key);
		} catch (Exception e) {
		} finally {
			returnJedis(jedis);
		}
		return result;
	}

	public long setRemove(final String key, String members) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			long re = jedis.srem(key, members);
			return re;
		} catch (Exception e) {

		} finally {
			returnJedis(jedis);
		}
		return -1l;
	}

	public boolean sisMember(final String key, final String val) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.sismember(key, val);
		} catch (Exception e) {

		} finally {
			returnJedis(jedis);
		}
		return false;
	}

	public Set<String> sinter(final String key, final String key1) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			Set<String> list = jedis.sinter(key, key1);
			return list;
		} catch (Exception e) {

		} finally {
			returnJedis(jedis);
		}
		return new HashSet<>();
	}

	public Set<String> setMember(final String key) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			Set<String> list = jedis.smembers(key);
			return list;
		} catch (Exception e) {

		} finally {
			returnJedis(jedis);
		}
		return new HashSet<>();
	}

	public void removeGroup(String group) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			Set<String> keySet = jedis.keys(group + Constant.CONNECTOR + '*');
			if (keySet != null && keySet.size() > 0) {
				String[] keys = new String[keySet.size()];
				jedis.del(keySet.toArray(keys));
			}
		} catch (Exception e) {

		} finally {
			returnJedis(jedis);
		}

	}

	public void flushdb() {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			jedis.flushDB();
		} catch (Exception e) {
		} finally {
			returnJedis(jedis);
		}
	}

	void returnJedis(Jedis jedis) {
		if (jedis != null) {
			pool.returnResource(jedis);
		}
	}

	static void destory() {
		if (pool != null) {
			pool.destroy();
		}
	}

	@Override
	public <T> void addCacheEx(String group, String key, T t, int sec) {
		if (t == null) {
			return;
		}
		String jkey = getRedisKey(group, key);
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			byte[] date = Serializer.serialize(t);
			jedis.setex(jkey.getBytes(), sec, date);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnJedis(jedis);
		}

	}
}
