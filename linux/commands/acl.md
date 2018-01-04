# ACL权限

当所有者、所属组、其他人这三组的权限不够用的时候，就用到ACL权限了。简单来说，它就是给某个特定的用户(组)特定的权限。

### 查看ACL权限功能是否开启

```
dumpe2fs -h /dev/sda5
```

-h:仅显示超级块中信息，而不显示磁盘块组的详细信息  
/dev/sda5:通过df命令查看你需要的分区来获得

### 临时开启ACL权限

```
mount -o remount,acl /
```

重新挂载根分区，并挂载加入acl权限

### 永久开启ACL权限

```
vim /etc/fstab
修改需要的部分如下：(增加acl)
UID=db0ee0f7-ea96-409a-a886-cb0e7b2154d5 /    ext4    defaults,acl 1 1
重新挂载文件系统
mount -o remount /
```

### 查看ACL权限

```
getfacl 文件名
```

### 设定ACL权限

```
setfacl 选项 文件名
```

选项|作用
---|---
-m|设定acl权限
-x|删除指定的acl权限
-b|删除所有acl权限
-d|设定默认acl权限
-k|删除默认acl权限
-R|递归设定acl权限

设定ACL权限：  

```
setfacl -m u:用户名:权限 文件
setfacl -m g:组名:权限 文件
setfacl -m u:lw:rx /aa
```

删除ACL权限：  

```
setfacl -x u:用户名 文件
setfacl -x g:组名 文件
setfacl -b 文件
```

### 最大有效权限

getfacl时可以看到有一个mask值。mask是用来指定最大有效权限的。如果我们给用户赋予了ACL权限，是需要和mask的权限进行“与”运算，才能得到用户真正的权限。  
修改最大有效权限：  
`setfacl -m m:rx 文件名`

### 默认ACL权限和递归ACL权限


递归是父目录在设定ACL权限时，所有的子文件和子目录也会拥有相同的ACL权限。  
`setfacl -m u:用户名:权限 -R 文件名`


默认ACL权限的作用就是如果给父目录设定了默认ACL权限，那么父目录中所有新建的子文件都会继承父目录的ACL权限。  
`setfacl -m d:u:用户名:权限 文件名`



















