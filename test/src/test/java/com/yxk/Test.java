package com.yxk;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

public class Test {

    public static void main(String[] args) throws Exception {
        //D:/rocketmq_download/broker/test/target/test-classes/log4j.xml
        RandomAccessFile rm = new RandomAccessFile("D:/rocketmq_download/broker/test/target/test-classes/12.xml", "rw");
        FileChannel channel = rm.getChannel();
        MappedByteBuffer rw = channel.map(FileChannel.MapMode.READ_WRITE, 1, 4);
        //System.out.println(new String(new byte[]{rw.get(),rw.get(),rw.get()}));
         rw.put((byte) 99);
         channel.write(rw);
        channel.close();


    }
}
