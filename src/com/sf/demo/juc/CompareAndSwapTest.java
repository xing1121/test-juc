package com.sf.demo.juc;


/**
 * 
 * 描述：

模拟CAS算法（Compare-And-Swap）
    CAS（Compare-And-Swap） 算法保证数据变量的原子性
    CAS 算法是硬件对于并发操作的支持
    CAS 包含了三个操作数：
    ①内存值  V
    ②预估值  A
    ③更新值  B
当且仅当 V == A 时， V = B; 否则，不会执行任何操作。
 
CAS算法是计算机底层硬件对于并发操作的支持：
    将要修改内存（主存）时重新读取内存值，若值和自己原来读取的不一样（内存已经被其他线程改变），则将不再修改内存。（相当于乐观锁）

 * @author wdx
 * @date   2020年5月5日
 */
public class CompareAndSwapTest {

	public static void main(String[] args) {
		final CompareAndSwap cas = new CompareAndSwap();
		
		for (int i = 0; i < 10; i++) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					int expectedValue = cas.get();
					boolean b = cas.compareAndSet(expectedValue, (int)(Math.random() * 101));
					System.out.println(b);
				}
			}).start();
		}
		
	}
	
}

class CompareAndSwap {
	private int value;
	
	// 获取内存值
	public synchronized int get(){
		return value;
	}
	
	// 比较
	public synchronized int compareAndSwap(int expectedValue, int newValue){
		int oldValue = value;
		
		if(oldValue == expectedValue){
			this.value = newValue;
		}
		
		return oldValue;
	}
	
	// 设置
	public synchronized boolean compareAndSet(int expectedValue, int newValue){
		return expectedValue == compareAndSwap(expectedValue, newValue);
	}
}
