package com.ksyun.ks3.service.response;
/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年11月6日 下午4:20:08
 * 
 * @description sdk MD5校验采用client端校验的方式，即将返回的etag解密成MD5值进行校验
 **/
public interface Md5CheckAble {
	public String getETag();
}
