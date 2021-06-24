每一个类的设计目的是什么

由于一个类的引用可能会被多个线程持有，所以类中的数据有并发安全问题，而类中的锁就是为了解决这个问题；

铁锹  每个人都用自己的铁锹自然不会有问题


结构   有机整体    持久运作 


# consumer结构分析

MQClientManager只有一个
MQClientInstance可以有多个
defaultMQPushConsumer可以有多个，每个defaultMQPushConsumer可以用自己的MQClientInstance，也可以公用一个

每个defaultMQPushConsumer有自己的RebalanceImpl，RebalanceImpl里面有一个hash表，里面记录了当前消费者的订阅信息
每个defaultMQPushConsumer有自己的PullAPIWrapper
每个defaultMQPushConsumer有自己的consumeMessageService
每个MQClientInstance都维护了一个map，里面是使用自己的defaultMQPushConsumer和其group的对应关系

每个MQClientInstance有自己的mQClientAPIImpl 》负责netty
每个MQClientInstance有自己的pullMessageService 》负责拉取消息
每个MQClientInstance有自己的rebalanceService 》负责负载均衡
每个MQClientInstance有自己的ConsumerStatsManager 》负责消费状态管理

每个mQClientAPIImpl有自己的NettyRemotingClient

总结： 假如只有一个消费者，那么就只有一个MQClientInstance，也就只有一个mQClientAPIImpl，也就只有一个NettyRemotingClient；



## 负载均衡：
每个MQClientInstance拿出自己维护的consumer
每个consumer拿出自己的订阅信息，一个topic一个topic的去负载均衡
先根据topic从broker上拉取当前topic下的queue的分布情况
然后根据消费者组获取clientid(前面我们说：【每个MQClientInstance都维护了一个map，里面是使用自己的defaultMQPushConsumer和其group的对应关系】，反过来看，就是要找到所有的map，
当前group存在于其中；然后根据map找到对应的clientid)


消费者  记录自己的订阅信息    有netty    有负载均衡    有消息拉取服务





生产者和消费者 只是客户端    broker是服务端 接受生产者和消费者的请求  也是客户端 向nameServ发送请求
nameServ是服务端，接收生产者、消费者、broker的请求









