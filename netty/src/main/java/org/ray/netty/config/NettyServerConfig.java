package org.ray.netty.config;

import org.ray.netty.server.NettyServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

/**
 * NettyServerConfig.java
 * <br><br>
 * @author: ray
 * @date: 2020年12月9日
 */
public class NettyServerConfig {

	@Value("${server.port}")
	private int port;
	
	@Bean
	public NettyServer nettyServer(){
		return new NettyServer(port);
	}
}

