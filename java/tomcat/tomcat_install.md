# tomcat安装配置

### 1. 下载tomcat二进制包

下载[apache-tomcat-7.0.70.tar.gz](http://tomcat.apache.org/download-70.cgi)

### 2. 解压tar包

```
tar -zxvf apache-tomcat-7.0.70.tar.gz
```

### 3. 修改配置

```
vim apache-tomcat-7.0.70/conf/server.xml
修改:
<Connector port="8080" protocol="HTTP/1.1"
                connectionTimeout="20000"
                redirectPort="8443" URIEncoding="UTF-8" />
```

还能进行其他的设置，根据需要设定：


| 配置                   | 作用                                       |
| -------------------- | ---------------------------------------- |
| maxThreads           | 客户请求最大线程数                                |
| minSpareThreads      | Tomcat初始化时创建的socket线程数                   |
| maxSpareThreads      | Tomcat连接器的最大空闲socket线程数                  |
| enableLookups        | 若设为true,则支持域名解析，可把ip地址解析为主机名             |
| redirectPort         | 在需要基于安全通道的场合，把客户请求转发到基于ssl的redirectPort端口 |
| acceptAccount        | 监听端口队列最大数，满了之后客户请求会被拒绝                   |
| connectionTimeout    | 连接超时                                     |
| minProcessors        | 服务器创建时的最小处理线程数                           |
| maxProcessors        | 服务器同时最大处理线程数                             |
| URIEncoding          | URL统一编码                                  |
| compression          | 若为on，表示启用压缩                              |
| compressionMinSize   | 当超过最小数据大小时才进行压缩                          |
| compressableMimeType | 配置项压缩的数据类型，默认是text/html,text/xml,text/plain |

### 4. 启动tomcat

```
apache-tomcat-7.0.70/bin/startup.sh
```

### 5. 测试访问

在浏览器内输入`localhost:8080` 
访问成功则表示安装配置完成