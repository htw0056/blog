package com.htw.test;

import java.net.MalformedURLException;
import java.net.URL;

public class TestUrl
{

	public static void main(String[] args) throws MalformedURLException
	{
		// 创建url实例
		URL url = new URL("http://www.google.com");
		URL url2 = new URL(url, "/index.html?username=abc#test");
		System.out.println("协议:" + url2.getProtocol());
		System.out.println("主机:" + url2.getHost());
		// 如果未指定端口号，则使用默认端口号80，此时使用getPort()返回-1
		System.out.println("端口:" + url2.getPort());
		System.out.println("文件路径:" + url2.getPath());
		System.out.println("文件名:" + url2.getFile());
		System.out.println("相对路径:" + url2.getRef());
		System.out.println("查询字符串" + url2.getQuery());

	}

}
