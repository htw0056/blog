# Shell 字符串

#### 单引号

`str='this is a string'`

- 单引号里的任何字符都会原样输出，单引号字符串中的变量是无效的；
- 单引号字串中不能出现单引号（对单引号使用转义符后也不行）。


#### 双引号

```
your_name='qinjx'
str="Hello, I know your are \"$your_name\"! \n"
```

- 双引号里可以有变量
- 双引号里可以出现转义字符

#### 拼接字符串

```
your_name="qinjx"
greeting="hello, "$your_name" !"
greeting_1="hello, ${your_name} !"
echo $greeting $greeting_1
```

#### 获取字符串长度

```string="abcd"
echo ${#string} #输出 4
```

#### 提取子字符串

```
以下实例从字符串第 2 个字符开始截取 4 个字符：
string="runoob is a great site"
echo ${string:1:4} # 输出 unoo
注意:下标从0开始
```

#### 模式匹配运算符

**此处的匹配都是使用通配符，而不是正则**

变量运算符|替换
---|---
${varname#pattern}|如果模式匹配变量取值的开头处，则删除匹配的最短部分，并返回剩下部分
${varname##pattern}|如果模式匹配变量取值的开头处，则删除匹配的最长部分，并返回剩下部分
${varname%pattern}|如果模式匹配变量取值的结尾处，则删除匹配的最短部分，并返回剩下部分
${varname%%pattern}|如果模式匹配变量取值的结尾处，则删除匹配的最长部分，并返回剩下部分
${varname/pattern/string} ${varname//pattern/string}|将varname中匹配模式的最长部分替换为string。第一种格式中，只有匹配的第一部分被替换；第二种格式中，varname中所有匹配的部分都被替换。如果模式以#开头，则必须匹配varname的开头，如果以%开头，则必须匹配varname的结尾。如果string为空，匹配部分被删除。如果varname为@或*，操作被一次应用于每个位置参数，并且扩展为结果列表






```
string="/home/test/test.long.file.name"

echo ${string#/*/}	
#test/test.long.file.name

echo ${string##/*/}	
#test.long.file.name

echo ${string%.*}	
#/home/test/test.long.file

echo ${string%%.*}	
#/home/test/test

echo ${string/test/TEST}	
#/home/TEST/test.long.file.name

echo ${string//test/TEST}	
#/home/TEST/TEST.long.file.name
```




# array

#### 定义数组

在Shell中，用括号来表示数组，数组元素用"空格"符号分割开。定义数组的一般形式为：
`数组名=(值1 值2 ... 值n)`

#### 读取数组

读取数组元素值的一般格式是：
`${数组名[下标]}`

使用@符号可以获取数组中的所有元素，例如：
`echo ${array_name[@]}`

#### 获取数组的长度
获取数组长度的方法与获取字符串长度的方法相同，例如：
```
# 取得数组元素的个数
length=${#array_name[@]}
# 或者
length=${#array_name[*]}
# 取得数组单个元素的长度
lengthn=${#array_name[n]}
```