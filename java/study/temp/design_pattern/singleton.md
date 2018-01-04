# java单例设计模式  Singleton

java单例模式的版本有很多，目前也在逐步的学习中。  
以下内容收集自:  
[深入浅出单实例Singleton设计模式](http://coolshell.cn/articles/265.html)  
[如何正确地写出单例模式](http://wuchong.me/blog/2014/08/28/how-to-correctly-write-singleton-pattern/)

### 懒汉式，线程不安全

```java
public class Singleton {
    private static Singleton instance=null;
    private Singleton (){}
    public static Singleton getInstance() {
     if (instance == null) {
         instance = new Singleton();
     }
     return instance;
    }
}
```

这是最简单的一个版本，我们需要知道以下几点：

1. 私有（private）的构造函数，表明这个类是不可能形成实例了。这主要是怕这个类会有多个实例。
2. 即然这个类是不可能形成实例，那么，我们需要一个静态的方式让其形成实例：getInstance()。注意这个方法是在new自己，因为其可以访问私有的构造函数，所以他是可以保证实例被创建出来的。
3. 在getInstance()中，先做判断是否已形成实例，如果已形成则直接返回，否则创建实例。
4. 所形成的实例保存在自己类中的私有成员中。
5. 我们取实例时，只需要使用Singleton.getInstance()就行了。

缺点：

- 线程不安全，多线程下不能安全工作


### 懒汉式，线程安全

```java
public class Singleton {
    private static Singleton instance=null;
    private Singleton (){}
    public static synchronized Singleton getInstance() {
     if (instance == null) {
         instance = new Singleton();
     }
     return instance;
    }
}
```

此时线程安全了，但是任何时候只有一个线程调度getInstance(),所以并不高效。

### 双重检验锁

这是目前我用到多线程时，使用的版本了。

```java
public class Singleton
{
    private static Singleton instance = null;
    private Singleton() {  }
    public static Singleton getInstance() {
			if (instance == null) {                         
            synchronized (Singleton.class) {
                if (instance == null) {       
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
```


这段代码看起来很完美，很可惜，它是有问题。主要在于instance = new Singleton()这句，这并非是一个原子操作，事实上在 JVM 中这句话大概做了下面 3 件事情。

给 instance 分配内存
调用 Singleton 的构造函数来初始化成员变量
将instance对象指向分配的内存空间（执行完这步 instance 就为非 null 了）
但是在 JVM 的即时编译器中存在指令重排序的优化。也就是说上面的第二步和第三步的顺序是不能保证的，最终的执行顺序可能是 1-2-3 也可能是 1-3-2。如果是后者，则在 3 执行完毕、2 未执行之前，被线程二抢占了，这时 instance 已经是非 null 了（但却没有初始化），所以线程二会直接返回 instance，然后使用，然后顺理成章地报错。

我们只需要将 instance 变量声明成 volatile 就可以了。

改进:

```java
public class Singleton
{
    private static Singleton instance = null;
    private Singleton() {  }
    public static Singleton getInstance() {
			if (instance == null) {                         
            synchronized (Singleton.class) {
                if (instance == null) {       
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
```

但是特别注意在 Java 5 以前的版本使用了 volatile 的双检锁还是有问题的。其原因是 Java 5 以前的 JMM （Java 内存模型）是存在缺陷的，即时将变量声明成 volatile 也不能完全避免重排序，主要是 volatile 变量前后的代码仍然存在重排序问题。这个 volatile 屏蔽重排序的问题在 Java 5 中才得以修复，所以在这之后才可以放心使用 volatile。

### 饿汉式

```java
public class Singleton{
    //类加载时就初始化
    private static final Singleton instance = new Singleton();
    private Singleton(){}
    public static Singleton getInstance(){
        return instance;
    }
}
```

这种写法如果完美的话，就没必要在啰嗦那么多双检锁的问题了。缺点是它不是一种懒加载模式（lazy initialization），单例会在加载类后一开始就被初始化，即使客户端没有调用 getInstance()方法。饿汉式的创建方式在一些场景中将无法使用：譬如 Singleton 实例的创建是依赖参数或者配置文件的，在 getInstance() 之前必须调用某个方法设置参数给它，那样这种单例写法就无法使用了。


### 静态内部类

```java
public class Singleton {  
    private static class SingletonHolder {  
        private static final Singleton INSTANCE = new Singleton();  
    }  
    private Singleton (){}  
    public static final Singleton getInstance() {  
        return SingletonHolder.INSTANCE; 
    }  
}
```

这种写法仍然使用JVM本身机制保证了线程安全问题；由于 SingletonHolder 是私有的，除了 getInstance() 之外没有办法访问它，因此它是懒汉式的；同时读取实例的时候不会进行同步，没有性能缺陷；也不依赖 JDK 版本。


### 枚举

相当精简的方式。

```java
public enum EasySingleton{
    INSTANCE;
}
```

我们可以通过EasySingleton.INSTANCE来访问实例，这比调用getInstance()方法简单多了。创建枚举默认就是线程安全的，所以不需要担心double checked locking，而且还能防止反序列化导致重新创建新的对象。



### Singleton的其它问题

##### 其一,Class Loader

不知道你对Java的Class Loader熟悉吗？“类装载器”？！C++可没有这个东西啊。这是Java动态性的核心。顾名思义，类装载器是用来把类(class)装载进JVM的。JVM规范定义了两种类型的类装载器：启动内装载器(bootstrap)和用户自定义装载器(user-defined class loader)。 在一个JVM中可能存在多个ClassLoader，每个ClassLoader拥有自己的NameSpace。一个ClassLoader只能拥有一个class对象类型的实例，但是不同的ClassLoader可能拥有相同的class对象实例，这时可能产生致命的问题。如ClassLoaderA，装载了类A的类型实例A1，而ClassLoaderB，也装载了类A的对象实例A2。逻辑上讲A1=A2，但是由于A1和A2来自于不同的ClassLoader，它们实际上是完全不同的，如果A中定义了一个静态变量c，则c在不同的ClassLoader中的值是不同的。

于是，如果我们的双重检验锁版本如果面对着多个Class Loader会怎么样？呵呵，多个实例同样会被多个Class Loader创建出来，当然，这个有点牵强，不过他确实存在。难道我们还要整出个新版吗？可是，我们怎么可能在我的Singleton类中操作Class Loader啊？是的，你根本不可能。在这种情况下，你能做的只有是——“保证多个Class Loader不会装载同一个Singleton”。

##### 其二,序例化

如果我们的这个Singleton类是一个关于我们程序配置信息的类。我们需要它有序列化的功能，那么，当反序列化的时候，我们将无法控制别人不多次反序列化。不过，我们可以利用一下Serializable接口的readResolve()方法，比如：

```
public class Singleton implements Serializable
{
    ......
    ......
    protected Object readResolve()
    {
        return getInstance();
    }
}
```

##### 其三,多个Java虚拟机

如果我们的程序运行在多个Java的虚拟机中。什么？多个虚拟机？这是一种什么样的情况啊。嗯，这种情况是有点极端，不过还是可能出现，比如EJB或RMI之流的东西。要在这种环境下避免多实例，看来只能通过良好的设计或非技术来解决了。

##### 其四,volatile变量

关于volatile这个关键字所声明的变量可以被看作是一种 “程度较轻的同步synchronized”；与 synchronized 块相比，volatile 变量所需的编码较少，并且运行时开销也较少，但是它所能实现的功能也仅是synchronized的一部分。当然，如前面所述，我们需要的Singleton只是在创建的时候线程同步，而后面的读取则不需要同步。所以，volatile变量并不能帮助我们即能解决问题，又有好的性能。而且，这种变量只能在JDK 1.5+版后才能使用。

##### 其五,关于继承

是的，继承于Singleton后的子类也有可能造成多实例的问题。不过，因为我们早把Singleton的构造函数声明成了私有的，所以也就杜绝了继承这种事情。

##### 其六,关于代码重用

也话我们的系统中有很多个类需要用到这个模式，如果我们在每一个类都中有这样的代码，那么就显得有点傻了。

