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
package com.laurt.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>Title: DemoConnections
 * <p>Description: tutorials
 * <p>Copyright: 2017/9/23 下午6:38
 * <p>Company: rongshu
 * <p>author: LiuQingqing
 * <p>package: com.laurt.mongoasync
 *
 * @version v1.0.0
 */
@Slf4j
public class DemoConnections {

    /**
     * 不使用netty
     *
     * @return
     */
    public static MongoClient newInstanceDefault() {

        MongoClientURI connectionString = new MongoClientURI("mongodb://localhost:27017");
        MongoClient mongoClient = new MongoClient(connectionString);
        return mongoClient;
    }
}
