package com.sf.demo.juc2;

/**
 * 描述：生产者消费者
 * 		虚假唤醒（判断条件改变但是代码没有侦测到，本例就是if中的条件），也可以是本不该被唤醒的线程却被唤醒了（若消费快则造成商品负数），或造成线程卡主永远结束不了，去掉else可以解决1生产者对1消费者的，把if判断条件改为while可以彻底解决
 * 		解决虚假唤醒（spurious wakeups）：把等待wait()放到循环中，只要满足循环条件就一直等待，不会被虚假唤醒。
 * 
 * 		if:第一次进入时满足条件，wait()，被唤醒时，仍然满足条件，但直接执行到if外面。
 * 		while:第一次进入时满足条件，wait()，被唤醒时，仍然满足条件，再次循环判断，再次wait()直到不满足条件。
 * 
 * @author 80002888
 * @date   2018年6月8日
 */
public class TestProductorAndConsumer2 {
	public static void main(String[] args) {
		// 共享一个店员
		Clerk clerk = new Clerk();
		Productor productor = new Productor(clerk);
		Consumer consumer = new Consumer(clerk);
		
		new Thread(productor, "生产者A").start();
		new Thread(consumer, "消费者B").start();
		
		new Thread(productor, "生产者C").start();
		new Thread(consumer, "消费者D").start();
	}
}

// 店员
class Clerk {
	
	private int product = 0;
	
	// 进货
	public synchronized void get(){
		
		while(product >= 1) {	// 为了避免虚假唤醒问题，应该总是使用在循环中
//		if (product >= 1) {
			System.out.println("货仓已满！");
			// 货仓满时不再生产，而是等待
			try {
				this.wait();
			} catch (InterruptedException e) {
			}
			
			
		}	
		System.out.println(Thread.currentThread().getName() + " : " + ++product);
		// 生产成功，唤醒消费者可以消费了
		this.notifyAll();
		
		
//		} else {
//			System.out.println(Thread.currentThread().getName() + " : " + ++product);
//			// 生产成功，唤醒消费者可以消费了
//			this.notifyAll();
//		}
	}
	
	// 卖货
	public synchronized void sale(){
		
		while (product <= 0) {
//		if (product <= 0) {
			System.out.println("货仓缺货！");
			// 缺货时不再消费，而是等待
			try {
				this.wait();
			} catch (InterruptedException e) {
			}
			
			
		}
		System.out.println(Thread.currentThread().getName() + " : " + --product);
		// 消费成功，唤醒生产者可以生产了
		this.notifyAll();
		
		
//		} else {
//			System.out.println(Thread.currentThread().getName() + " : " + --product);
//			// 消费成功，唤醒生产者可以生产了
//			this.notifyAll();
//		}
		
	}
}

// 生产者
class Productor implements Runnable{
	
	private Clerk clerk;

	public Productor(Clerk clerk) {
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
class Consumer implements Runnable {
	
	private Clerk clerk;

	public Consumer(Clerk clerk) {
		this.clerk = clerk;
	}

	@Override
	public void run() {
		for (int i = 0; i < 20; i++) {
			clerk.sale();
		}
	}
}