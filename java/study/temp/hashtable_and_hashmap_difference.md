# Hashtable and HashMap difference


附上类似的比较组合: 
ArrayList和Vector有什么区别？
HashMap和HashTable有什么区别？
StringBuilder和StringBuffer有什么区别？


Hashtable是个过时的集合类，存在于Java API中很久了。在Java 4中被重写了，实现了Map接口，所以自此以后也成了Java集合框架中的一部分。


### 区别 

1. HashTable的方法是同步的，HashMap未经同步，所以在多线程场合要手动同步HashMap这个区别就像Vector和ArrayList一样。

2. HashTable不允许null值(key和value都不可以),HashMap允许null值(key和value都可以)。

3. HashTable有一个contains(Object value)，功能和containsValue(Object value)功能一样。

4. HashTable使用Enumeration，HashMap使用Iterator。

5. HashTable中hash数组默认大小是11，增加的方式是 old*2+1。HashMap中hash数组的默认大小是16，而且一定是2的指数。

6. 哈希值的使用不同，HashTable直接使用对象的hashCode，代码是这样的：

   ```
   int hash = key.hashCode();
   int index = (hash & 0x7FFFFFFF) % tab.length;
   ```
   而HashMap重新计算hash值，而且用与代替求模：

   ```
   int hash = hash(k);
   int i = indexFor(hash, table.length);
   static int hash(Object x) {
   　　int h = x.hashCode();
   　　h += ~(h << 9);
   　　h ^= (h >>> 14);
   　　h += (h << 4);
   　　h ^= (h >>> 10);
   　　return h;
   }
   static int indexFor(int h, int length) {
   　　return h & (length-1);
   }
   ```

   hashtable:由于作为key的对象将通过计算其**散列函数**来确定与之对应的value的位置，因此**任何作为key的对象都必须实现hashCode和equals方法**。

   hashCode和equals方法继承自根类Object，如果你用自定义的类当作key的话，要相当小心，按照散列函数的定义，如果两个对象相同，即obj1.equals(obj2)=true，则它们的hashCode必须相同，但如果两个对象不同，则它们的hashCode不一定不同，如果两个不同对象的hashCode相同，这种现象称为冲突，冲突会导致操作哈希表的时间开销增大，所以尽量定义好的hashCode()方法，能加快哈希表的操作。  

   如果相同的对象有不同的hashCode，对哈希表的操作会出现意想不到的结果（期待的get方法返回null），要避免这种问题，只需要牢记一条：**要同时复写equals方法和hashCode方法，而不要只写其中一个**。


  
