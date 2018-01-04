# gawk


### 命令格式

`gawk options program file`

选项|作用
----|----
-F|指定分隔符
-f file|指定读取程序的文件名
-v var=value|指定gawk程序中的一个变量


### demo


###### 1

```
gawk '
BEGIN {print "hello"; FS=":"}
{print $1}
END {print "end"}' /etc/passwd
```

在gawk中，$0表示整个文本行，$1表示第1个数据字段，$n表示第n个数据字段。


###### 2 -F的使用


```
gawk -F : '{print $1}' /etc/passwd
```

###### 3 -f的使用

```
cat script2

BEGIN {
print "hello"
}
{
print $1
}
END {
print "end"
}

gawk -F : -f script2 /etc/passwd
```


