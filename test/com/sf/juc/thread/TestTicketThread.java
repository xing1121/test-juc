package com.sf.juc.thread;

/**
 * 描述：使用继承Thread的方式实现3个窗口买票100张，核心：线程操作资源类、线程同步
 * @author 80002888
 * @date   2018年7月7日
 */
public class TestTicketThread {
	public static void main(String[] args) {
		// 三个线程操作一个ticket对象
		Ticket ticket = new Ticket(100);
		new TicketThread(ticket, "A窗口").start();
		new TicketThread(ticket, "B窗口").start();
		new TicketThread(ticket, "C窗口").start();
	}
}

class TicketThread extends Thread {

	private Ticket ticket;
	
	private String name;
	
	public TicketThread(Ticket ticket, String name) {
		this.ticket = ticket;
		this.name = name;
	}

	@Override
	public void run() {
		while (true) {
			synchronized (TicketThread.class) {
				if (ticket.getNum() > 0) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					ticket.setNum(ticket.getNum() - 1);
					System.out.println(name + "成功售票，余票：" + ticket.getNum());
				} else {
					return;
				}
			}
		}
	}
	
}

class Ticket {
	
	private int num;

	public Ticket(int num) {
		this.num = num;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}
	
}
