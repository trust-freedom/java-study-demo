package basics.basic_types.packing_type;

/**
 * 运行结果:
 * i1==i2     true
 * i1==i2+i3  true
 * i1==i4     false
 * i4==i5     false
 * i4==i5+i6  true
 * i7==i8     false
 * i1==i9     true
 * i4==i9     true
 *	
 * 结论：
 * 首先，在java中用 ==对于基本类型比较的是值，对于对象比较的是引用地址
 * 此例在jdk1.6 和 1.7的运行结果一致
 * 
 * 对于i1和i2，Integer i1 = 40
 * Java在编译的时候会执行将代码封装成Integer i1 = Integer.valueOf(40)
 * Integer.valueOf()判断是new一个Integer，还是从IntegerCache中获取
 * IntegerCache是Integer的内部类，是一个Integer数组，在类加载时初始化
 * Integer已经默认创建了数值【-128~127】的Integer缓存数据
 * 所以i1、i2都是引用常量池中的同一对象，故i1==i2为true
 * 
 * 而i4是直接new出来的，i4指向堆内存中的对象，虽然数值相同，但i1==i4为false
 * i4、i5指向了堆中不同的内存地址，故i4==i5为false
 * 
 * i1==i2+i3 和 i4==i5+i6情况类似
 * Integer本身无法进行+操作，所以需要自动拆箱，计算得40，Integer(40)也无法直接和40比较，也会自动拆箱
 * 故最终变成了40==40的数值比较，结果为true
 * 从javap -verbose的结果看，在打印i4==i5+i6结果时，发生了3次 Integer.intValue()调用
 * 证明了自己拆箱做数值计算后比较
 * 
 * i7、i8已经超过了【-128~127】范围，故不是从IntegerCache中获取，而都是new出来的，在堆中
 * 
 * i1==i9 和 i4==i9证实确实存在自动拆箱
 * 根据javap -verbose看 i1==i9
 * 在构造i1时，发生Integer.valueOf()自动装箱
 * 在比较时发生Integer.intValue()自动拆箱
 */
public class IntegerDemo1_ConstantPool {
	public static void main(String[] args) {
		Integer i1 = 40;
        Integer i2 = 40;
        Integer i3 = 0;
        Integer i4 = new Integer(40);
        Integer i5 = new Integer(40);
        Integer i6 = new Integer(0);
        Integer i7 = 128;
        Integer i8 = 128;
        int i9 = 40;

        System.out.println("i1==i2     " + (i1 == i2));
        System.out.println("i1==i2+i3  " + (i1 == i2 + i3));
        System.out.println("i1==i4     " + (i1 == i4));
        System.out.println("i4==i5     " + (i4 == i5));
        System.out.println("i4==i5+i6  " + (i4 == i5 + i6));    
        System.out.println("i7==i8     " + (i7 == i8));
        System.out.println("i1==i9     " + (i1 == i9));
        System.out.println("i4==i9     " + (i4 == i9));
	}
}
