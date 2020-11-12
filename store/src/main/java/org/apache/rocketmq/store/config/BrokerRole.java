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
package org.apache.rocketmq.store.config;

/**
 * SYNC和ASYNC表示Master和Slave同步消息设置，SYNC的意思是当Slave和Master消息同步完成后，再返回发送成功的状态。
 */
public enum BrokerRole {
    //新的消息存储时不需要等slave同步好
    ASYNC_MASTER,
    //新消息存现时需要等slave同步好(也就是返回的 Ack Offset >= 当前消息的CommitLog Offset;)
    SYNC_MASTER,
    SLAVE;
}
