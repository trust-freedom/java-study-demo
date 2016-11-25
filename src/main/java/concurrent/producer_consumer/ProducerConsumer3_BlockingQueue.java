package concurrent.producer_consumer;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * 使用阻塞队列实现生产者-消费者模式
 * ArrayBlockingQueue的put()、take()都是加锁的，阻塞的
 * 阻塞队列除了是线程安全的，其本身就实现了生产者-消费者模型（线程间协作）
 * 打印结果有问题，因为put()后获取size()不是原子的，take()后获取size()也不是原子的
 */
public class ProducerConsumer3_BlockingQueue {
	private static ArrayBlockingQueue queue = new ArrayBlockingQueue(10);
	
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
				try {
					queue.put(1);
//					System.out.println("放入一个，size="+queue.size());
				} 
				catch (InterruptedException e) {
					e.printStackTrace();
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
				try {
					queue.take();
//					System.out.println("取出一个，size="+queue.size());
				} 
				catch (InterruptedException e) {
					e.printStackTrace();
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
