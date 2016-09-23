package concurrent.synchronized_;

/**
 * 运行结果：
 * 进入insertWithException()方法
 * insertWithException()方法发生异常，释放锁
 * 离开insertWithException()方法
 * 进入insert()方法
 * 离开insert()方法
 *
 * 结论：
 * 对于synchronized的方法或者代码框，当有异常发生时，JVM会自动释放当前线程占用的锁
 */
public class SynchronizedDemo1 {
	
	/**
	 * 同步方法
	 */
	public synchronized void insert(){
		System.out.println("进入insert()方法");
		System.out.println("离开insert()方法");
	}
	
	/**
	 * 同步方法抛异常
	 */
	public synchronized void insertWithException(){
		System.out.println("进入insertWithException()方法");
		try{
			Thread.sleep(500);
			
			if(true){
				throw new Exception();
			}
		}
		catch(Exception e){
			System.out.println("insertWithException()方法发生异常，释放锁");
		}
		System.out.println("离开insertWithException()方法");
	}
	
	
	public static void main(String[] args) throws Exception{
		final SynchronizedDemo1 demo = new SynchronizedDemo1();
		
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
