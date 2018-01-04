# TELNET

### 概念

Telnet协议是一种应用层协议，使用于互联网及局域网中，使用虚拟终端机的形式，提供双向、以文字字符串为主的交互功能。属于TCP/IP协议族的其中之一，是Internet远程登录服务的标准协议和主要方式，常用于网页服务器的远程控制，可供用户在本地主机运行远程主机上的工作。

### 安装

1. 检测是否已经安装  

   `rpm -qa | grep telnet`

2. 使用yum安装

   `yum install -y telnet telnet-server`

3. 配置telnet配置文件

   ```
vim /etc/xinetd.d/telnet
修改
disable = yes
为
disable = no
   ```

4. 启动服务

   `/etc/init.d/xinetd start`

### 设置telnet端口（可省略）

1. 修改配置文件

   ```
vim /etc/services
找到如下内容
telnet 23/tcp
telnet 23/udp
将23修改成未使用的端口号(如：2000)
   ```

2. 重启服务

   `/etc/init.d/xinetd restart`

### telnet连接linux

```
telnet 192.168.254.133 23
```

**注意**如果你的端口号修改了，必须调整登录端口号，不输入则默认端口为23

**注意**默认root用户无法登录，请使用普通用户登录，如果需要root登录，可以修改配置文件（不安全，略过）

**注意**要是以上步骤都正确，却没法连接，有可能是防火墙的问题，简单点处理的话，直接关闭防火墙

