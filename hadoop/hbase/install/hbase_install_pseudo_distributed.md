# HBase 伪分布式安装

### 系统

- 系统：centos6.5(64位)
- hadoop:2.5.2
- HBase:0.98

### 安装

> 请先自行安装好伪分布式hadoop，并启动，可参考[hadoop单机/伪分布式安装](https://github.com/htw0056/blog/blob/master/hadoop/hadoop/install/pseudo_distributed%20/hadoop_install_pseudo_distributed.md)

##### 1. 下载HBase

在官网下载[HBase](http://www.apache.org/dyn/closer.cgi/hbase/)

```
#解压到/usr/local
tar -zxvf hbase-0.98.24-hadoop2-bin.tar.gz -C /usr/local

#重命名
mv /usr/local/hbase-0.98.24-hadoop2/ /usr/local/hbase

#进入目录/usr/local/hbase,后续将在这个目录下进行操作
cd /usr/local/hbase
```



##### 2. 修改 conf/hbase-env.sh

```
#修改JAVA_HOME为真实的安装路径
export JAVA_HOME=/usr/local/java/jdk1.8.0_91
```

##### 3. 修改conf/hbase-site.xml

```
<property>
	<name>hbase.rootdir</name>
	<value>hdfs://192.168.134.201:8020/hbase</value>
</property>
<property>
	<name>hbase.zookeeper.property.dataDir</name>
	<value>/usr/local/hbase/data/zkdata</value>
</property>
<property>
	<name>hbase.cluster.distributed</name>
	<value>true</value>
</property>
```

并在/usr/local/hbase下创建目录'data/zkdata':`mkdir -p data/zkdata`


##### 4. 修改conf/regionservers

```
192.168.134.201
```


##### 5. 启动

```
bin/hbase-daemon.sh start zookeeper
bin/hbase-daemon.sh start master
bin/hbase-daemon.sh start regionserver
```

##### 6. 查看进程

```
jps
```

出现以下三个进程即表示安装成功

```
39142 HQuorumPeer
39239 HMaster
39385 HRegionServer
```



