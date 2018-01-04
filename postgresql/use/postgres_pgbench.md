# postgres压测工具-pgbench


### pgbench描述


pgbench是在PostgreSQL上运行基准测试的简单程序。 它一遍一遍的运行SQL命令的相同序 列,可能是在多个并行的数据库会话上, 然后计算平均事务率(每秒钟事务数)。默认 的,pgbench 测试基于TPC-B的宽松的情节,每个事务包括5个SELECT、UPDATE 和INSERT命 令。不过,通过写你自己的事务脚本文件,可以很容易的测试其他情况。

系统默认的测试脚本：  

```
1. BEGIN;2. UPDATE pgbench_accounts SET abalance = abalance + :delta WHERE aid =:aid;
3. SELECT abalance FROM pgbench_accounts WHERE aid = :aid;
4. UPDATE pgbench_tellers SET tbalance = tbalance + :delta WHERE tid = :tid;
5. UPDATE pgbench_branches SET bbalance = bbalance + :delta WHERE bid =:bid;
6. INSERT INTO pgbench_history (tid, bid, aid, delta, mtime) VALUES (:tid, :bid, :aid, :delta, CURRENT_TIMESTAMP);
7. END;```

### 常用参数


#### 初始化选项

参数|作用
---|---
-i|要求调用初始化模式。
-s|乘以比例因子生成的行数。例如,-s 100将在 pgbench_accounts表中创 建10,000,000行。缺省是1。
-F|用 给 定 的 填 充 因 子 创 建pgbench_accounts、pgbench_tellers 和pgbench_branches表。缺省是100。


#### 基准选项

参数|作用
---|---
-c clients|模拟客户端的数量,也就是并发数据库会话的数量。缺省是1。
-C|为每个事务建立一个新的连接,而不是每客户端会话只执行一次。 这对于测量连接开销 是有用的。
-f filename|从filename中读取事务脚本。-N、-S、和-f是互相排斥的。
-j threads|pgbench中工作线程的数量。在多CPU的机器上使用多个线程会很有帮助。 客户端的数 量必须是线程数量的倍数,因为每个线程都有相同数量的客户端会话管理。 缺省是1。
-N|不要更新pgbench_tellers和pgbench_branches。 这将避免争用这些表,但是它使得 测试用例更不像TPC-B。
-r|在benchmark完成后报告每个命令的平均每语句延迟(从客户的角度看的执行时间)。
-t transactions|每个客户端运行的事务数量。缺省是10。
-T seconds|运行测试这么多秒,而不是每客户端固定数量的事务。-t 和-T是互相排斥的。


#### 公共选项

参数|作用
---|---
-h hostname|数据库服务器的主机名
-p port|数据库服务器的端口号
-U login|要连接的用户名


### 实例

这里采用默认的脚本测试。如果你是测试自己的脚本，那么不需要初始化数据库。你要根据官方文档里的介绍来编写测试脚本。

```
1. 初始化数据库 
pg_bench -i database_name
或者带上其他的参数，如：pg_bench -i -s 5 database_name
2. 测试
pgbench -c 50 -T 300 database_name
pgbench -c 50 -t 60  -j 10 -r database_name
```

### 建议

首先,**决不相信任何只运行几秒钟的测试**。使用-t 或-T选项使测试最少运行几分钟,以平 均噪音。 在某些情况下,你可能需要几小时才能获得可再生的数字。 尝试让测试多运行几 次是一个好的想法,以找出你的数字是否再生了。  
对于缺省的类TPC-B测试情况,**初始化比例因子(-s) 应该至少和你要测试的最大客户端数 量(-c)一样大**; 否则你大多会测量到**更新争用(也就是update wait)**。在pgbench_branches表中只有-s行, 并且每个事务都想更新它们中的一条,所以-c值大于-s时, 将会毫无疑问的导致大量事务锁 住,等待其他事务。  
**缺省的测试情节也对表被初始化了多久很敏感:表中死行的积累和死区会改变结果**。要理解结果,你必须跟踪更新的总数量和何时发生了清理。如果启用了自动清理,那么它会导致在测量的性能中有不可预知的改变。  
**pgbench的一个限制是在尝试测试大量的客户端会话时,它本身会成为瓶颈**。 这个可以通过 在不同的机器上从数据库服务器运行pgbench来减轻, 尽管少量的网络延迟是必然的。在几个客户端机器上,对同一个数据库服务器,同时运行几个pgbench实例可能也有帮助。


