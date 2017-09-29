/*
 * Copyright 2012-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.laurt.base.concurrent;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.StampedLock;

/**
 * <p>Title: dsada
 * <p>Description: journey-processor
 * <p>Copyright: 2017/9/26 下午6:34
 * <p>Company: rongshu
 * <p>author: LiuQingqing
 * <p>package: cn.rongcapital.mc2.journey.processor.sampler.itest
 *
 * @version v1.0.0
 */
public class StampedLockDemo {

    public StampedLockDemo() {

    }

    // -------- 读写锁
    StampedLock[] locks = new StampedLock[5];
    Map<Integer, Integer> data = new HashMap<>();

    public static void main(String[] args) {

        StampedLockDemo currency = new StampedLockDemo();
        currency.init();

        ExecutorService executor = Executors.newFixedThreadPool(10);

        CompletableFuture.runAsync(() -> {
            Thread.currentThread().setName("write-thread");
            for (int i = 0; i < 5; i++) {
                currency.write(i, i << 2);
            }
        }, executor);

        for (int i = 0; i < 5; i++) {
            final int index = i;
            CompletableFuture.runAsync(() -> {
                Thread.currentThread().setName("read-thread-" + index);
                currency.optimisticRead(index);
            }, executor);
        }
        executor.shutdown();
    }

    private void init() {
        for (int i = 0; i < locks.length; i++) {
            locks[i] = new StampedLock();
//                data.put(i, i << 2);
        }
    }

    public void write(int i, int value) {
        StampedLock lock = locks[i];
        long stamp = lock.writeLock();
        try {
            System.out.println("W-Lock[" + i + "] Origin: " + data.get(i) + " -> " + value + "]");
            data.put(i, value);
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlockWrite(stamp);
        }
    }

    public void optimisticRead(int i) {

        int value = 0;
        long stamp = locks[i].tryOptimisticRead();
        try {
            Thread.sleep(110);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Optimistic-Read[" + i + "]");
        if (!locks[i].validate(stamp)) {
            stamp = locks[i].readLock();
            try {
                value = data.get(i);
                Thread.sleep(10);
                System.out.println("R-Lock[" + i + "]");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                locks[i].unlockRead(stamp);
            }
        }

//            while (value == 0) {
//                try {
//                    wait();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
        System.out.println(" Read Value[" + i + "] Value: " + value);
    }
}
