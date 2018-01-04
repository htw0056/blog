# mysql5.6.25源码安装

## 安装环境

1. 系统版本：centos6.5  
2. mysql版本：5.6.25  

## 安装过程


#### 1.卸载旧版本mysql

1. 查看是否存在旧版本mysql  

 `rpm -qa | grep mysql`

2. 存在就删除

 `rpm -e --nodeps mysql`

#### 2.安装MySQL

1. 安装所需环境

 `yum -y install gcc gcc-c++ ncurses ncurses-devel cmake bison`

2. 解压源码包

 `tar -zxvf mysql-5.6.25.tar.gz`

3. 编译安装

 `cd mysql-5.6.25`  
 ```
cmake \  
-DCMAKE_INSTALL_PREFIX=/usr/local/mysql \  
-DMYSQL_DATADIR=/usr/local/mysql/data \  
-DSYSCONFDIR=/etc \  
-DWITH_MYISAM_STORAGE_ENGINE=1 \  
-DWITH_INNOBASE_STORAGE_ENGINE=1 \  
-DWITH_MEMORY_STORAGE_ENGINE=1 \  
-DWITH_READLINE=1 \  
-DMYSQL_UNIX_ADDR=/var/lib/mysql/mysql.sock \  
-DMYSQL_TCP_PORT=3306 \  
-DENABLED_LOCAL_INFILE=1 \  
-DWITH_PARTITION_STORAGE_ENGINE=1 \  
-DEXTRA_CHARSETS=all \  
-DDEFAULT_CHARSET=utf8 \  
-DDEFAULT_COLLATION=utf8_general_ci
 ```
 `make`  
 `make install`  

 
#### 3.配置mysql

1. 创建mysql用户和mysql组

 ```
groupadd mysql  
useradd mysql -g mysql -M -s /sbin/nologin
 ```
2. 修改目录权限

 `chown -R mysql:mysql /usr/local/mysql`

3. 删除原有的my.cnf

 `rm -rf /etc/my.cnf`

4. 初始化配置  

 ```
cd /usr/local/mysql  
scripts/mysql_install_db --basedir=/usr/local/mysql --datadir=/usr/local/mysql/data --user=mysql  
chown -R root /usr/local/mysql
 ```

5. 复制配置文件

 `cp support-files/my-default.cnf  /etc/my.cnf`

6. 复制启动文件  

 `cp support-files/mysql.server /etc/init.d/mysqld`

7. 启动mysql  

 `/etc/init.d/mysqld start`

8. 修改环境变量  

 ```
vim /etc/profile  
添加：
export PATH=$PATH:/usr/local/mysql/bin  
source /etc/profile
 ```

9. 设置root密码

 ```
mysql -uroot  
SET PASSWORD = PASSWORD('123456');
 ```

10. 设置远程访问

 `GRANT ALL PRIVILEGES ON *.* TO 'root'@'192.168.52.1' IDENTIFIED BY 'password' WITH GRANT OPTION;`

11. 设置防火墙

 ```
在/etc/sysconfig/iptables中  
在“-A INPUT -m state --state NEW -m tcp -p tcp --dport 22 -j ACCEPT”，下添加：
-A INPUT -m state --state NEW -m tcp -p tcp --dport 3306 -j ACCEPT  

/etc/init.d/iptables restart
 ```

