package com.cloud.rocketmq.transaction;

import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.HashMap;
import java.util.Map;

public class TransactionListenerImpl implements TransactionListener {
    /**
     * 如果half消息发送成功了，就会在这里回调下面的方法，就可以执行本地事务了
     */
    @Override
    public LocalTransactionState executeLocalTransaction(Message message, Object o) {
        //执行订单本地事务
        //接着根据本地一连串事务执行结果，去选择执行commit or rollback
        try {
            //如果本地事务都执行成功了，返回commit
            System.out.println("本地事务执行成功");
            return LocalTransactionState.COMMIT_MESSAGE;
        }catch (Exception e) {
            //如果本地事务执行失败，回滚所有一切执行过的操作
            //如果本地事务执行失败，返回rollback，标记half消息无效
            System.out.println("本地事务执行失败");
            return LocalTransactionState.ROLLBACK_MESSAGE;
        }
    }

    /**
     * 如果因为各种原因，没有返回commit或者rollback，RocketMQ就会回调下面方法，如果下面方法没有返回给RocketMQ,RocketMQ最大回调尝试15次，
     * 之后就认为当前这条消息为rollback。
     */
    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
        Map<String, Integer> localTrans = new HashMap<>();
        //查询本地事务，是否执行成功了
        Integer status = localTrans.get(messageExt.getTransactionId());
        System.out.println("查询本地事务是否执行成功");
        //根据本地事务的情况去选择执行commit or rollback
        if(status != null) {
            switch (status) {
                case 0: return LocalTransactionState.UNKNOW;
                case 1: return LocalTransactionState.COMMIT_MESSAGE;
                case 2: return LocalTransactionState.ROLLBACK_MESSAGE;
            }
        }
        return LocalTransactionState.COMMIT_MESSAGE;
    }
}
