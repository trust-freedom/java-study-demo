package concurrent.volatile_;

import java.util.concurrent.CountDownLatch;

/**
 * 运行结果：
 * Thread-0 done
 * Thread-1 done
 * Thread-3 done
 * Thread-2 done
 * Thread-7 done
 * Thread-5 done
 * Thread-6 done
 * Thread-9 done
 * Thread-4 done
 * Thread-8 done
 * 9858
 *
 * 结论：
 * 可以看到，虽然volatile虽然保证了可见性，但并不保证原子性，且自增操作不具有原子性
 * inc++操作是分为从主存中读取到工作内存，再+1，再写回主存
 * 虽然有volatile修饰，可以在修改inc的值后，使得其它线程在读取时，其工作内存中的缓存失效，从主存中读，读到最新的值
 * 但如果线程还没有修改，就不会从主存中读取，还是使用自己工作内存中的
 * 比如线程1读取到inc=100，没有+1，之后线程2也读取到inc=100，之后线程1再+1=101，但线程2已经读完inc=100，所以+1还是得101
 */
public class VolatileDemo1_non_atomic {
	public volatile int inc = 0;
	public CountDownLatch latch = new CountDownLatch(10);
    
    public void increase() {
        inc++;
    }
     
    public static void main(String[] args) throws Exception{
        final VolatileDemo1_non_atomic demo = new VolatileDemo1_non_atomic();
        for(int i=0;i<10;i++){
            new Thread(){
                public void run() {
                    for(int j=0;j<1000;j++){
                    	demo.increase();
                    }
                    demo.latch.countDown();
                    System.out.println(Thread.currentThread().getName()+" done");
                };
            }.start();
        }
         
        
        demo.latch.await();//保证所有的子线程都执行完
        System.out.println(demo.inc);
    }
}
