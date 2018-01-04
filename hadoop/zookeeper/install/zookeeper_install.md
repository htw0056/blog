# zookeeper 安装

### 环境 

- 系统:centos6.5
- zookeeper:3.4.9
- jdk:1.8

**zookeeper运行需要java环境，所以需要提前安装好jdk。**

### 单机版

##### 1. 下载zookeeper

[zookeeper下载](http://zookeeper.apache.org/releases.html#download)

##### 2. 解压zookeeper

将zookeeper解压到/opt目录下

```
tar -zxvf zookeeper-3.4.9.tar.gz -C /opt
```


##### 3. 修改配置文件

进入到目录`/opt/zookeeper-3.4.9/conf`下进行操作。

```
cp zoo_sample.cfg zoo.cfg
vim zoo.cfg

修改(也就是将数据保存在/var/zookeeper目录下)
dataDir=/var/zookeeper
最后一行添加:
server.1=192.168.134.101:5555:5556
```


##### 4. 创建/var/zookeeper/myid

```
mkdir /var/zookeeper

在/var/zookeeper目录下创建myid(写入的数据即zoo.cfg中server后的数字)

echo 1 >> /var/zookeeper/myid
```

##### 5. 启动服务

```
/opt/zookeeper-3.4.9/bin/zkServer.sh start 
```


##### 6. 确认服务启动

```
/opt/zookeeper-3.4.9/bin/zkCli.sh
```

成功进入cli即表示安装单机版成功。


### 伪分布式


伪分布式就是在同一台机器上运行多个不同的zookeeper，也就是在单机版的基础上拷贝多个服务.

伪分布式选择3个节点模式，先配置其中一个，其余的通过复制以及修改部分配置得到。

##### 1. 下载zookeeper

[zookeeper下载](http://zookeeper.apache.org/releases.html#download)

##### 2. 解压zookeeper

将zookeeper解压到/opt目录下

```
tar -zxvf zookeeper-3.4.9.tar.gz -C /opt
cd /opt
mv zookeeper-3.4.9  zookeeper1
```


##### 3. 修改配置文件

进入到目录`/opt/zookeeper1/conf`下进行操作。

```
cp zoo_sample.cfg zoo.cfg
vim zoo.cfg

修改(也就是将数据保存在/var/zookeeper目录下)
dataDir=/var/zookeeper1
最后一行添加:
server.1=192.168.134.101:5555:5556
server.2=192.168.134.101:6555:6556
server.3=192.168.134.101:7555:7556
```

拷贝并修改zookeeper2，zookeeper3

```
cp -r zookeeper1 zookeeper2
cp -r zookeeper1 zookeeper3
```

修改zookeeper2,zookeeper3中conf下的zoo.cfg

```
修改dataDir,clientPort
```

##### 4. 创建myid文件

```
mkdir /var/zookeeper1

在/var/zookeeper目录下创建myid(写入的数据即zoo.cfg中server后的数字)

echo 1 >> /var/zookeeper/myid
```

同样操作，得到/var/zookeeper2/myid，/var/zookeeper3/myid

##### 5. 启动服务

```
/opt/zookeeper1/bin/zkServer.sh start 
/opt/zookeeper2/bin/zkServer.sh start 
/opt/zookeeper3/bin/zkServer.sh start 
```


##### 6. 确认服务启动

```
/opt/zookeeper1/bin/zkCli.sh
```

成功进入cli即表示安装成功。


### 完全分布式


完全分布式同伪分布式配置一样，只是不同的zookeeper放在不同的机器上，此次不再演示。



