package org.ray.cluster;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * WebSecurityConfig.java <br>
 * <br>
 * @author: ray
 * @date: 2020年1月19日
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	private Logger log = LoggerFactory.getLogger(getClass());
	@Autowired
	private DiscoveryClient discoveryClient;
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		super.configure(http);
		http.csrf().disable();
	}
	@PostConstruct
	public void listService(){
		new Thread("Servicelistlistener"){
			@Override
			public void run() {
				do{
					log.debug("检查集群服务清单......");
					List<String> serviceList = discoveryClient.getServices();
					if(serviceList != null && serviceList.size() > 0){
						for(int i=0;i<serviceList.size();i++){
							String id = serviceList.get(i);
							List<ServiceInstance> insList = discoveryClient.getInstances(id);
							for(ServiceInstance si : insList){
								log.info("服务{}的实例：{}:{}", id, si.getHost(), si.getPort());
							}
						}
					}else{
						log.debug("当前没有可用服务");
					}
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}while(true);
			}
		}.start();
	}
}
