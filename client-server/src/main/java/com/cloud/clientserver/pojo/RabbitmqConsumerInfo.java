package com.cloud.clientserver.pojo;

import lombok.Data;

/**
 * @Author: 何立森
 * @Date: 2023/02/21/14:06
 * @Description:
 */
@Data
public class RabbitmqConsumerInfo {

    private ChannelDetailInfo channel_details;

    private Queue queue;

    @Data
    public class ChannelDetailInfo {
        public String connection_name;
        public String name;
        public String node;
        public Integer number;
        public String peer_host;
        public Integer peer_port;
        public String user;
    }

    @Data
    public class Queue {
        private String name;
        private String vhost;
    }
}
