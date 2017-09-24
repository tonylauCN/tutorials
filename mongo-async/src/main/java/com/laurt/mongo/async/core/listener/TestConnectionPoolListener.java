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

import com.mongodb.event.*;

/**
 * <p>Title: TestConnectionPoolListener
 * <p>Description: tutorials
 * <p>Copyright: 2017/9/24 下午5:40
 * <p>Company: rongshu
 * <p>author: LiuQingqing
 * <p>package: com.laurt.mongoasync.listener
 *
 * @version v1.0.0
 */
public class TestConnectionPoolListener implements ConnectionPoolListener {
    @Override
    public void connectionPoolOpened(final ConnectionPoolOpenedEvent event) {
        System.out.println(event);
    }

    @Override
    public void connectionPoolClosed(final ConnectionPoolClosedEvent event) {
        System.out.println(event);
    }

    @Override
    public void connectionCheckedOut(final ConnectionCheckedOutEvent event) {
        System.out.println(event);
    }

    @Override
    public void connectionCheckedIn(final ConnectionCheckedInEvent event) {
        System.out.println(event);
    }

    @Override
    public void waitQueueEntered(final ConnectionPoolWaitQueueEnteredEvent event) {
        System.out.println(event);
    }

    @Override
    public void waitQueueExited(final ConnectionPoolWaitQueueExitedEvent event) {
        System.out.println(event);
    }

    @Override
    public void connectionAdded(final ConnectionAddedEvent event) {
        System.out.println(event);
    }

    @Override
    public void connectionRemoved(final ConnectionRemovedEvent event) {
        System.out.println(event);
    }
}
