package org.ray.leveldb;

import java.io.UnsupportedEncodingException;

import javax.annotation.PostConstruct;

import org.iq80.leveldb.DBException;
import org.ray.leveldb.server.LevelDBServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class LevelDBApplication {
	@Autowired
	private LevelDBServer server;
	
	@PostConstruct
	public void run(){
		try {
			long begin = System.currentTimeMillis();
			for(int i=0; i<1000000; i++){
				server.put("key"+i, "value"+i);
			}
			long end = System.currentTimeMillis();
			System.out.println("插入100W条数据花费时间: "+(end-begin));
			
			begin = System.currentTimeMillis();
			String v = server.get("key979988");
			end = System.currentTimeMillis();
			System.out.println("从其中查一条["+v+"]所花费的时间: "+(end-begin));
		} catch (DBException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(LevelDBApplication.class);
    }
	
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(AppConfig.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }
}
