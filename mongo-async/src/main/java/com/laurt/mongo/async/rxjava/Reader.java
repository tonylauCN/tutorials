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
import com.mongodb.rx.client.MongoCollection;
import org.bson.Document;
import rx.Observable;
import rx.Observer;
import rx.observers.TestSubscriber;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

import static com.mongodb.client.model.Filters.*;

/**
 * <p>Title: Reader
 * <p>Description: tutorials
 * <p>Copyright: 2017/9/24 下午6:15
 * <p>Company: rongshu
 * <p>author: LiuQingqing
 * <p>package: com.laurt.mongo.async.core
 *
 * @version v1.0.0
 */
public class Reader {

    void find(MongoCollection<Document> mongoCollection) throws ExecutionException, InterruptedException {

        // Create a publisher
        Observable<Document> observable = mongoCollection.find(and(gte(Constants.MONGO_ACTIVITY_COUNT, 100)
                , lte(Constants.MONGO_ACTIVITY_COUNT, 400)))
//                .sort(Sorts.ascending(Constants.MONGO_ACTIVITY_COUNT, Constants.MONGO_ACTIVITY_SID))
                .projection(new Document(Constants.MONGO_ACTIVITY_BID, 1)
                        .append(Constants.MONGO_ACTIVITY_SID, 1)
                        .append(Constants.MONGO_ACTIVITY_COUNT, 1)
                        .append("_id", 0)).toObservable();

        // Non blocking
//        Subscriber<Document> subscriber = printDocumentSubscriber();
//        observable.subscribe(subscriber);

        long ts = System.currentTimeMillis();
        AtomicInteger atomicInteger = new AtomicInteger();
        observable.subscribe((document -> {
            System.out.println(atomicInteger.getAndIncrement() + "\t" + document.toString());
        }), throwable -> {
            System.out.println("The Observer errored: " + throwable.getMessage());
        }, () -> {
            System.err.println(" -> " + (System.currentTimeMillis() - ts) + "ms");
        });

        // Block for the publisher to complete
//        Document document = future.get();

//        System.err.println(document);
    }

    /**
     * A Subscriber that prints the json version of each document
     *
     * @return the subscriber
     */
    public static TestSubscriber<Document> printDocumentSubscriber() {

        return new TestSubscriber<Document>(new Observer<Document>() {
            long ts = System.currentTimeMillis();
            AtomicInteger atomicInteger = new AtomicInteger();

            @Override
            public void onCompleted() {
                System.err.println(" -> " + (System.currentTimeMillis() - ts) + "ms");
            }

            @Override
            public void onError(final Throwable t) {
                System.out.println("The Observer errored: " + t.getMessage());
            }

            @Override
            public void onNext(final Document document) {
                System.out.println(atomicInteger.getAndIncrement() + "\t" + document.toString());
            }
        });
    }
}
