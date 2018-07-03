package com.sf.demo.juc;

/**
 * 一、volatile 关键字：修饰变量，当多个线程进行操作共享数据时，可以保证内存中的数据是可见的（读线程缓存时不断从主存同步）
 * 			相较于 synchronized 是一种较为轻量级的同步策略
 * 
 * 注意：
 * 1. volatile 不具备互斥性（synchronized具备），多个线程可以同时操作变量
 * 2. volatile 不能保证变量的原子性
 * 
 * 内存可见性问题是，当多个线程操作共享数据时，彼此不可见
 * 
 * @author 80002888
 * @date   2018年6月6日
 */
public class TestVolatile {
	
	public static void main(String[] args) {
		ThreadDemo td = new ThreadDemo();
		new Thread(td).start();
		
		while(true){
			if (td.isFlag()) {
				System.out.println("----------------------------");//不会打印，因为线程有缓存，虽然主存的数据已经改变，但while循环很快而且一直读取的是线程缓存
				break;
			}
		}
		
	}
	
}

class ThreadDemo implements Runnable {

	private volatile boolean flag = false;
//	private boolean flag = false;
	
	@Override
	public void run() {
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
		}
		
		this.setFlag(true);
		
		System.out.println("flag = " + isFlag());
		
	}
	
	// get方法
	public boolean isFlag() {
		return flag;
	}

	// set方法
	private void setFlag(boolean flag){
		this.flag = flag;
	}
	
}