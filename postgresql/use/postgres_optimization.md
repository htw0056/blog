# postgres optimization

### 1. 优化查询


##### 1.1 分析查询语句EXPLAIN

EXPLAIN主要用于分析一个语句的执行规划。执行规划显示语句引用的表是如何被扫描的。

```
explain [analyze] [verbose] statement select_options;
```

##### 1.2 创建必要的索引


##### 1.3 优化子查询


适当的地方使用join来替换子查询，可以加快查询速度。


### 2. 优化数据库结构


##### 2.1 分解表

##### 2.2 中间表

##### 2.3 增加冗余字段

##### 2.4 优化插入记录的速度

1. 删除索引

   对于非空表，插入记录时，postgresql会根据表的索引对插入的记录建立索引。如果插入大量数据，建立索引会降低插入记录的速度。为了应对这种情况，可以在插入记录之前删除索引，数据插入完毕后再创建索引。
 
2. 使用批量插入

   ```
   insert into tb_name values (a,b,c),(a,b,c);
   ```
   
3. 删除外键约束

   和删除索引一样，在输入数据之前，删除对外键的约束；数据插入完成之后，再创建外键的约束。
   
4. 禁止自动提交

   在出入数据之前，禁止自动事务的自动提交，数据导入完成之后，执行回复自动提交操作。
   
5. 使用copy批量导入

##### 2.5 分析表的统计信息

```
analyze [verbose] [table [ (column[, ...]) ] ]
```
