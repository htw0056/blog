# FTP

FTP是 File Transfer Protocol 的缩写。FTP本质上是一个应用程序，用于在互联网上控制文件的双向传输

### VSFTP

VSFTP（Very Secure FTP）是一个类Unix系统上使用的FTP服务器软件。VSFTP是linux系统下使用做多的FTP服务端。

### VSFTP安装

```
yum install vsftpd
```

### VSFTP服务的启动和关闭

`service vsftpd start/restart/stop/status`

或者  

`/etc/init.d/vsftpd start/restart/stop/status`

### FTP两种传输模式

1. PORT（主动）模式

 所谓主动模式，指的是FTP服务器“主动”去连接客户端的数据端口来传输数据，其过程具体来说就是：客户端从一个任意的非特权端口N（N>1024）连接到FTP服务器的命令端口（即tcp 21端口），紧接着客户端开始监听端口N+1，并发送FTP命令“port N+1”到FTP服务器。然后服务器会从它自己的数据端口（20）“主动”连接到客户端指定的数据端口（N+1），这样客户端就可以和ftp服务器建立数据传输通道了。

2. PASV（被动）模式

 所谓被动模式，指的是FTP服务器“被动”等待客户端来连接自己的数据端口，其过程具体是：当开启一个FTP连接时，客户端打开两个任意的非特权本地端口（N >1024和N+1）。第一个端口连接服务器的21端口，但与主动方式的FTP不同，客户端不会提交PORT命令并允许服务器来回连它的数据端口，而是提交PASV命令。这样做的结果是服务器会开启一个任意的非特权端口（P > 1024），并发送PORT P命令给客户端。然后客户端发起从本地端口N+1到服务器的端口P的连接用来传送数据。（注意此模式下的FTP服务器不需要开启tcp 20端口了）


### 两种模式的比较

1. PORT（主动）模式只要开启服务器的21和20端口，而PASV（被动）模式需要开启服务器大于1024所有tcp端口和21端口。

2. 从网络安全的角度来看的话似乎ftp PORT模式更安全，而ftp PASV更不安全，那么为什么RFC要在ftp PORT基础再制定一个ftp PASV模式呢？其实RFC制定ftp PASV模式的主要目的是为了数据传输安全角度出发的，因为ftp port使用固定20端口进行传输数据，那么作为黑客很容使用sniffer等探嗅器抓取ftp数据，这样一来通过ftp PORT模式传输数据很容易被黑客窃取，因此使用PASV方式来架设ftp server是最安全绝佳方案。

因此：如果只是简单的为了文件共享，完全可以禁用PASV模式，解除开放大量端口的威胁，同时也为防火墙的设置带来便利。  
不幸的是，FTP工具或者浏览器默认使用的都是PASV模式连接FTP服务器，因此，必须要使vsftpd在开启了防火墙的情况下，也能够支持PASV模式进行数据访问。  

### VSFTP服务的配置文件

文件|作用
----|----
/etc/vsftpd/vsftpd.conf|主配置文件
/usr/sbin/vsftpd|主程序
/etc/vsftpd/ftpusers|FTP用户黑名单
/etc/vsftpd/user_list|控制用户登录
/var/ftp|匿名用户主目录


### VSFTP主配置文件解析

命令|作用
----|----
anonymous_enable=YES|是否使用匿名用户
anon_upload_enable=YES|是否允许匿名用户上传
anon_mkdir_write_enable=YES|是否允许匿名用户创建目录
local_enable=YES|是否允许本地用户
write_enable=YES|是否允许本地用户具有写权限
local_umask=022|本地用户掩码
chroot_list_enable=YES|锁定用户在自己的主目录
chroot_list_file=PATH|此文件中的用户将启用chroot
listen=YES|独立模式
userlist_enable=YES/NO|用户访问控制
userlist_deny=YES/NO|
tcp_wrappers=YES|是否启用TCP_Wrappers服务


### 关于/etc/vsftpd/目录下的user_list和ftpusers两个文件

**ftpusers文件中的用户在任何情况下都不能登录FTP服务**  

1. userlist_enable=NO时，user_list文件不能生效  
2. userlist_enable=YES，userlist_deny=YES时，user_list文件中的用户不能登录FTP，列表以外的用户可以登录  
3. userlist_enable=YES，userlist_deny=NO时，user_list文件中的用户能登录FTP，列表以外的用户不可以登录  


### FTP命令参数

参数|作用
----|----
-v|显示远程服务器的所有响应信息
-i|多个文件传送时关闭交互提示
-n|禁止自动登录到初始连接
-g|禁用文件名和路径中的通配符
-s|指定包含FTP命令的文本文件
-d|启动调试，显示客户端与服务器之间传递的所有FTP命令


### FTP常用内部命令

命令|作用
----|----
help|显示FTP内部命令的帮助信息
open ip|打开连接
user name|登陆用户
cd|进入目录
cdup|进入父目录
ls|列出文件和目录
pwd|显示当前路径
delete|删除文件
mkdir|创建目录
![command]|在本地执行交互shell
get|下载文件
mget|下载多个文件
put|上传文件
mput|上传多个文件
close|断开连接
bye,quit|退出FTP



### 设置自动登录

机器A自动登录机器B的ftp服务：

在机器A的家目录下:

```
1. vim ~/.netrc
添加：
mechine ip login name password pwd
#注意，ip、name、pwd分别为你需要登录的机器ip地址、用户名、密码

2. 修改文件权限
chmod 600 ~/.netrc
```


### 开启FTP虚拟用户

1. 生成虚拟用户口令库文件

	```
	vim login.txt
	
	guest1
	guest1
	guest2
	guest2
	```
	里面的内容：奇数行为账号，偶数行为密码
	
2. 生成FSFTPD的认证文件

	```
	db_load -T -t hash -f login.txt /etc/vsftpd/vsftpd_login.db
	chmod 600 /etc/vsftpd/vsftpd_login.db
	```

3. 修改PAM配置文件

	```
	vim /etc/pam.d/vsftpd
	
	将里面的内容全部注释，然后添加：
	
	auth required /lib/security/pam_userdb.so db=/etc/vsftpd/vsftpd_login
	account required /lib/security/pam_userdb.so db=/etc/vsftpd/vsftpd_login
	```
	注意，如果你的系统是64位的，上面的配置需要修改，否则虚拟用户模式无法正常使用:
	
	```
	auth required pam_userdb.so db=/etc/vsftpd/vsftpd_login
	account required pam_userdb.so db=/etc/vsftpd/vsftpd_login
	```
	
4. 建立虚拟用户的访问权限

	```
	useradd -d /home/ftp virtual
	chmod 700 /home/ftp
	```

5. 修改配置文件

	```
	vim /etc/vsftpd/vsftpd.conf
	修改:
	local_enable=YES	//PAM方式此处必须为YES
	guest_enable=YES	//启动虚拟用户
	guest_username=virtual	//映射到本地virtual用户
	```
	
6. 重启FTP服务

	```
	/etc/init.d/vsftpd restart
	```
	
### 开启虚拟ftp服务

1. 创建虚拟FTP服务器的根目录

	```
	mkdir -p /var/newftp/newpub
	chmod 755 /var/newftp -R
	```

2. 增加虚拟FTP服务器的匿名用户账号

	```
	useradd -d /var/newftp -M newftp
	```
	
3. 创建虚拟FTP服务器的配置文件

	```
	cp /etc/vsftpd/vsftpd.conf /etc/vsftpd/vsftpd2.conf
	
	vim /etc/vsftpd/vsftpd2.conf
	
	listen=YES
	listen_address=192.168.134.205
	ftp_username=newftp
	
	也要修改下原有的ftp监听:
	vim /etc/vsftpd/vsftpd.conf
	
	listen_address=192.168.134.204
	```
4. 重启服务

	```
	/etc/init.d/vsftpd restart
	```


