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

import com.mongodb.client.MongoCollection;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonDocument;
import org.bson.Document;

/**
 * <p>Title: Processor
 * <p>Description: tutorials
 * <p>Copyright: 2017/9/24 上午10:18
 * <p>Company: rongshu
 * <p>author: LiuQingqing
 * <p>package: com.laurt.mongoasync
 *
 * @version v1.0.0
 */
@Slf4j
public class Processor {

    private DemoConnections connections;
    private DemoCollections collections;
    private DemoIndexs indexs;
    private DemoWrite writer;
    private DemoRead read;

    public Processor() {
        collections = new DemoCollections();
        connections = new DemoConnections();
        indexs = new DemoIndexs();
        writer = new DemoWrite();
        read = new DemoRead();
    }

    public void process(String dbName, String collectionName) throws InterruptedException {

        MongoCollection<Document> collection = collections.createCollection(dbName, collectionName);
        BsonDocument document = collection.getWriteConcern().asDocument();
        System.err.println(document.toJson());
        long ts = System.currentTimeMillis();
//        writer.bulkWrite(collection);
//        indexs.createIndexs(collection);
        System.err.println(" ==> timestamp " + (System.currentTimeMillis() - ts) + "ms");

        read.find(collection);
    }
}
