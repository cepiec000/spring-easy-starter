package com.easy.constant;

/**
 * @author chendd
 * @Date Created in 2018/11/20 09:46
 * @Description:
 */
public class EasyConstant {
    public static final int DEFAULT_ORDER = 1000;
    //分布式锁是否启动
    public static boolean EASY_LOCK_START = false;
    //分布式限流 Lua脚本
    public static String RATE_LIMITER_LUA="local key1 = KEYS[1]\n" +
            "local val = redis.call('incr', key1)\n" +
            "local ttl = redis.call('ttl', key1)\n" +
            "local expire = ARGV[1]\n" +
            "local times = ARGV[2]\n" +
            "redis.log(redis.LOG_DEBUG,tostring(times))\n" +
            "redis.log(redis.LOG_DEBUG,tostring(expire))\n" +
            "redis.log(redis.LOG_NOTICE, \"incr \"..key1..\" \"..val);\n" +
            "if val == 1 then\n" +
            "    redis.call('expire', key1, tonumber(expire))\n" +
            "else\n" +
            "    if ttl == -1 then\n" +
            "        redis.call('expire', key1, tonumber(expire))\n" +
            "    end\n" +
            "end\n" +
            "if val > tonumber(times) then\n" +
            "    return 0\n" +
            "end\n" +
            "return 1";
}
