package concurrent.atomic;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 运行结果：
 * Thread-0,current=255,afterIncrement=256
 * Thread-1,current=242,afterIncrement=255
 * Thread-0,current=1256,afterIncrement=1257
 * Thread-1,current=1253,afterIncrement=1256
 * Thread-0,current=1387,afterIncrement=1388
 * Thread-1,current=1384,afterIncrement=1387
 * ...
 * Thread-0,current=4899,afterIncrement=4900
 * Thread-1,current=4897,afterIncrement=4899
 * Thread-1,current=9878,afterIncrement=9879
 * Thread-0,current=4981,afterIncrement=9878
 * run over, current value 20000
 * 
 * 结论：
 * 当然这个例子不是很准确，因为每次加一时没有使用synchronized，所以是不是原子性的
 * 比如current = ai.get() 与  afterIncrement = ai.getAndIncrement()两句之间，当前线程就可能被换下
 * 另一线程执行多次+1操作
 * 我只想表示，getAndIncrement()操作只能保证肯定会原子性的对AtomicInteger加一
 * 即两个线程分别加了1W次，最后的结果一定是2W，不会出现错误
 * 但不能保证每个线程每次操作肯定是1->2,2->3
 * 即使在compareAndSet()操作时，如果存在失败，说明已经有别的线程先+1，与预期不符
 * 所以当前线程如果是对 1 做getAndIncrement()，返回的不是旧值1，而是2
 * 可能这也是AtomicInteger的运算方法都会返回值的原因
 */
public class AtomicDemo2_AtomicInteger_CompareAndSet_False {
	private static AtomicInteger ai = new AtomicInteger(0);
	public static CountDownLatch latch = new CountDownLatch(2);
	
	/**
	 * 用于加一的线程
	 */
	static class IncrementTask implements Runnable{
		@Override
		public void run() {
			int current = 0;
			int afterIncrement = 0;
			for(int i=0; i<10000; i++){
				current = ai.get();
				afterIncrement = ai.getAndIncrement();
				
				if(current != afterIncrement){
					System.out.println(Thread.currentThread().getName()+",current="+current+",afterIncrement="+afterIncrement);
				}
			}
			
			latch.countDown();
		}
	}
	
	public static void main(String[] args) throws Exception{
		IncrementTask task = new IncrementTask();
		Thread t1 = new Thread(task);
		Thread t2 = new Thread(task);
		t1.start();
		t2.start();

		latch.await();
		
		System.out.println("run over, current value " + ai.get());
	}
}
