package concurrent.threadlocal;



/**
 * 运行结果：
 * main : main
 * R-A => main : main
 * R-B => main : main
 * before - A : main in subThread
 * after - A : A
 * before - B : main in subThread
 * after - B : B
 * 
 * 结论：
 * InheritableThreadLocal可以使当前线程及其子线程都可以访问
 * 例子中可以看到主线程先设置了InheritableThreadLocal的值为main
 * 在new MyRunnable()时，是主线程调用构造，还处在主线程中
 * 子线程A、B在第一次获取InheritableThreadLocal值为main
 * 自己分别设置为线程name后，再获取就不一样了
 * 
 * 实际上Thread类内部有两个 ThreadLocal.ThreadLocalMap，一个threadLocals，一个inheritableThreadLocals
 * InheritableThreadLocal继承了ThreadLocal，重写了getMap()和 createMap()方法
 * ThreadLocalMap getMap(Thread t) {
 *     return t.inheritableThreadLocals;
 * }
 * void createMap(Thread t, T firstValue) {
 *     t.inheritableThreadLocals = new ThreadLocalMap(this, firstValue);
 * }
 * 在new子线程时，会调用Thread类的init()方法，方法中如果发现parent.inheritableThreadLocals != null
 * 就会让当前线程的
 * this.inheritableThreadLocals =
 *               ThreadLocal.createInheritedMap(parent.inheritableThreadLocals);
 * 实际上就是
 * static ThreadLocalMap createInheritedMap(ThreadLocalMap parentMap) {
 *     return new ThreadLocalMap(parentMap);
 * }
 * 将父线程的inheritableThreadLocals复制了一份
 * 所以当子线程自己set InheritableThreadLocal后，从父线程继承的值就会被覆盖
 */
public class ThreadLocalDemo3_InheritableThreadLocal {
	private static InheritableThreadLocal<String> threadLocal = new InheritableThreadLocal<String>(){
		//重写childValue()方法，在构造子类的inheritableThreadLocals Map时会为子类复制一个Map，在给value赋值时会调用childValue()，可以对继承的值做一定修改
		@Override
		protected String childValue(String parentValue) {
			return parentValue + " in subThread";
		}
	};
	 
    public static class MyRunnable implements Runnable {
        public MyRunnable(String name) {
            System.out.println(name + " => " + Thread.currentThread().getName() + " : " + threadLocal.get());
        }
 
        @Override
        public void run() {
            System.out.println("before - " + Thread.currentThread().getName() + " : " + threadLocal.get());
            threadLocal.set(Thread.currentThread().getName());
            System.out.println("after - " + Thread.currentThread().getName() + " : " + threadLocal.get());
        }
    }
 
 
    public static void main(String[] args) {
        threadLocal.set("main");
 
        System.out.println(Thread.currentThread().getName() + " : " + threadLocal.get());
        Thread t1 = new Thread(new MyRunnable("R-A"), "A");
        Thread t2 = new Thread(new MyRunnable("R-B"), "B");
 
        t1.start();
        t2.start();
    }
}
