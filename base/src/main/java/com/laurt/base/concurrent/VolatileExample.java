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
 * <p>Title: VolatileExample
 * <p>Description: tutorials
 * <p>Copyright: 2017/9/28 上午10:43
 * <p>Company: rongshu
 * <p>author: LiuQingqing
 * <p>package: com.laurt.base.concurrent
 *
 * @version v1.0.0
 */
class VolatileExample {
    int x = 0;
    volatile boolean v = false;

    public void writer() {
        x = 42;
        v = true;
    }

    public int reader() {
        if (v == true) {
            //uses x - guaranteed to see 42.
            System.out.println(Thread.currentThread().getId() + " OK!");
            return x;
        }
        return 0;
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        int i = 500;
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        VolatileExample volatileExample = new VolatileExample();
        executorService.submit(() -> {
            while (true) {
                int v = volatileExample.reader();
                if (v != 0) {
                    System.err.println(Thread.currentThread().getId() + " -> " + v);
                    break;
                }
            }
            return volatileExample.reader();
        });

        TimeUnit.MILLISECONDS.sleep(2);

        executorService.submit(() -> {
            volatileExample.writer();
            System.out.println(Thread.currentThread().getId() + " -> w");
            if (!executorService.isShutdown()) {
                executorService.shutdown();
            }
        });
        if (!executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}
