# grep

内容根据鸟哥的linux私房菜编写。


### 用法

```
grep [选项] '搜寻字符串' 文件名
```

选项|作用
---|---
-n|输出行号
-v|反向选择，就是输出没有匹配上搜寻字符串的内容
-i|或略大小写
--color=auto |可以将找到的关键词部分加上颜色显示
-e|扩展正则,同egrep
-A|后面可加数字,为 after 的意思,除了列出该行外,后续的 n 行也列出来
-B|后面可加数字,为 before 的意思,除了列出该行外,前面的 n 行也列出来


### 使用演示


##### 准备:

设置好语系: export LANG=C  
设置别名:grep='grep --color=auto'  
准备文本:regular_express.txt  
  
```
"Open Source" is a good mechanism to develop programs.
apple is my favorite food.
Football game is not use feet only.
this dress doesn't fit me.
However, this dress is about $ 3183 dollars.
GNU is free air not free beer.
Her hair is very beauty.
I can't finish the test.
Oh! The soup taste good.
motorcycle is cheap than car.
This window is clear.
the symbol '*' is represented as start.
Oh! My god!
The gd software is a library for drafting programs.
You are the best is mean you are the no. 1.
The world <Happy> is the same with "glad".
I like dog.
google is the best tools for search keyword.
goooooogle yes!
go! go! Let's go.
# I am VBird
```

##### 1. 查找特定字符串

```
grep -n 'the' regular_express.txt
```

##### 2. 反向特定字符串

```
grep -vn 'the' regular_express.txt
```

##### 3. 忽略大小写

```
grep -in 'the' regular_express.txt
```

##### 4. 查找条件的前后文

```
grep -n -B1 -A1 'yes' regular_express.txt
```


