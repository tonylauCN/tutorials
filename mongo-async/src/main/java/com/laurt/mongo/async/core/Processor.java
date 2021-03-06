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
package com.laurt.mongo.async.core;

import com.mongodb.async.client.MongoCollection;
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

    private final Connections connections;
    private final Collections collections;
    private final Indexs indexs;
    private final Writer writer;
    private final Reader read;
    private final Aggregation aggregate;
    private final Command command;

    public Processor() {
        collections = new Collections();
        connections = new Connections();
        indexs = new Indexs();
        writer = new Writer();
        read = new Reader();
        aggregate = new Aggregation();
        command = new Command();
    }

    public void process(String dbName, String collectionName) throws InterruptedException {

        MongoCollection<Document> collection = collections.createCollection(dbName, collectionName);
        Thread.sleep(1000);

        BsonDocument document = collection.getWriteConcern().asDocument();
        System.err.println(document.toJson());
        long ts = System.currentTimeMillis();
        writer.bulkWrite(collection);
        indexs.createIndexs(collection);
        System.err.println(" ==> timestamp " + (System.currentTimeMillis() - ts) + "ms");

        System.out.println("--------------------------------------------");
//        read.find(collection);
        read.findWithProjection2(collection);

        System.out.println("--------------------------------------------");
//        aggregate.aggregate(collection);
        System.out.println("--------------------------------------------");

        System.out.println("--------------------------------------------");
//        command.runCommand(Connections.newInstanceDefault().getDatabase("demo"));
        System.out.println("--------------------------------------------");
    }
}
