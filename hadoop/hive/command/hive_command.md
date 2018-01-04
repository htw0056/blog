# hive command


### DATABASE


命令|作用
---|---
创建|
`create database t1;`|创建数据库
`create database if not exists t1;`|创建数据库，已存在忽略
`create database t1 location '/my/preferred/directory';`|创建数据库指定位置
`create database t1 comment 'comments';`|创建数据库带注释
`create database t1 with dbproperties ('creator'='htw','date'='20170301');`|创建数据库带信息
查看|
`show databases;`|显示所有数据库
`show databases like 'h.*';`|正则过滤显示数据库
`describe database t1;`|查看数据库详细信息
`describe database extended t1;`|查看数据库附带信息
切换|
`use t1;`|切换数据库
修改|
`alter database t1 set dbpropertied('update-info'='info');`|修改数据库附带信息
删除|
`drop database if exists t1;`|删除数据库
`drop database if exists t1 cascade;`|强行删除数据库


### TABLE

命令|作用
---|---
创建|
`create [external] table if not exists mydb.t1(name string comment 'name')comment 'comments' tblproperties('creator'='htw') location '/path';`|创建表(外部表)
`create [external] table mydb.t2 like mydb.t1 location '/path';`|拷贝表模式建表(外部表)
`create table p (name string) partitioned by(ds string, n bigint);`|创建分区表
查看|
`show tables [in db];`|查看数据库下所有表
`show tables like 'h.*';`|正则过滤显示数据库表
`describe [formatted | extended] t1;`|显示表结构
`show tblpropertied t1;`|查看表附带信息
`show create table t1;`|查看创建表语句
`show partitions t1 [partition (ds='1')];`|查看分区
修改数据|
`alter table t1 add partion (ds='1') [location 'hdfs://path'];`|添加分区(并指定数据位置)
`alter table t1 [partition(ds='1')] set location 'hdfs://path';`|修改表数据位置

