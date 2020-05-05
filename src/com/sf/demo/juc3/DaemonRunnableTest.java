package com.sf.demo.juc3;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Scanner;

/**
 * 
 * 描述：

测试守护线程，setDaemon(true)，辅助其他线程执行，当所有非守护线程结束时，守护线程也不复存在
使用JDK下的jstack工具可以查看JVM线程快照，命令：jstack -l PID（当前JVM进程的PID）
结果中的daemon标记的线程为守护线程 

 * @author wdx
 * @date   2020年5月5日
 */
public class DaemonRunnableTest {
    
	public static void main(String[] args) {
		System.out.println("进入主线程" + Thread.currentThread().getName());
		
		DaemonRunnable daemonThread = new DaemonRunnable();
		Thread thread = new Thread(daemonThread);
		thread.setDaemon(true);
		thread.start();
	
		Scanner scan = new Scanner(System.in);
		while (!"A".equals(scan.next())) {}
		scan.close();
		
		System.out.println("退出主线程" + Thread.currentThread().getName());
	}
}

class DaemonRunnable implements Runnable{

	@Override
	public void run() {
		System.out.println("进入守护线程：" + Thread.currentThread().getName());
		writeToFile();
		System.out.println("退出守护线程：" + Thread.currentThread().getName());
	}
	
	private void writeToFile(){
		RandomAccessFile raf = null;
		FileChannel channel = null;
		try {
			raf = new RandomAccessFile("D:\\app\\daemon.txt", "rw");
			// 1.获取通道
			channel = raf.getChannel();
			// 2.分配指定大小的缓冲区
			ByteBuffer buf = ByteBuffer.allocate(1024);
			// 3.循环写入
			int count = 0;
			while (count < 1000) {
				Thread.sleep(1000);
				String s = "word" + ++count + "\r\n";
				buf.put(s.getBytes());
				buf.flip();
				channel.write(buf);
				System.out.println("守护线程" + Thread.currentThread().getName() + "向文件写入了" + s);
				buf.clear();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				channel.close();
				raf.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
}