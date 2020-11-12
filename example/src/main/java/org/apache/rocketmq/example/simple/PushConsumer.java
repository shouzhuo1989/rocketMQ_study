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
package org.apache.rocketmq.example.simple;

import java.util.List;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;

public class PushConsumer {

    /**
     * push型消费者，是broker主动把消息推送过来
     * 那么当一个消息到达broker集群的时候，broker需要知道这个消息应该push给谁
     * 那么关于这个谁的信息从哪里获取呢？
     *
     * 如果让你写，你怎么写？
     *
     * 消费者：
     *     1、向namesvr发起连接，获取订阅topic下的路由信息(即所有broker)   线程1  定时更新路由信息
     *     2、向所有broker发起连接，注册自己是哪个消费者组，订阅的topic是什么 线程2  定时查看broker信息有没有变，变了的话，重新注册
     *     3、发送心跳包，时刻向broker报告自己的状态 线程3
     *     4、负载均衡，自己具体消费哪个消息队列中的消息 线程4  根据 消费者组 broker路由信息
     *     5、向broker发起连接，发送请求，拉取数据  线程5
     *     6、消费数据 线程6
     *
     *     是否需要专门的一个负载均衡线程？  根据代码，是需要的，具体为什么需要，后续还需研究
     *
     * 生产者：
     *     1、向namesvr发起连接，获取订阅topic下的路由信息(即所有broker)
     *     2、负载均衡，根据路由信息决定向哪个broker发起请求
     *     3、并发起请求
     *
     *
     */
    public static void main(String[] args) throws InterruptedException, MQClientException {
        //指定消费者组
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("kele_cg");
        //订阅topic
        consumer.subscribe("topicKele", "*");
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        //wrong time format 2017_0422_221800
        consumer.setConsumeTimestamp("20181109221800");
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                System.out.printf("%s Receive New Messages: %s %n", Thread.currentThread().getName(), msgs);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();
        System.out.printf("Consumer Started.%n");
    }
}
