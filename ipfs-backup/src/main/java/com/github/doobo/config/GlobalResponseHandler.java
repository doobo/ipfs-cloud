package com.github.doobo.config;

import com.github.doobo.conf.AbstractGlobalResponseHandler;
import org.springframework.stereotype.Component;

@Component
public class GlobalResponseHandler extends AbstractGlobalResponseHandler {

	@Override
	public boolean notNecessaryWrapperURI(String uri) {
		return false;
	}
}
