package com.sf.juc.test;

/**
 * 描述：volatile关键字用来修饰变量，保证共享数据的内存可见性。
 * 		①问题：main线程获取flag为缓存false，A线程延时200ms启动将flag设置为true时，main线程由于一直在循环判断导致没有资源从主存同步缓存，取到的flag仍为false，永远执行不了打印“哈哈哈哈”
 * 		②解决：共享变量flag加上关键字volatile，代表其他线程使用这个变量时是时时同步的。
 * @author 80002888
 * @date   2018年7月3日
 */
public class TestVolatile {
	public static void main(String[] args) {
		ThreadDemo threadDemo = new ThreadDemo();
		new Thread(threadDemo, "A").start();
		while (true) {
			if (threadDemo.isFlag()) {
				System.out.println("-----------------------------");
				break;
			}
		}
	}
}

class ThreadDemo implements Runnable{

//	private volatile boolean flag = false;
	private boolean flag = false;
	
	@Override
	public void run() {
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.setFlag(true);
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	
}