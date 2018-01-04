# touch

### 语法

`touch [选项] filename`

### 选项

选项|作用
---|---
-a|或--time=atime或--time=access或--time=use 只更改存取时间
-c|或--no-create 不建立任何文件 
-d|<时间日期> 使用指定的日期时间，而非现在的时间
-f|此参数将忽略不予处理，仅负责解决BSD版本touch指令的兼容性问题
-m|或--time=mtime或--time=modify 只更该变动时间
-r|<参考文件或目录> 把指定文件或目录的日期时间，统统设成和参考文件或目录的日期时间相同
-t|<日期时间> 使用指定的日期时间，而非现在的时间
--help|在线帮助
--version|显示版本信息。
