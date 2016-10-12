package basics.reference;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

/**
 * 运行结果：
 * 创建的弱引用为：java.lang.ref.WeakReference@3a5476a7
 * Before GC: Weak Get = I'm MyObject! --- java.lang.ref.WeakReference@3a5476a7
 * After GC: Weak Get = null --- java.lang.ref.WeakReference@3a5476a7
 * MyObject finalize() called!
 * 删除的弱引用为：java.lang.ref.WeakReference@3a5476a7  but获取弱引用的对象obj.get()=null
 * 
 * 结论：
 * 在将强引用object置为null后，只剩下弱引用 weakRef
 * 调用 System.gc()，通知JVM的gc进行垃圾回收
 * 而在发生gc后，无论内存是否足够，都会回收掉只被弱引用关联的对象，即堆内存
 * 所以调用了MyObject的finalize()方法，将回收后的弱引用加入到引用队列中
 * 再调用 weakRef.get()，由于对象已经被垃圾回收，故返回null
 * weakRef.get()方法实际是返回前期构造WeakReference时传入的 object引用，当clear() 或者 垃圾回收后，返回null
 */
public class WeakReferenceDemo1_AfterGC {
	//引用队列，用于存放被回收的弱引用
	static ReferenceQueue<MyObject> weakqueue = new ReferenceQueue<>();
	
	static class MyObject{
		@Override
		public String toString() {
			return "I'm MyObject!";
		}

		@Override
		protected void finalize() throws Throwable {
			System.out.println("MyObject finalize() called!");
			super.finalize();
		}
	}
	
	
	/**
	 * 用于阻塞的检测weakqueue中是否有弱引用被放入
	 * 即是否有若引用被回收
	 */
	static class CheckRefQueueThread extends Thread{
		@Override
		public void run() {
			Reference<MyObject> obj = null;
			try {
				//如果弱引用被回收会被放进weakqueue，在此之前weakqueue.remove()都是被阻塞的
				obj = (Reference<MyObject>)weakqueue.remove();
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if(obj != null){
				System.out.println("删除的弱引用为："+obj+"  but获取弱引用的对象obj.get()="+obj.get());
			}
		}
		
	}
	
	
	public static void main(String[] args) {
		MyObject object = new MyObject();//强引用
		WeakReference<MyObject> weakRef = new WeakReference<MyObject>(object,weakqueue);//object的弱引用
		System.out.println("创建的弱引用为："+weakRef);
		CheckRefQueueThread thread = new CheckRefQueueThread();
		thread.start();
		
		object = null;//把强引用object置为null
		
		System.out.println("Before GC: Weak Get = "+weakRef.get()+" --- "+weakRef);
		System.gc();
		System.out.println("After GC: Weak Get = "+weakRef.get()+" --- "+weakRef);
	}
	
}
