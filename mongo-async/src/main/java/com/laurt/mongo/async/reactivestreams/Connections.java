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
package com.laurt.mongo.async.reactivestreams;


import com.laurt.mongo.async.core.listener.TestClusterListener;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.async.client.MongoClientSettings;
import com.mongodb.connection.ClusterSettings;
import com.mongodb.connection.ConnectionPoolSettings;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;

import java.util.concurrent.TimeUnit;

import static java.util.Arrays.asList;

/**
 * <p>Title: Connections
 * <p>Description: tutorials
 * <p>Copyright: 2017/9/25 上午9:34
 * <p>Company: rongshu
 * <p>author: LiuQingqing
 * <p>package: com.laurt.mongo.async.reactivestreams
 *
 * @version v1.0.0
 */
public class Connections {

    /**
     *
     * @return
     */
    public static MongoClient newInstanceDefault() {

        MongoClientSettings settings = MongoClientSettings.builder()
                .readPreference(ReadPreference.primary())
                .connectionPoolSettings(ConnectionPoolSettings.builder()
                        .minSize(0x10)
                        .maxSize(0xA0)
                        .maxWaitTime(50000, TimeUnit.MILLISECONDS)
                        .maxWaitQueueSize(0x100)
                        .maxConnectionIdleTime(10000, TimeUnit.MILLISECONDS)
//                        .addConnectionPoolListener(new TestConnectionPoolListener())
                        .build())
//                .addCommandListener(new TestCommandListener())
                .clusterSettings(ClusterSettings.builder()
                        .hosts(asList(
                                new ServerAddress("localhost", 27017)))
                        .addClusterListener(new TestClusterListener(ReadPreference.primary()))
                        .build())
                .build();

        MongoClient mongoClient = MongoClients.create(settings);

        return mongoClient;
    }

}
