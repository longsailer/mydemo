package org.ray.netty;

import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Unit test for simple App.
 */
public class NettyAppTest {
	
	private static Logger log = LoggerFactory.getLogger(NettyAppTest.class);
	
	private ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
	
	private long totalTime;
	private int totalCount;
	
	public NettyAppTest(){
		pool.setCorePoolSize(100);
		pool.setMaxPoolSize(300);
		pool.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
		pool.setKeepAliveSeconds(60);
		pool.initialize();
	}
	
	public void execute(Runnable task){
		pool.execute(task, 10000);
	}
	
	public void shutdown(){
		while(pool.getActiveCount() > 0){
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		pool.shutdown();
	}
	
	public synchronized void addTime(long t){
		totalTime += t;
		totalCount ++;
	}
	
	public long getTotalTime(){
		return totalTime;
	}
	
	public int getTotalCount(){
		return totalCount;
	}
	
	public static void main(String[] args){
		NettyAppTest test = new NettyAppTest();
		long b = System.currentTimeMillis();
		long n = 300;
		for(long i=0;i<n;i++){
			NettyClient client = new NettyClient(i, "localhost", 6002);
			test.execute(new Runnable(){
				@Override
				public void run() {
					try {
						long begin = System.currentTimeMillis();
						client.start();
						long end = System.currentTimeMillis();
						long gap = end-begin;
						log.info("客户端【{}】链接结束.共花费时间是：{}", client.getId(), gap);
						test.addTime(gap);
					} catch (Exception e) {
						log.error("发生错误，详情：", e);
					}
				}
				
			});
		}
		test.shutdown();
		long e = System.currentTimeMillis();
		long g = e-b;
		log.info("成功执行：{}个；平均花费时间是:{}；并行花费时间是：{}", test.getTotalCount(), (test.getTotalTime()/test.getTotalCount()), g);
	}
}
