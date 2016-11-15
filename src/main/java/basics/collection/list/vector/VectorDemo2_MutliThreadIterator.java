package basics.collection.list.vector;

import java.util.Iterator;
import java.util.Vector;

/**
 * 运行结果：
 * 1
 * Exception in thread "Thread-0" java.util.ConcurrentModificationException
 * 	at java.util.Vector$Itr.checkForComodification(Vector.java:1156)
 * 	at java.util.Vector$Itr.next(Vector.java:1133)
 * 	at basics.collection.list.vector.VectorDemo2_MutliThreadIterator$1.run(VectorDemo2_MutliThreadIterator.java:20)
 * 
 * 结论：
 * 虽然Vector是线程安全的，add(),remove()时都使用了synchronized
 * iterator.remove也调用了vector.remove()
 * 源码：
 * public void remove() {
 *     if (lastRet < 0)
 *         throw new IllegalStateException();
 *     checkForComodification();
 * 
 *     try {
 *         AbstractList.this.remove(lastRet);
 *         if (lastRet < cursor)
 *             cursor--;
 *         lastRet = -1;
 *         expectedModCount = modCount;
 *      } catch (IndexOutOfBoundsException e) {
 *          throw new ConcurrentModificationException();
 *      }
 * }
 * 但由于多数情况下，iterator是线程私有的局部变量
 * Vector继承自AbstractList，其内部类Itr()在创建时会记录AbstractList的modCount
 * 当迭代集合同时对集合有修改，就有可能抛java.util.ConcurrentModificationException
 * 所以，即使是Vector，在多线程环境下操作也要小心
 * 尤其是迭代，无论是用下标的方式，还是iterator方式，集合都是线程共享的，其它都是线程私有的
 * 
 * 如果使用CopyOnWriteArrayList不会出现这个问题，因为其迭代器是自己实现的COWIterator
 * 在创建时会记录集合的快照Object[] snapshot
 * 之后及时集合有变动，迭代的也还是快照
 */
public class VectorDemo2_MutliThreadIterator {
	static Vector<Integer> vector = new Vector<Integer>();
//	static CopyOnWriteArrayList<Integer> cowList = new CopyOnWriteArrayList<Integer>();
    
	public static void main(String[] args)  {
        vector.add(1);
        vector.add(2);
        vector.add(3);
        vector.add(4);
        vector.add(5);
        
        Thread thread1 = new Thread(){
            public void run() {
                Iterator<Integer> iterator = vector.iterator();
                while(iterator.hasNext()){
                    Integer integer = iterator.next();
                    System.out.println(integer);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
        };
        
        Thread thread2 = new Thread(){
            public void run() {
                Iterator<Integer> iterator = vector.iterator();
                while(iterator.hasNext()){
                    Integer integer = iterator.next();
                    if(integer==2)
                        iterator.remove();
//                    	cowList.remove(integer);
                }
            };
        };
        
        thread1.start();
        thread2.start();
    }
}
