package org.ray.leveldb;

import org.ray.leveldb.config.LevelDBConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableAutoConfiguration
@ComponentScan({"org.ray"})
@Import({LevelDBConfiguration.class})
public class AppConfig {
}
