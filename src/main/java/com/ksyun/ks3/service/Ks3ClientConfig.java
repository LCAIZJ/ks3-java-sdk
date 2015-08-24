package com.ksyun.ks3.service;

import org.apache.http.client.HttpClient;

import com.ksyun.ks3.http.HttpClientConfig;

/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2015年5月14日 下午5:00:38
 * 
 * @description 客户端配置
 **/
public class Ks3ClientConfig {
	public static enum PROTOCOL{
		http,https
	}
	/**
	 * 服务地址</br>
	 * 中国标准:kss.ksyun.com</br>
	 * 中国cdn:kssws.ks-cdn.com</br>
	 * 美国（圣克拉拉）:ks3-us-west-1.ksyun.com</br>
	 */
	private String endpoint = "kssws.ks-cdn.com";
	/**
	 * http或者https
	 */
	private PROTOCOL protocol = PROTOCOL.http;
	/**
	 * 是否使用path style access方式访问
	 */
	private boolean pathStyleAccess = false;
	private String signerClass = "com.ksyun.ks3.signer.DefaultSigner";
	private HttpClientConfig httpClientConfig =  new HttpClientConfig();
	public String getEndpoint() {
		return endpoint;
	}
	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}
	public PROTOCOL getProtocol() {
		return protocol;
	}
	public void setProtocol(PROTOCOL protocol) {
		this.protocol = protocol;
	}
	public boolean isPathStyleAccess() {
		return pathStyleAccess;
	}
	public void setPathStyleAccess(boolean pathStyleAccess) {
		this.pathStyleAccess = pathStyleAccess;
	}
	public String getSignerClass() {
		return signerClass;
	}
	public void setSignerClass(String signerClass) {
		this.signerClass = signerClass;
	}
	public HttpClientConfig getHttpClientConfig() {
		return httpClientConfig;
	}
	public void setHttpClientConfig(HttpClientConfig httpClientConfig) {
		this.httpClientConfig = httpClientConfig;
	}
	public Ks3ClientConfig withHttpClientConfig(HttpClientConfig httpClientConfig) {
		this.httpClientConfig = httpClientConfig;
		return this;
	}
}
