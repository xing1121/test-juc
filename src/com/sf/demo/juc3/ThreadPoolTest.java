package com.sf.demo.juc3;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * 描述：

 一、线程池：提供了一个线程队列，队列中保存着所有等待状态的线程。避免了创建与销毁额外开销，提高了响应的速度。
 
 二、线程池的体系结构：
    java.util.concurrent.Executor : 负责线程的使用与调度的根接口
        |--**ExecutorService 子接口: 线程池的主要接口
            |--ThreadPoolExecutor 线程池的实现类
            |--ScheduledExecutorService 子接口：负责线程的调度
                |--ScheduledThreadPoolExecutor ：继承 ThreadPoolExecutor， 实现 ScheduledExecutorService
 
 三、工具类 : Executors 
 ExecutorService newFixedThreadPool() : 创建固定大小的线程池
 ExecutorService newCachedThreadPool() : 缓存线程池，线程池的数量不固定，可以根据需求自动的更改数量。
 ExecutorService newSingleThreadExecutor() : 创建单个线程池。线程池中只有一个线程
 
 ScheduledExecutorService newScheduledThreadPool() : 创建固定大小的线程，可以延迟或定时的执行任务。

 * @author wdx
 * @date   2020年5月5日
 */
public class ThreadPoolTest {
	public static void main(String[] args) throws Exception {
		
		// 1.创建线程池
//		ExecutorService pool = Executors.newFixedThreadPool(5);
		
		// 核心数量25个，最大数量100，非核心线程空闲最大时间5S，默认线程工厂，满时策略为调用者执行
		
		// 初始25个线程，始终保持（除非设置allowCoreThreadTimeOut()）
		// 当25个线程都忙时，新来的任务进入queue
		// 当queue满了再来任务时会新开非核心线程执行queue队头的任务，新来的任务进入队尾
		// 非核心线程闲置超过5S会销毁
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
		List<Future<Integer>> list = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			Future<Integer> future = pool.submit(new Callable<Integer>() {
				@Override
				public Integer call() throws Exception {
					int sum = 0;
					for (int i = 0; i <= 100; i++) {
						sum += i;
					}
					return sum;
				}
			});
			list.add(future);
		}
		pool.shutdown();
		for (Future<Integer> future : list) {
			System.out.println(future.get());
		}
		
		
		/*
		// 2.为线程池分配任务
		ThreadPoolDemo tpd = new ThreadPoolDemo();
		for (int i = 0; i < 10; i++) {
			pool.submit(tpd);
		}
		
		// 3.关闭线程池
		pool.shutdown();
		*/
		
//		new Thread(tpd).start();
//		new Thread(tpd).start();
	}
}

class ThreadPoolDemo implements Runnable {

	private int i = 0;
	
	@Override
	public void run() {
		while(i < 100) {
			System.out.println(Thread.currentThread().getName() + " : " + ++i);
		}
	}
	
}