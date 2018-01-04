# mysql数据导入导出

参考[菜鸟教程](http://www.runoob.com/mysql/mysql-database-import.html)

***

### 使用 LOAD DATA 导入数据

```
LOAD DATA LOCAL INFILE 'dump.txt' INTO TABLE mytbl;
```

> 如果指定LOCAL关键词，则表明从客户主机上按路径读取文件。如果没有指定，则文件在服务器上按路径读取文件。
你能明确地在LOAD DATA语句中指出列值的分隔符和行尾标记，但是默认标记是定位符和换行符。
两个命令的 FIELDS 和 LINES 子句的语法是一样的。两个子句都是可选的，但是如果两个同时被指定，FIELDS 子句必须出现在 LINES 子句之前。
如果用户指定一个 FIELDS 子句，它的子句 （TERMINATED BY、[OPTIONALLY] ENCLOSED BY 和 ESCAPED BY) 也是可选的，不过，用户必须至少指定它们中的一个。


```
LOAD DATA LOCAL INFILE 'dump.txt' INTO TABLE mytbl
FIELDS TERMINATED BY ','
LINES TERMINATED BY '\n';
```

> LOAD DATA 默认情况下是按照数据文件中列的顺序插入数据的，如果数据文件中的列与插入表中的列不一致，则需要指定列的顺序。
如，在数据文件中的列顺序是 a,b,c，但在插入表的列顺序为b,c,a，则数据导入语法如下：

```
LOAD DATA LOCAL INFILE 'dump.txt' 
INTO TABLE mytbl (b, c, a);
```

### 使用 mysqlimport 导入数据

mysqlimport客户端提供了LOAD DATA INFILEQL语句的一个命令行接口。mysqlimport的大多数选项直接对应LOAD DATA INFILE子句。
从文件 dump.txt 中将数据导入到 mytbl 数据表中, 可以使用以下命令：

```
mysqlimport -u root -p --local database_name dump.txt
```

mysqlimport命令可以指定选项来设置指定格式,命令语句格式如下：

```
mysqlimport -u root -p --local --fields-terminated-by="," 
--lines-terminated-by="\n"  database_name dump.txt
```

mysqlimport 语句中使用 --columns 选项来设置列的顺序：

```
mysqlimport -u root -p --local --columns=b,c,a database_name dump.txt
```

#### mysqlimport的常用选项介绍

 选项 	        |	功能                 
--------------- | --------------
-d or --delete	|新数据导入数据表中之前删除数据数据表中的所有信息
-f or --force	|不管是否遇到错误，mysqlimport将强制继续插入数据
-i or --ignore	|mysqlimport跳过或者忽略那些有相同唯一 关键字的行， 导入文件中的数据将被忽略。
-l or -lock-tables	|数据被插入之前锁住表，这样就防止了， 你在更新数据库时，用户的查询和更新受到影响。
-r or -replace	|这个选项与－i选项的作用相反；此选项将替代 表中有相同唯一关键字的记录。
--fields-enclosed- by= char	|指定文本文件中数据的记录时以什么括起的， 很多情况下 数据以双引号括起。 默认的情况下数据是没有被字符括起的。
--fields-terminated- by=char	|指定各个数据的值之间的分隔符，在句号分隔的文件中， 分隔符是句号。您可以用此选项指定数据之间的分隔符。 默认的分隔符是跳格符（Tab）
--lines-terminated- by=str	|此选项指定文本文件中行与行之间数据的分隔字符串 或者字符。 默认的情况下mysqlimport以newline为行分隔符。 您可以选择用一个字符串来替代一个单个的字符： 一个新行或者一个回车。
mysqlimport命令常用的选项还有-v 显示版本（version）， -p 提示输入密码（password）等。


***


### 使用 SELECT ... INTO OUTFILE 语句导出数据

```
SELECT * FROM runoob_tbl 
INTO OUTFILE '/tmp/tutorials.txt';
```

#### SELECT ... INTO OUTFILE 语句有以下属性:

- LOAD DATA INFILE是SELECT ... INTO OUTFILE的逆操作，SELECT句法。为了将一个数据库的数据写入一个文件，使用SELECT ... INTO OUTFILE，为了将文件读回数据库，使用LOAD DATA INFILE。
- SELECT...INTO OUTFILE 'file_name'形式的SELECT可以把被选择的行写入一个文件中。该文件被创建到服务器主机上，因此您必须拥有FILE权限，才能使用此语法。
- 输出不能是一个已存在的文件。防止文件数据被篡改。
- 你需要有一个登陆服务器的账号来检索文件。否则 SELECT ... INTO OUTFILE 不会起任何作用。
- 在UNIX中，该文件被创建后是可读的，权限由MySQL服务器所拥有。这意味着，虽然你就可以读取该文件，但可能无法将其删除。


### 导出SQL格式的数据

`mysqldump -u user -h host -ppassword dbname [tbname [,tbname]] > file.sql`

### 导入.sql格式的数据

1. `mysql -u user -p database_name < file.sql`
2. 或登录mysql以后使用`source file.sql`

mysqldump导出数据，然后用mysql命令导入到另一台主机的数据库
`mysqldump -u root -p database_name | mysql -h other-host.com database_name`

