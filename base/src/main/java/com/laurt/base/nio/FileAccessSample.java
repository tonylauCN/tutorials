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

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * <p>Title: FileAccessSample
 * <p>Description: tutorials
 * <p>Copyright: 2017/10/9 下午4:28
 * <p>Company: rongshu
 * <p>author: LiuQingqing
 * <p>package: com.laurt.base.nio
 *
 * @version v1.0.0
 */
public class FileAccessSample {

    static String FILE = "target/classes/file-access-sample.txt";
    static String TARGET_FILE = "target/classes/file-access-sample-trans.txt";

    public static void main(String[] args) throws IOException {

        String filePath = FILE;
        FileAccessSample sample = new FileAccessSample();

        sample.print(filePath);

        sample.transform(filePath, TARGET_FILE);

    }

    // 读取文件
    private void print(String filePath) throws IOException {
        System.out.println((new File(filePath)).getAbsolutePath());
        RandomAccessFile accessFile = new RandomAccessFile(filePath, "rw");
        FileChannel channel = accessFile.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(64);
        try {
            int bytesRead = channel.read(buffer);
            while (bytesRead != -1) {
                System.out.println("Read " + bytesRead);
                buffer.flip();

                while (buffer.hasRemaining()) {
                    System.out.print((char) buffer.get());
                }
                buffer.clear();
                bytesRead = channel.read(buffer);
            }
        } finally {
            channel.close();
            accessFile.close();
        }
    }

    // 文件管道写入
    private void transform(String filePathFrom, String filePathTo) {
        RandomAccessFile fromFile;
        RandomAccessFile toFile;
        FileChannel fromChannel = null;
        FileChannel toChannel = null;
        try {
            fromFile = new RandomAccessFile(filePathFrom, "rw");
            toFile = new RandomAccessFile(filePathTo, "rw");

            fromChannel = fromFile.getChannel();
            toChannel = toFile.getChannel();

            long position = 0, count = fromChannel.size();

            toChannel.transferFrom(fromChannel, position, count);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                toChannel.close();
            } catch (IOException e) {
                // nothing;
            }
            try {
                fromChannel.close();
            } catch (IOException e) {
                // nothing;
            }
        }
    }
}
