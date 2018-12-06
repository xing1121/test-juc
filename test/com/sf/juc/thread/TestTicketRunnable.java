package com.sf.juc.thread;

/**
 * 描述：使用实现Runnable接口的方式创建3个线程同时售票100张
 * @author 80002888
 * @date   2018年7月7日
 */
public class TestTicketRunnable {
	public static void main(String[] args) {
		// 三个线程公用一个runnable对象，共享runnable的num属性
		TicketRunnable runnable = new TicketRunnable(100);
		new Thread(runnable, "A窗口").start();
		new Thread(runnable, "B窗口").start();
		new Thread(runnable, "C窗口").start();
	}
}

class TicketRunnable implements Runnable{

	private int num;
	
	public TicketRunnable(int num) {
		this.num = num;
	}

	public TicketRunnable() {
	}

	@Override
	public void run() {
		while (true) {
			synchronized (TicketRunnable.class) {
				if (num > 0) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println(Thread.currentThread().getName() + "成功售票，余票：" + --num);
				} else {
					return;
				}
			}
		}
	}
	
}
