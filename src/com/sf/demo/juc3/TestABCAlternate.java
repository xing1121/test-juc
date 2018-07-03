package com.sf.demo.juc3;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 描述：编写一个程序，开启3个线程，这3个线程的ID分别为A、B、C，循环交替打印10遍，必须按顺序，如ABCABCABC...
 * 		Lock+Condition，线程操纵资源类
 * @author 80002888
 * @date   2018年6月11日
 */
public class TestABCAlternate {
	
	public static void main(String[] args) {
		AlternateDemo ad = new AlternateDemo();
		
		new Thread(()->{
			for (int i = 0; i < 10; i++) {
				ad.loopA();
			}
		}, "A").start();
		
		new Thread(()->{
			for (int i = 0; i < 10; i++) {
				ad.loopB();
			}
		}, "B").start();
		
		new Thread(()->{
			for (int i = 0; i < 10; i++) {
				ad.loopC();
			}
		}, "C").start();
	}

}

class AlternateDemo {
	
	private int flag = 1;
	
	private Lock lock = new ReentrantLock();
	private Condition condition1 = lock.newCondition();
	private Condition condition2 = lock.newCondition();
	private Condition condition3 = lock.newCondition();
	
	public void loopA() {
		lock.lock();
		try {
			// 1.判断
			while (flag != 1) {
				condition1.await();
			}
			// 2.打印
			System.out.println(Thread.currentThread().getName());
			// 3.唤醒
			flag = 2;
			condition2.signal();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
	
	public void loopB() {
		lock.lock();
		try {
			// 1.判断
			while (flag != 2) {
				condition2.await();
			}
			// 2.打印
			System.out.println(Thread.currentThread().getName());
			// 3.唤醒
			flag = 3;
			condition3.signal();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
	
	public void loopC() {
		lock.lock();
		try {
			// 1.判断
			while (flag != 3) {
				condition3.await();
			}
			// 2.打印
			System.out.println(Thread.currentThread().getName());
			System.out.println("-------------------------------------");
			// 3.唤醒
			flag = 1;
			condition1.signal();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
}
