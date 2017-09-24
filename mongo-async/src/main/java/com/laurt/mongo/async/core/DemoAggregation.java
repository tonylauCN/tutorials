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
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.Arrays;

import static com.mongodb.client.model.Filters.and;

/**
 * <p>Title: DemoAggregation
 * <p>Description: tutorials
 * <p>Copyright: 2017/9/24 下午4:41
 * <p>Company: rongshu
 * <p>author: LiuQingqing
 * <p>package: com.laurt.mongoasync
 *
 * @version v1.0.0
 */
public class DemoAggregation {

    void aggregate(MongoCollection<Document> mongoCollection) {

        long ts = System.currentTimeMillis();
        mongoCollection.aggregate(Arrays.asList(
                Aggregates.match(and(
                        Filters.gte(Constants.MONGO_ACTIVITY_COUNT, 10)
                        , Filters.lte(Constants.MONGO_ACTIVITY_COUNT, 20))),
                Aggregates.group("$bid", Accumulators.sum("count", 1))
        )).forEach(System.out::println, ((result, t) -> {
            if (t != null) {
                t.printStackTrace();
            }
            System.err.println(" -> " + (System.currentTimeMillis() - ts) + "ms");
        }));
    }
}
