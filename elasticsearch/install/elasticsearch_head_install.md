# elasticsearch-head插件安装



### 环境

- [node-8.6.0](https://nodejs.org/en/download/current/)
- [elasticsearch-head](https://github.com/mobz/elasticsearch-head)



### 安装

##### 1. node安装

下载node二进制包，并配置环境变量即可。



##### 2. 下载并解压elasticsearch-head

```
# 下载
wget git@github.com:mobz/elasticsearch-head.git
# 解压
unzip elasticsearch-head-master.zip
```



##### 3. 安装elasticsearch-head

```
# 进入elasticsearch-head目录
cd elasticsearch-head-master
# 安装
npm install
```



##### 4. 修改elasticsearch配置文件

```
# 设置head与es的访问
vim config/elasticsearch.yml
添加：
http.cors.enabled: true
http.cors.allow-origin: "*"
```



##### 5. 运行

```
npm run start
```



##### 6. 访问

```
127.0.0.1:9100
```

