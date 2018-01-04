# java HTTP通信

### 1. get


```java

package com.htw.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class TestForGet {
	public static void main(String[] args) {
		new Thread(new ReadByGet()).start();
	}
}

class ReadByGet implements Runnable {

	@Override
	public void run() {
		try {

			URL url = new URL(
					"http://fanyi.youdao.com/openapi.do?keyfrom=testforgetg&key=1560096584&type=data&doctype=json&version=1.1&q=welcome");
			URLConnection connection = url.openConnection();
			InputStream is = connection.getInputStream();
			InputStreamReader isr = new InputStreamReader(is,"UTF-8");
			BufferedReader br = new BufferedReader(isr);

			String line = null;
			StringBuilder builder = new StringBuilder();
			while ((line = br.readLine()) != null) {
				builder.append(line);
			}
			br.close();
			isr.close();
			is.close();

			System.out.println(builder.toString());

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
```


### 2. post

```java
package com.htw.http;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class TestForPost {

	public static void main(String[] args) {
		new Thread(new ReadByPost()).start();
	}

}

class ReadByPost implements Runnable {

	@Override
	public void run() {

		try {

			URL url = new URL("http://fanyi.youdao.com/openapi.do");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			// 顺序很重要
			// 创建了之后必须一次性设定参数
			connection.addRequestProperty("encoding", "UTF-8");
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");

			// 获得输出流
			OutputStream os = connection.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os);
			BufferedWriter bw = new BufferedWriter(osw);

			bw.write("keyfrom=testforgetg&key=1560096584&type=data&doctype=json&version=1.1&q=welcome");
			bw.flush();

			// 获得输入流
			InputStream is = connection.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			StringBuilder builder = new StringBuilder();
			while ((line = br.readLine()) != null) {
				builder.append(line);
			}

			System.out.println(builder.toString());
			bw.close();
			osw.close();
			os.close();
			br.close();
			isr.close();
			is.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
```

