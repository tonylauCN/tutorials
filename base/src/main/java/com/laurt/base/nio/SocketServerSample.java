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
 * <p>Title: SocketServerSample
 * <p>Description: tutorials
 * <p>Copyright: 2017/10/10 下午4:57
 * <p>Company: rongshu
 * <p>author: LiuQingqing
 * <p>package: com.laurt.base.nio
 *
 * @version v1.0.0
 */

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.*;

public class SocketServerSample {
    private Selector selector;
    private Map<SocketChannel, List> dataMapper;
    private InetSocketAddress listenAddress;

    public static void main(String[] args) throws Exception {

        Runnable server = () -> {
            try {
                new SocketServerSample("localhost", 8799).startServer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        Runnable client = () -> {
            try {
                new SocketClientSample().startClient();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        };

        Runnable clientExtra = () -> {
            try {
                new SocketClientSample().startClientExtra();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        };
        new Thread(server).start();
        Thread.sleep(1000);
        new Thread(client, "client-A").start();
        new Thread(client, "client-B").start();
        new Thread(clientExtra, "client-extra").start();
    }

    public SocketServerSample(String address, int port) throws IOException {
        listenAddress = new InetSocketAddress(address, port);
        dataMapper = new HashMap<>();
    }

    // create server channel
    private void startServer() throws IOException {
        this.selector = Selector.open();
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);

        // retrieve server socket and bind to port
        serverChannel.socket().bind(listenAddress);
        serverChannel.register(this.selector, SelectionKey.OP_ACCEPT);
        System.out.println("Server started...");

        while (true) {
            // wait for events
            this.selector.select();
            //work on selected keys
            Iterator keys = this.selector.selectedKeys().iterator();
            while (keys.hasNext()) {
                SelectionKey key = (SelectionKey) keys.next();

                // this is necessary to prevent the same key from coming up
                // again the next time around.
                keys.remove();

                if (!key.isValid()) {
                    continue;
                }

                if (key.isAcceptable()) {
                    this.accept(key);
                } else if (key.isReadable()) {
                    this.read(key);
                } else if (key.isWritable()) {
                    this.write(key);
                }
            }
        }
    }

    //accept a connection made to this channel's socket
    private void accept(SelectionKey key) throws IOException {
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        SocketChannel channel = serverChannel.accept();
        channel.configureBlocking(false);
        Socket socket = channel.socket();
        SocketAddress remoteAddr = socket.getRemoteSocketAddress();
        System.out.println("Connected to: " + remoteAddr);

        // register channel with selector for further IO
        dataMapper.put(channel, new ArrayList());
        channel.register(this.selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
    }

    //read from the socket channel
    private void read(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int numRead;
        numRead = channel.read(buffer);

        if (numRead == -1) {
            remove(key);
        } else {
            byte[] data = new byte[numRead];
            System.arraycopy(buffer.array(), 0, data, 0, numRead);
            System.out.println("Got: " + new String(data));
        }
    }

    private void write(SelectionKey key) throws IOException {

        SocketChannel channel = (SocketChannel) key.channel();
        String message = Thread.currentThread().getName() + " ***";
        ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());

        int numWrite;
        numWrite = channel.write(buffer);
        if (numWrite == -1) {
            remove(key);
        }
    }

    private void remove(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        this.dataMapper.remove(channel);
        Socket socket = channel.socket();
        SocketAddress remoteAddr = socket.getRemoteSocketAddress();
        System.out.println("Connection closed by client: " + remoteAddr);
        channel.close();
        key.cancel();
    }
}
