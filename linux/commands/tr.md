# tr


tr是translate的简写。这个命令实际上类似于sed命令的简化版。


##### 选项

参数|作用
----|----
-d|删除所有属于第一字符集的字符
-s|把连续重复的字符以单独一个字符表示
-c|取代所有不属于第一字符集的字符


##### 例子


```
a.txt

abcdefghijklmnopqrstuvwxyz
aaaafff
sdwffas
````



```
cat a.txt | tr 'a-z' 'A-Z'		#小写字母转大写
cat a.txt | tr 'adf' 'XYZ'		#指定字符转换
cat a.txt | tr '\r' '\n' >> a_new.txt 		#处理^M问题
```


```
cat a.txt | tr -d 'adf' 		#删除adf字符
cat a.txt | tr -s 'adf'			#删除连续重复的字符
cat a.txt | tr -c 'adf' '1'		#adf字符以外的全部替换成1
``` 