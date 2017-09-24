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
 * <p>Title: TopLauncher
 * <p>Description: tutorials TOP类的执行类
 * <p>Copyright: 2017/9/23 下午5:02
 * <p>Company: rongshu
 * <p>author: LiuQingqing
 * <p>package: com.laurt.base
 *
 * @version v1.0.0
 */
public class TopLauncher {

    public static void main(String[] args) {

        // 顶级类
        Top top = new Top("TopLauncher");

        // 静态嵌套类
        Top.Nested nested = new Top.Nested("TopLauncher");

        nested.print();
    }
}
