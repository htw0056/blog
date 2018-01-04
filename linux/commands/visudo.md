# visudo

root把本来只能超级用户执行的命令赋予普通的用户执行，shudo的操作对象是系统命令。

### visudo

visudo实际上修改的是`/etc/sudoers`文件。  

示例：  

```
root    ALL=(ALL)       ALL
用户名   被管理主机的地址=(可使用的身份)  授权命令(绝对路径)
wheel   ALL=(ALL)       ALL
组名   被管理主机的地址=(可使用的身份)  授权命令(绝对路径)
```


