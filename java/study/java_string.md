# Java String类


### 创建String

创建String的方式很多，但是其中会遇到一种特殊的情况

```java
package com.htw.string;

public class TestForString {
	public static void main(String[] args) {
		String s1 = "hello";
		String s2 = "hello";
		String s3 = new String("hello");
		String s4 = new String("hello");
		
		System.out.println(s1 == s2);//true
		System.out.println(s1 == s3);//false
		System.out.println(s3 == s4);//false
		System.out.println(s1.equals(s2));//true
		System.out.println(s1.equals(s3));//true
	}
}
```
由上面的代码可以看出，s1和s2比较返回了true。和我们平时所理解的不同，因为`==`比较的应该是引用的地址，按理来说这两个指向是不同的，那为什么会s1==s2成立？

实际上用`String s1 = "hello";`方式声明的字符串会在一个对象池中，且是唯一的，所以s1和s2只要使用这种方式创建字符串就会获得相同的引用地址。

**注意**:String类是不可改变的，所以你一旦创建了String对象，那它的值就无法改变了。 如果需要对字符串做很多修改，那么应该选择使用StringBuffer & StringBuilder 类。  

StringBuffer 和 StringBuilder 都是可变对象，StringBuilder速度比StringBuffer更快，但是StringBuilder是线程不安全的。
