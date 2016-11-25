package concurrent.producer_consumer;

import java.util.PriorityQueue;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用Lock + Condition实现生产者-消费者模式
 * Condition.await()类似于Object.wait()，Condition.signalAll()类似于Object.notityAll()
 * condition需要用lock.newCondition()创建
 * Condition.await()应该也是必须在lock()中调用，会释放锁，进入等待池
 * 条件变量可以实现更精确的等待和唤醒，此例中可以使用两个条件变量，当添加元素后，就指定唤醒因为empty等待的
 * 但实际上这个例子用一个Condition也是一样的，因为用的是signalAll()，线程数量也少，当队列为空时，只有少量消费者线程会等待，不会等到有生产者线程等待，就都被唤醒了
 * 
 * 如果有很多线程在读写，且一次只signal()一个，全唤醒没意义，之后也只能执行一个
 * 如果一开始消费快，30个消费者等待了，等待生产者，一个生产者生产了10个，队列慢了，唤醒了10个消费者，下一个生产者也会进入等待，这样一个condition的等待池中就既有消费者又有生产者了
 * 那么下一个唤醒的就不一定了
 * 如果有两个condittion，那么就可以精准控制唤醒谁
 */
public class ProducerConsumer2_Condition {
	private static PriorityQueue queue = new PriorityQueue(10);
	private static Lock lock = new ReentrantLock();
	private static Condition condition = lock.newCondition();
	private static Condition empty = lock.newCondition();
	private static Condition full = lock.newCondition();
	private static Random random = new Random();
	
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
				lock.lock();
				
				try{
					while(queue.size()==10){
						try {
							System.out.println(name + " 队列满了，等待...");
//							condition.await();
							full.await();
						} 
						catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					
					queue.offer(1);
					System.out.println(name + " 放入一个，size="+queue.size());
//					condition.signalAll();
					empty.signalAll();
				}
				catch(Exception e){
					e.printStackTrace();
				}
				finally{
					lock.unlock();
				}
				
				try {
					Thread.sleep(random.nextInt(3));
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
				lock.lock();
				
				try{
					while(queue.isEmpty()){
						try {
							System.out.println(name + " 队列空了，等待...");
//							condition.await();
							empty.await();
						} 
						catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					
					queue.poll();
					System.out.println(name + " 取出一个，size="+queue.size());
//					condition.signalAll();
					full.signalAll();
				}
				catch(Exception e){
					e.printStackTrace();
				}
				finally{
					lock.unlock();
				}
				
				try {
					Thread.sleep(random.nextInt(3));
				} 
				catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void main(String[] args) {
		ProducerThread producer1 = new ProducerThread("producer1");
		ProducerThread producer2 = new ProducerThread("producer2");
		ConsumerThread consumer1 = new ConsumerThread("consumer1");
		ConsumerThread consumer2 = new ConsumerThread("consumer2");
		ConsumerThread consumer3 = new ConsumerThread("consumer3");
		
		producer1.start();
		producer2.start();
		consumer1.start();
		consumer2.start();
		consumer3.start();
	}
}
