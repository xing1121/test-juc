package com.sf.demo.juc2;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class TestCallable {

	public static void main(String[] args) throws Exception {
		ThreadDemo td = new ThreadDemo();
		
		// 执行Callable的方式：需要FutureTask实现类的支持，用于接收计算结果
		FutureTask<Integer> result = new FutureTask<>(td);
		new Thread(result).start();

		// 接收线程运算后的结果
		Integer sum = result.get();	// FutureTask会等到线程运行结束才返回结果，可用于闭锁
		System.out.println(sum);
		System.out.println("---------------------------------");
	}

}

class ThreadDemo implements Callable<Integer> {

	@Override
	public Integer call() throws Exception {

		int sum = 0;

		for (int i = 0; i <= 100000; i++) {
//		for (int i = 0; i <= Integer.MAX_VALUE; i++) {		// 2的31次方 - 1
			System.out.println(i);
			sum += i;
		}

		return sum;
	}

}