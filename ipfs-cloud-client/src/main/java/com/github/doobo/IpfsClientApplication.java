package com.github.doobo;

import com.github.doobo.factory.PlatformInitFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class IpfsClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(IpfsClientApplication.class, args);
	}
}
