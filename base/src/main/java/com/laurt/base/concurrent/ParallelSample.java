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



}
