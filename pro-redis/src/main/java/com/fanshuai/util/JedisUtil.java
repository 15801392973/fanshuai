package com.fanshuai.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Tuple;

import java.util.List;
import java.util.Set;

public class JedisUtil {

    private static String ip1 = "127.0.0.1:6379";
    private static String ip2 = "127.0.0.1:6380";
    private static String ip3 = "127.0.0.1:6381";

    //可用连接实例的最大数目，默认值为8；
    //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
    public static int MAX_ACTIVE = 1024;

    //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
    public static int MAX_IDLE = 200;

    //等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
    public static int MAX_WAIT = 10000;

    public static int TIMEOUT = 10000;

    public static int RETRY_NUM = 5;

    private static JedisPool pool = null;

    static {
        JedisPoolConfig config = getPoolConfig();
        String ip = ip1.split(":")[0];
        String port = ip1.split(":")[1];

        pool = new JedisPool(config, ip, Integer.valueOf(port), TIMEOUT);
    }

    private static JedisPoolConfig getPoolConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxWaitMillis(MAX_WAIT);
        config.setMaxTotal(MAX_ACTIVE);
        config.setMaxIdle(MAX_IDLE);
        config.setTestOnBorrow(true);
        return config;
    }

    public static Jedis getResource() {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
        } catch (Exception e) {
            pool.returnResourceObject(jedis);
        }
        return jedis;
    }

    public static void closeJedis(Jedis jedis) {
        pool.returnResourceObject(jedis);
    }

    public static String get(String key) {
        return getResource().get(key);
    }

    public static void set(String key, String value) {
        getResource().set(key, value);
    }

    public static void expire(String key, int seconds) {
        getResource().expire(key, seconds);
    }

    public static boolean exists(String key) {
        return getResource().exists(key);
    }

    public static String getset(String key, String value) {
        return getResource().getSet(key, value);
    }

    public static boolean setnx(String key, String value) {
        return getResource().setnx(key, value) == 1;
    }

    public static void lpush(String key, String value) {
        getResource().lpush(key, value);
    }

    public static String lpop(String key) {
        return getResource().lpop(key);
    }

    public static long llen(String key) {
        return getResource().llen(key);
    }

    public static List<String> lrange(String key, long start, long end) {
        return getResource().lrange(key, start, end);
    }

    public static void sadd(String key, String value) {
        getResource().sadd(key, value);
    }

    public static boolean sismember(String key, String value) {
        return getResource().sismember(key, value);
    }

    public static long scard(String key) {
        return getResource().scard(key);
    }

    public static Set<String> smembers(String key) {
        return getResource().smembers(key);
    }

    public static void zadd(String key, double score, String value) {
        getResource().zadd(key, score, value);
    }

    public static Set<String> zrange(String key, long start, long end) {
        return getResource().zrange(key, start, end);
    }

    public static Set<String> zrangeByscore(String key, double low, double high) {
        return getResource().zrangeByScore(key, low, high);
    }

    public static Set<Tuple> zrangeWithScores(String key, long start, long end) {
        return getResource().zrangeWithScores(key, start, end);
    }

    public static Set<Tuple> zrangeByScoreWithScores(String key, double min, double max) {
        return getResource().zrangeByScoreWithScores(key, min, max);
    }

    public static long zcard(String key) {
        return getResource().zcard(key);
    }

    public static long zcount(String key, double min, double max) {
        return getResource().zcount(key, min, max);
    }

    public static double zscore(String key, String value) {
        return getResource().zscore(key, value);
    }
}
