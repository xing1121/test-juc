package com.sf.juc.test;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 描述：3个售票窗口同时售票100张
 * 		①问题：容易出现无序、重复、负数等现象
 * 		②解决：使用synchronze或lock
 * @author 80002888
 * @date   2018年7月3日
 */
public class TestTicket {
	public static void main(String[] args) {
		Ticket ticket = new Ticket();
		
		new Thread(ticket, "A窗口").start();
		new Thread(ticket, "B窗口").start();
		new Thread(ticket, "C窗口").start();
	}
}

class Ticket implements Runnable{

	private int ticket = 100;
	
	private Lock lock = new ReentrantLock();
	
	@Override
	public void run() {
		while (true) {
			try {
				lock.lock();
				if (ticket > 0) {
					Thread.sleep(100);
					System.out.println(Thread.currentThread().getName() + "成功售票，余票：" + --ticket);
				} else {
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
		}
	}
	
}