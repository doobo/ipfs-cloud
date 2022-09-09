package com.github.doobo.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.doobo.bo.*;
import com.github.doobo.config.IpfsInitConfig;
import com.github.doobo.config.IpfsInitDefaultConfig;
import com.github.doobo.factory.PlatformInitFactory;
import com.github.doobo.script.CollectingObserved;
import com.github.doobo.script.ScriptUtil;
import com.github.doobo.utils.FileUtils;
import com.github.doobo.utils.OsUtils;
import com.github.doobo.utils.ResultUtils;
import com.github.doobo.utils.WordUtils;
import com.github.doobo.vbo.Builder;
import com.github.doobo.vbo.ResultTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * 平台初始化抽象类
 *
 * @Description: ipfs-cloud
 * @User: doobo
 * @Time: 2022-07-29 13:47
 */
@Slf4j
public abstract class AbstractPlatformInitHandler implements PlatformInitHandler{

	/**
	 * 后台运行进程
	 */
	private static ForkJoinPool startPool;

	private static ForkJoinPool topicPool;

	/**
	 * 初始化返回信息
	 */
	public static PlatformInitResponse RESPONSE = new PlatformInitResponse();

	/**
	 * 初始化并启动ipfs
	 */
	public static synchronized ResultTemplate<PlatformInitResponse> initAndStartIpfs(PlatformStartRequest request){
		if(Objects.isNull(request)){
			return ResultUtils.ofFail("入参异常:request");
		}
		request.setOsName(Optional.ofNullable(request.getOsName()).filter(StringUtils::isNotBlank)
			.orElseGet(()-> System.getProperty("os.name")));
		request.setProperties(Optional.ofNullable(request.getProperties()).orElseGet(IpfsInitConfig::getIpfsProperties));
		ResultTemplate<PlatformInitResponse> result = PlatformInitFactory.executeHandler(request, proxy -> proxy.initIpfs(request));
		if(Objects.isNull(result) || !result.isSuccess()){
			return result;
		}
		//设置全局启动参数
		RESPONSE = Optional.ofNullable(result.getData()).orElse(RESPONSE);
		request.setInfo(RESPONSE.getInfo());
		request.setConfigDir(RESPONSE.getConfigDir());
		request.setExePath(RESPONSE.getExePath());
		startPool = Optional.ofNullable(startPool)
			.filter(startPool-> !startPool.isShutdown())
			.orElseGet(IpfsInitDefaultConfig::createForkJoinPool);
		startPool.execute(()-> PlatformInitFactory.executeHandler(request, handler-> {
			ResultTemplate<PlatformInitResponse> template = handler.startIpfs(request);
			Optional.ofNullable(template).filter(f -> !f.isSuccess()).ifPresent(m -> log.error("startIpfsError:{}", m));
			return template;
		}));
		return result;
	}

	/**
	 * 初始化并启动ipfs
	 */
	public static synchronized ResultTemplate<Boolean> startIpfsTopic(PlatformStartRequest request){
		if(Objects.isNull(request)){
			return ResultUtils.ofFail("入参异常:request");
		}
		request.setOsName(Optional.ofNullable(request.getOsName()).filter(StringUtils::isNotBlank)
			.orElseGet(()-> System.getProperty("os.name")));
		request.setProperties(Optional.ofNullable(request.getProperties()).orElseGet(IpfsInitConfig::getIpfsProperties));
		topicPool = Optional.ofNullable(topicPool)
			.filter(startPool-> !startPool.isShutdown())
			.orElseGet(IpfsInitDefaultConfig::createForkJoinPool);
		topicPool.execute(()-> PlatformInitFactory.executeHandler(request, handler-> {
			ResultTemplate<Boolean> template = handler.startTopic(request);
			Optional.ofNullable(template).filter(f -> !f.isSuccess()).ifPresent(m -> log.error("startTopicError:{}", m));
			return template;
		}));
		return ResultUtils.of(Boolean.TRUE);
	}

	/**
	 * 重启IPFS
	 */
	@Override
	public ResultTemplate<PlatformInitResponse> restartIpfs(PlatformStartRequest request) {
		try {
			ResultTemplate<Boolean> template = this.stopIpfs(request);
			log.info("stopIpfs:{}", template);
			TimeUnit.SECONDS.sleep(3);
		} catch (Exception e) {
			log.error("stopIpfs error:", e);
		}
		return AbstractPlatformInitHandler.initAndStartIpfs(request);
	}

	/**
	 * 	停止IPFS
	 */
	@Override
	public ResultTemplate<Boolean> stopIpfs(PlatformStartRequest request) {
		this.stopTopic(request);
		Optional.ofNullable(startPool).ifPresent(ForkJoinPool::shutdownNow);
		return ResultUtils.of(Boolean.TRUE);
	}


	/**
	 * 停止监听
	 */
	@Override
	public ResultTemplate<Boolean> stopTopic(PlatformInitRequest request) {
		Optional.ofNullable(topicPool).ifPresent(ForkJoinPool::shutdownNow);
		return ResultUtils.of(Boolean.TRUE);
	}

	/**
	 * 是否初始化IPFS
	 */
	protected boolean isInitIpfs(String pwd){
		//默认当前工作目录
		return new File(pwd + File.separator + ".ipfs" + File.separator + "config").exists();
	}

	/**
	 * 创建私有密钥
	 */
	protected boolean creatSwarmKey(IpfsProperties properties){
		if(Objects.isNull(properties)){
			return false;
		}
		byte[] rs = new byte[0];
		String swarmKey = properties.getSwarmKey();
		if(StringUtils.isBlank(swarmKey)){
			rs = FileUtils.readResourcesByte("key/swarm.key");
		}else {
			try {
				swarmKey = swarmKey.replace("\\n", System.lineSeparator());
				rs = swarmKey.getBytes(UTF_8.name());
			} catch (Exception e) {
				log.warn("creatSwarmKeyError:", e);
			}
		}
		String ipfsConfig = Optional.ofNullable(properties.getInitDir()).filter(StringUtils::isNotBlank).orElse(".") + File.separator + ".ipfs";
		FileUtils.createFile(ipfsConfig, "swarm.key");
		try (FileOutputStream out = new FileOutputStream(ipfsConfig + File.separator + "swarm.key")){
			out.write(rs);
		} catch (Exception e){
			log.error("creatSwarmKeyError:",e);
			return false;
		}
		return true;
	}

	/**
	 * 替换可用的端口号
	 */
	protected void configAvailablePort(IpfsProperties ipfsConfig){
		if(ipfsConfig == null){
			return;
		}
		if(ipfsConfig.getPort() == null){
			ipfsConfig.setPort(14001);
		}
		if(ipfsConfig.getAdminPort() == null){
			ipfsConfig.setAdminPort(15001);
		}
		if(ipfsConfig.getHttpPort() == null){
			ipfsConfig.setHttpPort(18080);
		}
		if(StringUtils.isBlank(ipfsConfig.getBindIp())){
			ipfsConfig.setBindIp("127.0.0.1");
		}
		int count = 0;
		while (OsUtils.checkIpPortOpen(ipfsConfig.getBindIp(), ipfsConfig.getPort())){
			count ++;
			ipfsConfig.setPort(ipfsConfig.getPort() + 1);
			log.warn("change ipfs port:{}", ipfsConfig.getPort());
			if(count > 10){
				break;
			}
		}
		count = 0;
		while (OsUtils.checkIpPortOpen(ipfsConfig.getBindIp(), ipfsConfig.getAdminPort())){
			count ++;
			ipfsConfig.setAdminPort(ipfsConfig.getAdminPort() + 1);
			log.warn("change ipfs admin port:{}", ipfsConfig.getAdminPort());
			if(count > 10){
				break;
			}
		}
		count = 0;
		while (OsUtils.checkIpPortOpen(ipfsConfig.getBindIp(), ipfsConfig.getHttpPort())){
			count ++;
			ipfsConfig.setHttpPort(ipfsConfig.getHttpPort() + 1);
			log.warn("change ipfs http port:{}", ipfsConfig.getHttpPort());
			if(count > 10){
				break;
			}
		}
	}

	/**
	 * 更新ipfs端口配置
	 */
	protected void updatePortConfig(IpfsProperties ipfsConfig, PlatformInitResponse response){
		if(Objects.isNull(ipfsConfig) || Objects.isNull(response)){
			return;
		}
		String swarm = ScriptUtil.execDefaultTime(response.getExePath()
			, "-c", response.getConfigDir(), "config", "Addresses.Swarm");
		//修改4001接口
		if(swarm != null && swarm.contains("[")){
			List<String> sm = JSON.parseArray(swarm, String.class);
			List<String> rm = new ArrayList<>();
			if(Objects.nonNull(sm) && sm.size() > 0){
				sm.forEach(m->{
					String num = WordUtils.getStrEndNumber(m);
					if(num != null){
						rm.add(m.replace(num, ipfsConfig.getPort().toString()));
					}
					if(m.endsWith("quic")){
						num = WordUtils.getStrEndNumber(m.substring(0,m.lastIndexOf("/")));
						if(num != null) {
							rm.add(m.replace(num, ipfsConfig.getPort().toString()));
						}
					}
				});
			}
			Optional.of(rm).filter(f -> !f.isEmpty()).ifPresent(c -> replaceSwarm(response, c));
		}
		//修改5001接口
		String api = ScriptUtil.execDefaultTime(response.getExePath()
			, "-c", response.getConfigDir(), "config", "Addresses.API");
		if(api != null && !api.isEmpty()){
			String num = WordUtils.getStrEndNumber(api);
			//初始化时替换一次
			api = api.replace("127.0.0.1", ipfsConfig.getBindIp());
			if(num != null) {
				String result = ScriptUtil.execDefaultTime(response.getExePath()
					, "-c", response.getConfigDir(), "config", "Addresses.API"
					, api.replace(num, ipfsConfig.getAdminPort().toString()));
				Optional.ofNullable(result).filter(StringUtils::isNotBlank).ifPresent(c -> log.info("config Addresses.API,{}", c));
			}
		}
		//修改8080接口
		String gateway = ScriptUtil.execDefaultTime(response.getExePath()
			,"-c", response.getConfigDir(), "config", "Addresses.Gateway");
		if(gateway != null && !gateway.isEmpty()){
			gateway = gateway.replace("127.0.0.1", ipfsConfig.getBindIp());
			String num = WordUtils.getStrEndNumber(gateway);
			if(num != null) {
				String result = ScriptUtil.execDefaultTime(response.getExePath()
					, "-c", response.getConfigDir(), "config", "Addresses.Gateway"
					, gateway.replace(num, ipfsConfig.getHttpPort().toString()));
				Optional.ofNullable(result).filter(StringUtils::isNotBlank).ifPresent(c -> log.info("config Addresses.Gateway,{}", c));
			}
		}
	}

	/**
	 * 替换Swarm内容
	 */
	protected void replaceSwarm(PlatformInitResponse response, List<String> rm){
		if(Objects.isNull(response) || Objects.isNull(rm) || rm.isEmpty()){
			return;
		}
		String result = ScriptUtil.execNotHandleQuoting(response.getExePath()
			, "-c", response.getConfigDir()
			, "config", "Addresses.Swarm", "--json", JSON.toJSONString(rm));
		Optional.ofNullable(result).filter(StringUtils::isNotBlank).ifPresent(c -> log.info("config Addresses.Swarm,{}", c));
	}

	/**
	 * 添加启动节点
	 */
	protected void addBootstrap(List<String> nodeConfigList, PlatformInitResponse response) {
		if (nodeConfigList == null || nodeConfigList.isEmpty() || Objects.isNull(response)) {
			return;
		}
		nodeConfigList.forEach(m -> {
			String s = ScriptUtil.execDefaultTime(response.getExePath()
				, "-c", response.getConfigDir()
				, "bootstrap", "add", m);
			Optional.ofNullable(s).filter(StringUtils::isNotBlank)
				.ifPresent(c -> log.info("bootstrap add:{}", s.replace("\n", "")));
		});
	}

	/**
	 * 获取节点配置信息
	 */
	protected IpfsNodeInfo queryNodeInfo(PlatformInitResponse response){
		IpfsNodeInfo info = new IpfsNodeInfo();
		if(Objects.isNull(response)){
			return info;
		}
		Optional.ofNullable(response.getIpfsConfig()).ifPresent(f -> info.setIp(f.getBindIp()));
		Optional.ofNullable(response.getIpfsConfig()).ifPresent(f -> info.setPort(f.getPort()));
		String config = ScriptUtil.execDefaultTime(response.getExePath(), "-c", response.getConfigDir()
			, "id");
		Optional.ofNullable(config).filter(StringUtils::isNotBlank).filter(f -> f.startsWith("{"))
			.ifPresent(m ->{
				JSONObject parseObject = JSON.parseObject(m);
				if(Objects.nonNull(parseObject)){
					info.setCid(parseObject.getString("ID"));
					info.setAgentVersion(parseObject.getString("AgentVersion"));
					info.setProtocolVersion(parseObject.getString("ProtocolVersion"));
					info.setPublicKey(parseObject.getString("PublicKey"));
					JSONArray addresses = parseObject.getJSONArray("Addresses");
					if(Objects.nonNull(addresses)){
						info.setAddress(addresses.stream().map(String::valueOf).collect(Collectors.toList()));
					}
				}
			});
		return info;
	}

	/**
	 * 通过脚本启动IPFS
	 */
	protected ResultTemplate<PlatformInitResponse> startIpfsByScript(PlatformStartRequest request){
		try {
			request.addExtParam("-c").addExtParam(request.getConfigDir())
				.addExtParam("daemon").addExtParam("--enable-pubsub-experiment");
			String[] params = request.getExtParams().toArray(new String[0]);
			File lock = new File(request.getConfigDir(),"repo.lock");
			lock.deleteOnExit();
			//初始化日志收集器
			PlatformObserverRequest observerRequest = Builder.of(PlatformObserverRequest::new)
				.with(PlatformObserverRequest::setConfigDir, request.getConfigDir())
				.with(PlatformObserverRequest::setExePath, request.getExePath())
				.with(PlatformObserverRequest::setType, "start")
				.build();
			CollectingObserved observed = new CollectingObserved(observerRequest);
			ScriptUtil.execObserved(request.getExePath(), observed, Long.MAX_VALUE, params);
		} catch (Exception e) {
			log.info("start ipfs daemon Error", e);
			return ResultUtils.ofFail("start ipfs error:" + e.getMessage());
		}
		return null;
	}

	/**
	 * 启动消息服务
	 */
	protected ResultTemplate<Boolean> startMsgServer(PlatformStartRequest request){
		try {
			IpfsProperties properties = request.getProperties();
			String rs = ScriptUtil.execDefaultTime(request.getExePath(), "-c", request.getConfigDir()
				, "pubsub", "ls");
			if(rs != null && rs.contains(properties.getTopic())){
				return ResultUtils.of(true, "topic already exist");
			}
			log.info("start msg server topic:{}", properties.getTopic());
			//初始化日志收集器
			PlatformObserverRequest observerRequest = Builder.of(PlatformObserverRequest::new)
				.with(PlatformObserverRequest::setConfigDir, request.getConfigDir())
				.with(PlatformObserverRequest::setExePath, request.getExePath())
				.with(PlatformObserverRequest::setType, "topic")
				.build();
			CollectingObserved observed = new CollectingObserved(observerRequest);
			ScriptUtil.execObserved(request.getExePath(), observed, Long.MAX_VALUE
				, "-c", request.getConfigDir(), "pubsub", "sub", properties.getTopic()
				, "--encoding", "json");
		} catch (Exception e) {
			log.error("startTopicError:", e);
			return ResultUtils.ofFail("startTopicError:" + e.getMessage());
		}
		return ResultUtils.of(true);
	}
}
