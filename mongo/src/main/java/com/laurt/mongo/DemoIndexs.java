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

import com.mongodb.Block;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.IndexModel;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;

import java.util.Arrays;

/**
 * <p>Title: DemoIndexs
 * <p>Description: tutorials
 * <p>Copyright: 2017/9/24 上午11:18
 * <p>Company: rongshu
 * <p>author: LiuQingqing
 * <p>package: com.laurt.mongoasync
 *
 * @version v1.0.0
 */
@Slf4j
public class DemoIndexs {

    /**
     * @param collection
     */
    public void createIndexs(MongoCollection<Document> collection) {

        collection.dropIndexes();
        collection.createIndexes(Arrays.asList(
                new IndexModel(Indexes.ascending(Constants.MONGO_ACTIVITY_BID)) // 升序索引
                , new IndexModel(Indexes.descending(Constants.MONGO_ACTIVITY_TIMESTAMP)
                        , new IndexOptions()
                        .unique(false)
                        .name("Index_" + Constants.MONGO_ACTIVITY_TIMESTAMP)) // 降序索引
                , new IndexModel(Indexes.compoundIndex( // 复合索引
                        Indexes.descending(Constants.MONGO_ACTIVITY_TIMESTAMP)
                        , Indexes.ascending(Constants.MONGO_ACTIVITY_SID, Constants.MONGO_ACTIVITY_BID)))
        ));
        printIndexs(collection);
    }

    public void printIndexs(MongoCollection<Document> collection) {
        collection.listIndexes().forEach((Block<? super Document>) document -> {
            System.err.println(document.toJson());
        });
    }
}
