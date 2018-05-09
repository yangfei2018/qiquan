package com.cjy.qiquan.cache.local;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import com.cjy.qiquan.utils.CustomJobFactory;

public class LocalCacheManager {
    private static final int DEFUALT_VALIDITY_TIME = 60;//默认过期时间 60秒  
    private static final Map<String, CacheEntity> map;  
//    private static Map<Integer, ScheduledFuture<?>> TimeFutures = new ConcurrentHashMap<Integer, ScheduledFuture<?>>();
    static{  
    	map = new  ConcurrentHashMap<String, CacheEntity>();  
    }  
              
    /** 
     * 增加缓存对象 
     * @param key 
     * @param ce 
     */  
    public static void addCache(String key, CacheEntity ce){  
        addCache(key, ce, DEFUALT_VALIDITY_TIME);  
    }  
      
    /** 
     * 增加缓存对象 
     * @param key 
     * @param ce 
     * @param validityTime 有效时间 
     */  
    public static synchronized void addCache(String key, CacheEntity ce, int validityTime){  
        map.put(key, ce);  
        //添加过期定时  
        CustomJobFactory.getInstance().addDelayJob(new TimeoutTimerTask(key), validityTime, TimeUnit.SECONDS);
    }  
      
    /** 
     * 获取缓存对象 
     * @param key 
     * @return 
     */  
    public static synchronized CacheEntity getCache(String key){  
        return map.get(key);  
    }  
      
    /** 
     * 检查是否含有制定key的缓冲 
     * @param key 
     * @return 
     */  
    public static synchronized boolean isConcurrent(String key){  
        return map.containsKey(key);  
    }  
      
    /** 
     * 删除缓存 
     * @param key 
     */  
    public static synchronized void removeCache(String key){  
        map.remove(key);  
    }  
      
    /** 
     * 获取缓存大小 
     * @param key 
     */  
    public static int getCacheSize(){  
        return map.size();  
    }  
      
    /** 
     * 清除全部缓存 
     */  
    public static synchronized void clearCache(){
        map.clear(); 
        System.out.println("clear cache");  
    }  
      
    
    
    
    
    static class TimeoutTimerTask implements Runnable {
        private String ceKey ;  
          
        public TimeoutTimerTask(String key){  
            this.ceKey = key;  
        }  
  
        @Override  
        public void run() {  
        	LocalCacheManager.removeCache(ceKey);  
            System.out.println("remove : "+ceKey);  
        }  
    }  
}
