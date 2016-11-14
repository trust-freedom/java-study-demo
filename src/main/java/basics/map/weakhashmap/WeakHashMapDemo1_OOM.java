package basics.map.weakhashmap;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * JVM参数设置： -Xmx50m
 * 使用 HashMap运行结果：
 * i=100000,map.size()=100000,1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
 * i=200000,map.size()=200000,1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
 * i=300000,map.size()=300000,1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
 * i=400000,map.size()=400000,1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
 * Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
 * 	at java.lang.Long.toString(Long.java:267)
 * 	at java.lang.String.valueOf(String.java:2968)
 * 	at basics.map.weakhashmap.WeakHashMapDemo1_OOM.main(WeakHashMapDemo1_OOM.java:23)
 * 
 * 使用WeakHashMap运行结果：
 * i=100000,map.size()=100000,1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
 * i=200000,map.size()=186581,1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
 * i=300000,map.size()=246825,1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
 * i=400000,map.size()=301579,null
 * i=500000,map.size()=127863,null
 * i=600000,map.size()=175611,null
 * 注意：i=200000时，map.size不到200000，说明已经发生了回收
 * 
 * 结论：
 * 说明使用WeakHashMap可以有效避免内存溢出，弱引用的HashMap在发生GC后会回收部分map中的Entry
 * 但从运行结果看，第二次打印就发生了回收，但没有从最先放进去的回收，且回收多少也不一定
 * 但始终没有发生OOM
 * WeakHashMap在put()时，会检查是否有Entry被回收且被放入回收后的queue中，调用expungeStaleEntries()，会删除那些被回收的Entry，重置size
 */
public class WeakHashMapDemo1_OOM {
	public static void main(String[] args) {  
        Map<String, String> map = new WeakHashMap<String, String>();  
        long i = 1;  
        while (i < 100000000L) {  
        	map.put(String.valueOf(i),  
                        "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"); 
  
            //每10W次查看key=1的是否依然存活  
            if (i % 100000 == 0) {  
                System.out.println("i="+i+",map.size()="+map.size()+","+map.get(String.valueOf(1)));  
            }  
  
            i++;  
        }  
    }  
}
