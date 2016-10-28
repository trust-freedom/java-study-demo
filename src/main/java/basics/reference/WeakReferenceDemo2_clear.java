package basics.reference;

import java.lang.ref.WeakReference;

/**
 * 运行结果：
 * 执行“将强引用置为Null”
 * before s = basics.reference.WeakReferenceDemo2_clear$Student@33a17727
 * before w.get() = basics.reference.WeakReferenceDemo2_clear$Student@33a17727
 * s = null，after s = null
 * s = null，after w.get() = basics.reference.WeakReferenceDemo2_clear$Student@33a17727
 * 
 * 执行“调用弱引用clear()”
 * before s = basics.reference.WeakReferenceDemo2_clear$Student@3781efb9
 * before w.get() = basics.reference.WeakReferenceDemo2_clear$Student@3781efb9
 * w.clear()，after s = basics.reference.WeakReferenceDemo2_clear$Student@3781efb9
 * w.clear()，after w.get() = null
 * 
 * 结论：
 * 首先运行期间没发生GC
 * 可以看到将强引用s置为null，弱引用 w.get()还存在
 * 
 * 调用弱引用的 w.clear()，只是将 Reference.referent置为null，不会将弱引用放入ReferenceQueue
 * 此clear()方法只供Java代码调用，GC时不会调用这个方法
 * w.clear()只是将弱引用置为null，而强引用还存在
 */
public class WeakReferenceDemo2_clear {
	
	static class Student{
		String name;
	}
	
	
	public static void main(String[] args) {
		Student s = new Student();
		s.name = "zhangsan";
		WeakReference w = new WeakReference(s);
		
		System.out.println("before s = "+s);
		System.out.println("before w.get() = "+w.get());
		
		
		//将强引用置为Null
//		s = null;
//		System.out.println("s = null，after s = "+s);
//		System.out.println("s = null，after w.get() = "+w.get());
		
		//调用弱引用clear()
		w.clear();
		System.gc();
		System.out.println("w.clear()，after s = "+s);
		System.out.println("w.clear()，after w.get() = "+w.get());
	}
}
