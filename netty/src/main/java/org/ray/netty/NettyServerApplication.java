package org.ray.netty;

import javax.annotation.PostConstruct;

import org.ray.netty.server.NettyServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Hello world!
 */
@EnableDiscoveryClient
@SpringBootApplication
public class NettyServerApplication {
	@Autowired
	private NettyServer nettyServer;
	
	@PostConstruct
	public void start(){
		nettyServer.start();
	}
	
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(NettyServerApplication.class);
    }
	
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(AppConfig.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }
}
