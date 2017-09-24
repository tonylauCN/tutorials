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
package com.laurt.mongoasync;

import com.mongodb.async.SingleResultCallback;
import com.mongodb.async.client.MongoClient;
import com.mongodb.async.client.MongoCollection;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ValidationOptions;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;

/**
 * <p>Title: DemoCollections
 * <p>Description: tutorials
 * <p>Copyright: 2017/9/24 上午11:22
 * <p>Company: rongshu
 * <p>author: LiuQingqing
 * <p>package: com.laurt.mongoasync
 *
 * @version v1.0.0
 */
@Slf4j
public class DemoCollections {

    /**
     * @param dbName
     * @param collectionName
     * @return
     */
    public MongoCollection<Document> createCollection(String dbName, String collectionName) {

        MongoClient client = DemoConnections.newInstanceDefault();
//         MongoClient client = DemoConnections.newInstanceWithNetty();

        // 插入结构验证
        ValidationOptions validationOptions = new ValidationOptions().validator(
                Filters.or(Filters.exists(Constants.MONGO_ACTIVITY_BID)
                        , Filters.exists(Constants.MONGO_ACTIVITY_SID)));

        MongoCollection<Document> mongoCollection = client.getDatabase(dbName).getCollection(collectionName);

        if (mongoCollection == null) {
            // 创建Collection
            client.getDatabase(dbName).createCollection(collectionName
                    , new CreateCollectionOptions()
                            .capped(true) // 集合设置封顶限制
                            .sizeInBytes(0x10000000) // 设置256m封顶集合
                            .validationOptions(validationOptions)
                    , callbackWhenFinished);
            mongoCollection = client.getDatabase(dbName).getCollection(collectionName);
        }

        return mongoCollection;
    }

    /**
     *
     */
    SingleResultCallback<Void> callbackWhenFinished = new SingleResultCallback<Void>() {

        /**
         * Called when the operation completes.
         *
         * @param result the result, which may be null.  Always null if e is not null.
         * @param t      the throwable, or null if the operation completed normally
         */
        @Override
        public void onResult(Void result, Throwable t) {
            if (t != null) {
                t.printStackTrace();
            } else {
                System.err.println("Collections Operation Finished!");
            }
        }
    };
}
