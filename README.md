# spring-easy-starter
## 项目简介
该项目用于分布式及高并发下 基于Redis及注解实现相关功能；  
1.注解缓存  
2.注解分布式锁  
3.注解分布式限流（基于 google guava 非分布式，redis 分布式） 两种模式  
## 使用
application.properties  
```
#注解缓存配置 
easy.namespace=EasyCache #分布式缓存 key前缀
#刷新缓存相关配置
easy.poolSize=10 
easy.maxPoolSize=20
easy.keepAliveTime=5
easy.queueCapacity=500

#redis配置 分布式缓存 及 分布式锁、分布式限流 也取该配置使用 于spring-data-redis配置一样
spring.redis.database=5
spring.redis.jedis.pool.max-idle=20
spring.redis.jedis.pool.min-idle=1
spring.redis.jedis.pool.max-active=20
spring.redis.jedis.pool.max-wait=-1
# name of Redis server  哨兵监听的Redis server的名称
spring.redis.sentinel.master=mymaster
# comma-separated list of host:port pairs  哨兵的配置列表
spring.redis.sentinel.nodes=xxxx
```

## 限流
`默认type 为 非分布式模式 type = RateLimiterType.SINGLETON_RATELIMITER`
```
@GetMapping("/get")
@RateLimiter(key = "rate:getUser1",limit = 5,expire = 1,fallBackMethod = "getUser1FallBack")
#@RateLimiter(key = "rate:getUser1",limit = 5,expire = 1,fallBackMethod = "getUser1FallBack",type = RateLimiterType.DISTRIBUTED_RATELIMITER)
public String getUser1() {
   return JSON.toJSONString(userService.getUserById(1));
}
public String getUser1FallBack(){
   return "限流降级成功";
}
```

##缓存
1.设置缓存  
```
@Cache(keys ={"USER:getSpelById","#userId"},expire = 1200)
public UserDO getUserById(int userId) {
    UserDO userDO=new UserDO();
    userDO.setUserId(userId);
    userDO.setUserName("easyCache");
    userDO.setAge(20);
    userDO.setPassword("中文");
    return userDO;
}
---------------------------------------------------
@Cache(keys = {"USER:getHashUserById"},hFields ={"#userId"},expire = 1200)
public UserDO getHashUserById(int userId) {
    UserDO userDO=new UserDO();
    userDO.setUserId(userId);
    userDO.setUserName("easyCache");
    userDO.setAge(20);
    userDO.setPassword("123123");
    return userDO;
}
----------------------------------------------------
@Cache(keys = {"USER:queryUser:","#user.userName"})
public List<UserDO> queryUser(UserDO user) {
    List<UserDO> list=new ArrayList<>();
    UserDO userDO=new UserDO();
    userDO.setUserId(1);
    userDO.setUserName(user.getUserName());
    userDO.setAge(20);
    userDO.setPassword("123123");
    list.add(userDO);
    return list;
}
``` 
2.删除缓存
```
@Override
@DelCache(keys = {"USER:getSpelById","#userId"})
public int delUserById(int userId) {
   return 0;
}
```

##分布式锁
```
@ELock(keys = {"and","#userId"},waitTime = 6000L,leaseTime = 6000L,lockType = LockType.Reentrant)
public UserDO getUserById(int userId) {
  UserDO userDO=new UserDO();
  userDO.setUserId(userId);
  userDO.setUserName("easyCache");
  userDO.setAge(20);
  userDO.setPassword("中文");
  return userDO;
}
锁类型
/**
* 可重入锁
*/
Reentrant,
/**
* 公平锁
*/
Fair,
/**
* 读锁
*/
Read,
/**
* 写锁
*/
Write;
```