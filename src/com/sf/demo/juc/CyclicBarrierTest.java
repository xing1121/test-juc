package com.sf.demo.juc;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * 描述：

CyclicBarrier：
main线程，N个其他线程
其他线程执行到某个条件进行等待，等到N个都进入等待时，N个线程一起放行。

 * @author wdx
 * @date   2020年5月5日
 */
public class CyclicBarrierTest {

	public static void main(String[] args) {
		int point = 10;
		CyclicBarrier cb = new CyclicBarrier(point, ()->System.out.println("所有线程达标，await结束，进行唤醒操作！"));
	
		ThreadPoolExecutor pool = new ThreadPoolExecutor(
				25,
				100, 
				5,
				TimeUnit.SECONDS, 
				new LinkedBlockingQueue<>(10), 
				new ThreadFactory() {
				    
				    private AtomicInteger atomicInteger = new AtomicInteger(0);
				    
                    @Override
                    public Thread newThread(Runnable r) {
                        int c = atomicInteger.incrementAndGet(); 
                        Thread thr = new Thread(r);
                        thr.setName("MyThread" + c);
                        return thr;
                    }
                    
                },
				new ThreadPoolExecutor.CallerRunsPolicy());
		
		ArrayList<Future<String>> resList = new ArrayList<>(point);
		for (int i = 1; i <= point; i++) {
			Future<String> resFuture = pool.submit(new BarrierRunnable(cb));
			resList.add(resFuture);
		}
		pool.shutdown();
		
		for (Future<String> resFuture : resList) {
			try {
				System.out.println(resFuture.get());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
}

class BarrierRunnable implements Callable<String> {
	
	private CyclicBarrier cb;
	
	public BarrierRunnable(CyclicBarrier cb) {
		this.cb = cb;
	}

	/**
	 * 打印0-49999，然后进行等待，等到进入等待状态线程数量达到阙值，被唤醒后结束。
	 */
	@Override
	public String call() {
		
		String tName = Thread.currentThread().getName();
		
		for (int i = 0; i < 50000; i++) {
			System.out.println(tName + "---" + i);
		}
		
		try {
			cb.await();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return tName + " 唤醒!";
	}
	
}