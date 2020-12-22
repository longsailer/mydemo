package org.ray.leveldb.config;

import java.io.IOException;

import org.ray.leveldb.server.LevelDBServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;

/**
 * LevelDBConfiguration.java
 * <br><br>
 * 数据库配置与初始化
 * @author: ray
 * @date: 2020年12月2日
 */
public class LevelDBConfiguration {

	private Logger log = LoggerFactory.getLogger(LevelDBConfiguration.class);
	
	@Bean
	public LevelDBServer levelDBServer(){
		try {
			return new LevelDBServer();
		} catch (IOException e) {
			log.error("创建数据库时发生错误", e);
			return null;
		}
	}
}

