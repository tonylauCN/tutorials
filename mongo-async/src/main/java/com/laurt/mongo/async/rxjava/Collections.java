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

import com.laurt.mongo.async.core.Constants;
import com.mongodb.ReadPreference;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ValidationOptions;
import com.mongodb.rx.client.MongoClient;
import com.mongodb.rx.client.MongoCollection;
import com.mongodb.rx.client.MongoDatabase;
import com.mongodb.rx.client.Success;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import rx.Observable;

/**
 * <p>Title: Collections
 * <p>Description: tutorials
 * <p>Copyright: 2017/9/24 上午11:22
 * <p>Company: rongshu
 * <p>author: LiuQingqing
 * <p>package: com.laurt.mongoasync
 *
 * @version v1.0.0
 */
@Slf4j
public class Collections {

    /**
     * @param dbName
     * @param collectionName
     * @return
     */
    public MongoCollection<Document> createCollection(MongoClient mongoClient, String dbName, String collectionName) {

        MongoClient client = mongoClient;
//         MongoClient client = Connections.newInstanceWithNetty();

        // 插入结构验证
        ValidationOptions validationOptions = new ValidationOptions().validator(
                Filters.or(Filters.exists(Constants.MONGO_ACTIVITY_BID)
                        , Filters.exists(Constants.MONGO_ACTIVITY_SID)));

        MongoCollection<Document> mongoCollection = client.getDatabase(dbName).getCollection(collectionName);

        if (mongoCollection == null) {
            // 创建Collection
            MongoDatabase database = client.getDatabase(dbName)
                    .withReadPreference(ReadPreference.primary()); // READ -> primary

            Observable<Success> observable = database.createCollection(collectionName
                    , new CreateCollectionOptions()
                            .capped(true) // 集合设置封顶限制
                            .sizeInBytes(0x10000000) // 设置256m封顶集合
                            .validationOptions(validationOptions));
            observable.subscribe(success -> {
                        System.out.println(success.name());
                    }, throwable -> {
                        System.err.println(throwable.getMessage());
                    }, () -> {
                        System.out.println("Create Collection Operate Finished!");
                    }
            );
            mongoCollection = database.getCollection(collectionName)
                    .withReadPreference(ReadPreference.primaryPreferred()); // READ -> primaryPreferred
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
