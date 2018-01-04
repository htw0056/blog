# python2.7 install ipython

> 直接使用[anaconda](https://www.anaconda.com/)是更好的选择

### 准备

请确保已经安装好了[python2.7](https://github.com/htw0056/blog/blob/master/python/install/python_install.md)以及[setuptools和pip](https://github.com/htw0056/blog/blob/master/python/install/setuptools_pip_install.md)。


### 安装

> 如果是python3，可以直接使用相应的`pip install ipython`,一步安装到位。但是目前最新的ipython并不支持python2.7，所以直接使用该命名会提示版本不对。


##### 1. 下载包

ipython5 是支持python2.7的最新版本，所以需要下载[ipython5](https://pypi.python.org/pypi/ipython/5.0.0).

##### 2. 安装包

```
tar -zxvf ipython-5.0.0
cd ipython-5.0.0
pip2.7 install .
```

