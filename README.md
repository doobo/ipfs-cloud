# ipfs-cloud
一个基于IPFS的星际文件系统，也是分布式的WebScoket服务端，基于区块链生成唯一标识，可用于文件服务器、数据签名、CDN服务、边缘实时计算、MQ消息服务等

> [说明文档](https://gitee.com/doobo/ipfs-cloud/wikis)
> 博客类小程序源码，快速开通流量主，中台图片上传基于IPFS实现，[体验地址](https://gitee.com/doobo/min-pro)

## 使用场景
### 文件服务器
启动ipfs-client，相当于启动了一个ipfs节点，节点间文件自动切割分发，多个区域内，可通过ipfs-client实现文件互通，并保证文件不可篡改，
如果启用了ipfs-search-es模块，可针对文件类型、名称、大小等信息，进行文件搜索查询

### 数据签名
可上传JSON文件到ipfs-client，生成唯一CID，再把生成CID带到请求header上，第三方系统，可通过CID来判断数据是否被修改

### CDN服务
多区域启用ipfs-client，各子区域上传的文件，其它区域可互相访问，节点越多速度越快，可快速搭建CDN服务

### 分布式webscoket服务
还在为webscoket链接数限制而烦恼吗，启用ipfs-client就相当启动了一个分布式的webscoket服务，可水平无限扩容webscoket连接数，
并且不需要特殊处理，就能广播、或定向发送消息到客户端

### 边缘实时计算
* 各个分组和消费端多有node节点ID,通过ipfs能互通
* 每个节点保证能和公网的ipfs节点通信，就能保证各个区域的节点能互相通信
* PTP计算，先广播需要的分组信息，分组接到广播信息后，定向广播本节点信息
* 广播计算，先查询有没有分组，然后指定分组广播
* 消费端可定时刷新分组信息，新分组进入时，可广播入场信息，离场时，广播离开信息

### MQ消息服务
还在构思中，主要基于ipfs广播订阅功能，可实现基于消息确认机制的MQ、无状态MQ中间件等功能

## 模块简单介绍
### ipfs-client
分布式客户端，单独启动，自动组网，内网启动一台即可快速调用ipfs相关的API接口，快速进行文件上传下载,
文件上传后，会调用ipfs进行文件同步，同时上传文件的接口会返回文件的cid，并在client端保存一份'文件名'+'.ipfs'的文件(可配置不生成)

## 系统兼容性
目前只上传来win64,linux64,macOs的ipfs的程序,考虑作为文件服务器,个人精力有限,暂不考虑其它环境.如需要实现其它环境,
可以在ipfs-cloud-platform下自定义相关的包,并写好初始化方法即可.

## 代码地址
* [ipfs-cloud-码云](https://gitee.com/doobo/ipfs-cloud)
* [ipfs-cloud-github](https://github.com/doobo/ipfs-cloud)
* [ipfs-cloud模块图](https://api.5fu8.com/template/link/go/rYzqmi)

## 部署
### 简单测试
启动ipfs-client即可，通过浏览器访问:http://127.0.0.1:6103/
,上传文件后，接口会返回cid，用cid访问文件，如：http://127.0.0.1:18080/ipfs/Qm...

> 所有区域推荐使用内网获取，这样会自动使用p2p传输，如：http://localhost:18080/ipfs/Qm...或者ipfs cat Qm...

### 北京、上海、广州、香港、北美、欧洲组网
> 文件上传尽可能使用本地的ipfs-client进行上传，会自动通过ipfs分发到全网
> ,为提高网络传输效率，上传文件后，ipfs-client不要立即停机，防止其它区域在同步文件时，访问不到文件

```
每个有web的模块多可以单独启动，不依赖注册中心，各个模块之间使用广播进行数据通信
```

## 打包命令
```
mvn clean
mvn install

//启动ipfs-client
java -jar -Dserver.port=9051 -Dipfs.port=9052 -Dipfs.adminPort=9053 -Dipfs.httpPort=9054 ipfs-client-1.1-SNAPSHOT.jar

//系统守护启动搜索服务
nohup java -jar -Dserver.port=9057 -Dipfs.port=9052 -Dipfs.adminPort=9053 -Dipfs.httpPort=9054 -Dipfs.startDaemon=false ipfs-search-es-1.1-SNAPSHOT.jar > /dev/null 2>&1 &
```

## IPFS讨论互助群
![QQ群.jpeg](http://cdn.5fu8.com/ipfs/QmQaeZXpSKZYnrvyuKKPGXkcDZTqyA9cdKuegH4qr3AMgr)