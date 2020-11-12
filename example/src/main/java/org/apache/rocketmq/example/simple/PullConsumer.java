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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.consumer.PullResult;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageQueue;

public class PullConsumer {
    /**
     * 通过MessageQueue可以唯一的定位到集群中的一个Queue，至于是到slaver上拿数据还是到master上，由broker决定
     */
    private static final Map<MessageQueue, Long> OFFSE_TABLE = new HashMap<MessageQueue, Long>();

    /**
     * pull型消费者是主动向broker拉去消息
     * pull型消费者需要先根据namesrv的地址获取topic的路由信息
     *
     * 1、首先根据topic到namesvr查询所有的MessageQueue；
     * 2、向broker发起rpc请求；
     *
     *
     *
     */
    public static void main(String[] args) throws MQClientException {
        // 指定消费者组
        DefaultMQPullConsumer consumer = new DefaultMQPullConsumer("please_rename_unique_group_name_5");
        // 指定namesvr的地址
        consumer.setNamesrvAddr("127.0.0.1:9876");
        consumer.start();
        // 根据topic获取所有的MessageQueue  （MessageQueue描述了唯一的一个queue，包括这个queue属于哪个broker上的哪个topic）
        Set<MessageQueue> mqs = consumer.fetchSubscribeMessageQueues("broker-a");
        for (MessageQueue mq : mqs) {
            SINGLE_MQ:
            while (true) {
                try {
                    // 每次消费 我们需要传入是消费哪个queue，以及这个queue我们上次消费到了哪里
                    PullResult pullResult =
                        consumer.pullBlockIfNotFound(mq, null, getMessageQueueOffset(mq), 32);
                    putMessageQueueOffset(mq, pullResult.getNextBeginOffset());
                    switch (pullResult.getPullStatus()) {
                        case FOUND:
                            break;
                        case NO_MATCHED_MSG:
                            break;
                        case NO_NEW_MSG:
                            break SINGLE_MQ;
                        case OFFSET_ILLEGAL:
                            break;
                        default:
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        consumer.shutdown();
    }

    private static long getMessageQueueOffset(MessageQueue mq) {
        Long offset = OFFSE_TABLE.get(mq);
        if (offset != null)
            return offset;

        return 0;
    }

    private static void putMessageQueueOffset(MessageQueue mq, long offset) {
        OFFSE_TABLE.put(mq, offset);
    }

}
