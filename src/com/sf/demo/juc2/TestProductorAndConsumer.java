//package com.sf.demo.juc2;
//
///**
// * 描述：生产者消费者
// * 		问题：生产者发现货仓已满，仍然在不断的生产产品。消费者发现货仓缺货，仍然在不断地消费产品。
// * 		解决：等待唤醒机制：当产品已满时，不再生产。当产品缺货时，不再消费。wait-notify机制
// * 
// * @author 80002888
// * @date   2018年6月8日
// */
//public class TestProductorAndConsumer {
//	public static void main(String[] args) {
//		// 共享一个店员
//		Clerk clerk = new Clerk();
//		Productor productor = new Productor(clerk);
//		Consumer consumer = new Consumer(clerk);
//		
//		new Thread(productor, "生产者A").start();
//		new Thread(consumer, "消费者B").start();
//	}
//}
//
//// 店员
//class Clerk {
//	
//	private int product = 0;
//	
//	// 进货
//	public synchronized void get(){
//		if (product >= 10) {
//			System.out.println("货仓已满！");
//			// 货仓满时不再生产，而是等待
//			try {
//				this.wait();
//			} catch (InterruptedException e) {
//			}
//		} else {
//			System.out.println(Thread.currentThread().getName() + " : " + ++product);
//			// 生产成功，唤醒消费者可以消费了
//			this.notifyAll();
//		}
//	}
//	
//	// 卖货
//	public synchronized void sale(){
//		if (product <= 0) {
//			System.out.println("货仓缺货！");
//			// 缺货时不再消费，而是等待
//			try {
//				this.wait();
//			} catch (InterruptedException e) {
//			}
//		} else {
//			System.out.println(Thread.currentThread().getName() + " : " + --product);
//			// 消费成功，唤醒生产者可以生产了
//			this.notifyAll();
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