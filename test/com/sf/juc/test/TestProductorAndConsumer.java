package com.sf.juc.test;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 描述：生产者消费者案例--------------线程通信，线程虚假唤醒
 * 		①问题：当货仓已满时仍在不停的进货，货仓缺货时仍在不停的售货，浪费资源。
 * 		②解决：使用线程通信wait-notify或lock的condition。
 * 		③问题：线程永远无法结束（使用if-else，且商品最大1），B成功售货唤醒A，B仍然占着资源执行代码发现缺货，进入等待状态，A抢到资源跳过else直接结束。
 * 		④解决：去掉else。
 * 		⑤问题：多个消费者时，货物出现负数，原因在于若两个消费者BC同时处于等待（货物为0），这时A进货货物变为1，BC同时都被唤醒，都会消费，造成负数。
 * 		⑥解决：使用while判断解决线程的虚假唤醒，只要不满足条件就一直处于等待的状态。
 * @author 80002888
 * @date   2018年7月3日
 */
public class TestProductorAndConsumer {

	public static void main(String[] args) {
		
		Clerk clerk = new Clerk();
		
		new Thread(()->{
			for (int i = 0; i < 20; i++) {
				clerk.get();
			}
		}, "生产者A").start();
		
		new Thread(()->{
			for (int i = 0; i < 20; i++) {
				clerk.sale();
			}
		}, "消费者B").start();
		
		new Thread(()->{
			for (int i = 0; i < 20; i++) {
				clerk.sale();
			}
		}, "消费者C").start();
		
	}
	
}

class Clerk {
	
	private int product = 0;
	
	private Lock lock = new ReentrantLock();
	
	private Condition condition = lock.newCondition();
	
	public void get(){
		lock.lock();
		try {
			while (product >= 1) {
				System.out.println("货仓已满！");
				condition.await();
			}
			System.out.println(Thread.currentThread().getName() + "成功进货，余货：" + ++product);
			condition.signalAll();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
	
	public void sale(){
		lock.lock();
		try {
			while (product <= 0) {
				System.out.println("货仓缺货！");
				condition.await();
			}
			System.out.println(Thread.currentThread().getName() + "成功售货，余货：" + --product);
			condition.signalAll();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
	
}
