package com.sf.demo.juc2;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 * 描述：
 
使用Lock解决生产者消费者（包括Lock的Condition通信）

1.
问题：多个消费者同时进入消费方法，造成商品数量负值。多个生产者同时进入生产方法，造成商品数量超额。
解决：使用加锁同步来解决，确保一次只一个消费者在消费或一个生产者在生产。

2.
问题：生产者发现货仓已满，仍然在不断的生产产品。消费者发现货仓缺货，仍然在不断地消费产品。
解决：等待唤醒机制（wait-notify机制），当产品已满时，不再生产。当产品缺货时，不再消费。

3.
问题：使用if来判断商品小于0则等待，会存在两个消费者同时等待，
        一个生产者生产后两个消费者顺序消费导致商品数量变为-1，即线程虚假唤醒（spurious wakeups）。
解决：使用while代替if来解决，把等待wait()放到循环中，
    while代码块中的wait时被唤醒后会重新运行while后面的条件是否符合，
        只要满足循环条件就一直等待，不会被虚假唤醒。
        
 * @author wdx
 * @date   2020年5月5日
 */
public class ProductorAndConsumerForLockTest {
    
	public static void main(String[] args) {
		// 共享一个店员
		Clerk clerk = new Clerk();
		ProductorRunnable productor = new ProductorRunnable(clerk);
		ConsumerRunnable consumer = new ConsumerRunnable(clerk);
		
		new Thread(productor, "生产者A").start();
		new Thread(productor, "生产者B").start();
		
		new Thread(consumer, "消费者C").start();
		new Thread(consumer, "消费者D").start();
	}
}

// 店员
class Clerk {
	
	private int product = 0;
	private Lock lock = new ReentrantLock();
	private Condition condition = lock.newCondition();
	
	// 进货
	public void get(){
	    String tName = Thread.currentThread().getName();
	    
		lock.lock();
		try {
		    // 使用while代替if，当被唤醒时会重新跑while (product >= 1)进行判断
			while (product >= 1) {		
				System.out.println(tName + "货仓已满，进入await()");
				// 货仓满时不再生产，而是等待并释放锁
				try {
					condition.await();
				} catch (InterruptedException e) {
				}
			}
			
			System.out.println(tName + "生产成功，即将signalAll()，目前商品数量：" + ++product);
			// 生产成功，唤醒消费者可以消费了
			condition.signalAll();
		} finally {
			lock.unlock();
		}
		
	}
	
	// 卖货
	public void sale(){
	    String tName = Thread.currentThread().getName();
	     
		lock.lock();
		try {
			while (product <= 0) {		//使用while代替if
				System.out.println(tName + "货仓缺货，进入await()");
				// 缺货时不再消费，而是等待并释放锁
				try {
					condition.await();
				} catch (InterruptedException e) {
				}
			}
			System.out.println(tName + "消费成功，即将signalAll()，目前商品数量：" + --product);
			// 消费成功，唤醒生产者可以生产了
			condition.signalAll();
		} finally {
			lock.unlock();
		}
	}
}

// 生产者
class ProductorRunnable implements Runnable{
	
	private Clerk clerk;

	public ProductorRunnable(Clerk clerk) {
		this.clerk = clerk;
	}

	@Override
	public void run() {
		for (int i = 0; i < 20; i++) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
			}
			clerk.get();
		}
	} 
}

// 消费者
class ConsumerRunnable implements Runnable {
	
	private Clerk clerk;

	public ConsumerRunnable(Clerk clerk) {
		this.clerk = clerk;
	}

	@Override
	public void run() {
		for (int i = 0; i < 20; i++) {
			clerk.sale();
		}
	}
}