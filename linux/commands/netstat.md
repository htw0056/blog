# netstat

参数|作用
----|----
-a|显示素有选项，默认不显示LISTEN相关
-t|仅显示tcp相关选项
-u|仅显示udp相关选项
-n|拒绝显示别名，能显示数字的全部转化成数字
-l|仅列出有在LISTEN（监听）的服务状态


命令|作用
----|----
`netstat -a`|列出所有端口
`netstat -at`|列出所有tcp端口
`netstat -au`|列出所有udp端口
`netstat -l`|只列出监听端口
`netstat -lt`|只列出监听的tcp端口
`netstat -lu`|只列出监听的udp端口
`netstat -lx`|只列出监听的unix端口
