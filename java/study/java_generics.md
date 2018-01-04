# java泛型

```Java
package com.htw.generics;

public class Testforgenerics {

	public static void main(String[] args) {
		Point<String, Integer> p = new Point<String, Integer>("hello", 1);
		test(p);
		System.out.println(tell("123"));
		//or
		System.out.println(Testforgenerics.<String>tell("123"));
		//or
		System.out.println(Testforgenerics.tell("123"));
	}

	//泛型函数
	public static <T> T tell(T t){
		return t;
	}
	//泛型指定子类型
	public static <E extends ABC> boolean isEqual(E a,E b ){
		return a.getArea()==b.getArea();
	}
	// 通配符
	public static void test(Point<?, ?> x) {
		System.out.println(x.toString());
	}
}

// 泛型类
// 多个参数
class Point<T, K> {
	private T x;
	private K y;

	// 泛型构造方法
	public Point(T x, K y) {
		super();
		this.x = x;
		this.y = y;
	}

	public T getX() {
		return x;
	}

	public void setX(T x) {
		this.x = x;
	}

	public K getY() {
		return y;
	}

	public void setY(K y) {
		this.y = y;
	}

	@Override
	public String toString() {
		return "Point [x=" + x + ", y=" + y + "]";
	}

}
```
