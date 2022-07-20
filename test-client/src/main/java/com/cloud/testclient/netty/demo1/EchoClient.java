package com.cloud.testclient.netty.demo1;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * @Author: helisen
 * @Date 2021/12/15 17:59
 * @Description:
 */
public class EchoClient {
    private final String host;
    private final int port;

    public EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)//指定EventLoopGroup以处理客户端时间；需要适用于NIO的实现
                    .channel(NioSocketChannel.class)//适用于NIO传输的Channel类型
                    .remoteAddress(new InetSocketAddress(host, port))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new EchoClientHandler());
                        }
                    });

            ChannelFuture f = b.connect().sync(); //连接到远程节点，阻塞等待直到连接完成
            f.channel().closeFuture().sync();//阻塞，直到Channel关闭
        }finally {
            group.shutdownGracefully().sync();  //关闭线程池并且释放所有的资源
        }
    }

    public static void main(String[] args) throws Exception {
        new EchoClient("localhost", 9999).start();
    }
}
