package basics.collection.list.abstractList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * 运行结果：
 * 上下两段代码都会报java.util.ConcurrentModificationException
 * 
 * 结论：
 * ArrayList 和 Vector都是List接口的实现类，集成自AbstractList
 * AbstractList有一个属性modCount-修改次数
 * iterator的实现类是AbstractList的内部类Itr，其有属性expectedModCount
 * 每当list.iterator()时，会new Itr()，将list的modCount赋值给迭代器的expectedModCount
 * 如果在迭代过程中，list发生了变化，在it.nect()时会检查，并快速的上抛ConcurrentModificationException异常
 * 下例中都是通过调用list.remove()删除元素，如果调用it.remove()就不会抛异常
 * 因为it.remove()中会调用 list.remove()，将modCount--，且为迭代器的expectedModCount重新赋值
 */
public class AbstractListDemo1_ConcurrentModificationException {
	public static void main(String[] args) {
		List<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(2);
		list.add(3);
		
		Iterator<Integer> it = list.iterator();
		while(it.hasNext()){
			Integer i = it.next();
			
			if(i.intValue() == 1){
				list.remove(i);
			}
		}
		
		
//		Vector<Integer> vector = new Vector<Integer>();
//		vector.add(1);
//		vector.add(2);
//		vector.add(3);
//		
//		Iterator<Integer> it2 = vector.iterator();
//		while(it2.hasNext()){
//			Integer i = it2.next();
//			
//			if(i.intValue() == 1){
//				vector.remove(i);
//			}
//		}
	}
}
