package concurrent.lock;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 执行结果：
 * Thread-0 和 Thread-1 是交替执行的
 * 
 */
public class LockDemo3_ReadWriteLock {
	private ReadWriteLock lock = new ReentrantReadWriteLock();
	
	public void read(){
		try{
			lock.readLock().lock();
			
			int i=0;
			while(i < 1000){
				System.out.println(Thread.currentThread().getName()+"正在执行");
				i++;
			}
			System.out.println(Thread.currentThread().getName()+"执行完毕。。。");
		}
		catch(Exception e){
			
		}
		finally{
			lock.readLock().unlock();
		}
	}
	
	
	public static void main(String[] args) {
		final LockDemo3_ReadWriteLock demo = new LockDemo3_ReadWriteLock();
		
		new Thread(){
			public void run(){
				demo.read();
			}
		}.start();
		
		new Thread(){
			public void run(){
				demo.read();
			}
		}.start();
	}
}
