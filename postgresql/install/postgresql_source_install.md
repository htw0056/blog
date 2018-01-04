# postgresql源码安装

以下内容参考了[官方教程](https://www.postgresql.org/docs/9.6/static/installation.html)

## 安装环境

- 操作系统：centos6.5
- 安装版本：postgresql-9.6.5


## 简单版

这是官网上提供的安装，据此即可成功安装。

```
./configure
make
su
make install
adduser postgres
mkdir /usr/local/pgsql/data
chown postgres /usr/local/pgsql/data
su - postgres
/usr/local/pgsql/bin/initdb -D /usr/local/pgsql/data
/usr/local/pgsql/bin/postgres -D /usr/local/pgsql/data >logfile 2>&1 &
/usr/local/pgsql/bin/createdb test
/usr/local/pgsql/bin/psql test
```

## 安装(无root权限版)


#### 1. 下载源码包

源码包[下载地址](https://www.postgresql.org/ftp/source/)

```
wget https://ftp.postgresql.org/pub/source/v9.6.5/postgresql-9.6.5.tar.gz
```

#### 2. 解压tar包

```
tar -zxvf postgresql-9.5.2.tar.gz
```

#### 3. configure

```
cd postgresql-9.5.2.tar.gz
./configure --prefix=/home/htw/pgsql
```

如果有报错，可能会有以下几种情况：

1. 没有gcc编译器

   ```
   configure: error: no acceptable C compiler found in $PATH
   ```
   解决办法:

   `yum install gcc`

2. **readline library not found**

   ```
   onfigure: error: readline library not found
   If you have readline already installed, see config.log for details on the
   failure.  It is possible the compiler isn't looking in the proper directory.
   Use --without-readline to disable readline support.
   ```

   解决办法:  

   `yum install readline-devel`

3. **zlib library not found**

   ```
   configure: error: zlib library not found
   If you have zlib already installed, see config.log for details on the
   failure.  It is possible the compiler isn't looking in the proper directory.
   Use --without-zlib to disable zlib support.
   ```

   解决办法:  

   `yum install zlib-devel`


#### 4. make

```
make
```

#### 5. make install

```
make install
```

#### 6. 配置启动

```
配置环境变量:
export PG_HOME=/home/htw/pgsql
export PGDATA=/home/htw/pgsql/data
export PATH=$PATH:${PG_HOME}/bin
export LD_LIBRARY_PATH=${PG_HOME}/lib
export C_INCLUDE_PATH=${PG_HOME}/include
export LIBRARY_PATH=${PG_HOME}/lib
```

```
mkdir /home/htw/pgsql/data		#创建服务目录
/home/htw/pgsql/bin/initdb -D /home/htw/pgsql/data	#初始化该目录,会自动创建当前用户为超级管理员
/home/htw/pgsql/bin/pg_ctl -D /home/htw/pgsql/data -l logfile start		#在该目录下启动服务
[/home/htw/pgsql/bin/postgres -D /home/htw/pgsql/data >logfile 2>&1 &]这句和上一句达到的效果一样,任选一句执行就行

/home/htw/pgsql/bin/createdb test
/home/htw/pgsql/bin/psql test
```

成功进入即表示安装成功。如果你还有其他文件要配置，可以在`/home/htw/pgsql/data`中找到对应的文件并修改。

