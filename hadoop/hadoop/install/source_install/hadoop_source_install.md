# 源码编译HADOOP

## 环境

- 系统:centos6.5(64位)
- Hadoop:[2.7.1](http://archive.apache.org/dist/hadoop/common/hadoop-2.7.1/hadoop-2.7.1-src.tar.gz)
- JDK:1.7(1.8版本太高会有问题)


## 编译安装

根据文档显示，编译安装的要求:

* Unix System
* JDK 1.7+
* Maven 3.0 or later
* Findbugs 1.3.9 (if running findbugs)
* ProtocolBuffer 2.5.0
* CMake 2.6 or newer (if compiling native code), must be 3.0 or newer on Mac
* Zlib devel (if compiling native code)
* openssl devel ( if compiling native hadoop-pipes and to get the best HDFS encryption performance )
* Jansson C XML parsing library ( if compiling libwebhdfs )
* Linux FUSE (Filesystem in Userspace) version 2.6 or above ( if compiling fuse_dfs )
* Internet connection for first build (to fetch all Maven and Hadoop dependencies)

根据以上要求，我们逐步进行安装。

##### 1. maven和Findbugs

maven和Findbugs只需要解压，然后设置环境变量即可。

##### 2. cmake等安装

```
yum -y install autoconf automake libtool cmake ncurses-devel openssl-devel lzo-devel zlib-devel gcc gcc-c++
```

##### 3. ProtocolBuffer

解压ProtocolBuffer，然后进行`configure,make,make install`安装即可。


##### 4. 编译hadoop

```
mvn package -Pdist,native -DskipTests -Dtar
```

##### 5. 完成

因为网络问题，编译了一天也没有成功，最终编译完成应该是可以得到一个hadoop二进制包。


