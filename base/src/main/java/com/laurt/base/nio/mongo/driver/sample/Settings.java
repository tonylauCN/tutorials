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

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

/**
 * <p>Title: Settings
 * <p>Description: tutorials
 * <p>Copyright: 2017/10/10 下午3:18
 * <p>Company: rongshu
 * <p>author: LiuQingqing
 * <p>package: com.laurt.base.nio.mongo.driver.sample
 *
 * @version v1.0.0
 */
public class Settings {


    final ServerAddress serverAddress;

    public Settings(ServerAddress serverAddress) {
        this.serverAddress = serverAddress;
    }

    private boolean keepAlive;

    private int receiveBufferSize = 0x1000; //1024
    private int sendBufferSize = 0x1000; //1024

    public int getReceiveBufferSize() {
        return receiveBufferSize;
    }

    public void setReceiveBufferSize(int receiveBufferSize) {
        this.receiveBufferSize = receiveBufferSize;
    }

    public int getSendBufferSize() {
        return sendBufferSize;
    }

    public void setSendBufferSize(int sendBufferSize) {
        this.sendBufferSize = sendBufferSize;
    }

    public void setKeepAlive(boolean keepAlive) {
        this.keepAlive = keepAlive;
    }

    public boolean isKeepAlive() {
        return keepAlive;
    }

    public static class ServerAddress {

        String host;
        int port;

        public String getHost() {
            return host;
        }

        public int getPort() {
            return port;
        }

        public ServerAddress(String host, int port) {
            this.host = host;
            this.port = port;
        }

        public InetSocketAddress getSocketAddress() {
            try {
                return new InetSocketAddress(InetAddress.getByName(host), port);
            } catch (UnknownHostException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
    }
}
