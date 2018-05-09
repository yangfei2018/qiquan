package com.cjy.qiquan.cache;

import java.util.Map;
import java.util.Set;

import com.cjy.qiquan.cache.redis.RedisManager;


public abstract class CacheManager {
	
	public final static CacheManager instance;

	  static {
	    CacheManager cacheManager = null;
	    cacheManager = new RedisManager();
	    
	    instance = cacheManager;
	  }
	  
public abstract <T> T getCache(String group, String key);
	  
	  public abstract <T> void addCache(String group, String key, T t);
	  
	  public abstract <T> void addCacheEx(String group, String key, T t,int sec);
	  
	  public abstract void removeCache(String group, String key);
	  
	  public abstract void removeGroup(String group);
	  
	  public abstract Set<String> getCacheKeyLikes(String group, String key);
	  
	  public abstract long setAdd(String key,String members);
	  
	  public abstract long setRemove(final String key,String members);
	  
	  public abstract Set<String> setMember(final String key);
	  
	  public abstract boolean sisMember(final String key,final String val);
	  
	  public abstract long hashSet(String key, String field, String value);
	  
	  public abstract String hashGet(String key, String field);
	  
	  public abstract Set<String> sinter(final String key,final String key1);
	  
	  public abstract long setzAdd(String key,double score,String member);
		
	  public abstract Set<String> zrevrange(String key,int limit);
	  
	  public abstract long zrevrank(String key,String member);
	  
	  public abstract void flushdb();
	  
	  public abstract Map<String, String> hashGetAll(String key);
	  
	  public abstract long incrKey(String group,String key,int sec);
}
