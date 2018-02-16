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

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * <p>Title: SocketAccessSample
 * <p>Description: tutorials
 * <p>Copyright: 2017/10/10 下午4:34
 * <p>Company: rongshu
 * <p>author: LiuQingqing
 * <p>package: com.laurt.base.nio
 *
 * @version v1.0.0
 */
public class SocketAccessSample {

    public static void main(String[] args) {

        SocketAccessSample sample = new SocketAccessSample();

        sample.print("127.0.0.1", 8799);
    }


    public void print(String host, int port) {


        SocketChannel channel = null;
        ByteBuffer buffer = ByteBuffer.allocate(0x100);
        int byteRead;
        try {
            InetSocketAddress hostAddress = new InetSocketAddress("localhost", 8799);
            channel = SocketChannel.open(hostAddress);
            channel.configureBlocking(false);
            byteRead = channel.read(buffer);

            if (byteRead != -1) {
//                byteRead = channel.read(buffer);
                byte[] data = new byte[byteRead];
                System.arraycopy(buffer.array(), 0, data, 0, byteRead);
                System.out.println("receive -> " + new String(data));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
