package com.yang.utown.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.http.Header;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @author yhw
 * @date 2023/2/10 17:39
 * @remark
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HttpResult implements Serializable {
	private String statusCode = ""; // HTTP status code
	private String resultBody = "";    // HTTP result body
	private Header[] allHeaders; // HTTP allheaders
	private String requestBody = "";    // HTTP request body
	private String captcha = "";

}
