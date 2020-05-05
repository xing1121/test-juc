package com.sf.demo.juc;

import java.util.concurrent.CountDownLatch;

/**
 * 
 * 描述：

CountDownLatch ：
闭锁，在完成某些运算时，只有其他所有线程的运算全部完成，当前运算才继续执行
就相当于一个计数器，达到想要的值时触发某些操作，可以使用AtomicInteger等来代替。
如：计算十个线程的执行时间
  
CountDownLatch：
main线程，N个其他线程
main线程await等待N个线程执行完毕，main线程才继续放行。
  
CyclicBarrier：
main线程，N个其他线程
其他线程执行到某个条件进行等待，等到N个都进入等待时，N个线程一起放行。

 * @author wdx
 * @date   2020年5月5日
 */
public class CountDownLatchTest {

	public static void main(String[] args) {
		final CountDownLatch latch = new CountDownLatch(50);
		LatchRunnable ld = new LatchRunnable(latch);

		long start = System.currentTimeMillis();

		for (int i = 0; i < 50; i++) {
			new Thread(ld).start();
		}

		try {
			// 这里等到50个线程执行完毕才放行
			latch.await();
		} catch (InterruptedException e) {
		}

		long end = System.currentTimeMillis();

		System.out.println("耗费时间为：" + (end - start));
	}

}

class LatchRunnable implements Runnable {

	private CountDownLatch latch;

	public LatchRunnable(CountDownLatch latch) {
		this.latch = latch;
	}

	@Override
	public void run() {

		try {
			for (int i = 0; i < 50000; i++) {
				if (i % 2 == 0) {
					System.out.println(i);
				}
			}
		} finally {
			System.out.println(Thread.currentThread().getName() + " over!");
			latch.countDown();
		}

	}

}