# Elasticsearch单机版安装



### 1. 系统环境

- centos6.5
- jdk1.8
- elasticsearch-5.6.2.tar.gz


### 2. 安装步骤

##### 1. 基础环境配置

1. [jdk1.8安装](https://github.com/htw0056/blog/blob/master/java/install/jdk_linux_install.md)

2. 添加系统用户es(es服务无法使用root启动，需提前添加用户)

3. 系统配置

   ```
   # 系统配置(root状态下)
   -----------------------------------------
   vim /etc/security/limits.conf 
   添加如下内容:
   * soft nofile 65536
   * hard nofile 131072
   * soft nproc 2048
   * hard nproc 4096
   -----------------------------------------
   vim /etc/security/limits.d/90-nproc.conf 
   修改如下内容：
   * soft nproc 2048
   -----------------------------------------
   vim /etc/sysctl.conf 
   添加下面配置：
   vm.max_map_count=655360
   并执行命令：
   sysctl -p
   ```

   ​


##### 2. 下载es

下载[es安装包](https://www.elastic.co/downloads/elasticsearch)



##### 3. 安装

Elasticsearch配置:

```
# 1. 解压安装包
tar -zxvf elasticsearch-5.6.2.tar.gz
mv elasticsearch-5.6.2 elasticsearch-master

# 2. 进入目录
cd elasticsearch-master

# 3. 修改配置文件
-----------------------------------------
vim config/elasticsearch.yml 
添加如下内容：
cluster.name: htw
node.name: master
http.port: 9200
discovery.zen.ping.unicast.hosts: ["127.0.0.1"]
# 可在外网访问es
network.host: 0.0.0.0
# 后续常见错误中介绍该选项作用
bootstrap.system_call_filter: false
http.cors.enabled: true
http.cors.allow-origin: "*"
-----------------------------------------
vim config/jvm.options  
修改如下内容：
-Xms512m
-Xmx512m
```



##### 4. 启动服务

```
bin/elasticsearch
```



### 3. 常见错误

##### 问题1

```
[WARN ][o.e.b.JNANatives] unable to install syscall filter:
java.lang.UnsupportedOperationException: seccomp unavailable: requires kernel 3.5+ with CONFIG_SECCOMP and CONFIG_SECCOMP_FILTER compiled in
	at org.elasticsearch.bootstrap.SystemCallFilter.linuxImpl(SystemCallFilter.java:351) ~[elasticsearch-5.6.2.jar:5.6.2]
```

解决：

系统版本问题，换成新版的centos即可消除警报。也可以通过修改es的配置文件:`vim config/elasticsearch.yml `添加`bootstrap.system_call_filter: false`

> https://github.com/elastic/elasticsearch/issues/22899



##### 问题2

```
[1]: max file descriptors [4096] for elasticsearch process is too low, increase to at least [65536]
[2]: max number of threads [1024] for user [es] is too low, increase to at least [2048]
```

解决：

```
vim /etc/security/limits.conf 
添加如下内容:
* soft nofile 65536
* hard nofile 131072
* soft nproc 2048
* hard nproc 4096

vim /etc/security/limits.d/90-nproc.conf 
修改如下内容：
* soft nproc 2048
```



##### 问题3

```
[3]: max virtual memory areas vm.max_map_count [65530] is too low, increase to at least [262144]
```

解决：

```
vim /etc/sysctl.conf 
添加下面配置：
vm.max_map_count=655360
并执行命令：
sysctl -p
```



##### 问题4

```
[4]: system call filters failed to install; check the logs and fix your configuration or disable system call filters at your own risk
```

解决：

```
# 修改es配置文件
vim config/elasticsearch.yml
添加:
bootstrap.system_call_filter: false
```



##### 问题5

```
Java HotSpot(TM) 64-Bit Server VM warning: INFO: os::commit_memory(0x0000000085330000, 2060255232, 0) failed; error='Cannot allocate memory' (errno=12)
```

解决：

```
# 由于elasticsearch5.0默认分配jvm空间大小为2g，修改jvm空间分配
# 如果使用虚拟机安装，内存最好不小于2G
# vim config/jvm.options  
-Xms512m
-Xmx512m
```

