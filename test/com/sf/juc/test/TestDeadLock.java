package com.sf.juc.test;

/**
 * 描述：死锁
 * @author 80002888
 * @date   2018年6月15日
 */
public class TestDeadLock {
	
	
	private static String A = "A";
	private static String B = "B";
	
	public static void main(String[] args) {
		
		Thread t1 = new Thread(()->{
			synchronized (A) {
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
				}
				synchronized (B) {
					System.out.println(1);
				}
			}
		});
		
		Thread t2 = new Thread(()->{
			synchronized (B) {
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
				}
				synchronized (A) {
					System.out.println(1);
				}
			}
		});
		
//		t1.run();				// 同步、串行
//		t2.run();
		t1.start();				// 异步、并行
		t2.start();
	}

}
