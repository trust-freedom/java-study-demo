package concurrent.threadlocal;

/**
 * 运行结果：
 * main:s.getName()=11
 * main:t.get().getName()=11
 * main:s.getName()=22
 * main:t.get().getName()=22
 * 
 * 结论：
 * ThreadLocal不是用来解决共享变量的访问问题，而是一种线程隔离手段，存放线程局部变量
 * 每个线程中都维护了一个Map用于存放其线程本地变量，以threadlocal为key，具体值为value
 * value值一般是在线程中创建，再通过threadlocal.set()设置，这样在当前线程中任何地方都可以调用threadlocal.get()
 * 而本例的主线程和其子线程中set()的都是同一个共享对象，在子线程中setName()，主线程中也会改变
 * 这样使用不当，不是threadlocal的本意
 */
public class ThreadLocalDemo2_SharedVar {
	private static ThreadLocal t = new ThreadLocal();
	
	static class Student{
		private String name;

		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
	}
	
	public static void main(String[] args) throws Exception{
		final Student s = new Student();
		s.setName("11");
		
		t.set(s);
		System.out.println("main:s.getName()=" + s.getName());
		System.out.println("main:t.get().getName()=" + ((Student)t.get()).getName());
		
		Thread thread = new Thread(){
			@Override
			public void run() {
				t.set(s);
				Student ss = (Student)t.get();
				ss.setName("22");
			}
		};
		thread.start();
		thread.join();
		
		System.out.println("main:s.getName()=" + s.getName());
		System.out.println("main:t.get().getName()=" + ((Student)t.get()).getName());
	}
}
