# mysql日志

## 日志种类

1. 二进制日志：记录所有更改数据的语句，可以用于数据复制
2. 错误日志：记录mysql服务的启动、运行或停止mysql服务时出现的问题
3. 查询日志：记录建立的客户端连接和执行的语句
4. 慢查询日志：记录所有执行时间超过long_query_time的所有查询或不适用索引的查询  


## 1. 二进制日志


#### 1. 设置二进制日志

在my.ini中：
```
[mysqld]
log-bin [=path/[filename]]
expire_logs_days = 10
max_binlog_size = 100M
```


#### 2. 查看二进制日志

1. 查看二进制日志文件个数以及文件名

 `SHOW BINARY LOGS;`

2. 查看二进制文件内容

 `mysqlbinlog /tmp/log/binlog.000001`

3. 查看日志设置

 `SHOW VARIABLES LIKE 'log_%';`

#### 3. 删除二进制日志

1. 删除所有二进制日志

 `RESET MASTER;`

2. 删除比指定文件名编号小的所有二进制日志

 `PURGE {MASTER|BINARY} LOGS TO 'log_name';`

3. 删除指定日期以前的所有二进制日志

 `PURGE {MASTER|BINARY} LOGS BEFORE 'data';`

#### 4. 恢复数据库

`mysqlbinlog [option] filename | mysql -uuser -ppwd`  
option:  

- --start-date     |   --stop-date
- --start-position |   --stop-position

#### 5. 暂时停止或开启二进制日志

`SET sql_log_bin = {0|1};`

## 2. 错误日志

#### 设置错误日志

```
[mysqld]
log-error [= path/[file_name]]
```

#### 删除错误日之后重新刷新生成错误日志

`FLUSH LOGS`

## 3. 通用查询日志


#### 设置通用查询日志

```
[mysqld]
log [= path/[file_name]]
```

## 4. 慢查询日志


#### 设置通用查询日志

```
[mysqld]
log-slow-queries [= path/[file_name]]
long_query_time=n
```

