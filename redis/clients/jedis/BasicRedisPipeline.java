package redis.clients.jedis;

import java.util.*;

public interface BasicRedisPipeline
{
    Response<String> bgrewriteaof();
    
    Response<String> bgsave();
    
    Response<String> configGet(String p0);
    
    Response<String> configSet(String p0, String p1);
    
    Response<String> configResetStat();
    
    Response<String> save();
    
    Response<Long> lastsave();
    
    Response<String> flushDB();
    
    Response<String> flushAll();
    
    Response<String> info();
    
    Response<List<String>> time();
    
    Response<Long> dbSize();
    
    Response<String> shutdown();
    
    Response<String> ping();
    
    Response<String> select(int p0);
}
