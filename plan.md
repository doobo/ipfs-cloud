## V3计划
* ipfs-cloud-platform分系统加载ipfs执行程序
* 注册中心基于广播进行节点交换,并提供外网节点发现服务
* client基于广播获取节点,并去除spring-cloud依赖
* mirror基于广播来备份文件,可选择cid策略来分组备份,hash(cid)%3
* 每个模块保证单独启动,不额外依赖springcloud相关功能,模块之间通过广播进行通信