# postgresql 常用命令

## 常用控制台命令

命令|作用
---|---
\l        |          列出所有数据库。
\d                 | 列出当前数据库的所有表格。
\d [table_name]    | 列出某一张表格的结构。
\du                | 列出所有用户。
\timing				|显示sql执行时间
\?        |        查看psql命令列表。
\q        |          退出。
\h        |         查看SQL命令的解释，比如\h select。
\e                 | 打开文本编辑器。
\conninfo          | 列出当前数据库和连接的信息。
\watch [SEC]       |   每隔SEC秒执行一次查询
\password [USERNAME] |          设置密码。
\c[onnect] {[DBNAME\|- USER\|- HOST\|- PORT\|-] | conninfo} | 连接其他数据库。