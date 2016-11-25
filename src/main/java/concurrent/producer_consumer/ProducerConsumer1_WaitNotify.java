package concurrent.producer_consumer;

import java.util.PriorityQueue;

/**
 * 用非阻塞队列 + wait-notify，实现生产者-消费者模式
 * PriorityQueue不是线程安全的
 */
public class ProducerConsumer1_WaitNotify {
	private static PriorityQueue queue = new PriorityQueue(10);
	
	/**
	 * 生产者线程
	 */
	static class ProducerThread extends Thread{
		private String name;
		
		public ProducerThread(String name){
			this.name = name;
		}
		
		@Override
		public void run() {
			while(true){
				synchronized(queue){
					while(queue.size()==10){
						try {
							System.out.println(name + " 队列满了，等待...");
							queue.wait();
						} 
						catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					
					queue.offer(1);
					System.out.println(name + " 放入一个，size="+queue.size());
					queue.notifyAll();
				}
			}
		}
	}
	
	/**
	 * 消费者线程
	 */
	static class ConsumerThread extends Thread{
		private String name;
		
		public ConsumerThread(String name){
			this.name = name;
		}
		
		@Override
		public void run() {
			while(true){
				synchronized(queue){
					//此处应使用while做循环判断
					//如线程1、2消费者都在等待
					//线程3生产一个，线程1、2被唤醒，先被线程1消费，又到线程2时，队列还是空的
					while(queue.isEmpty()){
						try {
							System.out.println(name + " 队列空了，等待...");
							queue.wait();
						} 
						catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					
					queue.poll();
					System.out.println(name + " 取出一个，size="+queue.size());
					queue.notifyAll();
				}
			}
		}
	}
	
	public static void main(String[] args) {
		ProducerThread producer = new ProducerThread("producer");
		ConsumerThread consumer1 = new ConsumerThread("consumer1");
		ConsumerThread consumer2 = new ConsumerThread("consumer2");
		
		producer.start();
		consumer1.start();
		consumer2.start();
	}
}
