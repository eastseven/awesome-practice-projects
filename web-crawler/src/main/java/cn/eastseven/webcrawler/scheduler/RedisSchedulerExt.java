package cn.eastseven.webcrawler.scheduler;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.JedisPool;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.scheduler.RedisScheduler;

@Slf4j
public class RedisSchedulerExt extends RedisScheduler {

    public RedisSchedulerExt(JedisPool pool) {
        super(pool);
    }

    @Override
    public boolean isDuplicate(Request request, Task task) {
        Object ignore = request.getExtra("ignore");
        if (ignore != null && Boolean.TRUE.equals(ignore)) return false;

        boolean bln = super.isDuplicate(request, task);
        return bln;
    }
}