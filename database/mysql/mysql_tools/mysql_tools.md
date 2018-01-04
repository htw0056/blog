# mysql实用工具

### 服务器端实用工具程序

命令|含义
---|---
mysqld|SQL后台程序（即mysql服务器进程）。改程序必须运行之后，客户端才能通过连接服务器来访问数据库
mysqld_safe|服务器启动脚本
mysql.server|服务器启动脚本。该脚本用于使用包含为特定级别的、运行启动服务的脚本的、运行目录的系统。它调用mysqld_safe来启动mysql服务器
mysql_multi|服务器启动脚本，可以启动或停止系统上安装的多个服务器
myisamchk|用来描述、检查、优化和维护MYISAM表的实用工具
mysqlbug|mysql缺陷报告脚本。它可以用来向mysql邮件系统发送缺陷报告
mysql_install_db|该脚本用默认权限创建mysql授权表。通常只是在系统上首次安装mysql时执行一次


### 客户端实用工具程序

命令|含义
---|---
myisampack|压缩myisam表以产生更小的只读表的一个工具
mysql|交互式输入sql语句或从文件以批处理模式执行它们的命令行工具
mysqlaccess|检查访问主机名、用户名和数据库组合的权限的脚本
mysqladmin|执行管理操作的客户程序
mysqlbinlog|从二进制日志读取语句的工具。在二进制日志文件中包含执行过的语句，可用来帮助系统从崩溃中恢复
mysqlcheck|检查、修复、分析以及优化表的表维护客户程序
mysqldump|讲mysql数据库转储到一个文件的客户程序
mysqlhotcopy|当服务器在运行时，快速备份myisam或isam表的工具
mysqlshow|显示数据库、表、列以及索引相关信息的客户程序
perror|像是系统或mysql错误代码含义的工具