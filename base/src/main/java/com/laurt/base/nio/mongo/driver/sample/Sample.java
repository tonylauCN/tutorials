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
package com.laurt.base.nio.mongo.driver.sample;

import java.io.IOException;

/**
 * <p>Title: Sample
 * <p>Description: tutorials
 * <p>Copyright: 2017/10/10 下午4:05
 * <p>Company: rongshu
 * <p>author: LiuQingqing
 * <p>package: com.laurt.base.nio.mongo.driver.sample
 *
 * @version v1.0.0
 */
public class Sample {

    public static void main(String[] args) throws IOException {

        ConnectionFactory connectionFactory = new ConnectionFactory();
        Connection connection = connectionFactory.build(null);
        Statement statement = connection.open();


        statement.sendMessage("db.serverStatus()", defaultResultCallback());
        statement.receiveMessage("db.serverStatus()", defaultResultCallback());


    }

    public static SingleResultCallback<String> defaultResultCallback() {

        return new SingleResultCallback<String>() {
            @Override
            public void onResult(String result, Throwable t) {
                System.out.println(result);
            }
        };
    }
}
