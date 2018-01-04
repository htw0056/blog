# hadoop分布式安装(基础版)

继续推荐好文章[Hadoop集群安装配置教程_Hadoop2.6.0_Ubuntu/CentOS](http://www.powerxing.com/install-hadoop-cluster/)。

参考[官方文档](http://hadoop.apache.org/docs/r2.5.2/hadoop-project-dist/hadoop-common/ClusterSetup.html)。

## 安装环境

- 系统:centos6.5(64位)  
- hadoop版本:2.5.2  
- jdk版本:1.8.0_91  

## 简易流程

1. 选定一台机器作为 Master
2. 在 Master 节点上进行基础配置配置:安装 Java 环境,ssh无密码登陆,修改hosts
3. 在 Master 节点上安装 Hadoop，并完成配置
4. 在其他 Slave 节点上进行基础配置配置:安装 Java 环境,ssh无密码登陆,修改hosts
5. 将 Master 节点上的 /usr/local/hadoop 目录复制到其他 Slave 节点上
6. 在 Master 节点上开启 Hadoop


## 安装过程

此次搭建集群的节点只有两个,一个master,一个slave。

```
master	ip:192.168.134.201
slave	ip:192.168.134.202
```

### 基础配置 

在master和slave的节点上进行基础配置，无特别说明，全部节点都要配置。


##### 1. 虚拟机安装linux

这个部分请参考网上的教程。

##### 2. linux基础配置

基本配置包括:设置静态ip，关闭selinux，关闭iptables。  
请参考[linux 网络配置](https://github.com/htw0056/blog/blob/master/linux/software/network_config.md).

##### 3. jdk安装

请参考[linux下安装JDK](https://github.com/htw0056/blog/blob/master/java/jdk/jdk_linux_install.md)

##### 4. ssh免密码登录

请参考[SSH](https://github.com/htw0056/blog/blob/master/linux/linux_operation/linux_ssh.md)

因为是集群安装，所以你需要实现master到各个slave节点的免密码登录。  
在master节点进行操作:

```
ssh-keygen			#生成密钥对
cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys	#对本机实现无密码登录
ssh root@192.168.134.202 'mkdir -p .ssh && cat >> .ssh/authorized_keys' < ~/.ssh/id_rsa.pub	#对slave实现无密码登录
```

##### 5. 设置hosts

```
vim /etc/hosts
添加：

192.168.134.201 master
192.168.134.202 slave
```

可以使用ping命令，检查是否设置成功。

##### 6. hadoop安装

**只在master下操作，配置完之后复制到各个节点即可**  

假设已经下载hadoop2.5.2.tar.gz到本地家目录。

```
cd ~
tar -zxvf hadoop-2.5.2.tar.gz -C /usr/local 	#解压到/usr/local
mv /usr/local/hadoop-2.5.2/ /usr/local/hadoop	#重命名
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


### 集群安装

**只在master下操作，配置完之后复制到各个节点即可**   

配置文件都在/usr/local/hadoop/etc/hadoop下。

##### 1. 修改文件 hadoop-env.sh

```
export JAVA_HOME=/usr/local/java/jdk1.8.0_91
export HADOOP_PREFIX=/usr/local/hadoop
```

##### 2. 修改文件 slaves

文件 slaves，将作为 DataNode 的主机名写入该文件，每行一个，默认为 localhost，所以在伪分布式配置时，节点即作为 NameNode 也作为 DataNode。分布式配置可以保留 localhost，也可以删掉，让 Master 节点仅作为 NameNode 使用。

本教程让 Master 节点仅作为 NameNode 使用，因此将文件中原来的 localhost 删除，只添加一行内容：slave。

```
slave
```

##### 3. 修改文件 core-site.xml

```
<configuration>
        <property>
                <name>fs.defaultFS</name>
                <value>hdfs://master:8020</value>
        </property>
        <property>
                <name>hadoop.tmp.dir</name>
                <value>/usr/local/hadoop/tmp</value>
        </property>
</configuration>
```
##### 4. 修改文件 hdfs-site.xml

dfs.replication 一般设为 3，但我们只有一个 Slave 节点，所以 dfs.replication 的值还是设为 1


```
<configuration>
		<property>
                <name>dfs.replication</name>
                <value>1</value>
        </property>
        <property>
                <name>dfs.namenode.secondary.http-address</name>
                <value>master:50090</value>
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

##### 5. 修改文件 mapred-site.xml

可能需要先重命名，默认文件名为 mapred-site.xml.template

```
<configuration>
        <property>
                <name>mapreduce.framework.name</name>
                <value>yarn</value>
        </property>
        <property>
                <name>mapreduce.jobhistory.address</name>
                <value>master:10020</value>
        </property>
        <property>
                <name>mapreduce.jobhistory.webapp.address</name>
                <value>master:19888</value>
        </property>
</configuration>
```

##### 6. 修改文件 yarn-site.xml

```
<configuration>
        <property>
                <name>yarn.nodemanager.aux-services</name>
                <value>mapreduce_shuffle</value>
        </property>
        <property>
                <name>yarn.resourcemanager.hostname</name>
                <value>master</value>
        </property>        
</configuration>
```

到此，hadoop基本设置已经完成。

##### 7. 子节点安装hadoop

将设置完的文件打包一份，并发送到各个子节点上,并解压到/usr/local下。

### 集群启动

在master节点进行操作:

##### 1. 格式化文件系统

```
hdfs namenode -format       # 首次运行需要执行初始化，之后不需要
```

##### 2. 启动服务

```
start-dfs.sh
start-yarn.sh
mr-jobhistory-daemon.sh start historyserver
```

##### 3. 查看服务是否成功启动

分别在master和slave节点执行`jps`:

```
在master节点可以看到
29794 JobHistoryServer
29925 Jps
29527 ResourceManager
29383 SecondaryNameNode
29211 NameNode

在slave节点可以看到
8530 Jps
4725 DataNode
4830 NodeManager
```

另外还需要在 Master 节点上通过命令 `hdfs dfsadmin -report`查看 DataNode 是否正常启动，如果 Live datanodes 不为 0 ，则说明集群启动成功。



### 执行分布式实例

在master节点操作.

```
hdfs dfs -mkdir -p /user/root
hdfs dfs -mkdir input
hdfs dfs -put etc/hadoop/ input
hadoop jar share/hadoop/mapreduce/hadoop-mapreduce-examples-2.5.2.jar grep input output 'dfs[a-z.]+'
hdfs dfs -cat output/*			#查看结果
```

### 关闭

```
stop-yarn.sh
stop-dfs.sh
mr-jobhistory-daemon.sh stop historyserver
```

