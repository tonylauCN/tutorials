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

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Stream;

/**
 * <p>Title: Statement
 * <p>Description: tutorials
 * <p>Copyright: 2017/10/10 下午3:02
 * <p>Company: rongshu
 * <p>author: LiuQingqing
 * <p>package: com.laurt.base.nio.mongo.driver.sample
 *
 * @version v1.0.0
 */
public class Statement {

    final AsynchronousSocketChannel channel;

    public Statement(AsynchronousSocketChannel channel) {
        this.channel = channel;
    }

    public Stream<String> sendMessage(final String command, SingleResultCallback<String> callback) {

        CountDownLatch countDownLatch = new CountDownLatch(1);
        ByteBuffer buffer = ByteBuffer.wrap(command.getBytes());
        channel.write(buffer, null, new CompletionHandler<Integer, Object>() {
            @Override
            public void completed(Integer result, Object attachment) {
                countDownLatch.countDown();
                System.out.println(result);
            }

            @Override
            public void failed(Throwable exc, Object attachment) {
                countDownLatch.countDown();
                System.err.println(exc.getMessage());
            }
        });
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Stream.of();
    }

    public Stream<String> receiveMessage(final String command, SingleResultCallback<String> callback) {

        ByteBuffer buffer = ByteBuffer.allocate(0x1000);

        CountDownLatch countDownLatch = new CountDownLatch(1);
        channel.read(buffer, null, new CompletionHandler<Integer, Void>() {

            @Override
            public void completed(Integer result, Void attachment) {

                if (result == -1) {

                } else if (!buffer.hasRemaining()) {
                    buffer.flip();

                } else {
                    channel.read(buffer, null,
                            getCompletionHandler());
                }
                countDownLatch.countDown();
            }

            @Override
            public void failed(Throwable exc, Void attachment) {
                countDownLatch.countDown();
            }
        });
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Stream.of();
    }


    public CompletionHandler<Integer, Void> getCompletionHandler() {

        return new CompletionHandler<Integer, Void>() {

            /**
             * Invoked when an operation has completed.
             *
             * @param result     The result of the I/O operation.
             * @param attachment
             */
            @Override
            public void completed(Integer result, Void attachment) {

            }

            /**
             * Invoked when an operation fails.
             *
             * @param exc        The exception to indicate why the I/O operation failed
             * @param attachment
             */
            @Override
            public void failed(Throwable exc, Void attachment) {

            }
        };

    }

}
