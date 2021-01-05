package org.ray.rpc.consumer.demo.service;

import java.util.List;

import javax.annotation.PostConstruct;

import org.ray.rpc.consumer.demo.bean.User;
import org.ray.rpc.consumer.demo.client.HelloWorldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * TestService.java <br>
 * <br>
 * [write note]
 * 
 * @author: ray
 * @date: 2020年12月25日
 */
@Component
public class TestService {
	@Autowired
	private HelloWorldService helloWorldService;

	/*@PostConstruct
	public void test() {
		long begin = System.currentTimeMillis();
		for (int i = 0; i < 300; i++) {
			String result = helloWorldService.say();
			System.out.println("1.[" + i + "]" + result);
		}
		long end = System.currentTimeMillis();
		System.out.println("say Total:"+(end-begin));
	}

	@PostConstruct
	public void test1() {
		long begin = System.currentTimeMillis();
		for (int i = 0; i < 300; i++) {
			List<String> result = helloWorldService.list(10);
			System.out.println("2.[" + i + "]" + String.join(",", result.toArray(new String[result.size()])));
		}
		long end = System.currentTimeMillis();
		System.out.println("list Total:"+(end-begin));
	}*/

	@PostConstruct
	public void test2() {
		long begin = System.currentTimeMillis();
		for (int i = 0; i < 100; i++) {
			User result = helloWorldService.getUser("张三", true, 30);
			System.out.println("3.[" + i + "]" + result.getName());
		}
		long end = System.currentTimeMillis();
		System.out.println("getUser Total:"+(end-begin));
	}
}
