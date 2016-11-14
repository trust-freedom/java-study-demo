package basics.collection.list.vector;

import java.util.ArrayList;
import java.util.Vector;

/**
 * 运行结果：
 * ---JDK1.7
 * 10W：
 * ArrayList进行100000次插入操作耗时：13ms
 * Vector进行100000次插入操作耗时：10ms
 * 
 * 100W：
 * ArrayList进行1000000次插入操作耗时：26ms
 * Vector进行1000000次插入操作耗时：57ms
 * 
 * 1000W：
 * ArrayList进行10000000次插入操作耗时：2174ms
 * Vector进行10000000次插入操作耗时：2395ms
 * 
 * 在10W时，ArrayList稍慢
 * 在100W、1000W时，Vector稍慢
 * 
 * ---JDK1.6
 * 10W:
 * ArrayList进行100000次插入操作耗时：14ms
 * Vector进行100000次插入操作耗时：11ms
 * 
 * 100W:
 * ArrayList进行1000000次插入操作耗时：30ms
 * Vector进行1000000次插入操作耗时：61ms
 * 
 * 1000W:
 * ArrayList进行10000000次插入操作耗时：1077ms
 * Vector进行10000000次插入操作耗时：756ms
 * 
 * 和JDK1.7结果基本一致，只有在1000W时，两种集合的耗时都明显减小，且ArrayList比较慢
 * 
 * 
 * 结论：
 * 其实对ArrayList 和 Vector进行插入速度比较很难
 * 因为我们已知的差异主要是 Vector在 add()时会 synchronized同步
 * 而在单线程中，进入synchronized会获得可重入锁，即一个线程反复获得一个对象的锁，就像是在计数器上不断+1，并没有什么消耗
 * 反而是Vector每次扩容是乘2，ArrayList每次扩容是增加“原始容量 >> 1”，即一半
 * 所以某些情况下，单线程的add()，Vector更快
 * 而如果测试多线程，ArrayList又不是线程安全的，会IndexOutOfBounds
 */
public class VectorDemo1_AddSpeed {
	public static void main(String[] args) {
		ArrayList<Integer> list = new ArrayList<Integer>();
        Vector<Integer> vector = new Vector<Integer>();
        
        //往ArrayList中添加
        long start = System.currentTimeMillis();
        for(int i=0;i<10000000;i++)
            list.add(i);
        long end = System.currentTimeMillis();
        System.out.println("ArrayList进行10000000次插入操作耗时："+(end-start)+"ms");
        
        //往Vector中添加
        start = System.currentTimeMillis();
        for(int i=0;i<10000000;i++)
            vector.add(i);
        end = System.currentTimeMillis();
        System.out.println("Vector进行10000000次插入操作耗时："+(end-start)+"ms");
	}
}
