# JDBC

### JDBC编程步骤

1. 加载驱动程序

 ```
Class.forName(driverClass)
//加载MySql驱动
Class.forName("com.mysql.jdbc.Driver")
//加载postgresql驱动
Class.forName("org.postgresql.Driver")
//加载Oracle驱动
Class.forName("oracle.jdbc.driver.OracleDriver")
 ```

2. 获得数据库连接

 ```
DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/test", "root", "root");
 ```

3. 创建Statement\PreparedStatement对象

 ```
conn.createStatement();
conn.prepareStatement(sql);
 ```


### example


```java
package com.imooc.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {

	private static String URL  = "jdbc:mysql://192.168.254.133/test";
	private static String USER = "root";
	private static String PASSWORD = "root";
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		//1.加载驱动程序
		Class.forName("com.mysql.jdbc.Driver");
		
		//2.获得数据库的连接
		Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
		
		//3.通过数据库的连接操作数据库
		Statement stmt = conn.createStatement();
		
		ResultSet rs   = stmt.executeQuery("select user_name,age from imooc_goddess");
		
		while(rs.next()){
			System.out.println(rs.getString("user_name") + "," + rs.getInt("age"));
		}


		// 预处理方式
		String sql = "insert into imooc_goddess"
				+ "(user_name,sex,age,birthday,email,mobile,create_user,create_date,"
				+ "update_user,update_date,isdel) values("
				+ "?,?,?,?,?,?,?,current_date(),?,current_date(),?)";
		System.out.println(sql);
		PreparedStatement ptmt = conn.prepareStatement(sql);
		ptmt.setString(1, g.getUser_name());
		ptmt.setInt(2, g.getSex());
		ptmt.setInt(3, g.getAge());
		ptmt.setDate(4, new Date(g.getBirthday().getTime()));
		ptmt.setString(5, g.getEmail());
		ptmt.setString(6, g.getMobile());
		ptmt.setString(7, g.getCreate_user());
		ptmt.setString(8, g.getUpdate_user());
		ptmt.setInt(9, g.getIsdel());

		ptmt.execute();
	}

}
```


### 创建 JDBC 应用程序

构建一个 JDBC 应用程序包括以下六个步骤-

1. 导入数据包：需要你导入含有需要进行数据库编程的 JDBC 类的包。大多数情况下，使用 import java.sql. 就足够了。

2. 注册 JDBC 驱动器：需要你初始化一个驱动器，以便于你打开一个与数据库的通信通道。

3. 打开连接：需要使用 DriverManager.getConnection() 方法创建一个 Connection 对象，它代表与数据库的物理连接。

4. 执行查询：需要使用类型声明的对象建立并提交一个 SQL 语句到数据库。

5. 提取结果数据：要求使用适当的 ResultSet.getXXX() 方法从结果集中检索数据。

6. 清理环境：依靠 JVM 的垃圾收集来关闭所有需要明确关闭的数据库资源。

```java
/******************************************************************/
//STEP 1. Import required packages
import java.sql.*;

public class FirstExample {
   // JDBC driver name and database URL
   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
   static final String DB_URL = "jdbc:mysql://localhost/EMP";

   //  Database credentials
   static final String USER = "username";
   static final String PASS = "password";

   public static void main(String[] args) {
   Connection conn = null;
   Statement stmt = null;
   try{
      //STEP 2: Register JDBC driver
      Class.forName("com.mysql.jdbc.Driver");

      //STEP 3: Open a connection
      System.out.println("Connecting to database...");
      conn = DriverManager.getConnection(DB_URL,USER,PASS);

      //STEP 4: Execute a query
      System.out.println("Creating statement...");
      stmt = conn.createStatement();
      String sql;
      sql = "SELECT id, first, last, age FROM Employees";
      ResultSet rs = stmt.executeQuery(sql);

      //STEP 5: Extract data from result set
      while(rs.next()){
         //Retrieve by column name
         int id  = rs.getInt("id");
         int age = rs.getInt("age");
         String first = rs.getString("first");
         String last = rs.getString("last");

         //Display values
         System.out.print("ID: " + id);
         System.out.print(", Age: " + age);
         System.out.print(", First: " + first);
         System.out.println(", Last: " + last);
      }
      //STEP 6: Clean-up environment
      rs.close();
      stmt.close();
      conn.close();
   }catch(SQLException se){
      //Handle errors for JDBC
      se.printStackTrace();
   }catch(Exception e){
      //Handle errors for Class.forName
      e.printStackTrace();
   }finally{
      //finally block used to close resources
      try{
         if(stmt!=null)
            stmt.close();
      }catch(SQLException se2){
      }// nothing we can do
      try{
         if(conn!=null)
            conn.close();
      }catch(SQLException se){
         se.printStackTrace();
      }//end finally try
   }//end try
   System.out.println("Goodbye!");
}//end main
}//end FirstExample

```