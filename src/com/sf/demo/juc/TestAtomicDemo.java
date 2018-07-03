package com.sf.demo.juc;

import java.util.concurrent.atomic.AtomicInteger;

/*
 * 一、i++ 的原子性问题：i++ 的操作实际上分为三个步骤“读-改-写”
 * 		  int i = 10;
 * 		  i = i++; //10
 * 
 * 		  int temp = i;
 * 		  i = i + 1;
 * 		  i = temp;
 * 
 * 
 * 
 * 		  int i = 10;
 * 		  i = ++i; //11
 * 
 * 		  i = i + 1;
 * 		  int temp = i;
 * 		  i = temp;
 * 
 * 二、原子变量：在 java.util.concurrent.atomic 包下提供了一些原子变量。
 * 		1. volatile 保证内存可见性
 * 		2. CAS（Compare-And-Swap） 算法保证数据变量的原子性
 * 			CAS 算法是硬件对于并发操作的支持
 * 			CAS 包含了三个操作数：
 * 			①内存值  V
 * 			②预估值  A
 * 			③更新值  B
 * 			当且仅当 V == A 时， V = B; 否则，不会执行任何操作。
 * 
 * CAS算法（Compare And Swap，计算机底层硬件对于并发操作的支持）：
 * 将要修改内存（主存）时重新读取内存值，若值和自己原来读取的不一样（内存已经被其他线程改变），则将不再修改内存。（相当于乐观锁）
 *
 */
public class TestAtomicDemo {

	public static void main(String[] args) {
		AtomicDemo ad = new AtomicDemo();
		
		for (int i = 0; i < 10; i++) {
			new Thread(ad).start();
		}
	}
	
}

class AtomicDemo implements Runnable{
	
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
