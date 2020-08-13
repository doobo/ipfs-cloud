package com.github.doobo.conf;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.stereotype.Component;

/**
 * 基本配置类
 */
@Component
@ComponentScans({@ComponentScan("com.github.doobo.conf")})
public class SoftwareAutoConfiguration {
}
