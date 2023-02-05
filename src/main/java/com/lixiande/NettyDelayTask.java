package com.lixiande;

import java.util.concurrent.TimeUnit;

import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;

public class NettyDelayTask {
    static class MyTimerTask implements TimerTask {
        boolean flag;

        MyTimerTask(boolean flag) {
            this.flag = flag;
        }

        @Override
        public void run(Timeout timeout) throws Exception {
            System.out.println("删除数据库数据");
            this.flag = false;
        }

    }

    public static void main(String[] args) {
        MyTimerTask task = new MyTimerTask(true);
        Timer timer = new HashedWheelTimer();
        timer.newTimeout(task, 5, TimeUnit.SECONDS);
        int i = 1;
        while (task.flag) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(i++ + "秒过去了");
        }
    }
}
