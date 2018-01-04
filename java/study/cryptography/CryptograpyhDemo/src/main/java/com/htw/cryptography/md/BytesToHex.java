package com.htw.cryptography.md;

public class BytesToHex {
	public static String fromBytesToHex(byte[] resultBytes) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < resultBytes.length; i++) {
			String tmp = Integer.toHexString(0xFF & resultBytes[i]);
			if (tmp.length() == 1) {
				builder.append("0").append(tmp);
			} else {
				builder.append(tmp);
			}
		}
		return builder.toString();
	}
}
