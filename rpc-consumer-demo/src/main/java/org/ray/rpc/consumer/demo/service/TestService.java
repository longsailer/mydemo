package org.ray.rpc.consumer.demo.service;

import java.util.List;

import javax.annotation.PostConstruct;

import org.ray.rpc.consumer.demo.bean.User;
import org.ray.rpc.consumer.demo.client.HelloWorldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * TestService.java
 * <br><br>
 * [write note]
 * @author: ray
 * @date: 2020年12月25日
 */
@Component
public class TestService{
	@Autowired
	private HelloWorldService helloWorldService;
	
	@PostConstruct
	public void test(){
		String result = helloWorldService.say();
		System.out.println("1."+result);
	}
	
	@PostConstruct
	public void test1(){
		List<String> result = helloWorldService.list(10);
		System.out.println("2."+new StringBuffer().append(result.toArray(new String[]{})));
	}
	
	@PostConstruct
	public void test2(){
		User result = helloWorldService.getUser("张三", true, 30);
		System.out.println("3."+result.getName());
	}
}

