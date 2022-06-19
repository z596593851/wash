package com.washhandler.web.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.NettyRuntime;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Server {
    private static final Logger log = LoggerFactory.getLogger(Server.class);
    public static final int PORT=1096;

    public void start() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1,new DefaultThreadFactory("boss"));
        EventLoopGroup workerGroup = new NioEventLoopGroup(NettyRuntime.availableProcessors(),
                new DefaultThreadFactory("datawash_worker"));
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childHandler(new ServerInit());
        b.bind(PORT).sync();
        log.info("Server启动成功:"+PORT);
        //todo 启动zk
        //todo 使用这个jar包的项目new一个Server然后调用其start。然后可以利用spi机制加载处理类，将收到的消息传给spi接口。但是这样没法区分环境，还是需要编程式调用，手动注册回调函数
    }

    public static void main(String[] args) throws InterruptedException {
        new Server().start();
    }
}
