# mysql性能优化

### 1. 查看性能参数

`SHOW STATUS LIKE 'value';`

values is one of :

- connections:连接mysql服务器的次数
- uptime:mysql服务器的上线时间
- slow_queries:慢查询的次数
- com_select:查询操作的次数
- com_insert:插入操作的次数
- com_update:更新操作的次数
- com_delete:删除操作的次数

### 2. 分析查询语句

`EXPLAIN [EXTENDED] SELECT select_options`  
or  
`DESCRIBE SELECT select_options`



