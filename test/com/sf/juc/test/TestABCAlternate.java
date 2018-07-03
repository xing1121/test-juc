package com.sf.juc.test;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 描述：轮询打印ABC------使用线程的通信机制
 * @author 80002888
 * @date   2018年7月3日
 */
public class TestABCAlternate {
	public static void main(String[] args) {
		
		Printer printer = new Printer();
		
		new Thread(()->{
			for (int i = 0; i < 10; i++) {
				printer.printA();
			}
		}).start();
		
		new Thread(()->{
			for (int i = 0; i < 10; i++) {
				printer.printB();
			}
		}).start();
		
		new Thread(()->{
			for (int i = 0; i < 10; i++) {
				printer.printC();
			}
		}).start();
		
	}
}

class Printer {
	
	private Lock lock = new ReentrantLock();
	private int flag = 1;
	
	private Condition conditionA = lock.newCondition();
	private Condition conditionB = lock.newCondition();
	private Condition conditionC = lock.newCondition();
	
	public void printA(){
		lock.lock();
		try {
			while (flag != 1) {
				conditionA.await();
			}
			System.out.println("A");
			Thread.sleep(100);
			flag++;
			conditionB.signal();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
	
	public void printB(){
		lock.lock();
		try {
			while (flag != 2) {
				conditionB.await();
			}
			Thread.sleep(100);
			System.out.println("B");
			flag++;
			conditionC.signal();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
	
	public void printC(){
		lock.lock();
		try {
			while (flag != 3) {
				conditionC.await();
			}
			Thread.sleep(100);
			System.out.println("C");
			flag = 1;
			conditionA.signal();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
	
}