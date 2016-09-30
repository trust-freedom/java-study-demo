package concurrent.volatile_;

/**
 * 使用volatile实现线程安全的，延时加载的单例模式，且只在初始化实例时同步
 * 
 * 1、不在getInstance()方法上加synchronized
 * 因为只有在初始化单例时，需要同步，之后的getInstance()都是直接返回单例，这时候的同步是没有意义的，浪费的
 * 
 * 2、为什么使用双重校验，即两次判断 if(instance == null)
 * 外层判断意思是，只有instance为null时，才需要初始化
 * 但在多线程环境下，在instance==null时，有可能多个线程都通过了外层校验，
 * 之后第一个线程先进入同步代码块，初始化一次，如果没有内部校验，那么第二个线程再进入同步代码块就会又初始化一次，就不是单例了
 * 
 * 3、为什么必须使用 volatile
 * instance = new VolatileDemo2_DoubleCheckSingleton()可以分解为如下3行伪代码
 * memory = allocate();   //1：分配对象的内存空间
 * ctorInstance(memory);  //2：初始化对象
 * instance = memory;     //3：设置instance指向刚分配的内存地址
 * 
 * 上面三行伪代码中的2和3之间，可能会被重排序，2和3之间重排序之后的执行时序如下：
 * memory = allocate();   //1：分配对象的内存空间
 * instance = memory;     //3：设置instance指向刚分配的内存地址
 *                        //注意，此时对象还没有被初始化！
 * ctorInstance(memory);  //2：初始化对象
 * 如果按照如上顺序，线程A进入同步块，在执行完3后被换下，线程B此时判断instance不为null，return instance
 * 这样就将还没有初始化，只是分配了内存空间的instance实例返回了
 * 
 * 当加上volatile关键字后，2、3的重排序将会被禁止
 * 
 * 注意：需要jdk 1.5或更高的版本，因为从JDK5开始使用新的JSR-133内存模型规范，这个规范增强了volatile的语义
 */
public class VolatileDemo2_DoubleCheckSingleton {
	private static volatile VolatileDemo2_DoubleCheckSingleton instance;
	
	private VolatileDemo2_DoubleCheckSingleton(){}
	
	public static VolatileDemo2_DoubleCheckSingleton getInstance(){
		if(instance == null){
			synchronized(VolatileDemo2_DoubleCheckSingleton.class){
				if(instance == null){
					instance = new VolatileDemo2_DoubleCheckSingleton();
				}
			}
		}
		
		return instance;
	}
}
