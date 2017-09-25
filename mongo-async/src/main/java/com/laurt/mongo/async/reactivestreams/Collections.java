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

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoCollection;
import org.bson.Document;

/**
 * <p>Title: Collections
 * <p>Description: tutorials
 * <p>Copyright: 2017/9/25 上午9:38
 * <p>Company: rongshu
 * <p>author: LiuQingqing
 * <p>package: com.laurt.mongo.async.reactivestreams
 *
 * @version v1.0.0
 */
public class Collections {

    /**
     * @param mongoClient
     * @param dbName
     * @param collectionName
     * @return
     */
    public MongoCollection<Document> createCollection(MongoClient mongoClient, String dbName, String collectionName) {

        MongoCollection<Document> documentMongoCollection = mongoClient.getDatabase(dbName).getCollection(collectionName);

        return documentMongoCollection;
    }
}
