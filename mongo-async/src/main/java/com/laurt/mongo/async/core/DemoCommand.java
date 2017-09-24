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

import com.mongodb.async.client.MongoDatabase;
import org.bson.Document;

/**
 * <p>Title: DemoCommand
 * <p>Description: tutorials
 * <p>Copyright: 2017/9/24 下午5:02
 * <p>Company: rongshu
 * <p>author: LiuQingqing
 * <p>package: com.laurt.mongoasync
 *
 * @version v1.0.0
 */
public class DemoCommand {

    /**
     * 执行脚本命令
     * @param database
     */
    void runCommand(MongoDatabase database) {

        database.runCommand(new Document("buildInfo", 1), ((result, t) -> {
            System.out.println(" runCommand -> " + result);
        }));

    }
}
