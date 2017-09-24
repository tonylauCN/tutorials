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

import com.mongodb.event.CommandFailedEvent;
import com.mongodb.event.CommandListener;
import com.mongodb.event.CommandStartedEvent;
import com.mongodb.event.CommandSucceededEvent;

/**
 * <p>Title: TestCommandListener
 * <p>Description: tutorials
 * <p>Copyright: 2017/9/24 下午5:31
 * <p>Company: rongshu
 * <p>author: LiuQingqing
 * <p>package: com.laurt.mongoasync.listener
 *
 * @version v1.0.0
 */
public class TestCommandListener implements CommandListener {

    /**
     * Listener for command started events.
     *
     * @param event the event
     */
    @Override
    public void commandStarted(CommandStartedEvent event) {

        System.out.println(String.format("Sent command '%s:%s' with id %s to database '%s' "
                        + "on connection '%s' to server '%s'",
                event.getCommandName(),
                event.getCommand().get(event.getCommandName()),
                event.getRequestId(),
                event.getDatabaseName(),
                event.getConnectionDescription()
                        .getConnectionId(),
                event.getConnectionDescription().getServerAddress()));

    }

    /**
     * Listener for command completed events
     *
     * @param event the event
     */
    @Override
    public void commandSucceeded(CommandSucceededEvent event) {

        System.out.println(String.format("Successfully executed command '%s' with id %s "
                        + "on connection '%s' to server '%s'",
                event.getCommandName(),
                event.getRequestId(),
                event.getConnectionDescription()
                        .getConnectionId(),
                event.getConnectionDescription().getServerAddress()));

    }

    /**
     * Listener for command failure events
     *
     * @param event the event
     */
    @Override
    public void commandFailed(CommandFailedEvent event) {

        System.out.println(String.format("Failed execution of command '%s' with id %s "
                        + "on connection '%s' to server '%s' with exception '%s'",
                event.getCommandName(),
                event.getRequestId(),
                event.getConnectionDescription()
                        .getConnectionId(),
                event.getConnectionDescription().getServerAddress(),
                event.getThrowable()));
    }
}
