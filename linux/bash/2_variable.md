# 1.变量

#### 1.1 声明变量

```
a=1
b='aaa'
c="dd"
```

#### 1.2 声明系统变量

```
export a
export a="abc"
```

#### 1.3 查看变量

```
set
set -u  #如果设定此选项，调用未声明变量时会报错（默认无任何提示）
```

#### 1.4 查看系统变量

```
env
```

#### 1.5 使用变量

```
echo $a
echo ${b}	
```
**注意**只有使用变量值的时候才加上$,建议使用${}形式

#### 1.6 删除变量

```
unset a
unset b
```

#### 1.7 declare声明

```
declare [+/-] [选项] 变量名
```

选项|作用
---|---
-|给变量设定类型属性
+|取消变量的类型属性
-a|将变量声明为数组型
-i|将变量声明为整数型
-x|将变量声明为环境变量
-r|讲变量声明为只读变量
-p|显示指定变量的被声明的类型

# 2.输入输出

#### 2.1 echo

```
# 不带引号输出,等价于字符串两边加上""
echo afds fsdf f
echo "aaa"
echo 'abc'
echo -e "abc\tdef"     -e识别输出内容里的转意序列
echo -n "ffff"		   -n忽略行尾的换行
```

#### 2.2 printf

```
printf "abc%saa%s" $a $b   支持格式化输出
```
#### 2.3 read

```
read a
echo $a
read -p "hint" b
echo $b
```

选项|含义
---|---
-p|提示信息，在等待read输入时，输出的提示信息
-t 秒数|read命令会一直等待用户输入，使用此选项可以指定等待时间
-n 字符数|read命令只接受指定的字符数
-s|隐藏输入的数据，适用于机密信息

#### 2.4 <<

```
mysql <<!
status
select count(*) from test.test
!
```

# 3.运算符

#### 3.1 let

```
#! /bin/bash
i=1
n=3
while [ 1 ]
do
    if [ $i -le $n ]
    then
        echo "$i"
    else
        break
    fi
#let i=$i+1
   let i++
done
```

**不能计算浮点数**

#### 3.2 $[]

```
#! /bin/bash
x=1
y=$[${x}*2+1]
echo ${y}
```

**不能计算浮点数**


#### 3.2 (())

```
((a=$b+1))
```



#### 3.4 expr

```
#! /bin/bash
a=1
b=2
c=3
res1=`expr $a + $b`
res2=$( expr $a - $c )
res3=`expr $a \* $b`
echo $res1
echo $res2
echo $res3
```

- 乘法要转义,写成\*  
- 算式之间要空格
- 不能计算浮点数


#### 3.5 bc

```
res4=`echo "scale=5;$a*$b/$c" | bc`
```

or

```
!/bin/bash
var1=1.2
var2=2.2
var3=`bc << EOF
scale=4
a1=${var1}+${var2}
a1+${var2}
EOF
`
echo ${var3}
```

- 可计算浮点数

#### 3.6 expr操作字符串

```
#! /bin/bash
s1=abcd123
expr length $s1
expr substr $s1 1 2
```

