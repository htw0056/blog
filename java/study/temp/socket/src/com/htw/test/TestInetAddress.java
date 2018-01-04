package com.htw.test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

public class TestInetAddress
{
	public static void main(String[] args) throws UnknownHostException
	{
		// 获得本机的InetAddress实例
		InetAddress inetAddress = InetAddress.getLocalHost();
		System.out.println("计算机名:" + inetAddress.getHostName());
		System.out.println("IP地址:" + inetAddress.getHostAddress());
		byte[] bytes = inetAddress.getAddress();
		System.out.println("字节数组形式的IP地址:" + Arrays.toString(bytes));
		System.out.println(inetAddress);
		// 根据机器名获得InetAddress实例
		// InetAddress inetAddress2 = InetAddress.getByName("bogon");
		// 根据ip地址获得InetAddress实例
		InetAddress inetAddress2 = InetAddress.getByName("10.32.30.64");
		System.out.println("计算机名:" + inetAddress2.getHostName());
		System.out.println("IP地址:" + inetAddress2.getHostAddress());

	}
}
