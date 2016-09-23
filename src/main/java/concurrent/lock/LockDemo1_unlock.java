package concurrent.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 运行结果：
 * 进入insertWithException()方法
 * insertWithException()方法发生异常，没有释放锁
 * 离开insertWithException()方法
 *
 * 结论：
 * lock和synchronized不同，synchronized在发生异常时会自动释放锁，而lock必须通过unlock()释放，否则一直阻塞
 */
public class LockDemo1_unlock {
	private Lock lock = new ReentrantLock();
	
	public void insert(){
		lock.lock();
		System.out.println("进入insert()方法");
		
		System.out.println("离开insert()方法");
		lock.unlock();
	}
	
	public void insertWithException(){
		try{
			lock.lock();
			System.out.println("进入insertWithException()方法");
			
			Thread.sleep(500);
			
			if(true){
				throw new Exception();
			}
		}
		catch(Exception e){
			System.out.println("insertWithException()方法发生异常，没有释放锁");
		}
		finally{
			//lock.unlock();
		}
		System.out.println("离开insertWithException()方法");
	}
	
	
	public static void main(String[] args) throws Exception{
		final LockDemo1_unlock demo = new LockDemo1_unlock();
		
		new Thread(){
			public void run() {
				demo.insertWithException();
			}
		}.start();
		
		Thread.sleep(100);
		
		new Thread(){
			public void run() {
				demo.insert();
			}
		}.start();
	}
}
