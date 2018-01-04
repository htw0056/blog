package com.htw.cryptography.md;

import java.io.File;
import java.io.FileInputStream;
import java.security.DigestInputStream;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class MDUtil {
	/**
	 * MD5
	 * 
	 * @param data
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String encryptMD5(byte[] data) throws NoSuchAlgorithmException {

		// param:MD2,MD5,SHA,SHA-1,SHA-256,SHA-384,SHA-512
		MessageDigest md5 = MessageDigest.getInstance("md5");

		md5.update(data);

		byte[] resultBytes = md5.digest();
		return BytesToHex.fromBytesToHex(resultBytes);
	}

	/**
	 * FILE MD5
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public static String getFileMD5(String path) throws Exception {
		FileInputStream fis = new FileInputStream(new File(path));
		DigestInputStream dis = new DigestInputStream(fis, MessageDigest.getInstance("MD5"));
		byte[] buffer = new byte[1024];
		// input
		while (dis.read(buffer, 0, 1024) != -1) {
			;
		}

		MessageDigest md = dis.getMessageDigest();
		byte[] resultBytes = md.digest();

		dis.close();
		fis.close();

		return BytesToHex.fromBytesToHex(resultBytes);
	}

	// hmac密钥生成
	public static byte[] initHmacKey() throws NoSuchAlgorithmException {
		KeyGenerator keyGen = KeyGenerator.getInstance("HmacMD5");
		// get SecretKey
		SecretKey secretKey = keyGen.generateKey();
		// SecretKey to byte[]
		return secretKey.getEncoded();
	}

	// hmac
	public static String encrypHmac(byte[] data, byte[] key) throws NoSuchAlgorithmException, InvalidKeyException {
		// byte[] key to SecretKey
		SecretKey secretkey = new SecretKeySpec(key, "hmacMD5");

		Mac mac = Mac.getInstance("hmacMD5");
		mac.init(secretkey);
		byte[] resultBytes = mac.doFinal(data);

		return BytesToHex.fromBytesToHex(resultBytes);
	}

	public static void main(String[] args) throws Exception {
		System.out.println(MDUtil.encryptMD5("hello".getBytes()));

		byte[] hmackey = MDUtil.initHmacKey();
		System.out.println("hmac key:\t" + BytesToHex.fromBytesToHex(hmackey));
		String hmacResult = MDUtil.encrypHmac("hello".getBytes(), hmackey);
		System.out.println(hmacResult);
	}
}
