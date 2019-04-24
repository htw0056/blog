# VIM文件编码

### 1. Vim相关设置

和所有的流行文本编辑器一样，Vim 可以很好的编辑各种字符编码的文件，这当然包括UCS-2、UTF-8 等流行的 Unicode 编码方式。然而不幸的是，和很多来自 Linux 世界的软件一样，这需要你自己动手设置。 
Vim 有四个跟字符编码方式有关的选项:encoding、fileencoding、fileencodings、termencoding。

#### enc(encoding)

**Vim内部使用的字符编码方式**，包括 Vim 的 buffer (缓冲区)、菜单文本、消息文本等。默认是根据你的locale选择.用户手册上建议只在 .vimrc 中改变它的值，事实上似乎也只有在.vimrc 中改变它的值才有意义。Vim 在工作的时候，如果编码方式与它的内部编码不一致，它会先把编码转换成内部编码。也就是你可以用另外一种编码来编辑和保存文件，如你的vim的encoding为utf-8,所编辑的文件采用cp936(GBK)编码,vim会自动将读入的文件转成utf-8(vim的能读懂的方式），而当你写入文件时,又会自动转回成cp936（文件的保存编码).

#### fenc(fileencoding)

当 Vim 从磁盘上读取文件的时候，会对文件的编码进行探测。如果文件的编码方式和 Vim 的内部编码方式不同，Vim 就会对编码进行转换。转换完毕后，Vim 会将 `fileencoding` 选项**设置为文件的编码**。当 Vim 存盘的时候，如果 `encoding` 和`fileencoding` 不一样，Vim 就会进行编码转换。因此，通过打开文件后设置 `fileencoding`，我们可以将文件由一种编码转换为另一种编码。但是，由前面的介绍可以看出，`fileencoding` 是在打开文件的时候，由 Vim 进行探测后自动设置的。因此，如果出现乱码，我们无法通过在打开文件后重新设置 `fileencoding` 来纠正乱码。

#### fencs(fileencodings)

编码的自动识别是通过设置 fileencodings 实现的，注意是复数形式。fileencodings 是一个用逗号分隔的列表，列表中的每一项是一种编码的名称。当我们打开文件的时候，VIM 按顺序使用 fileencodings 中的编码进行尝试解码，如果成功的话，就使用该编码方式进行解码，并将 `fileencoding` 设置为这个值，如果失败的话，就继续试验下一个编码。

因此，我们在设置 `fileencodings` 的时候，一定要把要求严格的、当文件不是这个编码的时候更容易出现解码失败的编码方式放在前面，把宽松的编码方式放在后面。

例如，latin1 是一种非常宽松的编码方式，任何一种编码方式得到的文本，用 latin1 进行解码，都不会发生解码失败——当然，解码得到的结果自然也就是理所当然的“乱码”。因此，如果你把 `latin1` 放到了 `fileencodings` 的第一位的话，打开任何中文文件都是乱码也就是理所当然的了。

#### tenc(termencoding)

`termencoding` 是 Vim 用于屏幕显示的编码，在显示的时候，Vim 会把内部编码转换为屏幕编码，再用于输出。内部编码中含有无法转换为屏幕编码的字符时，该字符会变成问号，但不会影响对它的编辑操作。如果 `termencoding` 没有设置，则直接使用 `encoding` 不进行转换。

举个例子，当你在 Windows 下通过 telnet 登录 Linux 工作站时，由于 Windows 的 telnet 是 GBK 编码的，而 Linux 下使用 UTF-8 编码，你在 telnet 下的 Vim 中就会乱码。此时有两种消除乱码的方式：一是把 Vim 的 `encoding` 改为 `gbk`，另一种方法是保持 `encoding` 为 `utf-8`，把 `termencoding` 改为 `gbk`，让 Vim 在显示的时候转码。显然，使用前一种方法时，如果遇到编辑的文件中含有 GBK 无法表示的字符时，这些字符就会丢失。但如果使用后一种方法，虽然由于终端所限，这些字符无法显示，但在编辑过程中这些字符是不会丢失的。

### 2. Vim工作方式

1. Vim 启动，根据 .vimrc 中设置的 encoding 的值来设置 buffer、菜单文本、消息文的字符编码方式。 
2. 读取需要编辑的文件，根据 fileencodings 中列出的字符编码方式逐一探测该文件编码方式。并设置 fileencoding 为探测到的，看起来是正确的字符编码方式。 
3. 对比 fileencoding 和 encoding 的值，若不同则调用 iconv 将文件内容转换为encoding 所描述的字符编码方式，并且把转换后的内容放到为此文件开辟的 buffer 里，此时我们就可以开始编辑这个文件了。
4. 编辑完成后保存文件时，再次对比 fileencoding 和 encoding 的值。若不同，再次调用 iconv 将即将保存的 buffer 中的文本转换为 fileencoding 所描述的字符编码方式，并保存到指定的文件中。由于 Unicode 能够包含几乎所有的语言的字符，而且 Unicode 的 UTF-8 编码方式又是非常具有性价比的编码方式 (空间消耗比 UCS-2 小)，因此建议 encoding 的值设置为utf-8。这么做的另一个理由是 encoding 设置为 utf-8 时，Vim 自动探测文件的编码方式会更准确 (或许这个理由才是主要的)。

### 3. Vimrc配置

```
set encoding=utf-8
set fileencodings=ucs-bom,utf-8,utf-16,gbk,big5,gb18030,latin1
set fileencoding=utf-8
```

### 4. 利用VIM进行编码转换

| 命令                                                         | 作用                                                         |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| set fileencoding                                             | 查看当前打开文件的编码格式(vim解析出来的编码根式)            |
| set fileencoding encoding=(utf-8/gb18030/..)                 | 设置之后，保存后的文件为改编码格式；<br />如果本身文件打开时就是乱码，此时该操作无法起到修改编码格式的作用 |
| :e ++enc=someencoding somefile<br />or ( vim file.txt -c "e ++enc=GB18030") | 使用某种编码方式打开某文件                                   |

> 1. 编码转换命令: `iconv` 
> 2. mac系统中存在 简体中文(mac os) 的编码格式(是一种移位的 GB2312)，此时最好使用系统自带的编辑器来处理文件(or 从编辑器中拷贝出来，使用其他文本编辑器处理)