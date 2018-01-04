# java注解



### 利用注解生成SQL

```java
package com.htw.study.annotation;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Column {
    String value();
}
```

```java
package com.htw.study.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Table {
    String value();
}
```

```java
package com.htw.study.annotation;

@Table("user")
public class User {
    @Column("id")
    private int id;
    @Column("user_name")
    private String userName;
    @Column("nick_name")
    private String nickName;
    @Column("age")
    private int age;
    @Column("city")
    private String city;
    @Column("email")
    private String email;
    @Column("mobile")
    private String mobile;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getNickName() {
        return nickName;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
```

```java
package com.htw.study.annotation;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Main {
    public static void main(String[] args) {

        User f1 = new User();
        f1.setId(1);
        f1.setUserName("htw");
        f1.setEmail("huangtw1995@gmail.com");
        //利用注解实现自动生成sql
        String sql1 = query(f1);

        System.out.println(sql1);

    }

    private static String query(User user) {
        StringBuffer sb = new StringBuffer();
        //获取Class
        Class c = user.getClass();
        //如果没有类注解，直接返回null
        if (!c.isAnnotationPresent(Table.class)) {
            return null;
        }
        Table table = (Table) c.getAnnotation(Table.class);
        String tableName = table.value();
        sb.append("select * from ").append(tableName).append(" where 1=1");
        //获取where属性
        for (Field f : c.getDeclaredFields()) {
            //如果没有Column注释
            if (!f.isAnnotationPresent(Column.class)) {
                continue;
            }
            Column column = f.getAnnotation(Column.class);
            //获取注解的值
            String columnValue = column.value();
            //获取字段名称
            String fieldName = f.getName();
            //获取字段值
            Object fieldValue = null;

            try {
                Method method = c.getMethod("get" +
                        fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));

                fieldValue = method.invoke(user);

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            if (fieldValue == null || (fieldValue instanceof Integer && (Integer) fieldValue == 0)) {
                continue;
            }
            sb.append(" and ").append(columnValue + " = ");
            if (fieldValue instanceof String) {
                sb.append("'").append(fieldValue).append("'");
            } else if (fieldValue instanceof Integer) {
                sb.append(fieldValue);
            }
        }
        sb.append(";");
        return sb.toString();
    }
}
```

