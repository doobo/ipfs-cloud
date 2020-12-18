# ipfs-cloud

> 基于SpringCloud的IPFS私有云,主要解决文件存储分发问题,不考虑挖矿,支持内网部署和多区域集群部署,(北京、上海、广州、香港、北美、欧洲)组网

## 支持全内网隔离部署
内网快速搭建私有文件存储服务，能快速部署、快速访问、无限扩容、自动分发、自动备份，支持内网部署和多跨区域组网部署

## 主要应用场景
### 文件上传同步
本地启动ipfs-client后，调用它的接口上传文件(本地上传文件不占用外网)，其它外网节点可(ipfs cat Qm...)获取文件内容(p2p,速度更快)

### 接口数据校验
上传的文件生成的cid是通过内容签名生成的，不同系统可通过cid校验数据是否被修改，因为在本地生成cid，不会被劫持，第三方拿到cid后再获取文件内容

### CDN文件分发
多区域启用网关，各子区域上传的文件，会自动同步其它区域，暴露一个区域的下载地址，即可获取文件

## 模块简单介绍
### ipfs-client
分布式客户端，单独启动，自动组网，内网启动一台即可快速调用ipfs相关的API接口，快速进行文件上传下载,
文件上传后会自动分发到ipfs机器，上传接口会返回文件的cid，同时在client端保存一份'文件名'+'.ipfs'的文件，里面的内容是该文件的全网唯一CID编码，通过该编码可以
在任意集群的节点进行文件下载、查看等操作

### ipfs-gateway
网关，需要多区域组网的可启动，用来给客户端自动组网，组建集群时，用来自动添加网关节点，配合注册中心使用即可，客户端会自动把网关的节点加入自己的bootstrap中
这样，不同地区的子集群也能通过网关进行文件分发和获取，不同地区的子集群，可通过网关的IPFS获取任意集群内容的文件

### ipfs-search-es
文件搜索服务的es实现,需要搜索文件的可启动，可根据文件的cid、节点id、文件类型、文件名、文件内容、添加的时间范围来搜索相关文件
[搜索页面前端代码](https://gitee.com/doobo/ipfs-search)
![简单的搜索界面](https://ipfs.ipav.vip/ipfs/QmWjXPAAeSV7XwjnfLkxCCvSpwsHPr5bK3qUtGaXgsGUWM)

### ipfs-backup
文件备份服务，需要备份上传文件的可启动，备份以年月日时作为目录，文件的cid作为文件名，保存到备份服务器，备份服务器最好能和网关共用IPFS节点，
即备份节点不启动IPFS,-Dipfs.startDaemon=false;如果是全内网部署，随便一台服务器启动即可

### ipfs-register
简单的springCloud注册中心程序，需要内网隔离部署的可启动，其它服务启动时，添加参数:
```shell
-Deureka.client.serviceUrl.defaultZone=http://union:123456@127.0.0.1:6109/eureka/
```

## 文件搜索
搜索服务可基于elasticSearch来搜索，也可用基于MySQL等数据库实现，基于统一接口调用，保证实现相关接口即可，注意保证数据幂等性和搜索结构一致性。

## 系统兼容性
目前只上传来win64,linux64,macOs的ipfs的程序,考虑作为文件服务器,个人精力有限,暂不考虑其它环境.如需要实现其它环境,
可以在ipfs-software的resources/lib下放入相关的ipfs包,并写好初始化方法即可.

## 代码地址
* [ipfs-cloud-码云](https://gitee.com/doobo/ipfs-cloud)
* [ipfs-cloud-github](https://github.com/doobo/ipfs-cloud)

## 部署
### 简单测试
启动ipfs-client即可，通过浏览器访问:[ipfs-client](http://127.0.0.1:6103/)
,上传文件后，接口会返回cid，用cid可再外网获取到文件，如：http://ipfs.ipav.vip/ipfs/Qm...

> 所有区域推荐使用内网获取，这样会自动使用p2p传输，如：http://localhost:18080/ipfs/Qm...或者ipfs cat Qm...
> 对本地测试，推荐把.ipfs/go-ipfs目录添加到环境变量，这样可以直接执行ipfs命令，ipfs block stat Qm...检测文件是否存在

### 多区域集群部署,(北京、上海、广州、香港、北美、欧洲)组网
> 文件上传尽可能使用本地的ipfs-client进行上传，会自动通过ipfs分发到全网
> ,提高网络传输效率，上传文件后，ipfs-client不要立即停机，防止其它区域在同步文件时，访问不到文件

```
每个区域至少暴露一个spring cloud注册中心和一个ipfs-gateway服务，保证使用公网IP可以相互访问
ipfs-backup服务以-Dipfs.startDaemon=false启动到ipfs-gateway所在的服务器上，保证公网可访问
启用ipfs-search-es服务，用来搜索文件，测试时，可以不启用,提供可外网访问的地址，如-Deureka.instance.preferIpAddress=false -Deureka.instance.hostname=5fu8.com -Deureka.instance.nonSecurePort=6107
启用ipfs-client服务，这个不需要暴露到外网，每个区域按需求启动即可，每个区域调用本区域的http接口进行文件上传，会通过ipfs-backup服务自动同步到全网
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

## IPFS讨论区
微信小程序，基于ipfs实现的图片上传
![微雅视频](https://ipfs.ipav.vip/ipfs/QmRJd1LoyQWQFmoM88hohaHkhAnSkNVATAs8AXvTamZS1m)