package com.htw.test;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TestURLDownload2 {

	public static void main(String[] args) throws Exception {
		String url = "http://www.baidu.com";
		URL httpUrl = new URL(url);

		HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
		conn.setReadTimeout(5000);
		conn.setRequestMethod("GET");
		conn.connect();
		InputStream is = conn.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);

		StringBuffer sb = new StringBuffer();
		String line = null;
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		System.out.println(sb.toString());
		br.close();
		isr.close();
		is.close();

	}

}
