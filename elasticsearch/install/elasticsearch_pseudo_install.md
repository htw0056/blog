# Elasticsearch伪分布式安装



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

Master:

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

Slave:

> 和master配置的主要区别:`node.name`,`http.port`

```
# 1. 解压安装包
tar -zxvf elasticsearch-5.6.2.tar.gz
mv elasticsearch-5.6.2 elasticsearch-slave

# 2. 进入目录
cd elasticsearch-slave

# 3. 修改配置文件
-----------------------------------------
vim config/elasticsearch.yml 
添加如下内容：
cluster.name: htw
node.name: slave
http.port: 8200
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
~/elasticsearch-master/bin/elasticsearch -d
~/elasticsearch-slave/bin/elasticsearch -d
```

