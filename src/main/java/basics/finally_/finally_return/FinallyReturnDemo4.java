package basics.finally_.finally_return;

/**
 * 测试目的：
 * 测试try中return的执行、finally执行、和真正的返回数据的顺序
 * 后续的一点测试（Java中是值传递，还是引用传递的问题）
 * 
 * 如果b是在test1()中声明，且test2(int b)
 * 运行结果：
 * try block
 * return statement
 * test2 b=100
 * finally block b=20
 * 100
 * 原因：
 * Java中是值传递，传递给test2(b)的是值20，在test2()中计算得100，但test1() finally中b的值还是20
 * 如果b不是在test1()中声明，而是static变量，那么结果不变
 * 因为return test2(b); 也是将static变量b的值20传递给test2().在test2()中b是一个局部变量
 * 
 * 如果b是static变量，且test2()没有参数，无论b是int，还是Integer
 * 运行结果：
 * try block
 * return statement
 * test2 b=100
 * finally block b=100
 * b>25, b = 100
 * 100
 * 原因：
 * 没有参数传递，都是修改的统一的static变量
 * 
 * 结论：
 * Java中是值传递，如果是基本类型，那么传递的就是基本类型的值
 * 如果是引用类型，那么传递的是引用地址，指向具体的对象
 * 基础类型被分配在栈中，具体对象被分配在堆中
 */
public class FinallyReturnDemo4 {
//	public static int b = 20;
	public static Integer b = new Integer(20);
	
	public static void main(String[] args) {
        System.out.println(test1());
    }

    public static int test1() {
//    	int b = 20;

        try{
            System.out.println("try block");

            return test2(); 
//          return test2(); 
        }
        catch(Exception e){
            System.out.println("catch block");
        }
        finally{
            System.out.println("finally block b=" + b);
            
            if(b > 25){
                System.out.println("b>25, b = " + b);
            }
            
//            return 200;
        }
        
        return b;
    }
    
    
    public static int test2(){
//    public static int test2(){
    	System.out.println("return statement");
    	b += 80; 
    	System.out.println("test2 b="+b);
    	return b;
    }
}
