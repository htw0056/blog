# maven install

## maven安装

maven的安装很简单，下载完只需要配置环境变量即可。可参考官方的[maven安装](https://maven.apache.org/install.html)。

### 1. 前提

确保你已经正确安装了jdk，并配置好了环境变量。

### 2. 下载maven

从[官网](https://maven.apache.org/download.cgi)下载你需要的maven版本。

### 3. 解压文件

```
tar -xzvf apache-maven-3.3.9-bin.tar.gz
```

### 4. 配置环境变量

```
vim /etc/profile
# 添加如下内容
export PATH=/usr/local/maven/bin:$PATH  #根据自己的maven路径修改
```

### 5. 检验是否成功安装

```
mvn -v
```

如果显示相关信息，则表示已经成功安装了maven。



## maven settings.xml简单配置

### 1. 为用户设置单独的配置文件

```
cp maven/conf/settings.xml ~/.m2/
```

### 2. 配置maven镜像仓库(可选)

```
vim ~/.m2/settings.xml
# 找到被注释的<mirror>...</mirror>部分，从注释中提出，然后修改为

<mirror>
      <id>alimaven</id>
      <name>aliyun maven</name>
      <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
      <mirrorOf>central</mirrorOf>
</mirror>
```

### 3. 修改仓库位置

```
vim ~/.m2/settings.xml
# 找到被注释的<localRepository>/path/to/local/repo</localRepository>部分，从注释中提出，然后修改为

<localRepository>/home/htw/.m2/repository/</localRepository>
```

