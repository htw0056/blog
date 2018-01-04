# setuptools 和 pip 安装

### 说明

此文档描述的是python2.7安装setuptools和pip，python3已经自带了这两者，所以不需要安装。


### setuptools 安装方式一(推荐)


```
wget --no-check-certificate https://pypi.python.org/packages/2.7/s/setuptools/setuptools-0.6c11-py2.7.egg 
ln -s /usr/local/python2.7/bin/python2.7 /usr/bin/   #使用该方法安装，需要保证/usr/bin下有python2.7
bash setuptools-0.6c11-py2.7.egg				      #执行脚本即可
``` 


### setuptools 安装方式二


参考自[官方文档](https://pypi.python.org/pypi/setuptools)。

优点:自动下载各种所需文件，并执行安装  
缺点: 下载速度有点慢


```
wget --no-check-certificate https://bootstrap.pypa.io/ez_setup.py #下载脚本
/usr/local/python2.7/bin/python2.7 ez_setup.py --insecure            #注意，要使用你想安装setuptools的python来运行脚本
```


### setuptools 安装方式三(推荐)

该安装方式其实是在安装方式二上的优化，使用手动下载包来安装。

首先要在官网获得setuptools的安装包[setuptools-28.5.0.tar.gz](https://pypi.python.org/pypi/setuptools)


```
tar -zxvf setuptools-28.5.0.tar.gz
cd setuptools-28.5.0
/usr/local/python2.7/bin/python2.7 setup.py install   #注意，要使用你想安装setuptools的python来运行脚本	
```


### pip 安装安装方式一(推荐)

前提:需要先安装好setuptools

```
wget --no-check-certificate https://pypi.python.org/packages/source/p/pip/pip-1.3.1.tar.gz		#下载pip
tar -zxvf pip-1.3.1.tar.gz
cd pip-1.3.1
/usr/local/python2.7/bin/python2.7 setup.py install   #注意，要使用你想安装pip的python来运行脚本	
```

如果你需要其他版本的pip，可以去[官网](https://pypi.python.org/pypi/pip/8.1.2)查找。

### pip 安装方式二

前提:需要先安装好setuptools

直接使用easy_install-2.7为你安装pip，但是可能会出现下载慢的情况。

```
/usr/local/python2.7/bin/easy_install-2.7 pip
```