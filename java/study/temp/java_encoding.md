# JAVA编码


```java
package com.htw.testio;

import java.io.UnsupportedEncodingException;

public class testforio {

	public static void main(String[] args) throws UnsupportedEncodingException {
		String s = "你好ABC";
		
		byte[] bytes1 = s.getBytes();
		for (byte b : bytes1) {
			System.out.print(Integer.toHexString(b & 0xff) + " ");
		}
		//out:e4 bd a0 e5 a5 bd 41 42 43
		//采用默认编码,我的环境是UTF-8
		
		
		System.out.println();
		byte[] bytes2 = s.getBytes("gbk");

		for (byte b : bytes2) {
			System.out.print(Integer.toHexString(b & 0xff) + " ");
		}
		//out:c4 e3 ba c3 41 42 43
		//采用gbk

		System.out.println();
		byte[] bytes3 = s.getBytes("utf-16be");
		for (byte b : bytes3) {
			System.out.print(Integer.toHexString(b & 0xff) + " ");
		}
		//out:4f 60 59 7d 0 41 0 42 0 43
		//采用utf-16be

		System.out.println();
		byte[] bytes4 = s.getBytes("utf-8");
		for(byte b : bytes4){
			System.out.print(Integer.toHexString(b & 0xff)+ " ");
		}
		//out:e4 bd a0 e5 a5 bd 41 42 43
		//采用utf-8

		System.out.println();
		String str1 = new String(bytes4,"utf-8");
		System.out.println(str1);
		//out:你好ABC
		
		System.out.println();
		String str2 = new String(bytes4,"gbk");
		System.out.println(str2);
		//乱码
	}
}

```

根据上面的代码可以知道,  
中文在uft-8格式下是占3个字节，字母占1个字节  
中文在gbk格式下是占2个字节，字母占1个字节  
中文在utf-16be格式下是占2个字节，字母占2个字节  

而字节转换为字符串的时候，如果指定的编码不正确就不能得到正确的结果。