# hive2.0.1 安装

## 安装环境

- 系统:centos6.5
- hadoop版本:2.5.2
- hive版本:2.0.1
- jdk版本:1.8.0_91

> 可以在`http://archive.apache.org`中找到所有历史版本的软件。


## 安装



本教程主要是为了学习安装beeline，如果只想简单直接使用hive cli，可以参考[hive0.13安装](https://github.com/htw0056/blog/blob/master/hadoop/hive/install/hive0.13_install.md).

**以下过程建立在已经安装好hadoop环境([伪分布式](https://github.com/htw0056/blog/blob/master/hadoop/hadoop/install/pseudo_distributed%20/hadoop_install_pseudo_distributed.md))，并且已经启动的基础上。**


##### 1. 下载并解压

可以从官网获得hive2.0.1版本。

```
# 解压
tar -zxvf apache-hive-2.0.1-bin.tar.gz -C /usr/local/
# 进入目录
cd /usr/local/apache-hive-2.0.1-bin/
```

##### 2. 配置mysql

可以参考[mysql安装](https://github.com/htw0056/blog/blob/master/database/mysql/install/mysql_yum_install_5.1.7.md)

> 对于hive，不要将数据库编码设置为utf-8，否则会出现问题。

在mysql cmd里执行:

```
create database hive;
create user 'hive'@'localhost' identified by 'htw';
grant all privileges on *.* to 'hive'@'localhost' with grant option;
flush privileges;
```

##### 3. 添加mysql jdbc驱动

```
cp ~/mysql-connector-java-5.1.41-bin.jar /usr/local/apache-hive-2.0.1-bin/lib/
```

##### 4. 修改配置

复制配置 

```
cd conf/
cp hive-exec-log4j2.properties.template hive-exec-log4j2.properties
cp hive-log4j2.properties.template hive-log4j2.properties
cp hive-env.sh.template hive-env.sh
```

```
vim hive-env.sh
添加:

#hadoop目录
HADOOP_HOME=/usr/local/hadoop
#hive conf目录
export HIVE_CONF_DIR=/usr/local/apache-hive-2.0.1-bin/conf
#hive lib目录
export HIVE_AUX_JARS_PATH=/usr/local/apache-hive-2.0.1-bin/lib
```


```
vim hive-site.xml
添加：

<?xml version="1.0"?>
<?xml-stylesheet type="text/xsl" href="configuration.xsl"?>
<configuration>
<property>
    <name>javax.jdo.option.ConnectionURL</name>
    <value>jdbc:mysql://localhost:3306/hive?createDatabaseIfNotExist=true</value>
    <description>JDBC connect string for a JDBC metastore</description>
</property>
<property>
    <name>javax.jdo.option.ConnectionDriverName</name>
    <value>com.mysql.jdbc.Driver</value>
    <description>Driver class name for a JDBC metastore</description>
</property>
<property>
    <name>javax.jdo.option.ConnectionUserName</name>
    <value>hive</value>
    <description>username to use against metastore database</description>
</property>
<property>
    <name>javax.jdo.option.ConnectionPassword</name>
    <value>htw</value>
</property>
<property>
    <name>hive.metastore.warehouse.dir</name>
    <value>/hive/warehouse</value>
</property>
<property>
    <name>hive.exec.scratchdir</name>
    <value>/hive/tmp</value>
</property>
<property>
    <name>hive.exec.local.scratchdir</name>
    <value>/hive/tmp</value>
</property>
<property>
    <name>hive.downloaded.resources.dir</name>
    <value>/hive/tmp</value>
</property>
<property>
  <name>hive.metastore.uris</name>
  <value>thrift://192.168.134.201:9083</value>
  </property>
</configuration>
```

##### 5. 启动服务

```
bin/schematool -dbType mysql -initSchema
bin/hive --service metastore &
bin/hive --service hiveserver2 &
```

##### 6. 连接

```
bin/beeline -u jdbc:hive2://

# or 
bin/beeline -u jdbc:hive2://192.168.134.201:10000

# or 

bin/beeline
!connect jdbc:hive2://
```

----

### 问题

##### 1. 使用`bin/beeline -u jdbc:hive2://192.168.134.201:10000`
登陆时,出现:

```
Error: Failed to open new session: java.lang.RuntimeException: org.apache.hadoop.ipc.RemoteException(org.apache.hadoop.security.authorize.AuthorizationException): User: root is not allowed to impersonate anonymous (state=,code=0)
```


解决：

在hadoop配置文件,core-site.xml中添加:

```
<property>
  <name>hadoop.proxyuser.root.hosts</name>
  <value>*</value>
 </property>
 <property>
  <name>hadoop.proxyuser.root.groups</name>
  <value>*</value>
</property>
```
重启hadoop即可。

