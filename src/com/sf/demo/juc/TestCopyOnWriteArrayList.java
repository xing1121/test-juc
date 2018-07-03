package com.sf.demo.juc;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/*
 * CopyOnWriteArrayList/CopyOnWriteArraySet : “写入时复制”
 * 注意：添加操作多时，效率低，因为每次添加时都会进行复制一个集合，开销非常的大。并发迭代操作多时可以选择。
 */
public class TestCopyOnWriteArrayList {

	public static void main(String[] args) {
		HelloThread ht = new HelloThread();
		
		for (int i = 0; i < 10; i++) {
			new Thread(ht).start();
		}
		
	}
	
}

class HelloThread implements Runnable{
	
//	private static List<String> list = Collections.synchronizedList(new ArrayList<String>());
	private static CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();	// 迭代时集合添加元素，迭代的是原来的集合，新添加元素到的是复制的集合中，最后把复制的集合赋给原来的引用。
	
	static{
		list.add("AA");
		list.add("BB");
		list.add("CC");
	}

	@Override
	public void run() {
		
		Iterator<String> it = list.iterator();
		System.out.println(list.hashCode());
		while(it.hasNext()){
			System.out.println(it.next());
			
			list.add("AA");
		}
		System.out.println(list.hashCode());	// 一个新的集合
	}
	
}