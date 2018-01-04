# linux 网络配置

# 系统环境

- 系统：centos6.5




## 1. NAT模式下配置动态ip


dhcp(动态ip)方式，这是最简单粗暴的方式，但缺点就是ip不固定。


1. 查看网卡

   ```
   ifconfig
   ```

   在完全没配置的情况下，只能查看到lo这一个网卡

2. 配置网卡

   ```
   vim /etc/sysconfig/network-scripts/ifcfg-eth0
   修改或添加：

   ONBOOT=yes
   NM_CONTROLLED=no
   BOOTPROTO=dhcp
   ```

3. 重启网络

   ```
   /etc/init.d/network restart
   ```

4. 检查是否成功

   ```
   ifconfig
   ```

   应该会出现eth0这块网卡

   ```
   ping www.baidu.com
   ```

   能够ping的通就说明配置成功。


## 2. NAT模式下配置静态ip


1. 查看网卡

   ```
   ifconfig
   ```

   在完全没配置的情况下，只能查看到lo这一个网卡

2. 配置网卡

   ```
   vim /etc/sysconfig/network-scripts/ifcfg-eth0
   修改或添加：

   ONBOOT=yes
   NM_CONTROLLED=no
   BOOTPROTO=static
   IPADDR=192.168.134.103
   NETMASK=255.255.255.0
   GATEWAY=192.168.134.2
   ```

   关于这里面的ip,netmask和gateway，你会想问，我怎么知道自己的网段和网关的？解决方法有三种：

   1. 粗暴的方式(纯属个人的小技巧),你先设置你的连网方式为dhcp方式，成功后,使用命令`ifconfig`查看，可以猜出自己的网段了。然后使用命令`route -n`，然后可以找到网关

   2. 在win下，选择菜单“编辑”，“编辑虚拟网络”，“nat设置”。在里面能找到子网和网关

   3. mac下的fusion是没有图形界面的nat设置的，你可以用命令查看`cat /Library/Preferences/VMware\ Fusion/vmnet8/dhcpd.conf`在其中找到网段，然后`cat /Library/Preferences/VMware\ Fusion/vmnet8/nat.conf`，找到网关

## 3. 配置网关

```
vim /etc/sysconfig/network
修改或添加：

GATEWAY=192.168.134.2
```

   如果你在配置网卡的时候就写了GATEWAY这一配置，那么这一步可以不配。简而言之，`/etc/sysconfig/network-scripts/ifcfg-eth0`和`/etc/sysconfig/network`中至少配一个GATEWAY。

## 4. 配置DNS

```
vim /etc/resolv.conf
修改或添加：

search localdomain
nameserver 8.8.8.8
```

 如果你没配置DNS，而其他设置都正确。那么你使用ping命令的时候，能ping通ip地址(xxx.xxx.xxx.xxx)，但是ping不通网址(www.abc.com)。

## 5. 关闭selinux方法

```
vim /etc/sysconfig/selinux
修改或添加:
SELINUX=disabled
```

之后重启电脑即可。  
验证输入命令:`getenforce`得到`Disabled`则表明已关闭selinux。

## 6. 关闭iptables

```
/etc/init.d/iptables stop		#关闭iptables
chkconfig iptables off			#设置开机不启动
```






