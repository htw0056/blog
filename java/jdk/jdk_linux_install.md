# linux下安装JDK

#### 1.检验系统原版本


```
[root@admin ~]# java -version
java version "1.6.0_24"
OpenJDK Runtime Environment (IcedTea6 1.11.1) (rhel-1.45.1.11.1.el6-x86_64)
OpenJDK 64-Bit Server VM (build 20.0-b12, mixed mode)
```  
如果正常显示结果，说明你的linux已经安装了java环境

```
[root@admin ~]# rpm -qa | grep java
tzdata-java-2012c-1.el6.noarch
java-1.6.0-openjdk-1.6.0.0-1.45.1.11.1.el6.x86_64
```

卸载OpenJDK:

```
[root@admin ~]# rpm -e --nodeps tzdata-java-2012c-1.el6.noarch
[root@admin ~]# rpm -e --nodeps java-1.6.0-openjdk-1.6.0.0-1.45.1.11.1.el6.x86_64
```

#### 2.下载JDK包

```
wget --no-cookies --no-check-certificate --header "Cookie: gpw_e24=http%3A%2F%2Fwww.oracle.com%2F; oraclelicense=accept-securebackup-cookie" http://download.oracle.com/otn-pub/java/jdk/8u91-b14/jdk-8u91-linux-x64.tar.gz
```
因为oracle上面在下载页面还要选择同意协议，但是在Linux里面无法选择，在网上经过多方查找，最后就找到这个是可以用的  

#### 3.安装JDK

```
[root@admin ~]# mkdir /usr/local/java
[root@admin ~]# cp jdk-8u91-linux-x64.tar.gz /usr/local/java/
[root@admin ~]# cd /usr/local/java
[root@admin ~]# tar -zxvf jdk-8u91-linux-x64.tar.gz
```

#### 4.配置环境变量

```
[root@admin ~]# vim /etc/profile
 
在最后一行加入:
export JAVA_HOME=/usr/local/java/jdk1.8.0_91
export PATH=${JAVA_HOME}/bin:${PATH}
export CLASSPATH=.:${JAVA_HOME}/lib/dt.jar:${JAVA_HOME}/lib/tools.jar

[root@admin ~]# source /etc/profile
```

#### 5.检验是否安装成功

```
[root@VM_25_225_centos ~]# java -version
java version "1.8.0_91"
Java(TM) SE Runtime Environment (build 1.8.0_91-b14)
Java HotSpot(TM) 64-Bit Server VM (build 25.91-b14, mixed mode)
```
