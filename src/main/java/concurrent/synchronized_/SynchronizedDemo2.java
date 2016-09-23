package concurrent.synchronized_;

/**
 * 使用 javap -c 反汇编字节码文件
 * 结构：
 * public synchronized void insert1();
    Code:
       0: return        

   public void insert2();
    Code:
       0: aload_0       
       1: getfield      #12       // Field obj:Ljava/lang/Object;
       4: dup           
       5: monitorenter            //会使对象的锁计数+1
       6: monitorexit             //会使对象的锁计数-1
       7: return    
 */
public class SynchronizedDemo2 {
	Object obj = new Object();
	
	public synchronized void insert1(){
		
	}
	
	public void insert2(){
		synchronized (obj) {
			
		}
	}
}
