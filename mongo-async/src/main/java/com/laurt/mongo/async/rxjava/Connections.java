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
package com.laurt.mongo.async.rxjava;

import com.laurt.mongo.async.core.listener.TestClusterListener;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.async.client.MongoClientSettings;
import com.mongodb.connection.ClusterSettings;
import com.mongodb.connection.ConnectionPoolSettings;
import com.mongodb.rx.client.MongoClient;
import com.mongodb.rx.client.MongoClients;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

import static java.util.Arrays.asList;

/**
 * <p>Title: Connections
 * <p>Description: tutorials
 * <p>Copyright: 2017/9/23 下午6:38
 * <p>Company: rongshu
 * <p>author: LiuQingqing
 * <p>package: com.laurt.mongoasync
 *
 * @version v1.0.0
 */
@Slf4j
public class Connections {

//    /**
//     * 获得连接实例
//     *
//     * @return
//     */
//    public static MongoClient newInstanceWithNetty() {
//
//        ClusterSettings clusterSettings = ClusterSettings.builder()
//                .hosts(asList(
////                        new ServerAddress("host1", 27017),
////                        new ServerAddress("host2", 27017),
//                        new ServerAddress("localhost", 27017)))
//                .build();
//
//        SocketSettings socketSettings = SocketSettings.builder()
//                .connectTimeout(15, TimeUnit.MINUTES)
//                .receiveBufferSize(0x100000) // 1M
//                .readTimeout(5, TimeUnit.MINUTES)
//                .build();
//        MongoClientSettings settings = MongoClientSettings.builder()
//                .clusterSettings(clusterSettings)
//                .addCommandListener(new TestCommandListener())
//                .socketSettings(socketSettings)
//                .readPreference(ReadPreference.primary())
//                .streamFactoryFactory(NettyStreamFactoryFactory.builder().build())
//                .build();
//
//        MongoClient mongoClient = MongoClients.create(settings);
//
//        return mongoClient;
//    }

    /**
     * 不使用netty
     *
     * @return
     */
    public static MongoClient newInstanceDefault() {

        MongoClientSettings settings = MongoClientSettings.builder()
                .readPreference(ReadPreference.primary())
                .connectionPoolSettings(connectionPoolSettings())
//                .addCommandListener(new TestCommandListener())
                .clusterSettings(clusterSettings())
                .build();

        MongoClient mongoClient = MongoClients.create(settings);

        return mongoClient;
    }


    static ClusterSettings clusterSettings() {
        ClusterSettings clusterSettings = ClusterSettings.builder()
                .hosts(asList(
                        new ServerAddress("localhost", 27017)))
                .addClusterListener(new TestClusterListener(ReadPreference.primary()))
                .build();
        return clusterSettings;
    }

    static ConnectionPoolSettings connectionPoolSettings() {
        ConnectionPoolSettings connectionPoolSettings = ConnectionPoolSettings.builder()
                .minSize(0x10)
                .maxSize(0xA0)
                .maxWaitTime(50000, TimeUnit.MILLISECONDS)
                .maxWaitQueueSize(0x100)
                .maxConnectionIdleTime(10000, TimeUnit.MILLISECONDS)
//                .addConnectionPoolListener(new TestConnectionPoolListener())
                .build();
        return connectionPoolSettings;
    }


}
