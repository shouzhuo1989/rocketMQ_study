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

import java.util.List;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;


/**
 * 关于消费者的一些问题：
 * 1 消息来了，是broker告诉consumer呢，还是consumer去问broker呢？  后面的问题假设是后者
 * 2 有多个broker，consumer怎么决定去问哪个呢？
 * 3 从namesvr上获取了broker信息，但去问的时候，这个broker已经挂了？
 * 4、因为broker有主有从，怎么决定从哪个上面消费呢？
 * 5、消费有两种模式，第一种是一个消费者组中的消费者消费全量的订阅的消息，第二中是一条消息一旦被一个消费者组中一个消费者消费了，那么就不能被其他消费者消费了;两种分别是如何实现的？
 * 6、消费者需要向broker注册组信息，这个注册是向所有broker注册吗？
 * 7 如何标记一个消息已经被消费过了？
 *
 */

/**
 * 消费者启动的时候需要指定：消费者组、nameServer、订阅的topic(可以订阅多个topic)
 */
public class Consumer {

    public static void main(String[] args) throws InterruptedException, MQClientException {


        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("consumer_group_xiaoyanNew1");

        consumer.setNamesrvAddr("127.0.0.1:9876");
        //consumer.setUnitName();

        //consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);

        /**
         * 假如订阅多个topic的话，就调用subscribe方法多次
         */
        consumer.subscribe("kele", "*");

        consumer.registerMessageListener(new MessageListenerConcurrently() {

            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
                ConsumeConcurrentlyContext context) {
                System.out.printf("%s Receive New Messages: %s %n", Thread.currentThread().getName(), msgs);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        consumer.start();

        System.out.printf("Consumer Started.%n");
    }
}
