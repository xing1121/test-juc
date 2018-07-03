package com.sf.juc.test;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * 描述：实现Callable接口实现计算0-10000总和
 * @author 80002888
 * @date 2018年7月3日
 */
public class TestCallable {
	public static void main(String[] args) throws Exception {
		FutureTask<Integer> futureTask = new FutureTask<>(new MyCall());
		new Thread(futureTask).start();
		System.out.println(futureTask.get());
	}
}

class MyCall implements Callable<Integer> {

	@Override
	public Integer call() throws Exception {
		int sum = 0;
		for (int i = 0; i <= 10000; i++) {
			sum += i;
		}
		return sum;
	}

}