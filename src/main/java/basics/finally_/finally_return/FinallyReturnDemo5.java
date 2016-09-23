package basics.finally_.finally_return;

import java.util.HashMap;
import java.util.Map;

/**
 * 测试目的：finally中修改返回值，会不会生效
 * 
 * 运行结果：FINALLY
 *
 * 结论：
 * finally中的修改，可能会影响到返回值，也可能不会影响到
 * 原因还是：Java中是值传递的
 * 如果返回值是基础类型的，那么finally中的修改不会生效
 * 如果返回类型是引用类型，那么返回的是引用指向的对象地址，finally中修改对象的属性，是可以生效的
 */
public class FinallyReturnDemo5 {
	
	public static void main(String[] args) {
        System.out.println(getMap().get("KEY").toString());
    }
     
    public static Map<String, String> getMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("KEY", "INIT");
         
        try {
            map.put("KEY", "TRY");
            return map;//返回的是对象map指向的对象地址
        }
        catch (Exception e) {
            map.put("KEY", "CATCH");
        }
        finally {
            map.put("KEY", "FINALLY");//修改对象属性
            map = null;//将map引用置为null，但没用修改原来指向的堆中的具体对象，所有不影响get("KEY")
        }
         
        return map;
    }
}
