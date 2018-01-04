### postgresql数据导入和导出

### 导出数据

##### 命令pg_dump

格式:pg_dump [option...] [dbname]  
以下只列出一部分参数：


option|explain
---|---
dbname|数据库名称
-h|数据库服务器的主机名
-p|数据库服务器的端口号  
-U|指定数据库连接的用户
-W|指定密码
-f|把输出发往指定的文件
-t|备份相匹配table的表


 
##### example

- `pg_dump -h 192.168.52.101 -U postgres -W -f d:/a1.txt data_name`

 - 主机:192.168.52.101
 - 用户:postgres
 - 输出文件位置:d:/a1.txt
 - 备份数据库名称:data_name
 - 备份了该数据库下所有的表

- `pg_dump -h 192.168.52.101 -U postgres -t table1 -t table2 -f d:/a2.txt data_name`   

 - 主机:192.168.52.101
 - 用户:postgres
 - 指定备份的表格:table1,table2
 - 输出文件位置:d:/a2.txt
 - 备份数据库名称:data_name





### 数据导入

- `psql -h 192.168.52.101 -d data_name -U postgres -f d:/a3.txt`

 - 主机:192.168.52.101
 - 指定还原的数据库:data_name
 - 用户:postgres
 - 导入的文件位置:d:/a3.txt


### COPY命令

```
COPY table_name [ ( column_name [, ...] ) ]
FROM { 'filename' | STDIN }
[ [ WITH ] ( option [, ...] ) ]
```
```
COPY { table_name [ ( column_name [, ...] ) ] | ( query ) }
TO { 'filename' | STDOUT }
[ [ WITH ] ( option [, ...] ) ]
```

where option can be one of:

```
FORMAT format_name
OIDS [ boolean ]
DELIMITER 'delimiter_character'
NULL 'null_string'
HEADER [ boolean ]
QUOTE 'quote_character'
ESCAPE 'escape_character'
FORCE_QUOTE { ( column_name [, ...] ) | * }
FORCE_NOT_NULL ( column_name [, ...] )
ENCODING 'encoding_name'
```

##### example

1. `copy (select * from t_ab) to '/tmp/a.tt' `
2. `copy t_ab from '/tmp/a.tt' `
3. `copy (select * from t_ab) to '/tmp/a.tt' csv`
4. `copy t_ab from '/tmp/a.tt' csv`
5. `copy (select * from t_ab) to '/tmp/a.tt' DELIMITER '@'`
6. `copy t_ab from '/tmp/a.tt' DELIMITER '@'`