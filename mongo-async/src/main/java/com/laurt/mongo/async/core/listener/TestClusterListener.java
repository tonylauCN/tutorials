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
package com.laurt.mongo.async.core.listener;

import com.mongodb.ReadPreference;
import com.mongodb.event.ClusterClosedEvent;
import com.mongodb.event.ClusterDescriptionChangedEvent;
import com.mongodb.event.ClusterListener;
import com.mongodb.event.ClusterOpeningEvent;

/**
 * <p>Title: ds
 * <p>Description: tutorials
 * <p>Copyright: 2017/9/24 下午5:51
 * <p>Company: rongshu
 * <p>author: LiuQingqing
 * <p>package: com.laurt.mongoasync.listener
 *
 * @version v1.0.0
 */
public class TestClusterListener implements ClusterListener {
    private final ReadPreference readPreference;
    private boolean isWritable;
    private boolean isReadable;

    public TestClusterListener(final ReadPreference readPreference) {
        this.readPreference = readPreference;
    }

    @Override
    public void clusterOpening(final ClusterOpeningEvent clusterOpeningEvent) {
        System.out.println(String.format("Cluster with unique client identifier %s opening",
                clusterOpeningEvent.getClusterId()));
    }

    @Override
    public void clusterClosed(final ClusterClosedEvent clusterClosedEvent) {
        System.out.println(String.format("Cluster with unique client identifier %s closed",
                clusterClosedEvent.getClusterId()));
    }

    @Override
    public void clusterDescriptionChanged(final ClusterDescriptionChangedEvent event) {
        if (!isWritable) {
            if (event.getNewDescription().hasWritableServer()) {
                isWritable = true;
                System.out.println("Writable server available!");
            }
        } else {
            if (!event.getNewDescription().hasWritableServer()) {
                isWritable = false;
                System.out.println("No writable server available!");
            }
        }

        if (!isReadable) {
            if (event.getNewDescription().hasReadableServer(readPreference)) {
                isReadable = true;
                System.out.println("Readable server available!");
            }
        } else {
            if (!event.getNewDescription().hasReadableServer(readPreference)) {
                isReadable = false;
                System.out.println("No readable server available!");
            }
        }
    }
}
