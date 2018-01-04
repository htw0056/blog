# httpbin安装

因为httpbin服务是国外的，所以你用来做测试的话，速度会比较慢，可以选择在本地搭建一个httpbin服务。

### 安装httpbin

```
pip2.7 install gunicorn httpbin		#安装
gunicorn httpbin:app				#启动服务
curl -v "http://127.0.0.1:8000"		#测试
```