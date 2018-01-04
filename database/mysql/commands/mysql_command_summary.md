# mysql学习总结

### SQL语言的组成部分

1. DDL(Data Defination Language):数据定义语言，主要用于定义数据库、表、视图、索引和触发器等。像DROP、CREATE、ALTER等语句  
2. DML(Data Manipulation Language):主要包括对数据的增删改。INSERT插入数据、UPDATE更新数据、DELETE删除数据。  
3. DQL(Data Query Language):数据检索语句，用来从表中获得数据，确定数据怎样在应用程序中给出。像SELECT查询数据。  
4. DCL(Data Control Language):数据控制语言，主要用于控制用户的访问权限。像GRANT、REVOKE、COMMIT、ROLLBACK等语句。  

### 安装目录简介(win)

1. bin目录，存储可执行文件
2. data目录，存储数据文件
3. include目录，存储包含的头文件
4. lib目录，存储库文件
5. docs目录，文档
6. share目录，错误消息和字符集文件
7. my.ini文件，MySQL的配置文件

### 登陆/退出MySQL

##### 登陆常用参数

1. -u 用户名
2. -p 密码
3. -h 服务器名称
4. -P 端口号
5. -D 打开指定数据库
6. --prompt=name,设置命令提示符
7. --delimiter=name,指定分隔符
8. -V,--version,输出版本信息并且退出
9. -e,执行sql语句并退出

##### 修改MySQL命令提示符

1. \D:完整的日期
2. \d:当前数据库
3. \h:服务器名称
4. \u:当前用户名

### 数据库操作(DDL)

1. 创建数据库  

 `CREATE {DATABASE | SCHEMA} [IF NOT EXISTS] db_name [[DEFAULT] CHARACTER SET [=] charset_name]`

2. 查看当前服务器下的数据库列表  

 `SHOW {DATABASES|SCHEMAS}`

3. 查看指定数据库的定义  

 `SHOW CREATE {DATABASE|SCHEMA} db_name`

4. 修改指定数据库的编码方式  

 `ALTER {DATABASE|SCHEMA} db_name [DEFAULT] CHARACTER SET [=] charset_name`

5. 打开指定数据库  

 `USE db_name`

6. 删除指定数据库  

 `DROP {DATABASE|SCHEMA} [IF EXISTS] db_name`

### 数据表相关操作

1. 创建数据表  

 ```
CREATE TABLE [IF NOT EXISTS] tbl_name(
字段名称 字段类型 [完整性约束条件]
...
)ENGINE=引擎名称 CHARSET='编码方式';
  ```

2. 完整性约束条件  

 ```
PRIMARY KEY主键
AUTO_INCREMENT自增长
FOREIGN KEY外键
NOT NULL非空
UNIQUE KEY唯一
DEFAULT默认值
 ```

3. 查看数据库下的数据表  

 `SHOW TABLES`

4. 查看指定表的表结构  

 ```
DESC tbl_name
DESCRIBE tbl_name
SHOW COLUMNS FROM tbl_name
 ```

5. 修改表名  
 
 ```
ALTER TABLE tbl_name RENAME [TO|AS] new_name
RENAME TABLE tbl_name TO new_name
 ```

6. 添加字段  

 `ALTER TABLE tbl_name ADD 字段名称 字段类型 [完整性约束条件] [FIRST|AFTER 字段名称]`

7. 删除字段  

 `ALTER TABLE tbl_name DROP 字段名称`

8. 修改字段  
 
 `ALTER TABLE tbl_name MODIFY 字段名称 字段类型 [完整性约束条件] [FIRST|AFTER 字段名称]`

9. 修改字段名称  

 `ALTER TABLE tbl_name CHANGE 旧字段名称 新字段名称 字段类型 [完整性约束条件] [FIRST|AFTER 字段名称]`

10. 添加默认值  

 `ALTER TABLE tbl_name ALTER 字段名称 SET DEFAULT 默认值`

11. 删除默认值  

 `ALTER TABLE tbl_name ALTER 字段名称 DROP DEFAULT`

12. 添加主键  

 `ALTER TABLE tbl_name ADD [CONSTRAINT [symbol]] PRIMARY KEY[index_type] (字段名称,...)`

13. 删除主键  

 `ALTER TABLE tbl_name DROP PRIMARY KEY`

14. 添加唯一  

 `ALTER TABLE tbl_name ADD [CONSTRAINT [symbol]] UNIQUE [INDEX|KEY] [索引名称](字段名称,...)`

15. 删除唯一  

 `ALTER TABLE tbl_name DROP {INDEX|KEY} index_name`

16. 修改表的存储引擎  

 `ALTER TABLE tbl_name ENGINE=存储引擎名称`

17. 设置自增长的值  

 `ALTER TABLE tbl_name AUTO_INCREMNET=值`

18. 删除数据表  

 `DROP TABLE [IF EXISTS] tbl_name[,tbl_name...]`


### 数据的操作(DML)

1. 插入数据
 
 1. 不指定具体的字段名  
  
     `INSERT [INTO] tbl_name VALUES(值...)`
 
 2. 列出指定字段  
   
     `INSERT [INTO] tbl_name(字段名称1,...) VALUES(值1,...)`
 
 3. 同时插入多条记录  
 
     `INSERT [INTO] tbl_name[(字段名称...)] VALUES(值...),(值...)...`
 
 4. 通过SET形式插入记录  
 
     `INSERT [INTO] tbl_name SET 字段名称=值,...`
 
 5. 将查询结果插入到表中  
 
     `INSERT [INTO] tbl_name[(字段名称,...)] SELECT 字段名称 FROM tbl_name [WHERE 条件]`

2. 更新数据  

 `UPDATE tbl_name SET 字段名称=值,... [WHERE 条件][ORDER BY 字段名称][LIMIT 限制条数]`

3. 删除数据  

 `DELETE FROM tbl_name [WHERE 条件][ORDER BY 字段名称][LIMIT 限制条数]`

4. 彻底清空数据表  

 `TRUNCATE [TABLE] tbl_name`

### 查询数据操作(DQL)

1. 查询记录  
 
 ```
 SELECT select_expr [, select_expr ...]  
 [
 FROM table_references 
 [WHERE 条件]
 [GROUP BY {col_name | position}  [ASC | DESC], ... 分组]
 [HAVING 条件 对分组结果进行二次筛选]
 [ORDER BY {col_name | position} [ASC | DESC], ...排序]
 [LIMIT 限制显示条数]
 ]
  ```

### MySQL连接查询
1. 内连接查询  

 `JOIN|CROSS JOIN INNER JOIN`  
 通过ON 连接条件  
 显示两个表中符合连接条件的记录

2. 左外连接  

 `LEFT [OUTER] JOIN`  
 显示左表的全部记录及右表符合连接条件的记录

3. 右外连接  

 `RIGHT [OUTER] JOIN`  
 显示右表的全部记录以及左表符合条件的记录

4. 联合查询  
 
 ```
UNION
UNION ALL
 ```  
 UNION和UNION ALL 区别是UNION去掉相同记录，UNION ALL 是简单的合并到一起。

5. 外键  

 外键是表的一个特殊字段。被参照的表是主表，外键所在字段的表为子表。设置外键的原则需要记住，就是依赖于数据库中已存在的表的主键。外键的作用是建立该表与其父表的关联关系。父表中对记录做操作时，子表中与之对应的信息也应有相应的改变。  
 外键的作用保持数据的一致性和完整性,可以实现一对一或一对多的关系  
 **注意**
 1. 父表和子表必须使用相同的存储引擎，而且禁止使用临时表。
 2. 数据表的存储引擎只能为InnoDB。
 3. 外键列和参照列必须具有相似的数据类型。其中数字的长度或是否有符号位必须相同；而字符的长度则可以不同。
 4. 外键列和参照列必须创建索引。如果外键列不存在索引的话，MySQL将自动创建索引。
 5. 外键约束的参照操作

     - CASCADE：从父表删除或更新且自动删除或更新子表中匹配的行。
     - SET NULL：从父表删除或更新行，并设置子表中的外键列为NULL。如果使用该选项，必须保证子表列没有指定NOT NULL。
     - RESTRICT：拒绝对父表的删除或更新操作。
     - NO ACTION：标准SQL的关键字，在MySQL中与RESTRICT相同。


### 查看MySQL的存储引擎

1. 查看MySQL支持的存储引擎  

 `SHOW ENGINES`

2. 查看显示支持的存储引擎信息  

 `SHOW VARIABLES LIKE 'have%'`

3. 查看默认的存储引擎  
 
 `SHOW VARIABLES LIKE 'storage_engine'`

### 索引

1. 索引的分类  

 - 普通索引
 - 唯一索引
 - 全文索引
 - 单列索引
 - 多列索引
 - 空间索引

2. 创建索引  

 1. 创建表的时候创建索引  
 
 	```
CREATE TABLE tbl_name(
字段名称 字段类型 [完整性约束条件],
…,
[UNIQUE|FULLTEXT|SPATIAL] INDEX|KEY [索引名称](字段名称[(长度)] [ASC|DESC])
);
 	```

 2. 在已经存在的表上创建索引  
 
 	```
CREATE [UNIQUE|FULLTEXT|SPATIAL] INDEX 索引名称 
ON 表名 (字段名称[(长度)] [ASC|DESC]);

ALTER TABLE tbl_name ADD [UNIQUE|FULLTEXT|SPATIAL] 
INDEX 索引名称(字段名称[(长度)] [ASC|DESC]);
 	```

3. 删除索引  

 ```
DROP INDEX 索引名称 ON tbl_name

ALTER TABLE table_name DROP INDEX index_name
 ```


### 存储过程和函数

1. 创建存储过程  

 ```
CREATE PROCEDURE sp_name( [proc_parameter] )
[characteristics ...] routine_body
 ```
 proc_parameter:[IN | OUT | INOUT] param_name type

2. 创建存储函数  

 ```
CREATE FUNCTION func_name( [func_parameter])
RETURNS type
[characteristic ...] routine_body
 ```

3. 定义变量  

 `DECLARE var_name [,varname]... data_type [DEFAULT value];`

4. 调用存储过程  

 ` CALL sp_name([proc_parameter] )`

5. 查看存储过程和函数的状态  

 `SHOW {PROCEDURE | FUNCTION} STATUS [LIKE 'pattern']`

6. 查看存储过程和函数的定义  

 `SHOW CREATE {PROCDURE | FUNCTION} sp_name;`

7. 修改存储过程和函数  

 `ALTER {PROCEDURE | FUNCTION} sp_name [characteristic ...]`

8. 删除存储过程和函数  

 `DROP {PROCEDURE | FUNCTION} [IF EXISTS] sp_name`


### 视图

1. 创建视图  

 ```
CREATE [OR REPLACE] [ALGORITHM = {UNDEFINED | MERGE | TEMPTABLE}]
VIEW view_name [(column_list)]
AS SELECT_statement
[WITH [CASADED|LOCAL] CHECK POTION]
 ```

2. 查看视图  

 1. 查看数据基本信息  
 	
 	`DESCRIBE view_name`
 	`SHOW TABLE STATUS LIKE 'view_name'`

 2. 查看视图详细信息  

 	`SELECT * FROM information_schema.views;` 

3. 修改视图  

 1. 方法一  

 	```
CREATE [OR REPLACE] [ALGORITHM = {UNDEFINED | MERGE | TEMPTABLE}]
VIEW view_name [(column_list)]
AS SELECT_statement
[WITH [CASADED | LOCAL] CHECK POTION]
 	```

 2. 方法二  

 	```
ALTER [ALGORITHM = {UNDEFINED | MERGE | TEMPTABLE}]
VIEW view_name [(column_list)]
AS SELECT_statement
[WITH [CASADED | LOCAL] CHECK POTION]
 	```

4. 更新视图  

 `同table更新`

5. 删除视图

 ```
DROP VIEW [IF EXISTS]
view_name [, view_name]...
[RESTRICT | CASCADE]
 ```

### 触发器

1. 创建触发器  

 ```
CREATE TRIGGER trigger_name trigger_time trigger_vent
ON table_name FOR EACH ROW trigger_stmt
 ```

2. 查看触发器  

 1. `SHOW TRIGGERS`
 2. `SELECT * FROM INFORMATION_SCHEMA.TRIGGERS WHERE condition`  
 
3. 删除触发器  

 `DROP TRIGGER [schema_name.]trigger_name`



 
