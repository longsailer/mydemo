package org.ray.netty;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

/**
 * NettyClient.java
 * <br><br>
 * [write note]
 * @author: ray
 * @date: 2020年12月9日
 */
public class NettyClient {
	private final String host;
    private final int port;
    private long id;
    
    public NettyClient(long id, String host, int port) {
        this.host = host;
        this.port = port;
        this.id = id;
    }
    
    public long getId(){
    	return this.id;
    }
    
    public void start() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();                //1
            b.group(group)                                //2
             .channel(NioSocketChannel.class)            //3
             .remoteAddress(new InetSocketAddress(host, port))    //4
             .handler(new ChannelInitializer<SocketChannel>() {		//5
                 @Override
                 public void initChannel(SocketChannel ch) 
                     throws Exception {
                     ch.pipeline().addLast(new EchoClientHandler());
                 }
             });

            ChannelFuture f = b.connect().sync();        //6
            ByteBuf msg = Unpooled.copiedBuffer(id+":hello", CharsetUtil.UTF_8);
            f.channel().writeAndFlush(msg);
            f.channel().closeFuture().sync();            //7
        } catch(Exception e){
        	e.printStackTrace();
        } finally {
            group.shutdownGracefully().sync();            //8
        }
    }
}

