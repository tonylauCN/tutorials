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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * <p>Title: ParallelSample
 * <p>Description: tutorials
 * <p>Copyright: 2017/9/28 下午4:53
 * <p>Company: rongshu
 * <p>author: LiuQingqing
 * <p>package: com.laurt.base.concurrent
 *
 * @version v1.0.0
 */
public class ParallelSample {

    public static void main(String[] args) {

        ParallelSample sample = new ParallelSample();
        // 数据流与循环比较
        sample.run();
        // 并行流与ForkJoin比较(并行流的内部实现就是ForkJoin)
        sample.runForkJoin();
    }


    private void runForkJoin() {
        /************************************************
         * 分支与合并框架的计算 (Java1.7 引入的RecursiveTask)
         ************************************************/
        long ts, sum;
        LongStream stream = LongStream.range(0, 10_000_000);
        long[] numbers = LongStream.range(0, 10_000_000).toArray();

        ts = System.currentTimeMillis();
        sum = stream.parallel().reduce(Long::sum).getAsLong();
        System.out.println(sum);
        System.out.println("parallel-stream took " + (System.currentTimeMillis() - ts) + "ms\n");

        ts = System.currentTimeMillis();
        ForkJoinSumCalculator task = new ForkJoinSumCalculator(numbers);
        sum = new ForkJoinPool().invoke(task);
        System.out.println(sum);
        System.out.println("fork-join took " + (System.currentTimeMillis() - ts) + "ms");
    }

    private void run() {
        /************************************************
         * 1000w数据的创建与求和,用来查看各种方式的性能
         ************************************************/
        // 不同构造方法构建集合，计算求和
        // 循环创建,数据需要装箱
        long ts = System.currentTimeMillis();
        List<Long> data1 = new ArrayList<>(10000000);
        for (Long i = 0L; i < 10000000; i++) {
            data1.add(i);
        }
        System.out.println("init1 took " + (System.currentTimeMillis() - ts) + "ms");

        // 顺序流创建, 如果使用并行流性能大幅度下降，由于iterate很难并行且装箱操作
        ts = System.currentTimeMillis();
        List<Long> data2 = Stream.iterate(1L, i -> i + 1)
                .limit(10000000)
//                .parallel()
                .collect(Collectors.toList());
        System.out.println("init2 took " + (System.currentTimeMillis() - ts) + "ms");

        // 使用LongStream直接创建范围,不存在装箱,使用整体装箱转换,耗时最小.
        ts = System.currentTimeMillis();
        Stream<Long> data3 = LongStream.range(0, 10000000).boxed();
        System.out.println("init3 took " + (System.currentTimeMillis() - ts) + "ms");


        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        ts = System.currentTimeMillis();
        Float value = data1.stream()
                .filter(predicate -> predicate % 2 == 0)
//                .parallel()
                .reduce(0L, Long::sum).floatValue();
        System.out.println(value + " took1 " + (System.currentTimeMillis() - ts) + "ms");

        ts = System.currentTimeMillis();
        value = data2.parallelStream()
                .filter(predicate -> predicate % 2 == 0)
                .parallel()
                .reduce(0L, Long::sum).floatValue();
        System.out.println(value + " took2 " + (System.currentTimeMillis() - ts) + "ms");


        ts = System.currentTimeMillis();
        value = data3
                .filter(predicate -> predicate % 2 == 0)
//                .parallel()
                .reduce(0L, Long::sum).floatValue();
        System.out.println(value + " took3 " + (System.currentTimeMillis() - ts) + "ms");


        ts = System.currentTimeMillis();
        value = 0F;
        for (int i = 0; i < data1.size(); i++) {
            Long v = data1.get(i);
            if (v % 2 == 0) {
                value += data1.get(i).floatValue();
            }
        }
        System.out.println(value + " contrast took  " + (System.currentTimeMillis() - ts) + "ms");
    }


    public class ForkJoinSumCalculator extends java.util.concurrent.RecursiveTask<Long> {

        private final long[] numbers;
        private final int strat;
        private final int end;

        public static final long THRESHOLD = 100000;

        public ForkJoinSumCalculator(long[] numbers) {
            this(numbers, 0, numbers.length);
        }

        private ForkJoinSumCalculator(long[] numbers, int start, int end) {
            this.numbers = numbers;
            this.strat = start;
            this.end = end;
        }

        /**
         * The main computation performed by this task.
         *
         * @return the result of the computation
         */
        @Override
        protected Long compute() {
            int length = end - strat;
            if (length <= THRESHOLD) {
                return computeSequentially();
            }

            ForkJoinSumCalculator leftTask = new ForkJoinSumCalculator(numbers, this.strat, this.strat + length / 2);
            ForkJoinSumCalculator rightTask = new ForkJoinSumCalculator(numbers, this.strat + length / 2, this.end);

            leftTask.fork();
            rightTask.fork();
            long rightValue = rightTask.join();
            long leftValue = leftTask.join();

            return leftValue + rightValue;
        }

        private long computeSequentially() {
            long sum = 0;
            for (int i = strat; i < end; i++) {
                sum += numbers[i];
            }
            return sum;
        }
    }
}

