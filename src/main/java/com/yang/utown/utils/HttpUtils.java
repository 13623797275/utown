package com.yang.utown.utils;

import com.alibaba.fastjson.JSONObject;
import com.yang.utown.enums.HttpMethod;
import com.yang.utown.pojo.HttpResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * @author yhw
 * @date 2023/2/10 20:10
 * @remark
 */
@Slf4j
public class HttpUtils {

	/**
	 * 连接池管理
	 */
	private static Logger logger = LoggerFactory.getLogger(HttpUtils.class);
	// 池化管理
	private static PoolingHttpClientConnectionManager poolConnManager = null;
	// 它是线程安全的，所有的线程都可以使用它一起发送http请求
	private static CloseableHttpClient httpClient;

	static {
		try {
			// System.out.println("初始化HttpClientTest~~~开始");
			SSLContextBuilder builder = new SSLContextBuilder();
			builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
			//信任所有证书，关闭主机名校验
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
					SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build(),
					NoopHostnameVerifier.INSTANCE);
			// 配置同时支持 HTTP 和 HTPPS
			Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create().register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", sslsf).build();
			// 初始化连接管理器
			poolConnManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
			poolConnManager.setMaxTotal(640);// 同时最多连接数
			// 设置最大路由
			poolConnManager.setDefaultMaxPerRoute(320);
			// 此处解释下MaxtTotal和DefaultMaxPerRoute的区别：
			// 1、MaxtTotal是整个池子的大小；
			// 2、DefaultMaxPerRoute是根据连接到的主机对MaxTotal的一个细分；比如：
			// MaxtTotal=400 DefaultMaxPerRoute=200
			// 而我只连接到http://www.abc.com时，到这个主机的并发最多只有200；而不是400；
			// 而我连接到http://www.bac.com 和
			// http://www.ccd.com时，到每个主机的并发最多只有200；即加起来是400（但不能超过400）；
			//所以起作用的设置是DefaultMaxPerRoute
			// 初始化httpClient
			httpClient = getConnection();

			// System.out.println("初始化HttpClientTest~~~结束");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}
	}

	public static CloseableHttpClient getConnection() {
		RequestConfig config = RequestConfig.custom().setConnectTimeout(5000)
				.setConnectionRequestTimeout(5000)
				.setSocketTimeout(5000)
				.build();
		CloseableHttpClient httpClient = HttpClients.custom()
				//设置连接池管理
				.setConnectionManager(poolConnManager)
				.setDefaultRequestConfig(config)
				//设置重试次数
				.setRetryHandler(new DefaultHttpRequestRetryHandler(2, false))
				//该配置保证后台使用一个共享连接池，供剩下打开的连接去使用
				.setConnectionManagerShared(true)
				.build();
		return httpClient;
	}

	/**
	 * GET请求
	 *
	 * @param url
	 * @return
	 */
	public static HttpResult doGet(String url) {
		return sendHttp(HttpMethod.GET, url, null, null);
	}

	/**
	 * GET请求/带头部的信息
	 *
	 * @param url
	 * @param header
	 * @return
	 */
	public static HttpResult doGet(String url, Map<String, String> header) {
		return sendHttp(HttpMethod.GET, url, header, null);
	}

	/**
	 * GET请求/带头部的信息/带参数
	 *
	 * @param url
	 * @param header
	 * @param params
	 * @return
	 */
	public static HttpResult doGet(String url, HashMap<String, String> header, Map<String, Object> params) throws IOException, URISyntaxException {
		String infoMessage = new StringBuilder()
				.append("request sendHttp，url:")
				.append(url)
				.append("，header:")
				.append(JSONObject.toJSONString(header))
				.append("，param:")
				.append(params)
				.toString();
		log.info(infoMessage);

		String resultBody = "";
		String statusCode = "";
		CloseableHttpResponse httpResponse = null;
		long beginTime = System.currentTimeMillis();
		//设置url
		URIBuilder uriBuilder = new URIBuilder(url);
		// 遍历Map集合，设置参数的值
		if (params != null) { // 注意，如果说Map集合不为null，那么就依次设置参数
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				uriBuilder.setParameter(entry.getKey(), entry.getValue().toString());
			}
		}
		URI uri = uriBuilder.build();// 如果说Map集合就为null，那么就不设置参数
		HttpGet httpGet = new HttpGet(uri);
		//添加请求头
		packageHeader(header, httpGet);
		//执行请求
		httpResponse = httpClient.execute(httpGet);
		// 获取响应的结果，并封装到HttpResult对象中
		statusCode = String.valueOf(httpResponse.getStatusLine().getStatusCode()); // 获取响应状态码
		log.info("sendHttp response status:{}", httpResponse.getStatusLine());
		// 实体response.getEntity()其实封装的就是我们的内容，但是要注意response.getEntity()得到是一个null的话，那么就会报错，所以最好得判断一下
		if (httpResponse.getEntity() != null) {
			resultBody = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
			log.info("sendHttp response body:{}", resultBody);
		} else {
			logger.error("请求{}返回错误码：{},{}", url, statusCode, resultBody);
		}
		long endTime = System.currentTimeMillis();
		log.info("request sendHttp response time cost:" + (endTime - beginTime) + " ms");
		// HttpResult httpResult = new HttpResult(statusCode, resultBody, null,infoMessage,null);
		HttpResult httpResult = new HttpResult();
		httpResult.setRequestBody(infoMessage);
		httpResult.setResultBody(resultBody);
		httpResult.setStatusCode(statusCode);
		return httpResult;
	}

	/**
	 * POST请求/无参数
	 *
	 * @param url
	 * @return
	 */
	public static HttpResult doPost(String url) {
		return doPost(url, null, null);
	}

	/**
	 * POST请求/有参数
	 *
	 * @param url
	 * @param param
	 * @return
	 */
	public static HttpResult doPost(String url, String param) {
		return doPost(url, null, param);
	}

	/**
	 * POST请求/无参数带头部
	 *
	 * @param url
	 * @param header
	 * @return
	 */
	public static HttpResult doPost(String url, Map<String, String> header) {
		return doPost(url, header, null);
	}

	/**
	 * POST请求/有参数带头部
	 *
	 * @param url
	 * @param header
	 * @param params
	 * @return
	 */
	public static HttpResult doPost(String url, Map<String, String> header, String params) {
		return sendHttp(HttpMethod.POST, url, header, params);
	}

	/**
	 * 表单请求
	 *
	 * @param url
	 * @param param
	 * @return
	 * @throws IOException
	 */
	public static HttpResult doPostForm(String url, HashMap<String, String> param, HashMap<String, String> header) throws IOException {
		String infoMessage = new StringBuilder()
				.append("request postForm，url:")
				.append(url)
				.append("，header:")
				.append(JSONObject.toJSONString(header))
				.append("，param:")
				.append(param)
				.toString();
		log.info(infoMessage);
		CloseableHttpResponse httpResponse = null;
		String resultBody = "";
		String statusCode = "";
		long beginTime = System.currentTimeMillis();
		// 创建Http Post请求
		HttpPost httpPost = new HttpPost(url);
		//添加请求头
		packageHeader(header, httpPost);
		// 创建参数列表
		if (param != null) {
			List<NameValuePair> paramList = new ArrayList<>();
			for (String key : param.keySet()) {
				BasicNameValuePair basicNameValuePair = new BasicNameValuePair(key, param.get(key));
				paramList.add(new BasicNameValuePair(key, param.get(key)));
			}
			// 模拟表单
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
			httpPost.setEntity(entity);
			// 执行http请求
			httpResponse = httpClient.execute(httpPost);
		}
		// 获取响应的结果，并封装到HttpResult对象中
		statusCode = String.valueOf(httpResponse.getStatusLine().getStatusCode()); // 获取响应状态码
		log.info("sendHttp response status:{}", httpResponse.getStatusLine());
		if (httpResponse.getEntity() != null) {
			resultBody = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
			log.info("sendHttp response body:{}", resultBody);
		} else {
			logger.error("请求{}返回错误码：{},{}", url, statusCode, resultBody);
		}
		long endTime = System.currentTimeMillis();
		log.info("request sendHttp response time cost:" + (endTime - beginTime) + " ms");
		HttpResult httpResult = new HttpResult();
		httpResult.setRequestBody(infoMessage);
		httpResult.setResultBody(resultBody);
		httpResult.setStatusCode(statusCode);
		return httpResult;
	}

	/**
	 *单文件上传
	 * @param url
	 * @param file
	 * @param param
	 * @param header
	 * @return
	 * @throws IOException
	 */
	public static HttpResult doPostUploadOneFile(String url, File file, HashMap<String, String> param, HashMap<String, String> header) throws IOException {
		String infoMessage = new StringBuilder()
				.append("request postUploadFileStream，url:")
				.append(url)
				.append("，header:")
				.append(JSONObject.toJSONString(header))
				.append("，param:")
				.append(JSONObject.toJSONString(param))
				.append("，fileName:")
				.append(file)
				.toString();
		log.info(infoMessage);
		if (Objects.isNull(file)){
			log.warn("上传文件为空");
		}
		CloseableHttpResponse httpResponse = null;
		String resultBody = "";
		String statusCode = "";
		long beginTime = System.currentTimeMillis();
		HttpPost httpPost = new HttpPost(url);
		//设置传输参数,设置编码。设置浏览器兼容模式，解决文件名乱码问题
		MultipartEntityBuilder multipartEntity = MultipartEntityBuilder.create().setMode(HttpMultipartMode.RFC6532);

		FileBody fundFileBin = new FileBody(file, ContentType.MULTIPART_FORM_DATA);
		//这里的name 要与接口入参 name 保持一致
		multipartEntity.addPart("file", fundFileBin);

		Set<String> keySet = param.keySet();
		for (String key : keySet) {
			//解决中文乱码
			ContentType contentType = ContentType.create("text/plain", Charset.forName("UTF-8"));
			StringBody stringBody = new StringBody(param.get(key), contentType);
			multipartEntity.addPart(key, stringBody);
		}
		packageHeader(header, httpPost);
		HttpEntity reqEntity = multipartEntity.build();
		httpPost.setEntity(reqEntity);
		httpResponse = httpClient.execute(httpPost);
		statusCode = String.valueOf(httpResponse.getStatusLine().getStatusCode());
		log.info("sendHttp response status:{}", statusCode);
		if (httpResponse.getEntity() != null) {
			resultBody = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
			log.info("sendHttp response body:{}", resultBody);
		} else {
			logger.error("请求{}返回错误码：{},{}", url, statusCode, resultBody);
		}
		HttpResult httpResult = new HttpResult();
		httpResult.setRequestBody(infoMessage);
		httpResult.setResultBody(resultBody);
		httpResult.setStatusCode(statusCode);
		return httpResult;
	}

	/**
	 * 多文件上传
	 * @param url
	 * @param files
	 * @param param
	 * @param header
	 * @return
	 * @throws IOException
	 */
	public static HttpResult doPostUploadMoreFile(String url, File[] files, HashMap<String, String> param, HashMap<String, String> header) throws IOException {
		String infoMessage = new StringBuilder()
				.append("request postUploadFileStream，url:")
				.append(url)
				.append("，header:")
				.append(JSONObject.toJSONString(header))
				.append("，param:")
				.append(JSONObject.toJSONString(param))
				.append("，fileName:")
				.append(files)
				.toString();
		log.info(infoMessage);
		if (Objects.isNull(files)){
			log.warn("上传文件为空");
		}
		CloseableHttpResponse httpResponse = null;
		String resultBody = "";
		String statusCode = "";
		long beginTime = System.currentTimeMillis();
		HttpPost httpPost = new HttpPost(url);
		//设置传输参数,设置编码。设置浏览器兼容模式，解决文件名乱码问题
		MultipartEntityBuilder multipartEntity = MultipartEntityBuilder.create().setMode(HttpMultipartMode.RFC6532);

		for (int i = 0; i < files.length; i++) {
			File postFile = files[i];
			FileBody fundFileBin = new FileBody(postFile, ContentType.MULTIPART_FORM_DATA);
			//相当于<input type="file" name="media"/>
			//这里的名字要与接口的参数保持一致
			multipartEntity.addPart("file"+i, fundFileBin);
		}
		Set<String> keySet = param.keySet();
		for (String key : keySet) {
			//解决中文乱码
			ContentType contentType = ContentType.create("text/plain", Charset.forName("UTF-8"));
			StringBody stringBody = new StringBody(param.get(key), contentType);
			multipartEntity.addPart(key, stringBody);
		}
		packageHeader(header, httpPost);
		HttpEntity reqEntity = multipartEntity.build();
		httpPost.setEntity(reqEntity);
		httpResponse = httpClient.execute(httpPost);
		statusCode = String.valueOf(httpResponse.getStatusLine().getStatusCode());
		log.info("sendHttp response status:{}", statusCode);
		if (httpResponse.getEntity() != null) {
			resultBody = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
			log.info("sendHttp response body:{}", resultBody);
		} else {
			logger.error("请求{}返回错误码：{},{}", url, statusCode, resultBody);
		}


		HttpResult httpResult = new HttpResult();
		httpResult.setRequestBody(infoMessage);
		httpResult.setResultBody(resultBody);
		httpResult.setStatusCode(statusCode);
		return httpResult;
	}

	/**
	 * 通用的请求方法(json数据请求)
	 *
	 * @param httpMethod 请求方式（GET、POST、PUT、DELETE）
	 * @param url        请求路径
	 * @param header     请求头
	 * @param params     请求body（json数据）
	 * @return 响应文本
	 */
	public static HttpResult sendHttp(HttpMethod httpMethod, String url, Map<String, String> header, String params) {
		String infoMessage = new StringBuilder()
				.append("request sendHttp，url:")
				.append(url)
				.append("，method:")
				.append(httpMethod.name())
				.append("，header:")
				.append(JSONObject.toJSONString(header))
				.append("，param:")
				.append(params)
				.toString();
		log.info(infoMessage);
		//返回结果
		String resultBody = "";
		String statusCode = "";
		CloseableHttpResponse httpResponse = null;
		long beginTime = System.currentTimeMillis();
		try {
			ContentType contentType = ContentType.APPLICATION_JSON.withCharset("UTF-8");
			HttpRequestBase request = buildHttpMethod(httpMethod, url);
			if (Objects.nonNull(header) && !header.isEmpty()) {
				for (Map.Entry<String, String> entry : header.entrySet()) {
					//打印头部信息
					if (log.isDebugEnabled()) {
						log.debug(entry.getKey() + ":" + entry.getValue());
					}
					request.setHeader(entry.getKey(), entry.getValue());
				}
			}
			if (StringUtils.isNotEmpty(params)) {
				if (HttpMethod.POST.equals(httpMethod) || HttpMethod.PUT.equals(httpMethod)) {
					((HttpEntityEnclosingRequest) request).setEntity(new StringEntity(params, contentType));
				}
			}
			httpResponse = httpClient.execute(request);
			HttpEntity httpEntity = httpResponse.getEntity();
			statusCode = String.valueOf(httpResponse.getStatusLine().getStatusCode()); // 获取响应状态码
			log.info("sendHttp response status:{}", httpResponse.getStatusLine());
			if (Objects.nonNull(httpEntity)) {
				resultBody = EntityUtils.toString(httpEntity, "UTF-8");
				log.info("sendHttp response body:{}", resultBody);
			}
		} catch (Exception e) {
			log.error(infoMessage + " failure", e);
		} finally {
			HttpClientUtils.closeQuietly(httpResponse);//关闭返回对象
		}
		long endTime = System.currentTimeMillis();
		log.info("request sendHttp response time cost:" + (endTime - beginTime) + " ms");
		HttpResult httpResult = new HttpResult();
		httpResult.setRequestBody(infoMessage);
		httpResult.setResultBody(resultBody);
		httpResult.setStatusCode(statusCode);
		return httpResult;
	}


	/**
	 * 构建请求方法
	 *
	 * @param method
	 * @param url
	 * @return
	 */
	private static HttpRequestBase buildHttpMethod(HttpMethod method, String url) {
		if (HttpMethod.GET.equals(method)) {
			return new HttpGet(url);
		} else if (HttpMethod.POST.equals(method)) {
			return new HttpPost(url);
		} else if (HttpMethod.PUT.equals(method)) {
			return new HttpPut(url);
		} else if (HttpMethod.DELETE.equals(method)) {
			return new HttpDelete(url);
		} else {
			return null;
		}
	}

	/**
	 * 封装请求头
	 *
	 * @param headers
	 * @param httpMethod httpPost.setHeader("Cookie", "");
	 *                   httpPost.setHeader("Connection", "keep-alive");
	 *                   httpPost.setHeader("Accept", "application/json");
	 *                   httpPost.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
	 *                   httpPost.setHeader("Accept-Encoding", "gzip, deflate, br");
	 *                   httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
	 */
	public static void packageHeader(HashMap<String, String> headers, HttpRequestBase httpMethod) {
		// 封装请求头
		if (headers != null && headers.size() > 0) {
			Set<Map.Entry<String, String>> entrySet = headers.entrySet();
			for (Map.Entry<String, String> entry : entrySet) {
				// 设置到请求头到HttpRequestBase对象中
				httpMethod.setHeader(entry.getKey(), entry.getValue());
			}
		}
	}
}
