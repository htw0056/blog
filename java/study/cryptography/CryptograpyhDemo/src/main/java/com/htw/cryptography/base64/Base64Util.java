package com.htw.cryptography.base64;

import java.util.Base64;

public class Base64Util {
	/**
	 * JDK base64 encoder
	 * 
	 * @param data
	 * @return
	 */
	public static byte[] encryptBase64(byte[] data) {
		return Base64.getEncoder().encode(data);
	}

	/**
	 * JDK base64 decoder
	 * 
	 * @param data
	 * @return
	 */
	public static byte[] decryptBase64(byte[] data) {
		return Base64.getDecoder().decode(data);

	}

	/**
	 * Commons Codec base64 encoder
	 * 
	 * @param data
	 * @return
	 */
	public static byte[] encryptBase64CC(byte[] data) {
		return org.apache.commons.codec.binary.Base64.encodeBase64(data);
	}

	/**
	 * Commons Codec base64 decoder
	 * 
	 * @param data
	 * @return
	 */
	public static byte[] decryptBase64CC(byte[] data) {
		return org.apache.commons.codec.binary.Base64.decodeBase64(data);
	}

	/**
	 * Bouncy Castle base64 encoder
	 * 
	 * @param data
	 * @return
	 */
	public static byte[] encryptBase64BC(byte[] data) {
		return org.bouncycastle.util.encoders.Base64.encode(data);
	}

	/**
	 * Bouncy Castle base64 decoder
	 * 
	 * @param data
	 * @return
	 */
	public static byte[] decryptBase64BC(byte[] data) {
		return org.bouncycastle.util.encoders.Base64.decode(data);
	}

	public static void main(String[] args) {
		String s = "hello world!";
		// jdk
		byte[] encodeBytes = Base64Util.encryptBase64(s.getBytes());
		System.out.println(new String(encodeBytes));

		byte[] decodeBytes = Base64Util.decryptBase64(encodeBytes);
		System.out.println(new String(decodeBytes));

		// cc
		encodeBytes = Base64Util.encryptBase64CC(s.getBytes());
		System.out.println(new String(encodeBytes));

		decodeBytes = Base64Util.decryptBase64CC(encodeBytes);
		System.out.println(new String(decodeBytes));

		// bc
		encodeBytes = Base64Util.encryptBase64BC(s.getBytes());
		System.out.println(new String(encodeBytes));

		decodeBytes = Base64Util.decryptBase64BC(encodeBytes);
		System.out.println(new String(decodeBytes));

	}
}
