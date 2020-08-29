package com.github.doobo;

import com.github.doobo.model.IpfsFileInfo;
import com.github.doobo.service.IpfsSearchService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class IpfsSearchEsApplicationTests {

	@Resource
	IpfsSearchService ipfsSearchService;

	@Test
	public void testSave(){
		IpfsFileInfo info = new IpfsFileInfo();
		info.setIpfs("Qm...");
		ipfsSearchService.saveFileInfo(info);
	}
}
