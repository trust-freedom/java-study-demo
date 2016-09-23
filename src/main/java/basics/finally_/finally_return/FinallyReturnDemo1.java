package basics.finally_.finally_return;

/**
 * 测试目的：
 * 测试try中return的执行、finally执行、和真正的返回数据的顺序
 * 
 * 运行结果：
 * try block
 * finally block
 * b>25, b = 100
 * 100
 * 
 * 结论：
 * 说明先执行了return语句，不过并没有直接返回，再去执行finally内的代码，等finally内的代码执行完了才返回
 */
public class FinallyReturnDemo1 {
	
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
            System.out.println("finally block");
            
            if(b > 25){
                System.out.println("b>25, b = " + b);
            }
        }
        
        return b;
    }
    
}
