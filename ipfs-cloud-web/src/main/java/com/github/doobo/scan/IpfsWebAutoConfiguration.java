package com.github.doobo.scan;

import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.stereotype.Component;

/**
 * 基础包扫描
 */
@Component
@AutoConfigureOrder(99)
@ComponentScans({@ComponentScan("com.github.doobo.handler")
	,@ComponentScan("com.github.doobo.config")
})
public class IpfsWebAutoConfiguration {
}
