# Maven目录规范

## 1. Maven目录结构

maven遵循**约定优于配置**的原则，maven项目提供了默认的目录结构。一般情况下，maven项目目录结构如下（忽略了一些不常用的目录）：

```
├── src
│   ├── main			
│   │   ├── java		// 代码
│   │   ├── resources	// 资源
│   │   └── webapp		// 如果是web项目，一般有webapp目录
│   │       └── WEB-INF
│   └── test
│       ├── java		// 测试代码
│       └── resources	// 测试资源
└── target				// 构建结果存放目录
    ├── {package}		// web项目打包后目录(解压)
    │   ├── META-INF
    │   │   └── MANIFEST.MF
    │   └── WEB-INF
    │       ├── classes
    │       └── lib
    ├── classes			// 类+资源 存放目录
    └── test-classes	// 测试 类+资源 存放目录
```

## 2. src下目录与打包后目录对应关系

| maven目录               | target目录对应      | jar包内目录对应 | war包内目录对应  |
| ----------------------- | ------------------- | --------------- | ---------------- |
| src/main/java           | target/classes      | /               | /WEB-INF/classes |
| src/main/resources      | target/classes      | /               | /WEB-INF/classes |
| src/main/webapp         | -                   | war包专有       | /                |
| src/main/webapp/WEB-INF | -                   | war包专有       | /WEB-INF         |
| src/test/java           | target/test-classes | 不放入包        | 不放入包         |
| src/test/resources      | target/test-classes | 不放入包        | 不放入包         |

## 3. Spring Boot与maven目录

对于Spring Boot web项目，一般习惯将web资源也都直接放在`src/resources`目录下，它并不完全遵循maven目录规范。因此Spring Boot web项目打包后的web资源存放在`/WEB-INF/classes`目录下，而非通常情况下`根目录`。

所以，声明一个ViewResolver时，前缀目录一般设置为`WEB-INF/classes`下的目录：

```java
@Bean
public InternalResourceViewResolver viewResolver(){
    InternalResourceViewResolver vi = new InternalResourceViewResolver();
    // 因为web资源打包后放在/WEB-INF/classes下，所以这里的前缀设置填写如下
    vi.setPrefix("/WEB-INF/classes/views/"); 
    vi.setSuffix(".jsp");
    vi.setViewClass(JstlView.class);
    return vi;
}
```

