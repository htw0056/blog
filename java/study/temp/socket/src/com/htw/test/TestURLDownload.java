package com.htw.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class TestURLDownload
{

	public static void main(String[] args)
	{
		try
		{
			// 创建url实例
			URL url = new URL("http://www.baidu.com");
			// 通过url的openstream方法获取url对象所表示的资源的字节流输入
			InputStream is = url.openStream();
			// 将字节输入流转换为字符输入流
			InputStreamReader isr = new InputStreamReader(is, "utf-8");
			// 字符输入流加入缓冲
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			StringBuilder builder = new StringBuilder();
			while ((line = br.readLine()) != null)
			{
				builder.append(line);
			}
			br.close();
			isr.close();
			is.close();

			System.out.println(builder.toString());

		} catch (MalformedURLException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}

	}

}
