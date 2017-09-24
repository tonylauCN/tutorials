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
import com.mongodb.client.model.BulkWriteOptions;
import com.mongodb.client.model.InsertOneModel;
import com.mongodb.client.model.WriteModel;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * <p>Title: DemoWrite
 * <p>Description: tutorials
 * <p>Copyright: 2017/9/24 上午11:44
 * <p>Company: rongshu
 * <p>author: LiuQingqing
 * <p>package: com.laurt.mongoasync
 *
 * @version v1.0.0
 */
public class DemoWrite {

    // 15 behaviors
    private String[] behaviors = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o"};

    // 10 users
    private String[] users = {"uid001", "uid002", "uid003", "uid004", "uid005", "uid006", "uid007", "uid008", "uid009", "uid010"};

    private Random random = new Random();

    /**
     * @param collection
     */
    void bulkWrite(MongoCollection<Document> collection) throws InterruptedException {

        collection.count(((count, e) -> {
            if (count < 1) {
                Long timestamp = timestamp();
                Long trunctime = (timestamp / (24 * 60 * 60 * 1000)) * (24 * 60 * 60 * 1000);
                for (int i = 0; i < 1000; i++) {
                    BulkWriteOptions bulkWriteOptions = new BulkWriteOptions();
                    List<WriteModel<Document>> writeModelList = new ArrayList<>();
                    for (int j = 0; j < 10000; j++) {
                        Document document = new Document();

                        document.append(Constants.MONGO_ACTIVITY_BID, behavior());
                        document.append(Constants.MONGO_ACTIVITY_SID, users[i % users.length]);
                        document.append(Constants.MONGO_ACTIVITY_COUNT, count());
                        document.append(Constants.MONGO_ACTIVITY_TIMESTAMP, trunctime + (10000 * i) + j);
                        document.append(Constants.MONGO_ACTIVITY_TRUNCTIME, trunctime);
                        WriteModel<Document> writeModel = new InsertOneModel<>(document);
                        writeModelList.add(writeModel);
                    }
                    int finalI = i;
                    collection.bulkWrite(writeModelList, bulkWriteOptions, (result, t) -> {
                        if (t != null) {
                            System.err.println(t.getMessage());
                        } else {
                            System.err.println("Index:" + finalI + "Inserted Count: " + result.getInsertedCount() + " - Modified Count: " + result.getModifiedCount());
                        }
                    });

                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                        e = e1;
                    }
                }
            }
        }));

    }


    private String behavior() {
        return behaviors[random.nextInt(behaviors.length)];
    }

    private Long timestamp() {
        return System.currentTimeMillis();
    }

    private int count() {
        return random.nextInt(1000);
    }
}
