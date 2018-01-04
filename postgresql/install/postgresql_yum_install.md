# postgresql yum安装

以下内容收集自网上各种资源，先贴原文链接：

- [PostgreSQL新手入门](http://www.ruanyifeng.com/blog/2013/12/getting_started_with_postgresql.html)
- [PostgreSQL 入门](http://blog.wuxu92.com/postgresql-introduction/)

### 安装环境

- 安装系统：centos6.5
- 安装版本：postgresql9.4



### 安装([根据官网的介绍安装](http://www.postgresql.org/download/linux/redhat/))

##### 1. 更新yum源  

```
yum install http://yum.postgresql.org/9.4/redhat/rhel-6-x86_64/pgdg-centos94-9.4-2.noarch.rpm
```

注意:不同的系统不同的pg版本选用不同的yum源，你根据官网里提供的的[yum源](http://yum.postgresql.org/repopackages.php)自行改正上行的链接地址   

##### 2. 安装postgresql  

```
yum install postgresql94-server postgresql94-contrib
```
##### 3. 简单设置一下，先初始化数据库，再开启服务，设置开机启动

```
service postgresql-9.4 initdb
/etc/init.d/postgresql-9.4 start 
chkconfig postgresql-9.4 on
```
##### 4. 登录postgresql  

```
su - postgres
psql
```
到此，postgresql已经安装完成了。



### 配置

##### 1. 添加新用户和新数据库  

初次安装后，默认生成一个名为postgres的数据库和一个名为postgres的数据库用户。这里需要注意的是，同时还生成了一个名为postgres的Linux系统用户。  
下面，我们使用postgres用户，来生成其他用户和新数据库。使用PostgreSQL控制台。  
首先，新建一个Linux新用户，可以取你想要的名字，这里为dbuser   

```
adduser dbuser  
passwd dbuser
```
##### 2. 切换到postgres用户  

 ```
su - postgres
 ```

##### 3. 使用psql命令登录PostgreSQL控制台  

 ```
psql
 ```

   这时相当于系统用户postgres以同名数据库用户的身份，登录数据库，这是不用输入密码的。如果一切正常，系统提示符会变为"postgres=#"，表示这时已经进入了数据库控制台。以下的命令都在控制台内完成。
第一件事是使用\password命令，为postgres用户设置一个密码。**注意这里设置的是数据库pg里postgres用户的密码，如果你需要设置linux用户postgres的密码那就用 passwd postgres,这两个密码还是有所不同的~**

##### 4. 创建数据库用户dbuser（刚才创建的是Linux系统用户），并设置密码  

```
CREATE USER dbuser WITH PASSWORD 'password'; 
```
##### 5. 创建用户数据库，这里为exampledb，并指定所有者为dbuser  

```
CREATE DATABASE exampledb OWNER dbuser;
```
##### 6. 将exampledb数据库的所有权限都赋予dbuser，否则dbuser只能登录控制台，没有任何数据库操作权限  

```
GRANT ALL PRIVILEGES ON DATABASE exampledb to dbuser;
```
##### 7. 最后，使用\q命令退出控制台  

```
\q
```
##### 8. 到此，用户已经创建了而且有了他自己的数据库，那我们去登录试试

```
psql -U dbuser -d exampledb -h 127.0.0.1 -p 5432
```
### 错误

> psql: 致命错误:  对用户"dbuser"的对等认证失败  

这时候，我们就需要修改下pg的配置文件了：  
这牵涉到pg的认证方式了。我们知道pg的认证方式是保存在配置文件 pg_hba.conf 中，这个文件在初始化数据(initdb) 所制定的目录，默认地址是 /var/lib/pgsql/9.4/data/pg_hba.conf ,这是一个比较标准的linux风格的配置文件，一行代表一条规则。主要内容如下：  

```
# TYPE  DATABASE        USER            ADDRESS                 METHOD  
# "local" is for Unix domain socket connections only  
local   all             all                                     peer  
# IPv4 local connections:  
host    all             all             127.0.0.1/32            ident  
# IPv6 local connections:  
host    all             all             ::1/128                 ident  
```

我们关心的是最后那个字段，METHOD，就是认证的方式，有很多种方式可以配置，常用的  

```
trust 任何连接都允许，不需要密码  
reject 拒绝符合条件(前面几个条件)的请求  
MD5 接收一个MD5加密过的密码  
password 接收一个密码来登陆，只在可信的网络使用这种方式  
gss 使用gssapi认证，只在tcp/ip连接可用  
sspi 只在windows可用的一种方式  
krb5 不常用，只在TCP/IP可用  
ident 使用操作系统用户名认证，验证它是否符合请求的的数据库用户名  
ldap 使用LDAP服务器认证  
cert 使用ssl客户端认证  
pam 使用操作系统的pam模块服务  
```

根据上面的报错，是配置文件使用了ident认证方式。所以我们修改

`host    all             all             127.0.0.1/32            ident`  为

 `host    all             all             127.0.0.1/32            password`  
之后重启服务  `/etc/init.d/postgresql-9.4 restart`  
重新登录`psql -U dbuser -d exampledb -h 127.0.0.1 -p 5432`  
应该就能成功登陆了



### pgadmin连接postgresql

##### 1. 安装pgadmin，这个你去官网自己下载，一路确认就安装好了

##### 2. 登录时候如果报错或者一直没反应，那么有以下几种情况

1. 你的linux虚拟机没关闭防火墙，尝试关掉防火墙看看  

    ```
    setenforce 0  
    /etc/init.d/iptables stop
    ```

2. 如果报错说没有监听，那么你需要修改下pg的配置文件  

   ```
   vim /var/lib/pgsql/9.4/data/pg_hba.conf  
   将改文件里的  
   host    all             all             127.0.0.1/32            ident  
   改为  
   host    all             all             0.0.0.0/0            password  
   表示所有的ip都能连接改服务器。  
   ```

   ```
   vim /var/lib/pgsql/9.4/data/postgresql.conf 
   listen_addresses = '*'     //监听所有ip的连接，默认是本机    
   port = 5432             //这个不开也行，默认就是5432端口 
   ```

最后应该可以登录了
