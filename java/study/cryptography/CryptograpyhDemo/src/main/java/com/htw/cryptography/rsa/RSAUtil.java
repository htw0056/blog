package com.htw.cryptography.rsa;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Hex;

public class RSAUtil {
	public static final String PUBLIC_KEY = "RSAPublicKey";
	public static final String PRIVATE_KEY = "RSAPrivateKey";

	/*
	 * 生成RSA的公钥和密钥
	 */
	public static Map<String, Object> initKey() throws Exception {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(1024);
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateCrtKey privateKey = (RSAPrivateCrtKey) keyPair.getPrivate();
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(PUBLIC_KEY, publicKey);
		keyMap.put(PRIVATE_KEY, privateKey);
		return keyMap;
	}

	public static RSAPublicKey getPublicKey(Map<String, Object> keyMap) {
		RSAPublicKey publickey = (RSAPublicKey) keyMap.get(PUBLIC_KEY);
		return publickey;
	}

	public static RSAPrivateKey getPrivateKeyKey(Map<String, Object> keyMap) {
		RSAPrivateKey privatekey = (RSAPrivateKey) keyMap.get(PRIVATE_KEY);
		return privatekey;
	}

	/*
	 * 公钥加密
	 */
	public static byte[] encrypt(byte[] data, RSAPublicKey publicKey) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] cipherBytes = cipher.doFinal(data);
		return cipherBytes;

	}

	/*
	 * 私钥解密
	 */
	public static byte[] decrypt(byte[] data, RSAPrivateKey privateKey) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] plainBytes = cipher.doFinal(data);
		return plainBytes;
	}

	public static void main(String[] args) throws Exception {
		Map<String, Object> keyMap = RSAUtil.initKey();
		RSAPublicKey rsaPublicKey = RSAUtil.getPublicKey(keyMap);
		RSAPrivateKey rsaPrivateKey = RSAUtil.getPrivateKeyKey(keyMap);

		System.out.println(rsaPublicKey);
		System.out.println(rsaPrivateKey);

		byte[] rsaResult = RSAUtil.encrypt("hello".getBytes(), rsaPublicKey);
		System.out.println(Hex.encodeHexString(rsaResult));

		byte[] result = RSAUtil.decrypt(rsaResult, rsaPrivateKey);
		System.out.println(new String(result));
	}
}
