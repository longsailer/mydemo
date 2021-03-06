package org.ray.rpc.server.api;

import java.util.List;

import org.ray.rpc.core.client.ProviderInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * RPCProviderBalanceController.java
 * <br><br>
 * [write note]
 * @author: ray
 * @date: 2020年12月28日
 */
@Controller
@RequestMapping("/cluster")
public class RPCProviderBalanceController {
	private Logger log = LoggerFactory.getLogger(getClass());
	@Autowired
	private DiscoveryClient discoveryClient;
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET)
	public ProviderInstance findOneService(@RequestParam("appName") String appName){
		log.debug("检查集群服务清单......");
		ProviderInstance pi = new ProviderInstance();
		List<ServiceInstance> insList = discoveryClient.getInstances(appName);
		if(insList != null && insList.size() > 0){
			pi = InstanceBalanceUtils.random(insList);
			log.info("服务{}的实例：{}:{}", appName, pi.getHost(), pi.getPort());
		}else{
			log.debug("【{}】当前没有可用服务", appName);
		}
		return pi;
	}
}

