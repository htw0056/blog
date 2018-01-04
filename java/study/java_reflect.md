# java反射

总结来自《java核心技术卷第九版I》

### 1. 定义

能够分析类能力的程序称为反射。

### 2. 作用

1. 运行中分析类的能力
2. 在运行中查看对象
3. 实现通用数组操作代码
4. 利用Method对象，类似于c++的函数指针效果


### 3. 获得class类对象的三种方式

class类定义:java运行时始终为所有的对象维护一个被称为运行时的类型标识。这个信息跟踪每个对象所属的类。保存这些信息的类称为class。

1. 通过实例获得class对象

   ```
   Employee e;
   Class cl = e.getClass();
   ```

2. 通过类静态方法forName获得class对象

   ```
   String className = "java.util.Date";
   Class cl = Class.forName(className);
   ```

3. 通过类直接获得class对象

   ```
    Class cl1 = Date.class;
    Class cl2 = int.class;
    Class cl3 = Double[].class;
   ```



### 实例

#### 1. 利用反射分析类的能力

代码来自java核心技术卷1第九版。

```java
package com.htw.test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Scanner;

public class Reflectiontest {

	public static void main(String[] args) throws InstantiationException, IllegalAccessException {
		String name = null;
		if (args.length > 0) {
			name = args[0];
		} else {
			Scanner in = new Scanner(System.in);
			System.out.println("enter class name:");
			name = in.next();
		}
		try {
			Class cl = Class.forName(name);
			Class supercl = cl.getSuperclass();
			String modifiers = Modifier.toString(cl.getModifiers());
			if (modifiers.length() > 0)
				System.out.print(modifiers + " ");
			System.out.print("class " + name);
			if (supercl != null & supercl != Object.class)
				System.out.print(" extends " + supercl.getName());
			System.out.print("\n{\n");
			printConstructors(cl);
			System.out.println();
			printMethods(cl);
			System.out.println();
			printFields(cl);
			System.out.println("}");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	private static void printFields(Class cl) {
		Field[] fields = cl.getDeclaredFields();
		for (Field f : fields) {
			Class type = f.getType();
			String name = f.getName();
			System.out.print("  ");
			String modifiers = Modifier.toString(f.getModifiers());
			if (modifiers.length() > 0) {
				System.out.print(modifiers + " ");
			}
			System.out.println(type.getName() + " " + name + ";");
		}
	}

	private static void printMethods(Class cl) {
		Method[] methods = cl.getDeclaredMethods();
		for (Method m : methods) {
			Class retType = m.getReturnType();
			String name = m.getName();

			System.out.print("  ");
			String modifiers = Modifier.toString(m.getModifiers());
			if (modifiers.length() > 0)
				System.out.print(modifiers + " ");
			System.out.print(retType.getName() + " " + name + "(");
			Class[] paramTypes = m.getParameterTypes();
			for (int j = 0; j < paramTypes.length; j++) {
				if (j > 0) {
					System.out.print(", ");
				}
				System.out.print(paramTypes[j].getName());
			}
			System.out.println(");");

		}

	}

	private static void printConstructors(Class cl) {
		Constructor[] constructors = cl.getDeclaredConstructors();
		for (Constructor c : constructors) {
			String name = c.getName();
			System.out.print("  ");
			String modifiers = Modifier.toString(c.getModifiers());
			if (modifiers.length() > 0)
				System.out.print(modifiers + " ");
			System.out.print(name + "(");
			Class[] paramTypes = c.getParameterTypes();
			for (int j = 0; j < paramTypes.length; j++) {
				if (j > 0) {
					System.out.print(", ");
				}
				System.out.print(paramTypes[j].getName());
			}
			System.out.println(");");
		}
	}

}
```

#### 2. 运行中查看对象

```java
Employee harry = new Employee("Harry Hacker", 3500, 10 , 1, 1989);
Class cl = harry.getClass();//获得class类对象
Field f = cl.getDeclaredField("name");//获得name字段的field
Object v = f.get(harry);//查看harry实例的name字段的值
```


#### 3. 数组拷贝

```java
public static Object goodCopyOf( Object a, int newLength)
{
	Class cl = a.getClass();//获得a的class类
	if(!cl.isArray()) return null;
	Class componentType = cl.getComponentType();//获得数组a内所包含元素的class类型
	Object newArray = Array.newInstance(componentType, newLength);
	System.arraycopy(a, 0, newArray, 0 Math.min(length,newLength));
	return newArray;
}
```


#### 4. 调用任意方法

```java
package com.htw.test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodTableTest {
	public static void main(String[] args) throws NoSuchMethodException, SecurityException {
		Method square = MethodTableTest.class.getMethod("square", double.class);
		Method sqrt = Math.class.getMethod("sqrt", double.class);

		printTable(1, 10, 10, square);

		printTable(1, 10, 10, sqrt);
	}

	public static double square(double x) {
		return x * x;
	}

	private static void printTable(double from, double to, int n, Method f) {
		System.out.println(f);

		double dx = (to - from) / (n - 1);
		for (double x = from; x <= to; x += dx) {
			try {

				double y = (double) f.invoke(null, x);
				System.out.printf("%10.4f | %10.4f\n", x, y);

			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}
}
```

#### 5. 通过反射理解泛型

```java
package com.htw.test;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class TestForGenerics
{

	public static void main(String[] args)
	{
		// 带有泛型的声明
		ArrayList<String> list1 = new ArrayList<>();
		// 普通声明
		ArrayList list2 = new ArrayList();

		// 添加元素
		list1.add("hello");
		// 以下会报错，编译不通过
		// list1.add(20);
		Class c1 = list1.getClass();
		Class c2 = list2.getClass();

		/*
		 * 反射的操作是编译后的操作 
		 * 返回true，说明编译之后集合的泛型是去泛型化的 
		 * java中的集合的泛型，是防止错误输入的，只在编译阶段有效
		 * 绕过编译就无效了
		 */

		System.out.println(c1 == c2);

		/*
		 * 通过反射来验证
		 */
		try
		{
			Method m = c1.getMethod("add", Object.class);
			// 在上面代码中可知，list1是无法添加20
			// 但是此时通过反射即可添加
			m.invoke(list1, 20);
			System.out.println(list1);
			/*
			 * int类型的20，通过这种方式输出会运行时报错 
			 * for(String s:list1) {
			 * 	System.out.println(s); 
			 * }
			 */

		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}
}
```