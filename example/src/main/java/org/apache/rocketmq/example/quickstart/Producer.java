/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.rocketmq.example.quickstart;

import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

/**
 * This class demonstrates how to send messages to brokers using provided {@link DefaultMQProducer}.
 *
 * 1、发送消息的时候需要指定：生产者组  nameServer地址
 *
 * 2、我们启动了一个应用，主要做以下几件事情：
 *      --启动一些线程
 *      --初始化数据，就是new一些对象，然后建立起他们之间的关联关系
 *      --监听一些端口
 */
public class Producer {
    public static void main(String[] args) throws MQClientException, InterruptedException {


        /*
         * Instantiate with a producer group name.
         */
        //给一个线程A，我们new一个DefaultMQProducer对象
        DefaultMQProducer producer = new DefaultMQProducer("please_rename_unique_group_name");

        /*
         * Specify name server addresses.
         * <p/>
         *
         * Alternatively, you may specify name server addresses via exporting environmental variable: NAMESRV_ADDR
         * <pre>
         * {@code
         * producer.setNamesrvAddr("name-server1-ip:9876;name-server2-ip:9876");
         * }
         * </pre>
         */
        producer.setNamesrvAddr("127.0.0.1:9876");
        //假如设置了这个值，即使是同一进程中启动的生产者，使用的也是不同的MQClientInstance
       // producer.setUnitName("");
        /*
         * Launch the instance.
         * producer启动时应该做哪些事情？
         * 1、向nameserver发起请求，获取所有broker信息（获取到broker信息后，存储在哪里？）
         */
        producer.start();

        for (int i = 0; i < 12; i++) {
            try {

                /*
                 * Create a message instance, specifying topic, tag and message body.
                 */
                Message msg = new Message("kele" /* Topic */,
                    "TagA" /* Tag */,
                    ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET) /* Message body */
                );

                // 设置延时等级3,这个消息将在10s之后发送(现在只支持固定的几个时间,详看delayTimeLevel)
                //msg.setDelayTimeLevel(3);

                /*
                 * Call send message to deliver message to one of brokers.
                 */
                SendResult sendResult = producer.send(msg);

                System.out.printf("%s%n", sendResult);
            } catch (Exception e) {
                e.printStackTrace();
                Thread.sleep(1000);
            }
        }

        /*
         * Shut down once the producer instance is not longer in use.
         */
        producer.shutdown();
    }
}
