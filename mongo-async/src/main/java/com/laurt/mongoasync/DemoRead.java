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

import com.mongodb.async.client.MongoCollection;
import org.bson.Document;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Filters.lte;

/**
 * <p>Title: DemoRead
 * <p>Description: tutorials
 * <p>Copyright: 2017/9/24 下午3:10
 * <p>Company: rongshu
 * <p>author: LiuQingqing
 * <p>package: com.laurt.mongoasync
 *
 * @version v1.0.0
 */
public class DemoRead {


    void find(MongoCollection<Document> mongoCollection) {

        long ts = System.currentTimeMillis();
        mongoCollection.find(and(gte(Constants.MONGO_ACTIVITY_COUNT, 100)
                , lte(Constants.MONGO_ACTIVITY_COUNT, 200))).forEach(block -> {
                    System.out.println(block);
        }, ((result, t) -> {
            if (t != null) {
                t.printStackTrace();
            }
            System.err.println(" -> " + (System.currentTimeMillis() - ts) + "ms");
        }));
    }
}
