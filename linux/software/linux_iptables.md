# iptables

iptables是集成在Linux内核中的IP信息包过滤系统，通过iptables可以实现诸如控制数据包、系统防护、数据转发等多种系统功能。

iptables只是Linux防火墙的管理工具，位于/sbin/iptables。真正实现防火墙功能的是netfilter，它是Linux内核中实现包过滤的内部结构。

### 四表五链

###### 1. 四表

表名|作用|优先级
----|----|----
raw|数据跟踪处理|最高
mangle|包重构(修改)|高
nat|网络地址转换|低
filter|包过滤|最低


###### 2. 五链  

表|PREROUTING|POSTROUTING|INPUT|OUTPUT|FORWARD
--|---------|------------|-----|-----|-------
raw|Y|-|-|Y|-
mangle|Y|Y|Y|Y|Y
nat|Y|Y|-|Y|-
filter|-|-|Y|Y|Y


###### 3. state连接状态

状态|含义
----|----
ESTABLISHED|只要数据包能够成功通过防火墙，那么之后的所有数据包状态都会是ESTABLISHED
NEW|每一条连接中的第一个数据包
RELATED|被动产生的数据包
INVALID|状态不明的数据包，一般视为恶意的数据包并应该被丢弃


###### 4. 四表五链流向

```
in -> PREROUTING -> Destination==localhost -> [ INPUT -> LOCALHOST -> OUTPUT ] -> POSTROUTING -> out
in -> PREROUTING -> Destination!=localhost -> [ FORWARD ]-> POSTROUTING -> out	
```

当一个数据包进入网卡时，它首先进入PREROUTING链，内核根据数据包目的IP判断是否需要转送出去。  
如果数据包就是进入本机的，它就会到达INPUT链。数据包到了INPUT链后，任何进程都会收到它。本机上运行的程序可以发送数据包，这些数据会经过OUTPUT链，然后到达POSTROUTING链输出。  
如果数据包是要转发出去的，且内核允许转发，数据包就会到达FORWARD链，然后到达POSTROUTING链输出。

### iptables命令

###### 1. 配置文件

配置文件|位置
----|----
iptables规则文件|/etc/sysconfig/iptables
iptables配置文件|/etc/sysconfig/iptables-config
iptables恢复文件|/etc/sysconfig/iptables.old

###### 2. 参数


命令参数|作用
----|----
-h|帮助信息
-L|列出表的规则(默认表为filter)
-t|指定表
-A|追加规则
-I|插入规则
-D|删除规则
-P|设置默认规则
-F|清楚默认链中规则
-X|清楚自定义链中规则
-n|只显示IP地址，不显示域名


###### 3. iptables规则

iptables [-t 表名] <A|I|D|R> 链名 [规则编号] [-i|o 网卡名称] [-p 协议类型] [-s 源IP地址|源子网] [--sport 源端口号] [-d 目标IP地址|目标子网] [--dport 目标端口号] <-j 动作>

动作|反馈
----|----  
ACCEPT|接受   
DROP|拒绝，无反应  
REJECT|拒绝，有反应  




###### 4. demo

web服务的安全访问规则:


iptables -A INPUT -m state --state RELATED,ESTABLISHED -j ACCEPT  
iptables -A INPUT -p tcp --dport 80 -j ACCEPT  
iptables -A INPUT -p tcp -m tcp --dport 80 -j ACCEPT  
iptables -A INPUT -i lo -j ACCEPT  
iptables -P INPUT DROP




