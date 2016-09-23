package basics.finally_.finally_return;

/**
 * 
 * 运行结果：
 * try block
 * catch block
 * finally block
 * b>25, b = 35
 * 35
 * 
 * 结论：
 * 在catch中return也一样，finally是在return执行后，真正返回之前执行
 * 且返回的是基本类型，所以finally中对b的修改不生效
 */
public class FinallyReturnDemo6 {
	 public static void main(String[] args) {
		 System.out.println(test1());
	 }

	 public static int test1() {
		 int b = 20;

	     try {
	    	 System.out.println("try block");
	            
	         b = b /0;

	         return b += 80;
	     } 
	     catch(Exception e){
	         System.out.println("catch block");
	         return b += 15;
	     } 
	     finally{
	         System.out.println("finally block");

             if (b > 25) {
                 System.out.println("b>25, b = " + b);
             }

             b += 50;
         }

	     //return b;    
	 }
}
