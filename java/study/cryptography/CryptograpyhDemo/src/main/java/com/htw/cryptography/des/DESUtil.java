package com.htw.cryptography.des;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

public class DESUtil {
	public static byte[] initKey() throws NoSuchAlgorithmException {
		KeyGenerator keyGen = KeyGenerator.getInstance("DES");
		keyGen.init(56);
		SecretKey secretKey = keyGen.generateKey();
		return secretKey.getEncoded();
	}

	public static byte[] encrypt(byte[] data, byte[] key) throws Exception {
		SecretKey secreKey = new SecretKeySpec(key, "DES");

		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(Cipher.ENCRYPT_MODE, secreKey);

		byte[] cipherBytes = cipher.doFinal(data);
		return cipherBytes;

	}

	public static byte[] decrypt(byte[] data, byte[] key) throws Exception {
		SecretKey secreKey = new SecretKeySpec(key, "DES");

		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(Cipher.DECRYPT_MODE, secreKey);

		byte[] cipherBytes = cipher.doFinal(data);
		return cipherBytes;

	}
	public static void main(String[] args) throws Exception {
		byte[] initKey = DESUtil.initKey();
		System.out.println("initKey:\t" + Hex.encodeHexString(initKey));
		byte[] encrypt = DESUtil.encrypt("hello".getBytes(), initKey);
		System.out.println("DES:\t" + Hex.encodeHexString(encrypt));
		byte[] decrypt = DESUtil.decrypt(encrypt, initKey);
		System.out.println("DES:\t" + new String(decrypt));
	}
}
