# 模拟eclipse运行java

因为一开始学习java的时候就是使用eclipse这种ide来运行java，结果导致自己对于最基础的编译、运行java都不会。。特地去看书学习了一下。以下是模拟eclipse结构的java运行。


### 准备

1. 配置好java环境
2. 测试环境:centos6.6



### 步骤



##### 1. 建立project目录

```
mkdir test
cd test/
```

##### 2. 建立src目录以及class目录


```
mkdir src
mkdir class
```


##### 3. 创建类com.htw.test.Hello.java

```
mkdir -p src/com/htw/test
touch src/com/htw/test/Hello.java
vim src/com/htw/test/Hello.java
```

```java
package com.htw.test;
public class Hello{
        public static void main(String[] args){
                System.out.println("Hello World!");
        }
}
```
 
##### 4. 编译类com.htw.test.Hello.java

```
javac -d class/ src/com/htw/test/Hello.java
```

##### 5. 运行Hello类

```
java -classpath class/ com.htw.test.Hello
```