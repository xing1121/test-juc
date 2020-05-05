package com.sf.demo.juc;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 
 * 描述：

CopyOnWriteArrayList/CopyOnWriteArraySet : “写入时复制”，比较安全
注意：添加操作多时，效率低，因为每次添加时都会进行复制一个集合，开销非常的大。并发迭代操作多时可以选择。

Iterator迭代时不能修改集合，否则报并发修改异常（调用next()时会检查是否被修改过），但是可调用it.remove()。
CopyOnWriteArrayList的Iterator保存的是快照所以可以修改。

 * @author wdx
 * @date   2020年5月5日
 */
public class CopyOnWriteArrayListTest {

	public static void main(String[] args) {
		ListRunnable ht = new ListRunnable();
		
		for (int i = 0; i < 5; i++) {
			new Thread(ht).start();
		}
		
	}
	
}

class ListRunnable implements Runnable{
	
//	private static List<String> list = new ArrayList<String>();
//	private static List<String> list = Collections.synchronizedList(new ArrayList<String>());
	private static CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();	// 迭代时集合添加元素，迭代的是原来的集合，新添加元素到的是复制的集合中，最后把复制的集合赋给原来的引用。
	
	static{
		list.add("AA");
		list.add("BB");
		list.add("CC");
	}

	@Override
	public void run() {
		String tName = Thread.currentThread().getName();
		
		int hashCodeBefore = list.hashCode();
        System.out.println(tName + "--hashCodeBefore--" + hashCodeBefore);
		Iterator<String> it = list.iterator();
		while(it.hasNext()){
			System.out.println(tName + "--next--" + it.next());
			
			// 除了CopyOnWriteArrayList其他实现类都不可以这样操作
			list.add("AA");
		}
		int hashCodeAfter = list.hashCode();
		System.out.println(tName + "--hashCodeAfter--" + hashCodeAfter);	// 一个新的集合
		System.out.println("same one ? " + (hashCodeBefore == hashCodeAfter));
	}
	
}