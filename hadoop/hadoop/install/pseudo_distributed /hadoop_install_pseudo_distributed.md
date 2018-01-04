# hadoop单机/伪分布式安装

## 前言

首先推荐一篇好文章[Hadoop安装教程_单机/伪分布式配置_CentOS6.4/Hadoop2.6.0](http://www.powerxing.com/install-hadoop-in-centos/)，安装过程讲述的十分详细。

[官方文档](http://hadoop.apache.org/docs/r2.5.2/hadoop-project-dist/hadoop-common/SingleCluster.html.)也是必须要参考的。


其次,本文不是一个合格的安装教程，安装过程中有许多细节问题没有展开。如果你还是linux初学者，请参考上面推荐的教程来安装。本文只是记录自己的安装过程，用于学习。

## 安装环境

- 系统:centos6.5(64位)  
- hadoop版本:2.5.2  
- jdk版本:1.8.0_91  




## 安装过程

此次安装完全从头开始，即从虚拟机安装linux开始.

##### 1. 虚拟机安装linux

这个部分请参考网上的教程。

##### 2. linux基础配置

基本配置包括:设置静态ip，关闭selinux，关闭iptables。  
请参考[linux 网络配置](https://github.com/htw0056/blog/blob/master/linux/software/network_config.md).

##### 3. jdk安装

请参考[linux下安装JDK](https://github.com/htw0056/blog/blob/master/java/jdk/jdk_linux_install.md)

##### 4. ssh免密码登录

请参考[SSH](https://github.com/htw0056/blog/blob/master/linux/linux_operation/linux_ssh.md)

##### 5. 修改主机名

```
vim /etc/sysconfig/network
修改:

HOSTNAME=hadoop
```

##### 6. 设置hosts

```
vim /etc/hosts
添加：  

192.168.134.101 hadoop		#ip 主机名
```

如果host没设置，你会在运行时碰到`java.net.UnknownHostException`的错误。


##### 7. hadoop安装

假设已经下载hadoop2.5.2.tar.gz到本地家目录。

```
cd ~
# 解压到/usr/local
tar -zxvf hadoop-2.5.2.tar.gz -C /usr/local 	
# 重命名
mv /usr/local/hadoop-2.5.2/ /usr/local/hadoop	
cd /usr/local/hadoop/
```

然后修改PATH:

```
vim /etc/profile
添加修改:

export HADOOP_HOME=/usr/local/hadoop
export PATH=${HADOOP_HOME}/bin:${HADOOP_HOME}/sbin:${JAVA_HOME}/bin:${PATH}
```

使修改生效`source /etc/profile`



### 单机版

Hadoop 默认模式为非分布式模式，无需进行其他配置即可运行。非分布式即单 Java 进程，方便进行调试。


```
cd /usr/local/hadoop
mkdir input
cp etc/hadoop/*.xml input
bin/hadoop jar share/hadoop/mapreduce/hadoop-mapreduce-examples-2.5.2.jar grep input output 'dfs[a-z.]+'
cat output/*
```

##### 错误1：java.net.UnknownHostException

> 如果遇到这个问题，你需要修改/etc/hosts文件。  
>
> 请参考上文中的操作**设置hosts**。 

##### 错误2：WARN util.NativeCodeLoader: Unable to load native-hadoop library for your platform... using builtin-java classes where applicable

> 这个问题并不影响运行，如需了解可自行google



### 伪分布式

为了保持一致，如果你已经完成了单机版测试，建议先把生成的input和output文件删除。`rm -rf input output`


以下命令都在`/usr/local/hadoop`目录下执行(如果你根据上面已经设置好了PATH，其实就可以直接使用命令了)。

##### 1. 修改etc/hadoop/hadoop-env.sh


```
export JAVA_HOME=/usr/local/java/jdk1.8.0_91
export HADOOP_PREFIX=/usr/local/hadoop
```

如果这里的JAVA_HOME不修改，运行时是会报错的。

##### 2. 修改etc/hadoop/core-site.xml

```
<configuration>
    <property>
        <name>fs.defaultFS</name>
        <value>hdfs://hadoop:8020</value>
    </property>
    <property>
        <name>hadoop.tmp.dir</name>
        <value>/usr/local/hadoop/tmp</value>
    </property>
</configuration>
```

##### 3. 修改etc/hadoop/hdfs-site.xml

```
<configuration>
    <property>
        <name>dfs.replication</name>
        <value>1</value>
    </property>
    <property>
        <name>dfs.namenode.name.dir</name>
        <value>/usr/local/hadoop/tmp/dfs/name</value>
    </property>
    <property>
        <name>dfs.datanode.data.dir</name>
        <value>/usr/local/hadoop/tmp/dfs/data</value>
    </property>
</configuration>
```

##### 4. namenode格式化


```
bin/hdfs namenode -format
```

如果多次重复格式化或者你的hadoop指定的data目录为默认路径(/tmp)。在某些情况下你会发现启动的时候有部分node无法启动，此时将data目录(即本文中的/usr/local/hadoop/tmp)删除后重新启动hadoop即可。

##### 5. 开启 NameNode 和 DataNode 守护进程

```
sbin/start-dfs.sh
```

##### 6. 检查是否成功开启

```
jps
```

应该可以看到以下内容：

```
24688 Jps
24320 DataNode
24492 SecondaryNameNode
24237 NameNode
```

三个node只要缺少一个，就说明出现了问题。

##### 7. 浏览器访问

http://hadoop:50070

如果从虚拟机外部访问，遇到无法连接的问题。可能是由于没关闭iptables或者selinux。请关闭后再重试。

##### 8. 伪分布式运行实例


```
bin/hdfs dfs -mkdir /user
bin/hdfs dfs -mkdir /user/<username>	#你当前用户的名字
bin/hdfs dfs -put etc/hadoop input
bin/hadoop jar share/hadoop/mapreduce/hadoop-mapreduce-examples-2.5.2.jar grep input output 'dfs[a-z.]+'

bin/hdfs dfs -get output output 		#获取数据到本地文件系统
cat output/*							#在本地查看

or

bin/hdfs dfs -cat output/*				#直接在hdfs查看
```



### 启动yarn

##### 1. 修改etc/hadoop/mapred-site.xml.template

```
mv etc/hadoop/mapred-site.xml.template etc/hadoop/mapred-site.xml

修改内容
<configuration>
    <property>
        <name>mapreduce.framework.name</name>
        <value>yarn</value>
    </property>
</configuration>
```

##### 2. 修改etc/hadoop/yarn-site.xml

```
<configuration>
    <property>
        <name>yarn.nodemanager.aux-services</name>
        <value>mapreduce_shuffle</value>
    </property>
</configuration>
```

##### 3. 启动yarn

```
sbin/start-yarn.sh      #启动YARN
sbin/mr-jobhistory-daemon.sh start historyserver  #开启历史服务器，才能在Web中查看任务运行
```

**注意:**

> 不启动 YARN 需重命名 mapred-site.xml  
> 如果不想启动 YARN，务必把配置文件 mapred-site.xml 重命名，改成 mapred-site.xml.template，需要用时改回来就行。否则在该配置文件存在，而未开启 YARN 的情况下，运行程序会提示 “Retrying connect to server: 0.0.0.0/0.0.0.0:8032” 的错误，这也是为何该配置文件初始文件名为 mapred-site.xml.template。

**注意:**

有一个问题需要注意，之前在未开启yarn时，mr都是能正常执行的。但是开启yarn之后，一直出现这个问题

```
org.apache.hadoop.util.Shell$ExitCodeException:
        at org.apache.hadoop.util.Shell.runCommand(Shell.java:511)
        at org.apache.hadoop.util.Shell.run(Shell.java:424)
        at org.apache.hadoop.util.Shell$ShellCommandExecutor.execute(Shell.java:656)
        at org.apache.hadoop.yarn.server.nodemanager.DefaultContainerExecutor.launchContainer(DefaultContainerExecutor.java:195)
        at org.apache.hadoop.yarn.server.nodemanager.containermanager.launcher.ContainerLaunch.call(ContainerLaunch.java:300)
        at org.apache.hadoop.yarn.server.nodemanager.containermanager.launcher.ContainerLaunch.call(ContainerLaunch.java:81)
        at java.util.concurrent.FutureTask.run(FutureTask.java:262)
        at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1145)
        at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:615)
        at java.lang.Thread.run(Thread.java:745)
```

并且yarn进程会挂掉(yarn挂掉之后又会报一直在重连的提示)，或者系统直接崩溃。折腾了很久，网上也有类似问题的，但都是配置不对造成的，他们的方法并不能解决我的问题。研究了两天，终于发现问题是虚拟机内存太小！！将系统内存从1G改大到3g多，这问题就解决了！！

所以，如果你有足够的资源的话，请将内存调大或者将swap空间多分配一些！

##### 4. 检查是否成功开启yarn

```
jps
```
应该可以看到以下内容：

```
24320 DataNode
25189 ResourceManager
25590 JobHistoryServer
25623 Jps
25275 NodeManager
24492 SecondaryNameNode
24237 NameNode
```

##### 5. 浏览器访问

http://hadoop:8088

##### 6. 关闭

```
stop-yarn.sh
mr-jobhistory-daemon.sh stop historyserver
```
