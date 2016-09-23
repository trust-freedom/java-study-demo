package concurrent.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 运行结果：
 * 线程A启动，暂停5s
 * 线程A获取锁，暂停10s
 * 线程B启动，尝试获取锁1s
 * 线程B执行手动中断
 * 线程B被中断,isInterrupted()=false
 * 线程B.isInterrupted()=false
 * Exception in thread "线程B" java.lang.IllegalMonitorStateException
 * 	at java.util.concurrent.locks.ReentrantLock$Sync.tryRelease(ReentrantLock.java:155)
 * 	at java.util.concurrent.locks.AbstractQueuedSynchronizer.release(AbstractQueuedSynchronizer.java:1260)
 * 	at java.util.concurrent.locks.ReentrantLock.unlock(ReentrantLock.java:460)
 * 	at concurrent.lock.LockDemo2.insert(LockDemo2.java:28)
 * 	at concurrent.lock.LockDemo2$2.run(LockDemo2.java:51)
 * main线程,threadB.isInterrupted()=false
 * 线程A.isInterrupted()=false
 * 线程A执行完毕
 * 
 * 结论：
 * lock.lockInterruptibly()当通过这个方法获取锁，且阻塞时，这个线程可以被中断（调用 thread.interrupt()）
 * 上面抛 java.lang.IllegalMonitorStateException 非法的监视状态异常，是因为线程B在中断情况下调用了 unlock()
 * 而当调用 threadB.interrupt()后，threadB.isInterrupted应该是 true，在别的例子中体现
 * 但抛出InterruptedException后，又会被置为false
 * 此例中的几次输出isInterrupted(),只有main方法中的可能会使true，根据先打印，还是先catch异常
 * 其余均为false，因为没发捕获到抛出InterruptedException异常之前的时机 
 */
public class LockDemo2_lockInterruptibly {
	private Lock lock = new ReentrantLock();
	
	
	public void insert(){
		try{
			lock.lockInterruptibly();
			
			System.out.println(Thread.currentThread().getName()+"获取锁，暂停10s");
			Thread.sleep(10 * 1000);
			
			
		}
		catch(InterruptedException e){
			System.out.println(Thread.currentThread().getName()+"被中断"+",isInterrupted()="+Thread.currentThread().isInterrupted());
			return;
		}
		catch(Exception e){
			
		}
		finally{
			System.out.println(Thread.currentThread().getName()+".isInterrupted()="+Thread.currentThread().isInterrupted());
			lock.unlock();
		}
		
		System.out.println(Thread.currentThread().getName()+"执行完毕");
	}
	
	
	public static void main(String[] args){
		try{
			final LockDemo2_lockInterruptibly demo = new LockDemo2_lockInterruptibly();
			
			Thread threadA = new Thread(){
				public void run() {
					demo.insert();
				}
			};
			threadA.setName("线程A");
			threadA.start();
			System.out.println(threadA.getName()+"启动，暂停5s");
			Thread.sleep(5 * 1000);
			
			Thread threadB = new Thread(){
				public void run() {
					demo.insert();
				}
			};
			threadB.setName("线程B");
			threadB.start();
			System.out.println(threadB.getName()+"启动，尝试获取锁1s");
			Thread.sleep(1 * 1000);
			
			System.out.println(threadB.getName()+"执行手动中断");
			threadB.interrupt();
			System.out.println("main线程,threadB.isInterrupted()="+threadB.isInterrupted());
		}
		catch(Exception e){
			
		}
	}
}
