### mysql用户管理

##### 权限表

 - user
    - 用户列：host,user,password;某个主机连接某个用户对全局数据库的操作权限
     - 权限列
     - 安全列
     - 资源控制列  
 - db  
    - 用户列:host,user,db;某个主机连接某个用户对某个数据库的操作权限
     - 权限列
     - 安全列
     - 资源控制列
 - host
    - 用户列:host,db;某个主机连接的所有用户对某个数据库的操作权限
     - 权限列
     - 安全列
     - 资源控制列
 - tables_priv:表设置权限
 - columns_priv:表的某一列设置权限
 - procs_priv:存储过程和存储行数设置操作权限


##### 账户管理  


1. 创建新用户  


    1. 方式一  

   ```
   CREATE USER user_specification [,user_specification]...
   user_specification:
   user@host
   [
   IDENTIFIED BY [PASSWORD] 'password'
   | IDNTIFIED WITH auth_plugin [AS 'auth_string']
   ]
   ```


    1. 方式二  

   ```
   GRANT privileges ON db.table
   TO user@host [IDENTIFIED BY [PASSWORD] 'password']
   [WITH GRANT OPTION];
   ```


    1. 方式三  

   ```
   INSERT INTO MySQL.user(Host,User,Password,[privilegelist])
   VALUES ('host','username',PASSWORD('password'),privilegevaluelist);
   ```


2. 删除用户  

   1. 方式一  

      ```
      DROP USER user[,user];
      ```

     2.  方式二  

   ```
   DELETE FROM MySQL.user WHERE host='host' and user='username';
   ```
3. 修改密码

 4. `mysqladmin -u username -h localhost -p password 'newpwd'`
 5. `UPDATE mysql.user set password=PASSWORD('pwd') WHERE user='username' AND host='localhost'`
 6. `SET PASSWORD [FOR 'user'@'host' ] = PASSWORD('pwd');`
 7. `GRANT USAGE ON *.* TO 'someuser'@'host' IDENTIFIED BY 'somepassword'`

8. 加载权限表

 `FLUSH PRIVILEGES`

##### 权限管理

1. 授权

 ```
GRANT priv_type ON data.table
TO 'user'@'host'
[WITH GRANT OPTION]
 ```
2. 回收权限

 ```
REVOKE priv_type ON data.table
FROM 'user'@'host';
 ```
3. 查看权限

 ```
SHOW GRANTS FOR 'user'@'host';
 ```