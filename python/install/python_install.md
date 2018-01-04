# python2.7 安装

### 说明

虽然此文档写的是python2.7的安装，但同样适用于python3。而且python3的安装更简单，我们不需要后续easy_install和pip的安装(python3自带)。


### 环境

1. 系统版本：centos6.5(自带python2.6，不卸载)
2. python版本：2.7.12


### 下载

[python2.7](https://www.python.org/downloads/release/python-2712/)源码包下载

### 准备工作

因为后续要安装setuptools和pip，需要依赖，安装python时就准备好，以防出错。


##### 1. gcc安装

既然是编译安装，那就肯定需要gcc。

```
yum install -y gcc
```

##### 2. 依赖安装

```
yum install -y zlib zlib-devel openssl  openssl-devel readline readline-devel 
```

如果不安装zlib，安装setuptools时会出现错误:`zipimport.ZipImportError: can't decompress data; zlib not available`

如果不安装openssl，安装完pip，会发现pip无法使用，报错为:`AttributeError: 'module' object has no attribute 'HTTPSConnection'`


### 编译安装

```
mkdir /usr/local/python2.7  	#创建python目录
tar -zxvf Python-2.7.12.tgz 		#解压源码包
cd Python-2.7.12					#进入源码包
```

以防zlib找不到，修改以下文件

```
vim Modules/Setup.dist

取消该行的注释：
#zlib zlibmodule.c -I$(prefix)/include -L$(exec_prefix)/lib -lz
```


```
./configure --prefix=/usr/local/python2.7	#指定安装目录
make
make install
```

### 检验安装

```
/usr/local/python2.7/bin/python2.7
```

成功运行python，即表示安装成功。



