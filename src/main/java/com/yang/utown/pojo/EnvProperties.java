package com.yang.utown.pojo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author yhw
 * @date 2023/2/3 19:12
 * @remark
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "env")
public class EnvProperties {
	private String test_host;
	private String beta_host;
	private String prod_host;
	private String id_host;
}
