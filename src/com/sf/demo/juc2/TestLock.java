package com.sf.demo.juc2;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
 * 用于解决多线程安全的方式：
 * synchronized：隐式锁
 * 		1.同步代码块
 * 
 * 		2.同步方法
 * 
 * 		3.JDK1.5以后--同步锁，显示锁：需要通过lock()上锁，通过unlock()释放锁
 */
public class TestLock {
	
	public static void main(String[] args) {
		
		Ticket ticket = new Ticket();
		
		new Thread(ticket, "1号窗口").start();
		new Thread(ticket, "2号窗口").start();
		new Thread(ticket, "3号窗口").start();
		
	}
	
}

class Ticket implements Runnable {

	private int ticket = 100;
	
	private Lock lock = new ReentrantLock();
	
	@Override
	public void run() {
		while (true) {
			
			try {
				lock.lock();
				if (ticket > 0 ) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
					}
					System.out.println(Thread.currentThread().getName() + " 完成售票，余票：" + --ticket);
				}
			} finally {
				lock.unlock();
			}
			
		}
	}
	
}