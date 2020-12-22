package org.ray.cluster;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * 系统管理中心的注册中心
 * 
 * @see EurekaServiceApplication
 * @author ray
 */
@EnableEurekaServer
@SpringBootApplication
public class ClusterServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(ClusterServiceApplication.class, args);
	}
}
