# locale

locale这个单词中文翻译成地区或者地域，其实这个单词包含的意义要宽泛很多。Locale是根据计算机用户所使用的语言，所在国家或者地区，以及当地的文化传统所定义的一个软件运行时的语言环境。


```
[root@master ~]# locale
LANG=zh_CN.UTF-8
LC_CTYPE="zh_CN.UTF-8"
LC_NUMERIC="zh_CN.UTF-8"
LC_TIME="zh_CN.UTF-8"
LC_COLLATE="zh_CN.UTF-8"
LC_MONETARY="zh_CN.UTF-8"
LC_MESSAGES="zh_CN.UTF-8"
LC_PAPER="zh_CN.UTF-8"
LC_NAME="zh_CN.UTF-8"
LC_ADDRESS="zh_CN.UTF-8"
LC_TELEPHONE="zh_CN.UTF-8"
LC_MEASUREMENT="zh_CN.UTF-8"
LC_IDENTIFICATION="zh_CN.UTF-8"
LC_ALL=
```

locale把按照所涉及到的文化传统的各个方面分成12个大类，这12个大类分别是： 

1. 语言符号及其分类(LC_CTYPE) 
2. 数字(LC_NUMERIC) 
3. 比较和排序习惯(LC_COLLATE) 
4. 时间显示格式(LC_TIME) 
5. 货币单位(LC_MONETARY) 
6. 信息主要是提示信息,错误信息,状态信息,标题,标签,按钮和菜单等(LC_MESSAGES) 
7. 姓名书写方式(LC_NAME) 
8. 地址书写方式(LC_ADDRESS) 
9. 电话号码书写方式(LC_TELEPHONE) 
10. 度量衡表达方式 (LC_MEASUREMENT) 
11. 默认纸张尺寸大小(LC_PAPER) 
12. 对locale自身包含信息的概述(LC_IDENTIFICATION)

所以说，locale就是某一个地域内的人们的语言习惯和文化传统和生活习惯。一个地区的locale就是根据这几大类的习惯定义的，这些locale定 义文件放在/usr/share/i18n/locales目录下面，例如en_US, zh_CN and de_DE@euro都是locale的定义文件，这些文件都是用文本格式书写的，你可以用写字板打开，看看里边的内容，当然除了有限的注释以外，大部分 东西可能你都看不懂，因为是用的Unicode的字符索引方式。

```
[root@master locales]# cd /usr/share/i18n/locales/
[root@master locales]# ls
aa_DJ        en_HK           id_ID               sa_IN
aa_ER        en_IE           ig_NG               sc_IT
aa_ER@saaho  en_IE@euro      ik_CA               sd_IN
aa_ET        en_IN           is_IS               sd_IN@devanagari
af_ZA        en_NG           iso14651_t1         se_NO
am_ET        en_NZ           iso14651_t1_common  shs_CA
an_ES        en_PH           iso14651_t1_pinyin  sid_ET
...
```

**注意:**对于de_DE@euro的一点说明，@后边是修正项，也就是说你可以看到两个德国的locale：/usr/share/i18n/locales /de_DE@euro和/usr/share/i18n/locales/de_DE。打开这两个locale定义，你就会知道它们的差别在于 de_DE@euro使用的是欧洲的排序、比较和缩进习惯，而de_DE用的是德国的标准习惯。 

上面我们说到了zh_CN.GB18030的前半部分，后半部分是什么呢？大部分Linux用户都知道是系统采用的字符集。 

zh_CN.GB2312到底是在说什么？ Locale是软件在运行时的语言环境, 它包括语言(Language), 地域 (Territory) 和字符集(Codeset)。一个locale的书写格式为: 语言[_地域[.字符集]]。所以说呢，locale总是和一定的字符集相联系的。下面举几个例子： 

1. 我说中文，身处中华人民共和国，使用国标2312字符集来表达字符。zh_CN.GB2312＝中文_中华人民共和国＋国标2312字符集。 

2. 我说中文，身处中华人民共和国，使用国标18030字符集来表达字符。zh_CN.GB18030＝中文_中华人民共和国＋国标18030字符集。 

其中，与中文输入关系最密切的就是LC_CTYPE，LC_CTYPE规定了系统内有效的字符以及这些字符的分类， 诸如什么是大写字母，小写字母，大小写转换，标点符号、可打印字符和其他的字符属性等方面。而locale定 义zh_CN中最最重要的一项就是定义了汉字(Class“hanzi”)这一个大类，当然也是用Unicode描述的，这就让中文字符在Linux系统 中成为合法的有效字符，而且不论它们是用什么字符集编码的。 

### 如何设定locale

设定locale就是设定12大类的locale分类属性，即12个LC_*。除了这12个变量可以设定以外，为了简便起见，还有两个变量：LC_ALL和LANG。它们之间有一个优先级的关系：LC_ALL > LC_* >LANG。可以这么说，LC_ALL是最上级设定或者强制设定，而LANG是默认设定值。 

1. 如果你设定了LC_ALL＝zh_CN.UTF-8，那么不管LC_*和LANG设定成什么值，它们都会被强制服从LC_ALL的设定，成为 zh_CN.UTF-8。 

2. 假如你设定了LANG＝zh_CN.UTF-8，而其他的LC_*=en_US.UTF-8，并且没有设定LC_ALL的话，那么系统的locale设定以LC_*=en_US.UTF-8。 

3. 假如你设定了LANG＝zh_CN.UTF-8，而其他的LC_*，和LC_ALL均未设定的话，系统会将LC_*设定成默认值，也就是LANG的值zh_CN.UTF-8。 

4. 假如你设定了LANG＝zh_CN.UTF-8，而其他的LC_CTYPE=en_US.UTF-8，其他的LC_*，和LC_ALL均未设定的话， 那么系统的locale设定将是：LC_CTYPE=en_US.UTF-8，其余的 LC_COLLATE，LC_MESSAGES等等均会采用默认值，也就是 LANG的值，也就是LC_COLLATE＝LC_MESSAGES＝……＝ LC_PAPER＝LANG＝zh_CN.UTF-8。 

所以，locale是这样设定的： 

1. 如果你需要一个纯中文的系统的话，设定LC_ALL= zh_CN.XXXX，或者LANG=zh_CN.XXXX都可以，当然你可以两个都设定，但正如上面所讲，LC_ALL的值将覆盖所有其他的locale设定，不要作无用功。 

2. 如果你只想要一个可以输入中文的环境，而保持菜单、标题，系统信息等等为英文界面，那么只需要设定 LC_CTYPE＝zh_CN.XXXX，LANG=en_US.XXXX就可以了。这样LC_CTYPE＝zh_CN.XXXX，而LC_COLLATE＝LC_MESSAGES＝……＝ LC_PAPER＝LANG＝en_US.XXXX。 

3. 假如你高兴的话，可以把12个LC_*一一设定成你需要的值，打造一个古灵精怪的系统： LC_CTYPE＝zh_CN.GBK/GBK(使用中文编码内码GBK字符集)； LC_NUMERIC=en_GB.ISO-8859-1(使用大不列颠的数字系统) LC_MEASUREMEN=de_DE@euro.ISO-8859-15(德国的度量衡使用ISO-8859-15字符集) 罗马的地址书写方式，美国的纸张设定……。估计没人这么干吧。 

4. 假如你什么也不做的话，也就是LC_ALL，LANG和LC_*均不指定特定值的话，系统将采用POSIX作为lcoale，也就是C locale。
