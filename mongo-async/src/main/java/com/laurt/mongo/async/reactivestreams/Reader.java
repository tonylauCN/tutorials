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

import com.laurt.mongo.async.core.Constants;
import com.mongodb.reactivestreams.client.MongoCollection;
import org.bson.Document;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.atomic.AtomicInteger;

import static com.mongodb.client.model.Filters.*;

/**
 * <p>Title: Reader
 * <p>Description: tutorials
 * <p>Copyright: 2017/9/25 上午9:37
 * <p>Company: rongshu
 * <p>author: LiuQingqing
 * <p>package: com.laurt.mongo.async.reactivestreams
 *
 * @version v1.0.0
 */
public class Reader {

    public void find(MongoCollection<Document> collection) {

        Publisher<Document> publisher = collection.find(and(gte(Constants.MONGO_ACTIVITY_COUNT, 100)
                , lte(Constants.MONGO_ACTIVITY_COUNT, 400)))
                .projection(new Document(Constants.MONGO_ACTIVITY_BID, 1)
                        .append(Constants.MONGO_ACTIVITY_SID, 1)
                        .append(Constants.MONGO_ACTIVITY_COUNT, 1)
                        .append("_id", 0));

        publisher.subscribe(new Subscriber<Document>() {

            int size = 20000;
            AtomicInteger integer = new AtomicInteger(1);
            long ts = System.currentTimeMillis();
            Subscription subscription;

            @Override
            public void onSubscribe(Subscription s) {
                this.subscription = s;
                this.subscription.request(size);  // <--- Data requested and the insertion will now occur
            }

            @Override
            public void onNext(Document document) {
                integer.getAndIncrement();
                if (integer.get() % size == 1) {
                    this.subscription.request(size);
                }

                System.out.println(integer.get() + " " + document);
            }

            @Override
            public void onError(Throwable t) {
                System.err.println(t.getMessage());
            }

            @Override
            public void onComplete() {
                this.subscription.cancel();
                System.err.println("took " + integer.get() + "-> " + (System.currentTimeMillis() - ts) + "ms -> Find Operate Completed");
            }
        });

    }
}
