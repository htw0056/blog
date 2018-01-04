# 控制流程

## 1.if

#### 1.if

```
if condition
then
    command1 
    command2
    ...
    commandN 
fi
```
写成一行（适用于终端命令提示符）：
`if [ $(ps -ef | grep -c "ssh") -gt 1 ]; then echo "true"; fi`

#### 2.if else

```
if condition
then
    command1 
    command2
    ...
    commandN
else
    command
fi
```

#### 3.if else-if else

```
if condition1
then
    command1
elif condition2 
then 
    command2
else
    commandN
fi
```

## 2.for

```
for var in item1 item2 ... itemN
do
    command1
    command2
    ...
    commandN
done
```

c语言风格:

```
for (( expr1;expr2;expr3 ))
do
	statement
done
```

**注意，此时条件中的变量不需要以$开头**

## 3.while

```
while condition
do
    command
done
```

**在条件返回为0,时表示条件成立**

c语言风格:

```
while((expr1))
do
	statement
done
```

### 4.until

```
until condition
do
    command
done
```

**在条件返回为非0,时表示条件成立**

#### 5.case

```
case 值 in
模式1)
    command1
    command2
    ...
    commandN
    ;;
模式2）
    command1
    command2
    ...
    commandN
    ;;
*)
    command1
    command2
    ...
    commandN
    ;;
esac
```

