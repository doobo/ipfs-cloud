package com.github.doobo.script;

import com.github.doobo.utils.Base64Utils;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@Data
@Accessors(chain = true)
public class IpfsJsonVO {

	private String from;

	private String data;

	private String seqno;

	private List<String>  topicIDs;

	public String getData() {
		if(StringUtils.isBlank(data)){
			return data;
		}
		if(Base64Utils.isBase64(data)){
			try {
				StringBuffer sb = new StringBuffer();
				String str = new String(Base64Utils.decryptBASE64(data), UTF_8.name());
				if(StringUtils.isBlank(str)){
					return str;
				}
				sb.append(str);
				if(str.startsWith("'")){
					sb.deleteCharAt(0);
				}
				if(str.endsWith("'")){
					sb.deleteCharAt(str.length()-1);
				}
				return sb.toString();
			} catch (Exception e) {
				log.warn("decryptBASE64Error", e);
			}
		}
		return data;
	}
}
