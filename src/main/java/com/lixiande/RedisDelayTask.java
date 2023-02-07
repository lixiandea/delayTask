package com.lixiande;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.resps.Tuple;

import java.util.Calendar;
import java.util.List;

public class RedisDelayTask {
     static String REDIS_HOST = "localhost";
     static int PORT = 6379;
     static JedisPool redisPool = new JedisPool(REDIS_HOST, PORT);

    static private Jedis getRedis(){
        return redisPool.getResource();
    }
    public void productionDelayMessage() {
        for (int i = 0; i < 5; i++) {
            //延迟3秒
            Calendar cal1 = Calendar.getInstance();
            cal1.add(Calendar.SECOND, 3);
            int second3later = (int) (cal1.getTimeInMillis() / 1000);
            getRedis().zadd("OrderId", second3later, "OID0000001" + i);
            System.out.println(System.currentTimeMillis() + "ms:redis生成了一个订单任务：订单ID为" + "OID0000001" + i);
        }
    }

    public void consumeDelayMessage(){
        Jedis redis = getRedis();
        while (true){
            List<Tuple> items = redis.zrangeWithScores("OrderId", 0, 1);
            if (items == null || items.isEmpty()){
                System.out.println("当前没有等待的任务");
                try {
                    Thread.sleep(500);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                continue;
            }
            int score = (int)((Tuple) items.toArray()[0]).getScore();
            Calendar cal = Calendar.getInstance();
            int nowSecond = (int)(cal.getTimeInMillis()/1000);
            if(nowSecond >= score){
                String orderId = ((Tuple)items.toArray()[0]).getElement();
                redis.zrem("OrderId", orderId);
                System.out.println(System.currentTimeMillis() + " ms 消耗了一个任务，订单为："+orderId);
            }
        }
    }

    public static void main(String[] args) {
         RedisDelayTask task = new RedisDelayTask();
         task.productionDelayMessage();
         task.consumeDelayMessage();
    }
}
