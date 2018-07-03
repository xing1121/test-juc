package com.sf.demo.juc3;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.LongStream;

import org.junit.Test;

public class TestForkJoinPool {

	public static void main(String[] args) {
		Instant start = Instant.now();
		
		ForkJoinPool pool = new ForkJoinPool();
		ForkJoinTask<Long> task = new ForkJoinSumCalculate(0L, 10000000000L);
		Long sum = pool.invoke(task);
		System.out.println(sum);
		
		Instant end = Instant.now();
		System.out.println("耗费时间为：" + Duration.between(start, end).toMillis() + "毫秒");//耗费时间为：2904毫秒
	}
	
	//java8新特性
	@Test
	public void test2(){
		Instant start = Instant.now();
		
		Long sum = LongStream.rangeClosed(0L, 10000000000L)
							 .parallel()
							 .reduce(0L, Long::sum);
		System.out.println(sum);
		
		Instant end = Instant.now();
		System.out.println("耗费时间为：" + Duration.between(start, end).toMillis() + "毫秒");//耗费时间为：3094毫秒
	}
	
	@Test
	public void test1(){
		Instant start = Instant.now();
		
		long sum = 0L;
		for (long i = 0; i <= 10000000000L; i++) {
			sum += i;
		}
		System.out.println(sum);
		
		Instant end = Instant.now();
		System.out.println("耗费时间为：" + Duration.between(start, end).toMillis() + "毫秒");//耗费时间为：5935毫秒
	}
	
}

//RecursiveTask的compute()方法有返回值，RecursiveAction接口的compute()方法无返回值
class ForkJoinSumCalculate extends RecursiveTask<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2476607341231192867L;

	private long start;
	private long end;
	private final long THURSHOLD = 1000000L;//临界值
	
	public ForkJoinSumCalculate(long start, long end) {
		this.start = start;
		this.end = end;
	}

	@Override
	protected Long compute() {
		long length = end - start;
		if (length <= THURSHOLD) {
			long sum = 0L;
			for (long i = start; i <= end; i++) {
				sum += i;
			}
			return sum;
		} else {
			long middle = (start + end)/2;
			
			ForkJoinSumCalculate left = new ForkJoinSumCalculate(start, middle);
			left.fork();//进行拆分，同时压入线程队列
			
			ForkJoinSumCalculate right = new ForkJoinSumCalculate(middle + 1, end);
			right.fork();
		
			return left.join() + right.join();
		}
	}
	
}