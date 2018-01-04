# mysql安装记录


### 安装环境:   

- 系统：centos6.5
- mysql：5.1.7

### 安装步骤:  

1. 安装mysql  

 `yum install -y mysql-server mysql mysql-devel`  

2. 开启mysql  

 `/etc/init.d/mysqld start`

3. 你可以查看下是否设置开机启动  

 `chkconfig --list | grep mysqld`  

4. 设置开机启动  

 `chkconfig mysqld on`

5. mysql数据库安装完以后自动生成一个root管理员账号，但是此时的root账号还并没有为其设置密码，所以我们为其设置密码  

 `mysqladmin -u root password 'root'`

6. 现在就可以登录mysql了  

 `mysql -uroot -p`  

***

### 配置mysql:  

- 设置编码为utf8  
 
 1. `vim /etc/my.cnf`  

 	在[mysqld][mysql]下都增加  
	 `default-character-set = utf8`	
 
 2. 重启mysql  

	 `/etc/init.d/mysqld restart`  
 
 3. 登录mysql查看编码  

	 `show variables like 'character%';`