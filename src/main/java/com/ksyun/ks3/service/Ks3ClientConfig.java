package com.ksyun.ks3.service;
/**
 * @author lijunwei[lijunwei@kingsoft.com]  
 * 
 * @date 2015年5月14日 下午5:00:38
 * 
 * @description 每个client的单独配置，将会覆盖全局的配置
 **/
public class Ks3ClientConfig {
	private String endpoint;
	public String getEndpoint() {
		return endpoint;
	}
	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}
	
}
