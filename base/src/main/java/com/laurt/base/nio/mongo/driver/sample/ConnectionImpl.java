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
package com.laurt.base.nio.mongo.driver.sample;

import java.io.IOException;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * <p>Title: ConnectionImpl
 * <p>Description: tutorials
 * <p>Copyright: 2017/10/10 下午3:09
 * <p>Company: rongshu
 * <p>author: LiuQingqing
 * <p>package: com.laurt.base.nio.mongo.driver.sample
 *
 * @version v1.0.0
 */
public class ConnectionImpl implements Connection {

    AsynchronousSocketChannel channel;

    private final Settings settings;

    public ConnectionImpl(Settings settings) {
        this.settings = settings;
    }

    @Override
    public Statement open() throws IOException {

        channel = AsynchronousSocketChannel.open();
        channel.setOption(StandardSocketOptions.TCP_NODELAY, true);
        channel.setOption(StandardSocketOptions.SO_KEEPALIVE, settings.isKeepAlive());
        if (settings.getReceiveBufferSize() > 0) {
            channel.setOption(StandardSocketOptions.SO_RCVBUF, settings.getReceiveBufferSize());
        }
        if (settings.getSendBufferSize() > 0) {
            channel.setOption(StandardSocketOptions.SO_SNDBUF, settings.getSendBufferSize());
        }

        channel.connect(settings.serverAddress.getSocketAddress(), null, new CompletionHandler<Void, Object>() {

            /**
             * Invoked when an operation has completed.
             *
             * @param result     The result of the I/O operation.
             * @param attachment
             */
            public void completed(Void result, Object attachment) {
                System.out.println(attachment);
            }

            /**
             * Invoked when an operation fails.
             *
             * @param exc        The exception to indicate why the I/O operation failed
             * @param attachment
             */
            @Override
            public void failed(Throwable exc, Object attachment) {
                System.err.println(exc.getMessage());
            }
        });
        return new Statement(channel);
    }

    @Override
    public void close() {
        try {
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
