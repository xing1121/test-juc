package com.sf.demo.juc;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * 描述：

一、i++ 的原子性问题：i++ 的操作实际上分为三个步骤“读-改-写”
        int i = 10;
        i = i++; //10
 
        int temp = i;
        i = i + 1;
        i = temp;
 
 
        int i = 10;
        i = ++i; //11
 
        i = i + 1;
        int temp = i;
        i = temp;
 
二、原子变量：在 java.util.concurrent.atomic 包下提供了一些原子变量。
      1. 使用 volatile 保证内存可见性
      2. 使用 CAS（Compare-And-Swap） 算法保证数据变量的原子性

 * @author wdx
 * @date   2020年5月5日
 */
public class AtomicDemoTest {

	public static void main(String[] args) {
		AtomicRunnable ad = new AtomicRunnable();
		
		for (int i = 0; i < 10; i++) {
			new Thread(ad).start();
		}
	}
	
}

class AtomicRunnable implements Runnable {
	
//	private volatile int serialNumber = 0;
	private AtomicInteger serialNumber = new AtomicInteger(0);

	@Override
	public void run() {
		
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
		}
		
		System.out.println(getSerialNumber());
	}
	
	public int getSerialNumber(){
//		return serialNumber++;
		return serialNumber.getAndIncrement();
	}
	
	
}
