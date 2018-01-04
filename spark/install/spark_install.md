# Spark安装

> 安装参考:[Spark快速入门指南 – Spark安装与基础使用](http://www.powerxing.com/spark-quick-start-guide/)

## 环境

- Centos 6.5
- Hadoop 2.7.1
- Java JDK 1.8
- Spark 2.2.0



## 安装

> 需提前安装Hadoop,可参考[Hadoop伪分布式安装](https://github.com/htw0056/blog/blob/master/hadoop/hadoop/install/pseudo_distributed%20/hadoop_install_pseudo_distributed.md)



### 1. 下载Spark

从[官网](http://spark.apache.org/downloads.html)下载Spark,由于本机Hadoop版本是2.7，因此选择下载`spark-2.2.0-bin-hadoop2.7.tgz`



### 2. 配置Spark

```
# 解压spark
tar -zxvf spark-2.2.0-bin-hadoop2.7.tgz -C /usr/local/
cd /usr/local/
# 重命名
mv spark-2.2.0-bin-hadoop2.7/ spark
cd spark/

# 修改spark配置
cp ./conf/spark-env.sh.template ./conf/spark-env.sh
vim ./conf/spark-env.sh
添加:
# /usr/local/hadoop/bin/hadoop 是本机的hadoop家目录，根据hadoop安装目录修改
export SPARK_DIST_CLASSPATH=$(/usr/local/hadoop/bin/hadoop classpath)
```



### 3. 测试

```
/usr/local/spark/bin/spark-shell

val textFile = sc.textFile("file:///usr/local/spark/README.md")
textFile.count()
textFile.first()
```



