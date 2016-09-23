package basics.finally_.finally_return;

/**
 * 测试目的：
 * 测试try中return的执行、finally执行、和真正的返回数据的顺序
 * 
 * 运行结果：
 * try block
 * return statement
 * finally block
 * after return
 * 
 * 结论：
 * 换一种方式验证
 * 说明先执行了return语句的test2()，不过并没有直接返回，再去执行finally内的代码，等finally内的代码执行完了才返回
 */
public class FinallyReturnDemo2 {
	
	public static void main(String[] args) {
        System.out.println(test1());
    }

    public static String test1() {
        try{
            System.out.println("try block");

            return test2(); 
        }
        finally{
            System.out.println("finally block");
        }
    }
    
    public static String test2() {
        System.out.println("return statement");

        return "after return";
    }
    
}
