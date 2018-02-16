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
package com.laurt.guava.collections;

import com.google.common.collect.*;

import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * <p>Title: MultisetSample
 * <p>Description: tutorials
 * <p>Copyright: 2017/9/28 
 * <p>Company: rongshu
 * <p>author: LiuQingqing
 * <p>package: com.laurt.guava.collections
 *
 * @version v1.0.0
 */
public class MultisetSample {


    TreeMultiset<Long> data1;
    ImmutableSortedMultiset<Long> data2;
    TreeSet<Long> data3;

    RangeSet<Integer> data4 = null;
    long ts;

    public MultisetSample() {

        data1 = TreeMultiset.create();
        Random random = new Random();
        data3 = new TreeSet<>();
        ts = System.currentTimeMillis();

        for (int i = 0; i < 10000000; i++) {
            long v = Math.abs(random.nextLong());
            data1.add(v);

        }
        System.out.println("TreeMultiset-Create -> " + (System.currentTimeMillis() - ts) + "ms");

        ts = System.currentTimeMillis();
        data2 = ImmutableSortedMultiset.copyOf(data1);
        System.out.println("ImmutableSortedMultiset-Create -> " + (System.currentTimeMillis() - ts) + "ms");

        ts = System.currentTimeMillis();
        for (int i = 0; i < 10000000; i++) {
            long v = Math.abs(random.nextLong());
            data3.add(v);
        }
        System.out.println("TreeSet-Create -> " + (System.currentTimeMillis() - ts) + "ms");

        data4 = TreeRangeSet.create();
        long origin = Math.abs(random.nextLong());
        for (int i = 0; i < 10000000; i++) {
            long v = Math.abs(random.nextLong());
            data3.add(v);
        }



    }

    public static void main(String[] args) {
        MultisetSample sample = new MultisetSample();

        long from = Long.MIN_VALUE + (1 << 10);
        long to = Long.MIN_VALUE + (1 << 20);

        long ts = System.currentTimeMillis();
        sample.sub(sample.data1, from, to);
        System.out.println(" TreeMultiset-Sub -> " + (System.currentTimeMillis() - ts) + "ms");

        ts = System.currentTimeMillis();
        sample.sub(sample.data2, from, to);
        System.out.println(" ImmutableSortedMultiset-Sub -> " + (System.currentTimeMillis() - ts) + "ms");

        ts = System.currentTimeMillis();
        sample.sub(sample.data3, from, to);
        System.out.println(" TreeSet-Sub -> " + (System.currentTimeMillis() - ts) + "ms");
    }

    protected void sub(SortedMultiset<Long> data, long from, long to) {
        SortedMultiset<Long> result = data.subMultiset(from, BoundType.OPEN, to, BoundType.CLOSED);
//        System.out.println(result);
    }

    protected void sub(TreeSet<Long> data, long from, long to) {
        SortedSet<Long> result = data.subSet(from, to);
//        System.out.println(result);
    }
}
