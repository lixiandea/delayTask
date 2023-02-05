package com.lixiande;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class DelayQueueTask implements Delayed {
    private String orderId;
    private long timeout;

    @Override
    public int compareTo(Delayed other) {
        if (other == this) {
            return 0;
        }
        DelayQueueTask t = (DelayQueueTask) other;
        long d = (getDelay(TimeUnit.NANOSECONDS) - t.getDelay((TimeUnit.NANOSECONDS)));
        return (d == 0) ? 0 : ((d < 0) ? -1 : 1);
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(timeout - System.nanoTime(), TimeUnit.NANOSECONDS);
    }

    DelayQueueTask(String orderId, long timeout) {
        this.orderId = orderId;
        this.timeout = timeout + System.nanoTime();
    }

    public void executeTask() {
        System.out.println("now task execute: " + this.orderId);
    }

    public static void main(String[] args) {
        List<String> list = new ArrayList<String>();
        list.add("00000001");
        list.add("00000002");
        list.add("00000003");
        list.add("00000004");
        list.add("00000005");
        DelayQueue<DelayQueueTask> queue = new DelayQueue<>();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 5; i++) {
            queue.put(new DelayQueueTask(list.get(i), TimeUnit.NANOSECONDS.convert(3, TimeUnit.SECONDS)));
            try {
                queue.take().executeTask();
                System.out.println("After " + (System.currentTimeMillis() - start) + " MilliSeconds");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
