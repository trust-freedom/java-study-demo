package concurrent.atomic;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 运行结果：
 * Thread-2 done
 * Thread-0 done
 * Thread-3 done
 * Thread-8 done
 * Thread-4 done
 * Thread-7 done
 * Thread-6 done
 * Thread-5 done
 * Thread-1 done
 * Thread-9 done
 * 10000
 *
 * 结论：
 * 使用 java.util.concurrent.atomic.AtomicInteger 保证了每次自增时的原子性 
 * 其实是使用了CAS原理(Compare And Swap)，使用了非阻塞算法
 * 在进行getAndIncrement()时
 * public final int getAndIncrement() {
 *     for (;;) {
 *         int current = get();
 *         int next = current + 1;
 *             
 *         //调用unsafe.compareAndSwapInt()，原子性的比较当前值和之前获取的current相等，再设置新值，否则返回false，重新执行
 *         if (compareAndSet(current, next))
 *             return current;
 *         }
 *     }
 * }
 * 会调用原子性的compareAndSet()操作，保证此方法肯定会使得value值加一，返回旧值
 * 但有可能存在，当前值为1，线程A调用getAndIncrement()，返回的不是1，是2
 * 因为在期间另一线程B先执行成功了getAndIncrement()，线程A第一次compareAndSet()返回false
 * 第二次成功，此时value值为3，返回的旧值为2
 * 可能这也是为什么AtomicInteger的一些操作方法还要返回旧值或新值的原因
 */
public class AtomicDemo1_AtomicInteger {
	public AtomicInteger inc = new AtomicInteger(0);
	public CountDownLatch latch = new CountDownLatch(10);
    
    public void increase() {
        inc.getAndIncrement();
    }
     
    public static void main(String[] args) throws Exception{
        final AtomicDemo1_AtomicInteger demo = new AtomicDemo1_AtomicInteger();
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
