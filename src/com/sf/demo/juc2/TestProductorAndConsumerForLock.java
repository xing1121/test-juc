//package com.sf.demo.juc2;
//
//import java.util.concurrent.locks.Condition;
//import java.util.concurrent.locks.Lock;
//import java.util.concurrent.locks.ReentrantLock;
//
///**
// * 描述：使用Lock解决生产者消费者（包括Lock的Condition通信）
// * 
// * @author 80002888
// * @date   2018年6月8日
// */
//public class TestProductorAndConsumerForLock {
//	public static void main(String[] args) {
//		// 共享一个店员
//		Clerk clerk = new Clerk();
//		Productor productor = new Productor(clerk);
//		Consumer consumer = new Consumer(clerk);
//		
//		new Thread(productor, "生产者A").start();
//		new Thread(consumer, "消费者B").start();
//		
//		new Thread(productor, "生产者C").start();
//		new Thread(consumer, "消费者D").start();
//	}
//}
//
//// 店员
//class Clerk {
//	
//	private int product = 0;
//	private Lock lock = new ReentrantLock();
//	private Condition condition = lock.newCondition();
//	
//	// 进货
//	public void get(){
//		lock.lock();
//		try {
//			while (product >= 1) {		//使用while代替if
//				System.out.println("货仓已满！");
//				// 货仓满时不再生产，而是等待
//				try {
//					condition.await();
//				} catch (InterruptedException e) {
//				}
//			}
//			
//			System.out.println(Thread.currentThread().getName() + " : " + ++product);
//			// 生产成功，唤醒消费者可以消费了
//			condition.signalAll();
//		} finally {
//			lock.unlock();
//		}
//	}
//	
//	// 卖货
//	public void sale(){
//		lock.lock();
//		try {
//			while (product <= 0) {		//使用while代替if
//				System.out.println("货仓缺货！");
//				// 缺货时不再消费，而是等待
//				try {
//					condition.await();
//				} catch (InterruptedException e) {
//				}
//			}
//			System.out.println(Thread.currentThread().getName() + " : " + --product);
//			// 消费成功，唤醒生产者可以生产了
//			condition.signalAll();
//		} finally {
//			lock.unlock();
//		}
//	}
//}
//
//// 生产者
//class Productor implements Runnable{
//	
//	private Clerk clerk;
//
//	public Productor(Clerk clerk) {
//		this.clerk = clerk;
//	}
//
//	@Override
//	public void run() {
//		for (int i = 0; i < 20; i++) {
//			try {
//				Thread.sleep(200);
//			} catch (InterruptedException e) {
//			}
//			clerk.get();
//		}
//	} 
//}
//
//// 消费者
//class Consumer implements Runnable {
//	
//	private Clerk clerk;
//
//	public Consumer(Clerk clerk) {
//		this.clerk = clerk;
//	}
//
//	@Override
//	public void run() {
//		for (int i = 0; i < 20; i++) {
//			clerk.sale();
//		}
//	}
//}