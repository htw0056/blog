# hive0.13安装记录

## 安装环境

- 系统:centos6.5
- hadoop版本:2.5.2
- hive版本:0.13
- jdk版本:1.8.0_91

> 可以在`http://archive.apache.org`中找到所有历史版本的软件。

## 安装

**以下过程建立在已经安装好hadoop环境([伪分布式](https://github.com/htw0056/blog/blob/master/hadoop/hadoop/install/pseudo_distributed%20/hadoop_install_pseudo_distributed.md))，并且已经启动的基础上。**

### 内嵌式安装

最简单的一种安装方式，适合初学使用。

##### 1. 解压tar包

```
tar -zxvf apache-hive-0.13.0-bin.tar.gz -C /usr/local
cd /usr/local
mv apache-hive-0.13.0-bin hive
```

##### 2. 修改/etc/profile

```
vim /etc/profile
添加或修改:

export HIVE_HOME=/usr/local/hive
export PATH=${HIVE_HOME}/bin:${PATH}
```

使修改生效:`source /etc/profile`

##### 3. 测试

直接运行命令hive,进入cli。

```
hive
```



### 本地式和远程式


本地式和远程式的区别在于你元数据存储的数据库是否在本机，所以本质上并没有什么区别。


##### 1. 安装mysql

可以参考[mysql安装](https://github.com/htw0056/blog/blob/master/database/mysql/install/mysql_yum_install_5.1.7.md)

> 对于hive，不要将数据库编码设置为utf-8，否则会出现问题。

##### 2. 对mysql基本配置

在mysql cmd里执行:

```
create database hive;
create user 'hive'@'localhost' identified by 'htw';
grant all privileges on *.* to 'hive'@'localhost' with grant option;
flush privileges;
```

在后续可以使用hive账户连接mysql。

##### 3. 导入mysql jdbc驱动

在[mysql官网](http://dev.mysql.com/downloads/connector/j/)下载对应的mysql jdbc。  
然后将jar包导入到/usr/local/hive/lib下。

##### 4. 配置hive配置文件


```
cd /usr/local/hive/conf/
vim hive-site.xml
```

添加:

```
<?xml version="1.0"?>
<?xml-stylesheet type="text/xsl" href="configuration.xsl"?>
<configuration>
	
	<property>
		<name>javax.jdo.option.ConnectionURL</name>
		<value>jdbc:mysql://localhost:3306/hive?createDatabaseIfNotExist=true</value>
	</property>

	<property>
		<name>javax.jdo.option.ConnectionDriverName</name>
		<value>com.mysql.jdbc.Driver</value>
	</property>
	
	<property>
		<name>javax.jdo.option.ConnectionUserName</name>
		<value>hive</value>
	</property>
	
	<property>
		<name>javax.jdo.option.ConnectionPassword</name>
		<value>htw</value>
	</property>
</configuration>
```

##### 5. 启动hive

```
hive
```

至此，一般来说就成功了。



#### 问题解决


###### 问题1：提示指定的账号无法登陆mysql

解决:肯定是mysql的权限问题，你需要对此赋予权限，如登陆权限和一些其他权限，这部分可以参考网上的mysql权限来解决。

###### 问题2:

```
FAILED: Execution Error, return code 1 from org.apache.hadoop.hive.ql.exec.DDLTask. MetaException(message:javax.jdo.JDODataStoreException: An exception was thrown while adding/validating class(es) : Specified key was too long; max key length is 767 bytes  
com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException: Specified key was too long; max key length is 767 bytes  
        at sun.reflect.GeneratedConstructorAccessor28.newInstance(Unknown Source)  
        at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45)  
        at java.lang.reflect.Constructor.newInstance(Constructor.java:526)  
        at com.mysql.jdbc.Util.handleNewInstance(Util.java:411)  
        at com.mysql.jdbc.Util.getInstance(Util.java:386)  
        at com.mysql.jdbc.SQLError.createSQLException(SQLError.java:1054)  
        at com.mysql.jdbc.MysqlIO.checkErrorPacket(MysqlIO.java:4120)  
        at com.mysql.jdbc.MysqlIO.checkErrorPacket(MysqlIO.java:4052)  
        at com.mysql.jdbc.MysqlIO.sendCommand(MysqlIO.java:2503)  
        at com.mysql.jdbc.MysqlIO.sqlQueryDirect(MysqlIO.java:2664)  
        at com.mysql.jdbc.ConnectionImpl.execSQL(ConnectionImpl.java:2809)  
        at com.mysql.jdbc.ConnectionImpl.execSQL(ConnectionImpl.java:2758)  
        at com.mysql.jdbc.StatementImpl.execute(StatementImpl.java:894)  
        at com.mysql.jdbc.StatementImpl.execute(StatementImpl.java:732)  
        at com.jolbox.bonecp.StatementHandle.execute(StatementHandle.java:254)  
```

由于数据字符集设定的原因导致，需要进去mysql,将hive数据库的字符集修改:

```
alter database hive character set latin1;
```

如果你在mysql中的hive数据库已经创建好，并生成了许多相应的表。此时修改数据库的字符集，并不会影响到已创建表的字符集。  
因此，你可能在创建分区表的时候再次遇到类似报错，那么需要强制修改表的字符集。
比如`alter table PARTITIONS convert to character set latin1;`



### hive Web界面安装

hive0.13中默认没有安装web界面，所以我们需要从源码包中获取。


##### 1. 下载源码包

[hive-0.13-src.tar.gz](http://archive.apache.org/dist/hive/hive-0.13.0/apache-hive-0.13.0-src.tar.gz)

##### 2. 解压tar包

```
tar -zxvf apache-hive-0.13.0-src.tar.gz
```


##### 3. 打war包

```
cd apache-hive-0.13.0-src/hwi/
# 将web目录下的文件打包
jar cvfM0 hive-hwi-0.13.0.war -C web/ .
# 拷贝到hive/lib下
cp hive-hwi-0.13.0.war /usr/local/hive/lib/
```

##### 4. 添加jdk tools包

```
cp /usr/local/java/jdk1.8.0_91/lib/tools.jar /usr/local/hive/lib/
```

##### 5. 修改hive-site.xml

添加以下内容

```
	<property>
		<name>hive.hwi.listen.host</name>
		<value>0.0.0.0</value>
	</property>

	<property>
		<name>hive.hwi.listen.port</name>
		<value>9999</value>
	</property>
	
	<property>
		<name>hive.hwi.war.file</name>
		<value>lib/hive-hwi-0.13.0.war</value>
	</property>
```

##### 6. 启动服务

```
hive --service hwi
```

##### 7. 查看结果

```
进入网页:
http://192.168.134.222:9999/hwi
```





