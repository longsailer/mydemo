package org.ray.rpc.client;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInboundHandler;

/**
 * RpcIOChannal.java
 * <br><br>
 * @author: ray
 * @date: 2020年12月29日
 */
public class RpcIOChannelFactory {
	private static RpcIOChannelFactory factory = new RpcIOChannelFactory();
	private NettyClient nettyClient;
	
	private RpcIOChannelFactory(){}
	
	public static RpcIOChannelFactory getInstance(){
		return factory;
	}
	
	public RpcIOChannelFactory build(String host, int port, ChannelInboundHandler channelInboundHandler) throws IllegalStateException, InterruptedException{
		if(this.nettyClient == null){
			this.nettyClient = new NettyClient(System.currentTimeMillis(), host, port);
		}else if(!this.nettyClient.isSame(host, port)){
			this.nettyClient = new NettyClient(System.currentTimeMillis(), host, port);;
		}
		this.nettyClient.bootstrap(channelInboundHandler);
		return this;
	}
	
	public ChannelFuture connect() throws InterruptedException{
		return this.nettyClient.connect();
	}
	
	public void shutdown() throws InterruptedException{
		this.nettyClient.shutdown();
	}
}

