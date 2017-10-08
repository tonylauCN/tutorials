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
import org.bson.Document;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;

/**
 * <p>Title: Reader
 * <p>Description: tutorials
 * <p>Copyright: 2017/9/24 下午3:10
 * <p>Company: rongshu
 * <p>author: LiuQingqing
 * <p>package: com.laurt.mongoasync
 *
 * @version v1.0.0
 */
public class Reader {


    void find(MongoCollection<Document> mongoCollection) {

        long ts = System.currentTimeMillis();
        mongoCollection.find(and(gte(Constants.MONGO_ACTIVITY_COUNT, 100)
                , lte(Constants.MONGO_ACTIVITY_COUNT, 200))).forEach(block -> {
            System.out.println(block);
        }, ((result, t) -> {
            if (t != null) {
                t.printStackTrace();
            }
            System.err.println(" find -> " + (System.currentTimeMillis() - ts) + "ms");
        }));
    }

    /**
     * 投影文档
     *
     * @param mongoCollection
     */
    void findWithProjection(MongoCollection<Document> mongoCollection) {
        long ts = System.currentTimeMillis();
        mongoCollection.find(and(gte(Constants.MONGO_ACTIVITY_COUNT, 100)
                , lte(Constants.MONGO_ACTIVITY_COUNT, 200)))
                .projection(new Document(Constants.MONGO_ACTIVITY_BID, 1)
                        .append(Constants.MONGO_ACTIVITY_SID, 1)
                        .append(Constants.MONGO_ACTIVITY_COUNT, 1)
                        .append("_id", 0))
//                .cursorType(CursorType.NonTailable)
                .forEach(block -> {
                    System.out.println(block);
                }, ((result, t) -> {
                    if (t != null) {
                        t.printStackTrace();
                    }
                    System.err.println(" findWithProjection -> " + (System.currentTimeMillis() - ts) + "ms");
                }));
    }

    /**
     * 投影文档
     *
     * @param mongoCollection
     */
    void findWithProjection2(MongoCollection<Document> mongoCollection) {
        long ts = System.currentTimeMillis();
        mongoCollection.find(and(gte(Constants.MONGO_ACTIVITY_COUNT, 100)
                , lte(Constants.MONGO_ACTIVITY_COUNT, 400)))
//                .sort(Sorts.ascending(Constants.MONGO_ACTIVITY_COUNT
//                        , Constants.MONGO_ACTIVITY_SID))
                .projection(fields(
                        include(Constants.MONGO_ACTIVITY_BID
                                , Constants.MONGO_ACTIVITY_SID
                                , Constants.MONGO_ACTIVITY_COUNT)
                        , exclude("_id")))
//                .cursorType(CursorType.NonTailable)
                .forEach(block -> {
                    System.out.println(block);
                }, ((result, t) -> {
                    if (t != null) {
                        t.printStackTrace();
                    }
                    System.err.println(" findWithProjection2 -> " + (System.currentTimeMillis() - ts) + "ms");
                }));
    }
}
