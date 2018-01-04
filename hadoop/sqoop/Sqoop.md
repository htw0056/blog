# Sqoop



## 安装



### 1. 下载Sqoop

根据hadoop版本下载对应的[Sqoop](https://mirrors.tuna.tsinghua.edu.cn/apache/sqoop/1.4.6/)包。



### 2. 基础配置

```
# 解压tar包
tar -zxvf sqoop-1.4.6.bin__hadoop-2.0.4-alpha.tar.gz

# 配置环境变量
vim /etc/profile
添加:

# 两个变量设置为hadoop的家目录
export HADOOP_COMMON_HOME=/usr/local/hadoop
export HADOOP_MAPRED_HOME=/usr/local/hadoop

# 修改生效
source /etc/profile
```



### 3. jdbc包

根据后续使用到的数据库，下载相应的jdbc放到Sqoop/lib目录下.



### 4. 运行Sqoop

```
sqoop import --arguments...
```

