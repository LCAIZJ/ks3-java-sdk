package com.ksyun.ks3.utils;

import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hbase.util.Bytes;

import com.ksyun.ks3.dto.Authorization;
import com.ksyun.ks3.service.request.Ks3WebServiceRequest;

/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年10月16日 下午7:31:45
 * 
 * @description 
 **/
public class AuthUtils {
	private static final Log log = LogFactory.getLog(AuthUtils.class); 
	public static String calcAuthorization (Authorization auth,Ks3WebServiceRequest request) throws SignatureException
	{
		String signature = calcSignature(auth.getAccessKeySecret(),request);
		String value = "KSS "+auth.getAccessKeyId()+":"+signature;
		return value;
	}
	private static String calcSignature (String accessKeySecret,Ks3WebServiceRequest request) throws SignatureException
	{
        String resource = CanonicalizedKSSResource(request);
        String requestMethod = request.getHttpMethod().toString();
        String contentMd5 = request.getContentMD5();
        String contentType = request.getContentType();

        
        String _signDate = DateUtils.convertDate2Str(request.getDate(),
        		DateUtils.DATETIME_PROTOCOL.RFC1123);

        List<String> signList = new ArrayList<String>();
        signList.addAll(Arrays.asList(new String[] {
                requestMethod, contentMd5, contentType, _signDate
        }));

        String _headers = CanonicalizedKSSHeaders(request);
        if (_headers != null && !_headers.equals("")){
            signList.add(_headers);
        }

        signList.add(resource);
        
        String signStr = StringUtils.join(signList.toArray(), "\n");
        
        log.info("StringToSign:"+signStr.replace("\n","\\n"));
        
        String serverSignature = calculateRFC2104HMAC(signStr, accessKeySecret);
        return serverSignature;
	}
    public static String CanonicalizedKSSResource(Ks3WebServiceRequest request) {
    	boolean escapeDoubleSlash = true;

        String bucketName = request.getBucketname();
        String objectKey = request.getObjectkey();

        StringBuffer buffer = new StringBuffer();
        buffer.append("/");
        if (!StringUtils.isBlank(bucketName)) {
            buffer.append(bucketName).append("/");
        }
        
        if (!StringUtils.isBlank(objectKey)) {
        	String encodedPath = HttpUtils.urlEncode(objectKey, true);
            if (escapeDoubleSlash) {
                encodedPath = encodedPath.replace("//", "/%2F");
            }
            buffer.append(encodedPath);
        }
        
        String resource = buffer.toString();

        String queryParams = request.getParamsToSign();
        if (queryParams != null && !queryParams.equals(""))
        	resource = resource + "?" + queryParams;
        return resource;
    }
    private static String CanonicalizedKSSHeaders(Ks3WebServiceRequest request) {
    	String prefix = "x-kss";
        Map<String, String> headers = request.getHeader();

        List<String> headList = new ArrayList<String>();

        for (String _header : headers.keySet()) {
            if (_header.toLowerCase().startsWith(prefix)) {
                headList.add(_header);
            }
        }

        Collections.sort(headList, new Comparator<String>() {
            public int compare(String o1, String o2) {
                return Bytes.BYTES_COMPARATOR.compare(o1.getBytes(), o2.toString().getBytes());
            }
        });
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < headList.size(); i++) {
            String _key = headList.get(i);
            buffer.append(headList.get(i) + ":" + headers.get(_key));
            if (i < (headList.size() - 1))
                buffer.append("\n");
        }
        return buffer.toString();
    }
	private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
	private static String calculateRFC2104HMAC(String data, String key)
			throws java.security.SignatureException {
		String result;
		try {

			// get an hmac_sha1 key from the raw key bytes
			SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(),
					HMAC_SHA1_ALGORITHM);

			// get an hmac_sha1 Mac instance and initialize with the signing key
			Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
			mac.init(signingKey);

			// compute the hmac on input data bytes
			byte[] rawHmac = mac.doFinal(data.getBytes());

			// base64-encode the hmac
			result = new String(Base64.encodeBase64(rawHmac),"GBK");

		} catch (Exception e) {
			throw new SignatureException("Failed to generate HMAC : "
					+ e);
		}
		return result;
	}
}
