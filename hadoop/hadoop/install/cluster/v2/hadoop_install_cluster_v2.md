# hadoop分布式安装(进阶版)

基础设置请参考基础版。

### 架构

主机名|IP|进程
---|---|---
master|192.168.134.211|NameNode,JobHistoryServer,DataNode,NodeManager
slave01|192.168.134.212|ResourceManager,WebAppProxyServer,DataNode,NodeManager
slave02|192.168.134.213|SecondaryNameNode,DataNode,NodeManager


### 配置文件

具体的配置文件请参考conf文件夹下的文件。

配置文件|配置的服务
---|---
core-site.xml|NameNode
hdfs-site.xml|SecondaryNameNode
mapred-site.xml|JobHistoryServer
yarn-site.xml|ResourceManager,WebAppProxyServer,NodeManager
slaves|DataNode
hadoop-env.sh|-

### 启动服务

主机|启动服务
---|---
master|`hdfs namenode -format`,`start-dfs.sh`,`mr-jobhistory-daemon.sh start historyserver`
slave01|`start-yarn.sh`,`yarn-daemons.sh start proxyserver`
slave02|-


### 节点增加

在开启NameNode和ResourceManager节点中修改slave，添加新增节点，重启服务即可。

### 节点删除

静态删除可以直接删除slaves节点中的配置，重启服务即可。

动态删除，需要在开启NameNode的节点中修改dfs-site.xml中`dfs.hosts.exclude`和ResourceManager节点中修改yarn-site.xml中`yarn.resourcemanager.nodes.exclude-path`
