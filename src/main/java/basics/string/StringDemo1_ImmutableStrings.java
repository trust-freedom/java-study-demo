package basics.string;


/**
 * 运行结果：
 * Hello World!
 * Hello 
 * 
 * 结论：
 * String是“不可变的”
 * String类是final的，说明不能被继承
 * String是一串有序的字符组成了，而这个字符数组也是final的
 * 这个例子中，现在字符串常量池中创建了"Hello "，s1引用指向这个对象
 * 根据String#concat()的注释：
 * If the length of the argument string is 0, then this String object is returned. 
 * Otherwise, a new String object is created, representing a character
 * sequence that is the concatenation of the character sequence
 * represented by this String object and the character
 * sequence represented by the argument string.
 * 说明创建了一个新的字符串"Hello World!"，并使s2引用指向这个新字符串
 * 故再输出s1指向的字符串，还是原来的值
 * 
 * public String concat(String str) {
 *     int otherLen = str.length();
 *     if (otherLen == 0) {
 *         return this;
 *     }
 *     int len = value.length;
 *     char buf[] = Arrays.copyOf(value, len + otherLen);
 *     str.getChars(buf, len);
 *     return new String(buf, true);
 * }
 * 
 * String的不可变性是字符串常量池实现的基础
 * String在创建时就计算了hashcode，且由于不可变性，hashcode不会改变，故很适合做Map的键
 */
public class StringDemo1_ImmutableStrings {
	public static void main(String[] args) {
		String s1 = "Hello ";
		String s2 = s1.concat("World!");
		System.out.println(s2);
		System.out.println(s1);
	}
}
