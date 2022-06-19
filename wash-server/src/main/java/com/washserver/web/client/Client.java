package com.washserver.web.client;

import com.washserver.web.SyncFuture;
import com.washserver.web.protocol.model.Message;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

public class Client {

    private static final Logger log = LoggerFactory.getLogger(Client.class);
    private Channel channel;
    private final EventLoopGroup workerGroup = new NioEventLoopGroup(0, new DefaultThreadFactory("datawash_worker"));
    private static final LoadingCache<Long, SyncFuture<Message>> futureCache = CacheBuilder.newBuilder()
            //设置缓存容器的初始容量为10
            .initialCapacity(100)
            // maximumSize 设置缓存大小
            .maximumSize(10000)
            //设置并发级别为20，并发级别是指可以同时写缓存的线程数
            .concurrencyLevel(20)
            // expireAfterWrite设置写缓存后8秒钟过期
            .expireAfterWrite(8, TimeUnit.SECONDS)
            //设置缓存的移除通知
            .removalListener(notification -> log.info("LoadingCache: {} was removed, cause is {}",notification.getKey(), notification.getCause()))
            //build方法中可以指定CacheLoader，在缓存不存在时通过CacheLoader的实现自动加载缓存
            .build(new CacheLoader<Long, SyncFuture<Message>>() {
                @Override
                public SyncFuture<Message> load(Long id) throws Exception {
                    // 当获取key的缓存不存在时，不需要自动添加
                    return null;
                }
            });

    public void start(int port,String host) throws InterruptedException {
        Bootstrap b = new Bootstrap();
        b.group(workerGroup)
                .channel(NioSocketChannel.class)
                .handler(new ClientInit(this));
//            ChannelFuture future = b.connect(new InetSocketAddress(host,port)).sync();
        ChannelFuture channelFuture = b.connect(new InetSocketAddress(host,port)).sync();
        channelFuture.addListener((ChannelFutureListener) future -> {
            if(future.isSuccess()) {
                log.info("连接Netty服务端成功...");
            }else {
                log.info("连接Netty服务端失败，进行断线重连...");
                final EventLoop loop= future.channel().eventLoop();
                loop.schedule(() -> {
                    log.info("连接正在重试...");
                    try {
                        start(port, host);
                    } catch (InterruptedException e) {
                        log.error("重连失败:",e);
                    }
                }, 20, TimeUnit.SECONDS);
            }
        });
        channel = channelFuture.channel();
    }

    public void send(Message message) {
        if(channel==null||!channel.isActive()){
            throw new IllegalStateException("和服务器还未未建立起有效连接！" +
                    "请稍后再试！！");
        }
        ChannelFuture future = channel.writeAndFlush(message);
        future.addListener((ChannelFutureListener) f -> {
            if(f.isSuccess()) {
                log.info("客户端发送成功");
            }else {
                log.info("客户端发送失败");
            }
        });
    }

    /**
     * 发送同步消息
     */
    public Message sendSync(Message message) {
        if(channel==null||!channel.isActive()){
            throw new IllegalStateException("和服务器还未未建立起有效连接！" +
                    "请稍后再试！！");
        }
        SyncFuture<Message> syncFuture = new SyncFuture<>();
        futureCache.put(message.getId(), syncFuture);
        Message result=null;
        try {
            ChannelFuture future = channel.writeAndFlush(message);
            future.addListener((ChannelFutureListener) f -> {
                if(f.isSuccess()) {
                    log.info("客户端发送同步消息成功");
                }else {
                    log.info("客户端发送同步消息失败");
                }
            });
            result = syncFuture.get(8, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("sendSync错误:",e);
        }
        return result;
    }

    /**
     * 确认同步消息
     */
    public void ackSyncMsg(Message msg) {
        log.info("ACK确认信息: {}",msg.getId());
        SyncFuture<Message> syncFuture = futureCache.getIfPresent(msg.getId());
        // 如果不为null, 则通知返回
        if(syncFuture != null) {
            syncFuture.setResponse(msg);
            //主动释放
            futureCache.invalidate(msg.getId());
        }
    }

    public void close() {
        channel.close();
    }

}
