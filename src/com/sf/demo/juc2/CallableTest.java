package com.sf.demo.juc2;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * 
 * 描述：

使用FutureTask包装Callable的方式创建线程。

 * @author wdx
 * @date   2020年5月5日
 */
public class CallableTest {

	public static void main(String[] args) throws Exception {
		DemoCallable td = new DemoCallable();
		
		// 执行Callable的方式：需要FutureTask实现类的支持，用于接收计算结果
		FutureTask<Integer> result = new FutureTask<>(td);
		new Thread(result).start();

		// 接收线程运算后的结果
		// FutureTask.get()会等到线程运行结束才返回结果，可结合闭锁使用
		Integer sum = result.get();
		System.out.println(sum);
		System.out.println("---------------------------------");
	}

}

class DemoCallable implements Callable<Integer> {

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