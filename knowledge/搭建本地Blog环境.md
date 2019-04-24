# 搭建本地Blog环境

## 1. 背景

为了快速搭建blog，故采用了[GitHub Pages](https://pages.github.com/) + [jekyll](http://jekyll.com.cn/) 的方式。具体搭建方式可参考[博客搭建详细教程](https://github.com/qiubaiying/qiubaiying.github.io/wiki/%E5%8D%9A%E5%AE%A2%E6%90%AD%E5%BB%BA%E8%AF%A6%E7%BB%86%E6%95%99%E7%A8%8B)。

由于每次调试都需要commit代码到github，这样操作的效率低下，因此笔者在本地搭建了一个测试环境。



## 2. 要求

- Linux系统
- Docker：为了减少安装的麻烦，采用Docker来配置所需环境



## 3. 过程

采用[pages-gem](https://github.com/github/pages-gem)提供的Github Pages环境

```shell
$ git clone git@github.com:github/pages-gem.git # clone仓库

$ cd pages-gem/

$ make image

$ SITE=PATH_TO_YOUR_PROJECT make server  	# PATH_TO_YOUR_PROJECT就是你的blog所在的根目录
											# 成功后会在本机的4000端口搭建web服务
```



## 4. 访问

```
浏览器访问 127.0.0.1:4000
```





