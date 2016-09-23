package concurrent.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 运行结果：
 * Thread-0得到了锁
 * Thread-1.isInterrupted()=true  //可能为true或false，要看打印的快，还是catch InterruptedException快
 * Thread-1被中断,isInterrupted()=false
 * Thread-1.isInterrupted()=false
 *
 * 结论：
 * lock.lockInterruptibly()当调用这个方法被阻塞时，可以中断
 * 中断后会抛InterruptedException
 * 在中断之后，捕获InterruptedException之前，isInterrupted()返回true
 */
public class LockDemo2_lockInterruptibly2 {
    private Lock lock = new ReentrantLock();   
    public static void main(String[] args)  {
        LockDemo2_lockInterruptibly2 test = new LockDemo2_lockInterruptibly2();
        LockDemo2_lockInterruptibly2.MyThread thread1 = test.new MyThread(test);
        LockDemo2_lockInterruptibly2.MyThread thread2 = test.new MyThread(test);
        thread1.start();
        thread2.start();
         
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread2.interrupt();
        System.out.println(thread2.getName()+".isInterrupted()="+thread2.isInterrupted());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(thread2.getName()+".isInterrupted()="+thread2.isInterrupted());
    }  
     
    public void insert(Thread thread) throws InterruptedException{
        lock.lockInterruptibly();  //注意，如果需要正确中断等待锁的线程，必须将获取锁放在外面，然后将InterruptedException抛出
        try {  
            System.out.println(thread.getName()+"得到了锁");
            long startTime = System.currentTimeMillis();
            for(;;) {
                if(System.currentTimeMillis() - startTime >= Integer.MAX_VALUE)
                    break;
                //插入数据
            }
        }
        finally {
            System.out.println(Thread.currentThread().getName()+"执行finally");
            lock.unlock();
            System.out.println(thread.getName()+"释放了锁");
        }  
    }
    
    class MyThread extends Thread {
        private LockDemo2_lockInterruptibly2 test = null;
        public MyThread(LockDemo2_lockInterruptibly2 test) {
            this.test = test;
        }
        @Override
        public void run() {
             
            try {
                test.insert(Thread.currentThread());
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName()+"被中断,isInterrupted()="+Thread.currentThread().isInterrupted());
            }
        }
    }
}