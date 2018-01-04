# OpenJDK源码编译

### 环境

1. linux系统:centos6.5
2. OpenJDK源码版本:openjdk-7u6-fcs-src-b24-28_aug_2012

### 准备

1. [openjdk-7u6-fcs-src-b24-28_aug_2012.zip 源码下载](http://www.java.net/download/openjdk/jdk7u6/promoted/b24/openjdk-7u6-fcs-src-b24-28_aug_2012.zip)
2. [jdk-7u4-linux-x64.tar.gz 下载](http://www.oracle.com/technetwork/java/javase/downloads/java-archive-downloads-javase7-521261.html)

	> 注意: 因为OpenJDK的各个组成部分有的是使用c++编写的，更多的代码则是使用java自身实现的，因此编译这些java代码需要用到一个可用的jdk，官方称这个jdk为"Bootstrap JDK"。如果编译OpenJDK7，Bootstrap JDK必须使用JDK6 Update14 或以后的版本。在此我选择了jdk7u4.
	
3. [freetype-2.3.12.tar.gz 下载](https://sourceforge.net/projects/freetype/files/freetype2/)
4. [apache-ant-1.8.2-bin.zip 下载](http://archive.apache.org/dist/ant/binaries/)

### 安装

##### 1. 安装必要的依赖

```
yum -y install gcc gcc-c++ alsa-lib alsa-lib-devel libX*  cups cups-devel
```

##### 2. 解压下载的文件到/usr/local

解压到/usr/local之后的目录:

```
/usr/local/apache-ant-1.8.2
/usr/local/freetype-2.3.12
/usr/local/jdk1.7.0_04
/usr/local/openjdk
...
```

对于freetype-2.3.12，还需要单独编译安装。

```
cd /usr/local/freetype-2.3.12
./configure
make
make install
```

##### 3. 编译安装前的配置

```
修改/usr/local/openjdk/jdk/src/share/classes/java/util/CurrencyData.properties
将该文件里的所有日期距离今天超过10年的，全部改成10年内的日期。
否则后续openjdk源码编译会出错。
```


```
修改 ~/.bash_profile
添加:
export LANG="C"
export ALT_BOOTDIR="/usr/local/jdk1.7.0_04"
export ANT_HOME="/usr/local/apache-ant-1.8.2"
export ALT_FREETYPE_HEADERS_PATH="/usr/local/include/freetype2"
export ALT_FREETYPE_LIB_PATH="/usr/local/lib"
export ALLOW_DOWNLOADS=true
export SKIP_DEBUG_BUILD=false
export SKIP_FASTDEBUG_BUILD=true
export DEBUG_NAME=debug
unset JAVA_HOME
unset CLASSPATH

并且使之生效
. ~/.bash_profile
```


##### 4. 编译

```
cd /usr/local/openjdk 	#进去openjdk目录

make sanity		#测试环境是否健全,成功可以看到Sanity check passed.

make all		#编译安装
```


最后编译成功，可以看到:

```
#-- Build times ----------
Target debug_build
Start 2016-07-25 23:39:02
End   2016-07-26 00:19:18
00:04:19 corba
00:10:08 hotspot
00:00:41 jaxp
00:00:47 jaxws
00:22:55 jdk
00:01:26 langtools
00:40:16 TOTAL
-------------------------
make[1]: Leaving directory `/usr/local/openjdk'
```

##### 5. 测试


```
vim Test.java
public class Test{
        public static void main(String[] args){
                System.out.print("Hello World!");
        }
}


/usr/local/openjdk/build/linux-amd64/bin/javac Test.java		#编译
/usr/local/openjdk/build/linux-amd64/bin/java Test		#运行
```

##### 6. 修改源码重新编译测试

```
vim /usr/local/openjdk/jdk/src/share/classes/java/io/PrintStream.java
```

修改:

```
  public void print(String s) {
      if (s == null) {
          s = "null";
      }
      s = s + "Test!!";  // 重新赋值
      write(s);
  }
```

重新编译并根据步骤5测试。



### 问题


##### 1. bootstrap jdk使用版本过旧

刚开始bootstrap jdk使用了jdk6，结果后来发现lib下的tools.jar是以tools.pack形式存在，可以使用bin下的工具unpack200将其转化回tools.jar。不过我转过之后还出错，所以后来直接换新一点版本的jdk。

由此，也极力推荐在安装了jdk之后，对其进行测试，看其是否能正常使用。

```
/usr/local/jdk1.7.0_04/bin/java -version
```


##### 2. bootstrap jdk使用版本过新

这次一狠心，直接用了jdk7u80最新的版本，结果又出错了。我使用的openjdk是7u6，但是bootstrap jdk比7u6要新，导致用新版本的虚拟机去跑旧的openjdk，出问题了。最后换到了jdk7u4才成功。

过犹不及！

##### 3. 编译时日期出错


```
Error: time is more than 10 years from present: 1120165200000
java.lang.RuntimeException: time is more than 10 years from present: 1120165200000
        at build.tools.generatecurrencydata.GenerateCurrencyData.makeSpecialCaseEntry(GenerateCurrencyData.java:285)
        at build.tools.generatecurrencydata.GenerateCurrencyData.buildMainAndSpecialCaseTables(GenerateCurrencyData.java:225)
        at build.tools.generatecurrencydata.GenerateCurrencyData.main(GenerateCurrencyData.java:154)
```

通过修改CurrencyData.properties文件, 把10年之前的时间修改为10年之内即可。