package concurrent.threadlocal;


/**
 * 运行结果：
 * 1
 * main
 * 9
 * Thread-0
 * 1
 * main
 * 
 * 结论：
 * ThreadLocal为线程本地变量，为每个线程保存了一份变量的副本
 * 每个线程中有一个 ThreadLocal.ThreadLocalMap，可以理解为一个Map
 * Map中的 ThreadLocal.ThreadLocalMap.Entry 是以 ThreadLocal为key
 * 所以一个线程可以有多个ThreadLocal变量保存线程本地变量
 * 此例中
 */
public class ThreadLocalDemo1 {
	ThreadLocal<Long> longLocal = new ThreadLocal<Long>();
    ThreadLocal<String> stringLocal = new ThreadLocal<String>();
 
     
    public void set() {
        longLocal.set(Thread.currentThread().getId());
        stringLocal.set(Thread.currentThread().getName());
    }
     
    public long getLong() {
        return longLocal.get();
    }
     
    public String getString() {
        return stringLocal.get();
    }
    
    
    public static void main(String[] args) throws InterruptedException {
        final ThreadLocalDemo1 demo = new ThreadLocalDemo1();
         
        demo.set();//主线程设置线程变量
        System.out.println(demo.getLong());
        System.out.println(demo.getString());
     
         
        Thread thread1 = new Thread(){
            public void run() {
            	demo.set();//thread1设置自己的线程变量
                System.out.println(demo.getLong());
                System.out.println(demo.getString());
            };
        };
        thread1.start();
        thread1.join();//主线程等待thread1运行结束后再继续运行
         
        System.out.println(demo.getLong());
        System.out.println(demo.getString());
    }
}
