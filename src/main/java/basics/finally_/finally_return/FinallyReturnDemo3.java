package basics.finally_.finally_return;

/**
 * 测试目的：
 * 测试try中return的执行、finally执行、和真正的返回数据的顺序
 * 测试finally中有return
 * 
 * 运行结果：
 * try block
 * finally block b=100
 * b>25, b = 100
 * 200
 * 
 * 结论：
 * 说明finally中的return就执行返回了，就不管try中是否还有return了
 * 注意：finally中有return，finally外面的return就变成不可达的了
 */
public class FinallyReturnDemo3 {
	
	public static void main(String[] args) {
        System.out.println(test1());
    }

    public static int test1() {
    	int b = 20;

        try{
            System.out.println("try block");

            return b += 80; 
        }
        catch(Exception e){
            System.out.println("catch block");
        }
        finally{
            System.out.println("finally block b=" + b);
            
            if(b > 25){
                System.out.println("b>25, b = " + b);
            }
            
            return 200;
        }
        
//        return b;
    }
    
}
