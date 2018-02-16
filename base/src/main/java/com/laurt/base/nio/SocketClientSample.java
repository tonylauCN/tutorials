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
package com.laurt.base.nio;

/**
 * <p>Title: SocketClientSample
 * <p>Description: tutorials
 * <p>Copyright: 2017/10/10 下午4:59
 * <p>Company: rongshu
 * <p>author: LiuQingqing
 * <p>package: com.laurt.base.nio.mongo.driver.sample
 *
 * @version v1.0.0
 */

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class SocketClientSample {

    public void startClient()
            throws IOException, InterruptedException {

        InetSocketAddress hostAddress = new InetSocketAddress("localhost", 8799);
        SocketChannel client = SocketChannel.open(hostAddress);

        System.out.println("Client... started");

        String threadName = Thread.currentThread().getName();

        // Send messages to server
        String messages = new String(threadName + ": test");

        for (int i = 1; i < 100; i++) {
            byte[] message = new String(messages + i).getBytes();
            ByteBuffer buffer = ByteBuffer.wrap(message);
            client.write(buffer);
            System.out.println("send -> " + messages);
            buffer.clear();
            Thread.sleep(5000);
        }
        client.close();
    }

    public void startClientExtra()
            throws IOException, InterruptedException {

        InetSocketAddress hostAddress = new InetSocketAddress("localhost", 8799);
        SocketChannel client = SocketChannel.open(hostAddress);

        System.out.println("Client... started");

        int byteRead;
        ByteBuffer buffer = ByteBuffer.allocate(0x1000);

        byteRead = client.read(buffer);
        if (byteRead != -1) {
            buffer.flip();
            System.out.print((char) buffer.get());
            while (buffer.hasRemaining()) {
                System.out.print((char) buffer.get());
            }
            buffer.clear();
        }
        System.out.println();
        client.shutdownOutput();
        client.close();
    }
}
