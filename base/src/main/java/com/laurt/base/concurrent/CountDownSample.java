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

import java.util.concurrent.*;

/**
 * <p>Title: CountDownSample
 * <p>Description: tutorials
 * <p>Copyright: 2017/10/2 上午8:10
 * <p>Company: rongshu
 * <p>author: LiuQingqing
 * <p>package: com.laurt.base.concurrent
 *
 * @version v1.0.0
 */
public class CountDownSample {

    public static void main(String[] args)  {

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        try {
            executorService.submit(new ConcurrentExector());
            executorService.submit(new ConcurrentExector());
            countDownLatch.await();
            System.out.println("finished!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
    }

    static CountDownLatch countDownLatch = new CountDownLatch(2);

    static class ConcurrentExector implements Runnable {
        /**
         * When an object implementing interface <code>Runnable</code> is used
         * to create a thread, starting the thread causes the object's
         * <code>run</code> method to be called in that separately executing
         * thread.
         * <p>
         * The general contract of the method <code>run</code> is that it may
         * take any action whatsoever.
         *
         * @see Thread#run()
         */
        @Override
        public void run() {
            try {
                TimeUnit.MILLISECONDS.sleep(10);
                System.out.println(Thread.currentThread().getName());
                countDownLatch.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
