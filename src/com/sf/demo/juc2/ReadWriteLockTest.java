package com.sf.demo.juc2;

import java.util.Random;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 
 * 描述：

ReadWriteLock：读写锁，与普通锁比较可以提高效率

写写/读写：互斥
读读：不需要互 斥

读的时候可以读，不能写
写的时候不能写，不能读 

 * @author wdx
 * @date   2020年5月5日
 */
public class ReadWriteLockTest {
	
	public static void main(String[] args) {
		ReadWriterLockDemo rwd = new ReadWriterLockDemo();
		
		new Thread(()->{
			rwd.set(new Random().nextInt(101));
		}, "Write").start();
		
		for (int i = 0; i < 100; i++) {
			new Thread(()->{
				rwd.get();
			}, "Read" + i).start();
		}
		
	}
	
}

class ReadWriterLockDemo {
	
	private int number;
	
	private ReadWriteLock lock = new ReentrantReadWriteLock();
	
	public void get(){
		lock.readLock().lock();
		try {
			System.out.println(Thread.currentThread().getName() + " : " + number);
		} finally{
			lock.readLock().unlock();
		}
	}
	
	public void set(int number) {
		lock.writeLock().lock();
		try {
			this.number = number;
			System.out.println(Thread.currentThread().getName() + "写入" + number);
		} finally{
			lock.writeLock().unlock();
		}
	}
	
}