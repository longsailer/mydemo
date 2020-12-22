package org.ray.netty;

import org.ray.netty.config.NettyServerConfig;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableAutoConfiguration
@ComponentScan({"org.ray"})
@Import({NettyServerConfig.class})
public class AppConfig {
}
