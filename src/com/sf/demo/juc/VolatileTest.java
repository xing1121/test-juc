package com.sf.demo.juc;

/**
 * 
 * 描述：

volatile 关键字：用来修饰变量，相较于 synchronized 是一种较为轻量级的同步策略
    1.当多个线程进行操作共享数据时，可以保证内存中的数据是可见的（读线程缓存时不断从主存同步）
    2.禁止指令重排序

注意：
    1.volatile 不具备互斥性（synchronized具备），多个线程可以同时操作变量
    2.volatile 不能保证变量的原子性
    3.Java1.5以前，只能保证对象的内存可见性；
      Java1.5之后，具有内存可见性，有序性（被volatile修饰的对象，将禁止该对象上的读写指令重排序）
 
 内存可见性问题是，当多个线程操作共享数据时，线程是保存主内存的数据到线程各自的缓存，彼此不可见

 * @author wdx
 * @date   2020年5月5日
 */
public class VolatileTest {
    
    public static void main(String[] args) {
        VolatileRunnable td = new VolatileRunnable();
        new Thread(td).start();
        
        // flag不加volatile不会打印
        // 因为线程有缓存，虽然主存的数据已经改变，但while循环很快而且一直读取的是线程缓存
        
        // flag加了volatile可以使main线程中的flag内存可见，一直看到的最新的，可以打印
        // 如果while方法内执行的代码比较慢，main线程有时间刷新缓存，也可以打印
        while(true){
            if (td.isFlag()) {
                System.out.println("----------------------------");
                break;
            }
        }
        
    }
    
}

class VolatileRunnable implements Runnable {

    /**
     * flag
     */
    private volatile boolean flag = false;
//    private boolean flag = false;
    
    @Override
    public void run() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
        }
        
        this.setFlag(true);
        
        System.out.println("flag = " + isFlag());
        
    }
    
    // get方法
    public boolean isFlag() {
        return flag;
    }

    // set方法
    private void setFlag(boolean flag){
        this.flag = flag;
    }
    
}