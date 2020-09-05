# ipfs-cloud

> 基于SpringCloud的IPFS私有云,主要解决文件存储分发问题,不考虑挖矿
* 内网快速搭建私有文件存储服务，能快速部署、快速访问、无限扩容、自动分发、自动备份，支持内网部署和多跨区域组网部署

## 模块简单介绍
* ipfs-client为分布式客户端,单独启动，自动组网，内网启动一台即可快速调用ipfs相关的API接口，快速进行文件上传下载,
文件上传后会自动分发到ipfs机器，上传接口会返回文件的cid，同时在client端保存一份'文件名'+'.ipfs'的文件，里面的内容是该文件的全网唯一CID编码，通过该编码可以
在任意集群的节点进行文件下载、查看等操作
* ipfs-gateway为网关，用来给客户端自动组网，组建集群时，用来自动添加网关节点，配合注册中心使用即可，客户端会自动把网关的节点加入自己的bootstrap中
这样，不同地区的子集群也能通过网关进行文件分发和获取，不同地区的子集群，可通过网关的IPFS获取任意集群内容的文件
* ipfs-search-es为文件搜索服务的es实现,可根据文件的cid、节点id、文件类型、文件名、文件内容、添加的时间范围来搜索相关文件
* ipfs-backup为文件备份服务，可单独启动，备份以年月日时作为目录，文件的cid作为文件名，保存到备份服务器，备份服务器最好能和网关共用IPFS节点，
即备份节点不启动IPFS,-Dipfs.startDaemon=false;如果是全内网部署，随便一台服务器启动即可
* ipfs-register为简单的springCloud注册中心程序，对暂没有注册中心的，可以启动一个，其它服务启动时，添加命令
-Deureka.client.serviceUrl.defaultZone=http://union:123456@127.0.0.1:6109/eureka/,即可注册进去

## 搜索和加密
* 搜索服务可基于elasticSearch来搜索，也可用基于MySQL等数据库实现，基于统一接口调用，保证实现相关接口即可，注意保证数据幂等性和搜索结构一致性。
* 加密基于客户端实现，上传时自定义密码，下载时用户输入密码解压，程序不保存任何密码

## 系统兼容性
目前只上传来win64,linux64,macOs的ipfs的程序,考虑作为文件服务器,个人精力有限,暂不考虑其它环境.如需要实现其它环境,
可以在ipfs-software的resources/lib下放入相关的ipfs包,并写好初始化方法即可.

## 国内资源备份
[ipfs-cloud-码云](https://gitee.com/doobo/ipfs-cloud)

## 部署
### 单个内网集群部署
* 启用至少一个spring cloud注册中心，测试时，可使用自带的默认注册中心
* 启动ipfs-gateway服务，该服务注册到spring cloud注册中心后，其它服务可自动发现这个ipfs节点
* 启动ipfs-backup服务，该服务用来备份文件，可启动多个，每个上传的文件多会下载到备份服务器上，需要的磁盘空间比较大，测试可不启动
* 启用ipfs-search-es服务，用来搜索文件，测试时，可以不启用
* 启用ipfs-client服务，该服务提供了内网的http接口，可调用进行文件上传，上传后会自动同步到ipfs节点，同时备份文件和添加到搜索服务器上

> 所有的服务多可以在本地节点使用相关的http地址和文件cid进行文件下载，如：http://ipfs-gateway:18080/ipfs/Qm...
> 如需要提供对外http下载，可代理到相关URL即可，集群内部文件下载，推荐使用本机的localhost:18080/ipfs/Qm...获取，这样可利用p2p传输

### 多区域集群部署,(北京、上海、广州、香港、北美、欧洲)组网
* 每个区域至少暴露一个spring cloud注册中心和一个ipfs-gateway服务，保证使用公网IP可以相互访问
* ipfs-backup服务以-Dipfs.startDaemon=false启动到ipfs-gateway所在的服务器上，保证公网可访问
* 启用ipfs-search-es服务，用来搜索文件，测试时，可以不启用,提供可外网访问的地址，如-Deureka.instance.preferIpAddress=false -Deureka.instance.hostname=bj2.5fu8.com -Deureka.instance.nonSecurePort=6107
* 启用ipfs-client服务，这个不需要暴露到外网，每个区域按需求启动即可，每个区域调用本区域的http接口进行文件上传，会通过ipfs-backup服务自动同步到全网

> 文件上传尽可能使用本地的ipfs-client进行上传，会自动通过ipfs分发到全网
> ，提高网络传输效率，上传文件后，ipfs-client不要立即停机，防止其它区域在同步文件时，访问不到文件

## 打包命令
```
mvn clean
mvn install

//启动
nohup java -jar -Dserver.port=9051 -Dipfs.port=9052 -Dipfs.adminPort=9053 -Dipfs.httpPort=9054 ipfs-client-1.1-SNAPSHOT.jar > /dev/null 2>&1 &
nohup java -jar -Dserver.port=9057 -Dipfs.port=9052 -Dipfs.adminPort=9053 -Dipfs.httpPort=9054 -Dipfs.startDaemon=false ipfs-search-es-1.1-SNAPSHOT.jar > /dev/null 2>&1 &
```
