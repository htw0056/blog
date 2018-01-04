package com.htw.cryptography.base64;

import static org.junit.Assert.*;

import org.junit.Test;

public class Base64UtilTest {

	@Test
	public void testEncryptBase64() {
		assertEquals("aGVsbG8=", new String(Base64Util.encryptBase64("hello".getBytes())));
	}

	@Test
	public void testDecryptBase64() {
		assertEquals("hello", new String(Base64Util.decryptBase64("aGVsbG8=".getBytes())));
	}

	@Test
	public void testEncryptBase64CC() {
		assertEquals("aGVsbG8=", new String(Base64Util.encryptBase64CC("hello".getBytes())));
	}

	@Test
	public void testDecryptBase64CC() {
		assertEquals("hello", new String(Base64Util.decryptBase64CC("aGVsbG8=".getBytes())));
	}

	@Test
	public void testEncryptBase64BC() {
		assertEquals("aGVsbG8=", new String(Base64Util.encryptBase64BC("hello".getBytes())));
	}

	@Test
	public void testDecryptBase64BC() {
		assertEquals("hello", new String(Base64Util.decryptBase64BC("aGVsbG8=".getBytes())));
	}

	

}
