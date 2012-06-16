package com.gaohui.myDfs.storageServer;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * User: Administrator
 * Date: 11-2-10 Time: 下午10:27
 *
 * @author Basten Gao
 */
public class ScheduledThreadPoolExecutorTest_ {
    public static void main(String[] args) {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(4);

        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("run");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 1, TimeUnit.SECONDS);
    }
}
