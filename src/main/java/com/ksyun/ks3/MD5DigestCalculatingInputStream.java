package com.ksyun.ks3;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年10月27日 上午10:57:46
 * 
 * @description 当流被读取完毕时会计算出被读取的内容的md5值
 **/
public class MD5DigestCalculatingInputStream extends FilterInputStream {
	private MessageDigest digest;
	private MessageDigest digestLastMarked;

	public MD5DigestCalculatingInputStream(InputStream in) {
		super(in);
		try {
			digest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) { // should never occur
			throw new IllegalStateException("unexpected", e);
		}
	}

	public byte[] getMd5Digest() {
		return digest.digest();
	}

	@Override
	public void mark(int readlimit) {
		super.mark(readlimit);
		if (markSupported()) {
			try {
				digestLastMarked = (MessageDigest) digest.clone();
			} catch (CloneNotSupportedException e) { // should never occur
				throw new IllegalStateException("unexpected", e);
			}
		}
	}

	/**
	 * Resets the wrapped input stream and the in progress message digest.
	 */
	@Override
	public void reset() throws IOException {
		super.reset();
		if (digestLastMarked != null) {
			try {
				digest = (MessageDigest) digestLastMarked.clone();
			} catch (CloneNotSupportedException e) { // should never occur
				throw new IllegalStateException("unexpected", e);
			}
		}
	}

	@Override
	public int read() throws IOException {
		int ch = super.read();
		if (ch != -1) {
			digest.update((byte) ch);
		}
		return ch;
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		int result = super.read(b, off, len);
		if (result != -1) {
			digest.update(b, off, result);
		}
		return result;
	}
}