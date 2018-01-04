# redis install

十分简单，[官网教程](http://redis.io/download)

### 1. 下载解压编译 

```
$ wget http://download.redis.io/releases/redis-3.2.1.tar.gz
$ tar xzf redis-3.2.1.tar.gz
$ cd redis-3.2.1
$ make
```

### 2. 开启服务

```
$ src/redis-server
```

### 3. 连接使用

```
$ src/redis-cli
redis> set foo bar
OK
redis> get foo
"bar"
```