package com.github.doodob.handler;

import com.github.doobo.bo.IpfsProperties;
import com.github.doobo.bo.PlatformInitRequest;
import com.github.doobo.bo.PlatformInitResponse;
import com.github.doobo.bo.PlatformStartRequest;
import com.github.doobo.handler.AbstractPlatformInitHandler;
import com.github.doobo.script.ScriptUtil;
import com.github.doobo.utils.CompressorUtils;
import com.github.doobo.utils.FileUtils;
import com.github.doobo.utils.ResultUtils;
import com.github.doobo.vbo.ResultTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Mac系统处理器
 *
 * @Description: ipfs-cloud
 * @User: doobo
 * @Time: 2022-08-03 16:48
 */
@Slf4j
public class PlatformInitMacHandler extends AbstractPlatformInitHandler {

	@Override
	public boolean matching(PlatformInitRequest request) {
		if(Objects.isNull(request) || Objects.isNull(request.getOsName())){
			return Boolean.FALSE;
		}
		return request.getOsName().startsWith("Mac OS");
	}

	@Override
	public ResultTemplate<PlatformInitResponse> initIpfs(PlatformInitRequest request) {
		IpfsProperties properties = request.getProperties();
		if(Objects.isNull(properties)){
			return ResultUtils.ofFail("properties is empty");
		}
		//获取基础信息,并返回
		String pwd = Optional.ofNullable(properties.getInitDir()).filter(StringUtils::isNotBlank).orElse(".");
		PlatformInitResponse response = new PlatformInitResponse();
		try {
			response.setConfigDir(new File(pwd + File.separator + ".ipfs").getCanonicalPath());
			response.setExePath(new File(pwd + File.separator + "/go-ipfs/ipfs").getCanonicalPath());
		}catch (Exception e){
			log.error("response params set error:", e);
		}
		//未初始化时,初始化应用程序
		if(!isInitIpfs(pwd)){
			FileUtils.createFile(pwd+"/.ipfs", ".initIpfs");
			byte[] rs =FileUtils.readResourcesByte("mac64/go-ipfs.tar.gz");
			if(!initUnixIpfs(rs, pwd)){
				return ResultUtils.ofFail("initUnixIpfs is error");
			}
			//ipfs配置初始化
			String init = ScriptUtil.execToString(response.getExePath(), TimeUnit.SECONDS.toMillis(30)
				, "-c", response.getConfigDir(), "init");
			log.info("init ipfs:{}", init);
		}
		try {
			//私有密钥替换
			if(Objects.nonNull(properties.getPrivateNetwork()) && properties.getPrivateNetwork()
				&& !creatSwarmKey(properties)){
				return ResultUtils.ofFail("creatSwarmKey is error");
			}
			//共有网络删除相关的私钥
			if(Objects.isNull(properties.getPrivateNetwork()) || !properties.getPrivateNetwork()){
				FileUtils.deleteFiles(Collections.singletonList(response.getConfigDir() + File.separator + "swarm.key"));
			}
			//基础信息设置与返回
			configAvailablePort(properties);
			updateConfig(properties, response);
			addBootstrap(properties.getBootstrap(), response);
		}catch(Throwable e){
			log.error("initIpfsError:", e);
		}
		return ResultUtils.of(response);
	}

	@Override
	public ResultTemplate<PlatformInitResponse> startIpfs(PlatformStartRequest request) {
		IpfsProperties properties = request.getProperties();
		if(Objects.isNull(properties)){
			return ResultUtils.ofFail("properties is empty");
		}
		if(Objects.isNull(properties.getStartDaemon()) || !properties.getStartDaemon()){
			return ResultUtils.ofFail("not start, startDaemon is null or false");
		}
		return null;
	}

	@Override
	public ResultTemplate<PlatformInitResponse> startTopic(PlatformInitRequest request) {
		log.info("startTopic start:{}", request);
		return null;
	}

	/**
	 * linux和mac os下面的ipfs环境初始化
	 */
	private static boolean initUnixIpfs(byte[] rs, String pwd) {
		String filepath = pwd + "/go-ipfs.tar.gz";
		try (OutputStream out = new FileOutputStream(filepath)){
			out.write(rs);
			out.flush();
			out.close();
			//添加执行权限
			CompressorUtils.deGzipArchive(pwd, filepath);
			String ipfs = new File(pwd + "/go-ipfs/ipfs").getCanonicalPath();
			ScriptUtil.execToString("chmod", null, TimeUnit.SECONDS.toMillis(3), "+x" , ipfs);
		} catch (Exception e){
			log.error("initUnixIpfs ipfs env fail", e);
			return false;
		}
		return true;
	}
}