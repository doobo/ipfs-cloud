package com.github.doobo.scan;

import com.github.doobo.handler.PlatformInitHandler;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.stereotype.Component;

/**
 * 基础包扫描
 */
@Component
@AutoConfigureOrder(PlatformInitHandler.DEFAULT_PHASE)
@ComponentScans({@ComponentScan("com.github.doobo.factory")
	,@ComponentScan("com.github.doobo.config")
	,@ComponentScan("com.github.doobo.controller")
})
public class UnionAutoConfiguration {
}
