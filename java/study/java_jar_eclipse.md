# eclipse jar打包

### 两种方式

eclipse提供了两种方式打jar包:一种是jar file，另一种是runnable jar file。 

1. 目前我所知道的区别就是，用jar file打包要自己写manifest.mf(如果没有第三方库的引用，那就可以选择自动生成manifest.mf文件)，然后生成的jar包需要依赖外部的库(也就是你打好的包需要和第三方库放在一起) 
2. 使用runnable jar file 打包的话，只有一个jar包，可直接执行

### 1. jar file打包

1. 在当前目录下写MANIFEST.MF文件

   ```
   Manifest-Version: 1.0
   Main-Class: com.htw0056.test.Test
   Class-Path: lib/gson-2.2.4.jar lib/dom4j-1.6.1.jar
   ```
   **注意**

   1. manifest.mf文件最后一行必须是一个空行
   2. lib/gson-2.2.4.jar和lib/dom4j-1.6.1.jar之间用一个空格隔开
   3. 每个冒号后有一个空格

2. 右键项目->export->java->jar file->选择需要的文件->选择jar包的生成位置->next->next->手动选择MANIFEST.MF->finish

3. 在jar包所在的目录，放lib文件(里面包含指定的jar包)

### 2. runnable jar file打包

比较简单，直接打包即可。
