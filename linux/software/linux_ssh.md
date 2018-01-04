# SSH

### SSH概念

> SSH 为 Secure Shell 的缩写。SSH 为建立在应用层和传输层基础上的安全协议。SSH 是目前较可靠，专为远程登录会话和其他网络服务提供安全性的协议。利用 SSH 协议可以有效防止远程管理过程中的信息泄露问题。


### SSH安装

一般情况下，SSH在linux平台上都是自带安装的。如果你需要安装可以使用以下命令：  
`yum install openssh-server openssh-clients`

### SSH服务端配置文件

1. ssh服务器端配置文件路径：`/etc/ssh/sshd_config`
2. 常用配置说明

选项|说明
----|----
Port 22			|			SSH端口设置，默认使用的是22端口
Protocol 2		|			SSH协议版本
ListenAddress 0.0.0.0 |		监听的网卡IP
PermitRootLogin yes  	|	是否允许root登录，默认是允许的
asswordAuthentication yes|	是否开启密码验证
PermitEmptyPasswords no |	是否语序密码为空
PrintMotd yes			|	登录后是否显示一些信息，如上次登入时间及地点等
PrintLastLog yes		|	显示上次登录的信息
TCPKeepAlive yes		|	发送KeepAlive信息给客户端
MaxStartups 10:30:100	| 允许尚未登录的联机画面数
DenyUsers *			|	禁止用户登录，*表示所有用户
AllowUsers *			|	允许用户登录					

### SSH认证方式

##### 1. 基于口令的登录方式  

 `ssh options username@hostname command`

##### 2. 基于密钥认证登录方式

以下将模拟机器A免密码登陆机器B。需要注意的是，这种登陆方式是单向的，也就是你设置了A免密码登陆B，不能使得B免密码登陆A.



 1. 生成密钥  
 	
 	在机器A中执行命令：
 	`ssh-keygen`
 	
 	遇到问答，直接全部回车即可

 2. 生成的密钥默认保存位置为(机器A)  
  
 	`~/.ssh`

 3. 在机器B上生成authorized_keys文件

 	```
 	mkdir ~/.ssh
 	touch ~/.ssh/authorized_keys
 	```

 4. 导入机器A上的公钥到机器B的authorized_keys中

 	`ssh user@host 'cat >> .ssh/authorized_keys' < ~/.ssh/id_rsa.pub`
 	
 	以上方式是使用命令上传，如果觉得复杂，可以直接登陆机器B，将机器A生成的公钥复制到机器B的authorized_keys文件中即可。

 5. 修改机器B的authorized_keys文件权限

 	`chmod 600 authorized_keys`

 6. 使用ssh方式登录，在机器A使用ssh方式登陆机器B，不需要密码直接可登陆说明设置成功

###  限制用户登录

修改：`vim /etc/ssh/sshd_config`  

选项|说明
---|---
DenyUsers test	|禁止test用户登录  
AllowUsers test	|允许test用户登录  
DenyGroups test	|禁止test群组登录  
AllowUsers test	|允许test群组登录  


### 限制IP连接SSH

1. iptables防火墙  

 ```
iptables -A INPUT -p tcp --dport 22 -s 192.168.254.1/32 -j ACCEPT
iptables -A INPUT -p tcp --dport 22 -j DROP
 ```

2. TCP Wrappers  

 ```
/etc/hosts.allow  
sshd:192.168.0.1/255.255.255.255  
/etc/hosts.deny  
sshd:ALL 或 sshd:ALL EXCEPT 192.168.0.1
 ```


### SSH使用

1. SSH执行远程主机命令

 ```
ssh username@hostname 'command'
 ```
   
2. SSH构建跳板隧道

 ```
 ssh -t root@192.168.1.2 ssh root@192.168.1.3
 ```

3. SSH指定秘钥路径，端口，用户及配置文件

 选项|说明
 ---|---
 -i |指定秘钥路径
 -p | 指定SSH端口
 -l | 指定用户
 -F | 指定配置文件
 -t | 指定为终端迫使SSH客户端以交互模式工作，常配合expect使用

4. 构建socket5代理

 `ssh root@123.456.123.456 -D 1234`
   
   
   
### SSH免密码登录详解

#### 1. 基于口令的安全验证

这种方式使用用户名密码进行联机登录，一般情况下我们使用的都是这种方式。整个过程大致如下：

（1）客户端发起连接请求。

（2）远程主机收到用户的登录请求，把自己的公钥发给客户端。

（3）客户端接收远程主机的公钥，然后使用远程主机的公钥加密登陆密码，紧接着将加密后的登陆密码连同自己的公钥一并发送给远程主机。

（4）远程主机接收客户端的公钥及加密后的登陆密码，用自己的私钥解密收到的登录密码，如果密码正确则允许登陆，到此为止双方彼此拥有了对方的公钥，开始双向加密解密。   

PS：当网络中有另一台冒牌服务器冒充远程主机时，客户端的连接请求被服务器B拦截，服务器B将自己的公钥发送给客户端，客户端就会将密码加密后发送给冒牌服务器，冒牌服务器就可以拿自己的私钥获取到密码，然后为所欲为。因此当第一次链接远程主机时，在上述步骤的第（3）步中，会提示您当前远程主机的”公钥指纹”，以确认远程主机是否是正版的远程主机，如果选择继续后就可以输入密码进行登录了，当远程的主机接受以后，该台服务器的公钥就会保存到 ~/.ssh/known_hosts文件中。

#### 2. 基于密匙的安全验证

这种方式你需要在当前用户家目录下为自己创建一对密匙，并把**公匙放在需要登陆的服务器上**。当你要连接到服务器上时，客户端就会向服务器请求使用密匙进行安全验证。服务器收到请求之后，会在该服务器上你所请求登陆的用户的家目录下寻找你的公匙，然后与你发送过来的公匙进行比较。如果两个密匙一致，服务器就用该公匙加密“质询”并把它发送给客户端。客户端收到“质询”之后用自己的私匙解密再把它发送给服务器。与第一种级别相比，第二种级别不需要在网络上传送口令。

PS：简单来说，就是将客户端的公钥放到服务器上，那么客户端就可以免密码登陆服务器了，那么客户端的公钥应该放到服务器上哪个地方呢？默认为你要登陆的用户的家目录下的 .ssh 目录下的 authorized_keys 文件中（即：~/.ssh/authorized_keys）。


#### 公钥上传命令

```
ssh-copy-id user@host
```

等同于

```
ssh user@host 'mkdir -p .ssh && cat >> .ssh/authorized_keys' < ~/.ssh/id_rsa.pub
```

这条命令由多个语句组成，依次分解开来看：（1）"ssh user@host"，表示登录远程主机；（2）单引号中的mkdir .ssh && cat >> .ssh/authorized_keys，表示登录后在远程shell上执行的命令：（3）"mkdir -p .ssh"的作用是，如果用户主目录中的.ssh目录不存在，就创建一个；（4）'cat >> .ssh/authorized_keys' < ~/.ssh/id_rsa.pub的作用是，将本地的公钥文件 `~/.ssh/id_rsa.pub`，重定向追加到远程文件authorized_keys的末尾。