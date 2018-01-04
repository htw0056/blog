package com.htw.cryptography.signature;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Hex;

public class SignatureUtil {
	public static final String KEY_ALGORIGTHM = "RSA";
	public static final String RSA_PUBLIC_KEY = "RSA_PUBLIC_KEY";
	public static final String RSA_PRIVATE_KEY = "RSA_PRIVATE_KEY";
	public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

	/*
	 * 生成公私密钥堆
	 */
	public static Map<String, Object> initKey() throws Exception {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORIGTHM);
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(RSA_PUBLIC_KEY, publicKey);
		keyMap.put(RSA_PRIVATE_KEY, privateKey);
		return keyMap;
	}

	public static byte[] getPublicKey(Map<String, Object> keyMap) {
		RSAPublicKey key = (RSAPublicKey) keyMap.get(RSA_PUBLIC_KEY);
		return key.getEncoded();
	}

	public static byte[] getPrivateKey(Map<String, Object> keyMap) {
		RSAPrivateKey key = (RSAPrivateKey) keyMap.get(RSA_PRIVATE_KEY);
		return key.getEncoded();
	}

	/*
	 * 对原始数据用私钥进行签名
	 */
	public static byte[] sign(byte[] data, byte[] privateKey) throws Exception {
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKey);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORIGTHM);
		PrivateKey priKey = keyFactory.generatePrivate(keySpec);

		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initSign(priKey);
		signature.update(data);
		return signature.sign();
	}

	/*
	 * 根据原始数据，公钥，签名值进行验证
	 */
	public static boolean verify(byte[] data, byte[] publicKey, byte[] sign) throws Exception {
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORIGTHM);
		PublicKey pubKey = keyFactory.generatePublic(keySpec);

		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initVerify(pubKey);
		signature.update(data);
		boolean isValid = signature.verify(sign);
		return isValid;

	}

	public static void main(String[] args) throws Exception {

		String DATA = "test";
		// 初始化密钥对
		Map<String, Object> keyMap = initKey();
		byte[] rsaPublicKey = getPublicKey(keyMap);
		byte[] rsaPrivateKey = getPrivateKey(keyMap);
		System.out.println("RSASignature PublicKey  " + Hex.encodeHexString(rsaPublicKey));
		System.out.println("RSASignature PrivateKey  " + Hex.encodeHexString(rsaPrivateKey));

		// sign
		byte[] sign = sign(DATA.getBytes(), rsaPrivateKey);
		System.out.println("RSA Sign  " + Hex.encodeHexString(sign));

		// Verify
		boolean isValid = verify(DATA.getBytes(), rsaPublicKey, sign);
		System.out.println("RSA Verify  " + isValid);
	}
}
