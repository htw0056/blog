# Base64

### 1. [介绍](https://zh.wikipedia.org/zh-cn/Base64)

Base64是一种基于64个可打印字符来表示二进制数据的表示方法。由于2的6次方等于64，所以每6个位元为一个单元，对应某个可打印字符。三个字节有24个位元，对应于4个Base64单元，即3个字节需要用4个可打印字符来表示（3*8/6=4）。它可用来作为电子邮件的传输编码。在Base64中的可打印字符包括字母A-Z、a-z、数字0-9，这样共有62个字符，此外两个可打印符号在不同的系统中而不同。一些如uuencode的其他编码方法，和之后BinHex的版本使用不同的64字符集来代表6个二进制数字，但是它们不叫Base64。  

在MIME格式的电子邮件中，base64可以用来将binary的字节序列数据编码成ASCII字符序列构成的文本。使用时，在传输编码方式中指定base64。使用的字符包括大小写字母各26个，加上10个数字，和加号“+”，斜杠“/”，一共64个字符，等号“=”用来作为后缀用途。

### 2. 转换规则

转换的时候，将3个byte的数据，先后放入一个24bit的缓冲区中，先来的byte占高位。数据不足3byte的话，于缓冲器中剩下的bit用0补足。然后，每次取出6（因为2^6=64）个bit，按照其值选择ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/中的字符作为编码后的输出。不断进行，直到全部输入数据转换完成。

| Value | Char | Value | Char | Value | Char | Value | Char |
| ----- | ---- | ----- | ---- | ----- | ---- | ----- | ---- |
| 0     | `A`  | 16    | `Q`  | 32    | `g`  | 48    | `w`  |
| 1     | `B`  | 17    | `R`  | 33    | `h`  | 49    | `x`  |
| 2     | `C`  | 18    | `S`  | 34    | `i`  | 50    | `y`  |
| 3     | `D`  | 19    | `T`  | 35    | `j`  | 51    | `z`  |
| 4     | `E`  | 20    | `U`  | 36    | `k`  | 52    | `0`  |
| 5     | `F`  | 21    | `V`  | 37    | `l`  | 53    | `1`  |
| 6     | `G`  | 22    | `W`  | 38    | `m`  | 54    | `2`  |
| 7     | `H`  | 23    | `X`  | 39    | `n`  | 55    | `3`  |
| 8     | `I`  | 24    | `Y`  | 40    | `o`  | 56    | `4`  |
| 9     | `J`  | 25    | `Z`  | 41    | `p`  | 57    | `5`  |
| 10    | `K`  | 26    | `a`  | 42    | `q`  | 58    | `6`  |
| 11    | `L`  | 27    | `b`  | 43    | `r`  | 59    | `7`  |
| 12    | `M`  | 28    | `c`  | 44    | `s`  | 60    | `8`  |
| 13    | `N`  | 29    | `d`  | 45    | `t`  | 61    | `9`  |
| 14    | `O`  | 30    | `e`  | 46    | `u`  | 62    | `+`  |
| 15    | `P`  | 31    | `f`  | 47    | `v`  | 63    | `/`  |


![base64-1](https://github.com/htw0056/blog/raw/master/pic/java/study/cryptography/base64-1.png)

如果要编码的字节数不能被3整除，最后会多出1个或2个字节，那么可以使用下面的方法进行处理：

1. 当最后剩余一个八位字节（1个byte）时，末尾补四个0，得到一个两位的Base64编码，最后附加上两个等号。

![base64-2](https://github.com/htw0056/blog/raw/master/pic/java/study/cryptography/base64-2.png)

2. 如果最后剩余两个八位字节（2个byte）时，末尾补两个0，得到一个三位的Base64编码，最后附加一个等号。

![base64-3](https://github.com/htw0056/blog/raw/master/pic/java/study/cryptography/base64-3.png)



### 3. 使用Base64发送邮件


```
telnet smtp.163.com 25
HELO htw
AUTH LOGIN
test(账号的Base64)
test(密码的Base64)
MAIL FROM:<test@163.com>
RCPT TO:<test@163.com>
DATA
SUBJECT:Hello
FROM:test@163.com
TO:test@163.com

HELLO WROLD!
.
RSET
QUIT
```

### 4. Java代码发送邮件

```java
package com.htw.base64;

import java.io.IOException;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;

public class Base64Util {

	public static String encryptBase64(byte[] data) {
		return new BASE64Encoder().encode(data);
	}

	public static String decryptBase64(String data) throws IOException {
		byte[] resultBytes = new BASE64Decoder().decodeBuffer(data);
		return new String(resultBytes);
	}
}

```

```
package com.htw.base64;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Test {

	public static void main(String[] args)  {
		String sender = "test@163.com";
		String reciver = "test@qq.com";
		String password = "test";

		String userBase64 = Base64Util.encryptBase64(sender.substring(0,
				sender.indexOf("@")).getBytes());
		String passBase64 = Base64Util.encryptBase64(password.getBytes());

		Socket socket;
		
		try {
			socket = new Socket("smtp.163.com", 25);
			InputStream inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));
			PrintWriter writer = new PrintWriter(outputStream, true);
			System.out.println(reader.readLine());
			writer.println("HELO test");

			System.out.println(reader.readLine());
			writer.println("AUTH LOGIN");
			System.out.println(reader.readLine());
			writer.println(userBase64);
			System.out.println(reader.readLine());
			writer.println(passBase64);
			System.out.println(reader.readLine());

			writer.println("MAIL FROM:<" + sender + ">");
			System.out.println(reader.readLine());
			writer.println("RCPT TO:<" + reciver + ">");
			System.out.println(reader.readLine());
			writer.println("DATA");
			System.out.println(reader.readLine());

			writer.println("SUBJECT:hello test");
			writer.println("FROM:" + sender);
			writer.println("TO:" + reciver);
			writer.println("");
			writer.println("hello world!");
			writer.println(".");
			System.out.println(reader.readLine());

			writer.println("RSET");
			System.out.println(reader.readLine());
			writer.println("QUIT");
			System.out.println(reader.readLine());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
```