# Injecting Spring Prototype bean into Singleton bean

## 1. 背景

Spring框架中bean可以有多种作用域：`singleton`，`prototype`，`session`，`request`等，在绝大多数情况下，`singleton`作用域就能满足使用需求了。但是如果我们想把Prototype Beans注入到Singleton Instance中，使得每次调用Singleton实例的方法获取的Prototype Beans都是不同的实例，那该如何操作呢？

## 2. prototype错误注入

```java
@Component
@Scope("prototype")
public class PrototypeBean {
    private static final AtomicInteger instanceCounter = new AtomicInteger(0);

    // 每次初始化+1
    public PrototypeBean() {
        instanceCounter.incrementAndGet();
    }

    public Integer getCount() {
        return instanceCounter.get();
    }
}
```

```java
@Slf4j
@Component
public class SingletonBean {

    @Autowired
    private PrototypeBean prototypeBean;

    public SingletonBean() {
        log.info("Singleton instance created");
    }

    public PrototypeBean getPrototypeBean() {
        return prototypeBean;
    }
}
```

```java
public class ErrorConfigTest {

    @Autowired
    private SingletonBean singletonBean;

    @Test
    public void Test() {
        PrototypeBean prototypeBean1 = singletonBean.getPrototypeBean();
        log.info("---------"+prototypeBean1.getCount());
        log.info("---------"+prototypeBean1.getCount());
        PrototypeBean prototypeBean2 = singletonBean.getPrototypeBean();
        log.info("---------"+prototypeBean2.getCount());
        log.info("---------"+prototypeBean2.getCount());
        // 获得的是同一个bean，因为 prototypeBean 是在singletonBean构造时注入，即使prototypeBean作用域是prototype
        // 而singletonBean是单例，只实例化一次，因此直接在单例中注入prototype作用域bean不能起到每次新生成prototypeBean
        Assert.assertEquals(prototypeBean1,prototypeBean2);

        /**
         * 输出
         * 15:42:33.300 [main] INFO com.htw.study.scopetest.error.ErrorConfigTest - ---------1
         * 15:42:33.300 [main] INFO com.htw.study.scopetest.error.ErrorConfigTest - ---------1
         * 15:42:33.301 [main] INFO com.htw.study.scopetest.error.ErrorConfigTest - ---------1
         * 15:42:33.301 [main] INFO com.htw.study.scopetest.error.ErrorConfigTest - ---------1
         *
         * 这也能说明只实例化一次
         */
    }
}
```

PrototypeBean类作用域是`prototype`,SingletonBean类作用域是`singleton`，按正常逻辑来看，我们直接将PrototypeBean注入到SingletonBean即可。实际上，这样是达不到我们想要的效果的。因为SingletonBean类是单例，它在初始化时只进行一次注入PrototypeBean。这是一种错误的实现方式。

## 3.  ApplicationContextAware

```java
public class SingletonAppContextBean implements ApplicationContextAware {

    private ApplicationContext applicationContext;


    public SingletonAppContextBean() {
        log.info("Singleton instance created");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
        throws BeansException {
        this.applicationContext = applicationContext;
    }

    public PrototypeBean getPrototypeBean() {
        return applicationContext.getBean(PrototypeBean.class);
    }
}
```

在SingletonAppContextBean类中实现SingletonAppContextBean接口，通过`ApplicationContext.getBean()`来每次获取PrototypeBean。这种方法能满足需求，但是不建议这样实现，这样做会导致代码和Spring框架产生严重的耦合。

## 4. lookup

```java
@Slf4j
@Component
public class SingletonLookupBean {

    public SingletonLookupBean() {
        log.info("Singleton instance created");
    }

    @Lookup
    public PrototypeBean getPrototypeBean() {
        return null;
    }
}
```

使用[@Lookup](https://www.baeldung.com/spring-lookup)注释的方法，会在spring容器中寻找PrototypeBean类并返回。

## 5. javax.inject

```java
@Slf4j
@Component
public class SingletonProviderBean {

    public SingletonProviderBean() {
        log.info("Singleton instance created");
    }

    @Autowired
    private Provider<PrototypeBean> prototypeBeanObjectFactory;

    public PrototypeBean getPrototypeBean() {
        return prototypeBeanObjectFactory.get();
    }

}
```

利用javax.inject的Provider每次get来获取不同的PrototypeBean实例

## 6. ObjectFactory

```java
@Slf4j
@Component
public class SingletonObjectFactoryBean {

    public SingletonObjectFactoryBean() {
        log.info("Singleton instance created");
    }

    @Autowired
    private ObjectFactory<PrototypeBean> prototypeBeanObjectFactory;

    public PrototypeBean getPrototypeBean() {
        return prototypeBeanObjectFactory.getObject();
    }

}
```

原理和`javax.inject API`一样,只不过利用的是Spring框架内的ObjectFactory。

## 7. Scoped Proxy

```java
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE,
    proxyMode = ScopedProxyMode.TARGET_CLASS)
public class PrototypeBean {

    private static final AtomicInteger instanceCounter = new AtomicInteger(0);

    // 每次初始化+1
    public PrototypeBean() {
        instanceCounter.incrementAndGet();
    }

    public Integer getCount() {
        return instanceCounter.get();
    }
}
```

```java
@Slf4j
@Component
public class SingletonProxyBean {

    @Autowired
    private PrototypeBean prototypeBean;

    public SingletonProxyBean() {
        log.info("Singleton instance created");
    }

    public PrototypeBean getPrototypeBean() {
        return prototypeBean;
    }
}
```

对PrototypeBean的scope设置代理`@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE,
proxyMode = ScopedProxyMode.TARGET_CLASS)`，在SingletonProxyBean类中，直接注入PrototypeBean类即可。

> proxyMode的设置：如果PrototypeBean是接口，则可以使用接口代理，设置为ScopedProxyMode.INTERFACES；如果PrototypeBean不是接口而是具体的类，那么就没法使用基于接口的代理了。此时，必须使用CGLib代理，也就是设置为ScopedProxyMode.TARGET_CLASS。

使用代理方式实现，和上面的几种方法有些不同:

1. `getPrototypeBean()`每次获取到的`PrototypeBean`对象都是一样的，因为返回的是代理对象
2. 实际调用PrototypeBean的方法时，该代理才会把执行方法委托给实际对象来执行（有可能是新生成实例，也有可能复用已有的对象）

我们通过测试类来探究以上两点不同点导致输出的结果有何不同：

```java
@Slf4j
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {ProxyConfig.class})
public class ProxyConfigTest {

    @Autowired
    private SingletonProxyBean singletonProxyBean;

    @Test
    public void Test() {
        PrototypeBean prototypeBean1 = singletonProxyBean.getPrototypeBean();
        log.info("---------"+prototypeBean1.getCount());
        // 即使调用的是同一个引用，后台也会重新生成一个实例
        log.info("---------"+prototypeBean1.getCount());

        PrototypeBean prototypeBean2 = singletonProxyBean.getPrototypeBean();
        log.info("---------"+prototypeBean2.getCount());
        log.info("---------"+prototypeBean2.getCount());

        // 两者是相等的，因为返回的是一个代理类，只有真正调用的时候才会委托给真正的使用类
        Assert.assertEquals(prototypeBean1, prototypeBean2);

        /**
         * 输出
         * 15:57:20.063 [main] INFO com.htw.study.scopetest.proxy.ProxyConfigTest - ---------1
         * 15:57:20.064 [main] INFO com.htw.study.scopetest.proxy.ProxyConfigTest - ---------2
         * 15:57:20.064 [main] INFO com.htw.study.scopetest.proxy.ProxyConfigTest - ---------3
         * 15:57:20.064 [main] INFO com.htw.study.scopetest.proxy.ProxyConfigTest - ---------4
         *
         * 这里的输出和其他几个解决方案的输出不同
         * 原因是这里返回的是代理对象，每次调用代理对象方法都会重新委派给不同的实例
         */
    }

}
```

## 8. 总结

| 方法                    | 备注                                                         |
| ----------------------- | ------------------------------------------------------------ |
| ApplicationContextAware | 和spring框架耦合，不推荐使用                                 |
| lookup                  | 比较简单                                                     |
| javax.inject            | 需要外部类依赖                                               |
| ObjectFactory           | spring框架内提供，个人喜欢该实现方式                         |
| Scoped Proxy            | 实现逻辑和其他四种不同，使用时需要注意:被代理对象**不能维护状态信息**，否则执行逻辑并不是你所期望的。[Injecting Spring Prototype bean into Singleton bean](http://dolszewski.com/spring/accessing-prototype-bean-in-singleton)一文也对该问题进行了解释，可供参考 |

> 参考文章:
>
> [Injecting Prototype Beans into a Singleton Instance in Spring](https://www.baeldung.com/spring-inject-prototype-bean-into-singleton)
>
> [How to Inject Prototype Scoped Bean in Singleton Bean](https://netjs.blogspot.com/2016/02/injecting-prototype-bean-in-singleton-spring.html)
>
> [Injecting Spring Prototype bean into Singleton bean](http://dolszewski.com/spring/accessing-prototype-bean-in-singleton)

[本文源码](https://github.com/htw0056/blog/tree/master/java/code/scope-test)