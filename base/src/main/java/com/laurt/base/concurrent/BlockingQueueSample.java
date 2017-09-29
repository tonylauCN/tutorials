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

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.locks.StampedLock;

/**
 * <p>Title: BlockingQueueSample
 * <p>Description: journey-processor
 * <p>Copyright: 2017/9/27 下午1:46
 * <p>Company: rongshu
 * <p>author: LiuQingqing
 * <p>package: cn.rongcapital.mc2.journey.processor.sampler.itest
 *
 * @version v1.0.0
 */
public class BlockingQueueSample {

    BlockingQueue[] blockingQueue = new ArrayBlockingQueue[5];

    StampedLock lock = new StampedLock();
    Map<Integer, Integer> data = new ConcurrentHashMap<>();


    public static void main(String[] args) {

        /************************************************
         * 阻塞队列
         * 生产者线程生产的数据>可消费的数量,生产者线程阻塞
         * 消费者线程消费数据，无数据时阻塞消费
         ************************************************/
        BlockingQueueSample process = new BlockingQueueSample();
        process.init();


        ExecutorService executor = Executors.newFixedThreadPool(10);
        CompletableFuture.runAsync(() -> {
            Thread.currentThread().setName("write-thread");
            for (int i = 0; i < 5; i++) {
                process.write(i, i << 2);
            }
        }, executor);

        for (int i = 0; i < 5; i++) {
            final int index = i;
            CompletableFuture.runAsync(() -> {
                Thread.currentThread().setName("read1-thread-" + index);
                process.blockingRead(index);
            }, executor);
        }

        for (int i = 0; i < 5; i++) {
            final int index = i;
            CompletableFuture.runAsync(() -> {
                Thread.currentThread().setName("read2-thread-" + index);
                process.blockingRead(index);
            }, executor);
        }
        executor.shutdown();
    }


    private void init() {

        for (int i = 0; i < blockingQueue.length; i++) {
            blockingQueue[i] = new ArrayBlockingQueue(100);
        }
    }

    public void write(int i, int value) {

        try {
            blockingQueue[i].put(System.currentTimeMillis());
            System.out.println("W-Data " + i + " finished!");
            data.put(i, value);
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
        }
    }

    public void blockingRead(int i) {

        long ts;
        try {
            System.out.println("R-Data " + i + " ");
            ts = (long) blockingQueue[i].take();
            blockingQueue[i].put(System.currentTimeMillis()); // notify other threads to start
            System.out.println("R-Data " + i + ":" + ts + " finished!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
        }
    }

}
