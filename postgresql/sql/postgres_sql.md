# postgres SQL

### 创建表

```
create table if not exists test(
a integer ,
b text ,
c numeric,
d numeric default 100.1,--默认值
e integer DEFAULT nextval(’products_product_no_seq’),--自增
f serial, --自增
g numeric check(g > 0) ,--检查约束
h integer constraint my_check_1 check ( h > 0 ),--自命名的约束
i integer not null,--非空约束
j integer unique,--唯一约束
k integer primary key ,--主键
l integer references table2 (column_name),--外键
check ( a > 0),--表级约束
constraint my_check_2 check (a > 0 ) ,--自命名的表级约束,
unique(a,b)--表级唯一约束
)
```

### 修改表

1. 增加字段

   
   `ALTER TABLE products ADD COLUMN description text;`  
   
   `ALTER TABLE products ADD COLUMN description text CHECK (description <> '');`
   2. 删除字段

   `ALTER TABLE products DROP COLUMN description;`   
   
   `ALTER TABLE products DROP COLUMN description CASCADE;`
   	   3. 增加约束

   `ALTER TABLE products ADD CHECK (name <> '');`      
   `ALTER TABLE products ADD CONSTRAINT some_name UNIQUE (product_no);`      
   `ALTER TABLE products ADD FOREIGN KEY (product_group_id) REFERENCES product_groups;`   `ALTER TABLE products ALTER COLUMN product_no SET NOT NULL;`
   4. 删除约束   `ALTER TABLE products DROP CONSTRAINT some_name;`
   `ALTER TABLE products ALTER COLUMN product_no DROP NOT NULL;`
   5. 修改缺省值   `ALTER TABLE products ALTER COLUMN price SET DEFAULT 7.77;`
   `ALTER TABLE products ALTER COLUMN price DROP DEFAULT;`
   6. 修改字段数据类型 

   `ALTER TABLE products ALTER COLUMN price TYPE numeric(10,2);`7. 重命名字段   `ALTER TABLE products RENAME COLUMN product_no TO product_number;`
   8. 重命名表
   `ALTER TABLE products RENAME TO items;`
   
### 权限
权限一共有:  
SELECT, INSERT, UPDATE, DELETE, TRUNCATE, REFERENCES, TRIGGER, CREATE, CONNECT, TEMPORARY, EXECUTE, 和 USAGE
`GRANT UPDATE ON accounts TO joe;`
`REVOKE ALL ON accounts FROM PUBLIC;`
我们可以赋予一个“with grant option”权限,这样就允许接受权限的人将该权限转授他人。
### 模式
`CREATE SCHEMA myschema;`  
`DROP SCHEMA myschema;`
`DROP SCHEMA myschema CASCADE;`
`CREATE SCHEMA schemaname AUTHORIZATION username;`
`SHOW search_path;`
`SET search_path TO myschema,public;`
`REVOKE USAGE,CREATE ON SCHEMA public FROM PUBLIC;`

### 插入

`INSERT INTO products VALUES (1, ’Cheese’, 9.99);`

`INSERT INTO products (product_no, name) VALUES (1, ’Cheese’);`

` INSERT INTO products (product_no, name, price) VALUES(1, ’Cheese’, 9.99),(2, ’Bread’, 1.99),(3, ’Milk’, 2.99);`
### 更新
`UPDATE products SET price = 10 WHERE price = 5;`
` UPDATE products SET price = price * 1.10;`
### 删除
`DELETE FROM products WHERE price = 10;``DELETE FROM products;`
