# HTTP

### 概念

HTTP(Hyper Text Transfer Protocol,超文本传输协议)是一种通信协议，它允许将超文本标记语言(HTML)文档从Web服务器传送到客户端的浏览器。  
它是一个应用层协议，承载于TCP协议之上  
由请求和响应构成，是一个标准的客户端服务器模型  

### URI,RUL,RUN

- URI:Uniform Resource Identifier,统一资源标示符
- RUL:Uniform Resource Locator,统一资源定位符
- RUN:Uniform Resource Name,统一资源名称



### URI

#### 格式：http://user:pass@www.example.com:80/home/index.html?age=18#mask

格式|含义
---|---
http|协议名
user:pass|登录信息
www.example.com|服务器地址
80|端口号
/home/index.html|文件路径
age=18|查询字符串
mask|片段标示符


### GET和POST区别

1. GET用于信息获取，它是安全的（这里的安全含义是指非修改信息），而POST是用于修改服务器上资源的请求
2. GET请求的数据会附在URL之后，而POST把提交的数据则放置在HTTP报文实体的主体里，所以，POST的安全性要比GET的安全性高








