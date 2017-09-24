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
package com.laurt.base;

/**
 * <p>Title: Top
 * <p>Description: tutorials嵌套类
 * <p>Copyright: 2017/9/23 下午4:53
 * <p>Company: rongshu
 * <p>author: LiuQingqing
 * <p>package: com.laurt.base
 *
 * @version v1.0.0
 */
public class Top {

    private int limit;

    private static final int SIZE = 1 << 1;

    public Top(String input) {
        System.out.println(input + " -> Top");
        // 创建内嵌类
        Top.Nested nested = new Top.Nested("Top");

        // 外嵌类可以访问内嵌类的私有变量
        nested.limit++;
    }

    /**
     * 静态类
     * 静态类可以访问外嵌类的所有静态变量和方法，包含私有的
     * 静态类可以创建外嵌类,并且可以获得外嵌类的私有变量
     */
    static class Nested {

        private int limit;
        private static final int SIZE = 1 << 2;

        Nested(String input) {
            System.out.println(input + " -> Top.Nested");
        }

        void print() {
            // 创建外嵌类
            Top top = new Top("Top.Nested");
            // 访问外嵌类的私有变量
            top.limit = limit + SIZE + Top.SIZE;
            System.out.println(++top.limit);
        }
    }
}

