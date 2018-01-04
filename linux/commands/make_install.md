# Linux编译安装中configure、make和make install各自的作用

这些都是典型的使用GNU的AUTOCONF和AUTOMAKE产生的程序的安装步骤。 
 
1. ./configure是用来检测你的安装平台的目标特征的。比如它会检测你是不是有CC或GCC，**并不是需要CC或GCC，它是个shell脚本**。
2. make是用来编译的，**编译是需要gcc的**。它从Makefile中读取指令，然后编译。
3. make install是用来安装的，它也从Makefile中读取指令，安装到指定的位置。

#### 详细说明

1. configure

   这一步一般用来生成 Makefile，为下一步的编译做准备，你可以通过在 configure 后加上参数来对安装进行控制，比如代码:  
`./configure --prefix=/usr`  
上面的意思是将该软件安装在 /usr 下面，执行文件就会安装在 /usr/bin （而不是默认的 /usr/local/bin)，资源文件就会安装在 /usr/share（而不是默认的/usr/local/share）。  
   同时一些软件的配置文件你可以通过指定 --sys-config= 参数进行设定。有一些软件还可以加上 --with、--enable、--without、--disable 等等参数对编译加以控制，你可以通过允许 ./configure --help 察看详细的说明帮助。
   
2. make

   这一步就是编译，大多数的源代码包都经过这一步进行编译。  
   make 的作用是开始进行源代码编译，以及一些功能的提供，这些功能由他的 Makefile 设置文件提供相关的功能，比如 make install 一般表示进行安装，make uninstall 是卸载，**不加参数就是默认的进行源代码编译**。
   
3. make install

   这条命令来进行安装（当然有些软件需要先运行 make check 或 make test来进行一些测试），这一步一般需要你有 root 权限（因为要向系统写入文件）
   

#### 疑问

##### 1. make 和 make install 中的mark是系统自带的命令还是可执行程序文件？make install中，是不是可以认为 install是make的参数？  

**install 不是make的参数**，而是在makefile（Makefile）中有如：install:的语句。如果用make install，那么就执行install:后面的语句。

##### 2. Makefile是什么东东？有什么用？怎么用？

makefile是用于自动编译和链接的，一个工程有很多文件组成，每一个文件的改变都会导致工程的重新链接-----但是不是所有的文件都需要重新编译，makefile能够纪录文件的信息，决定在链接的时候需要重新编译哪些文件！



#### 扩展

了解过以上的信息，对于平时的编译安装基本就没有疑问了。但是如果你还想深入了解：Makefile具体内容？格式？那么一定要去看看[阮一峰 Make 命令教程](http://www.ruanyifeng.com/blog/2015/02/make.html)!简单易懂！！