package com.htw.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClient
{
	public static void main(String[] args) throws IOException
	{
		// 1.定义服务器的地址，端口号，数据
		InetAddress inetAddress = InetAddress.getByName("localhost");
		int port = 8888;
		byte[] data = "hello world!".getBytes();
		// 2.创建数据报，包含发送的数据信息
		DatagramPacket packet = new DatagramPacket(data, data.length, inetAddress, port);
		// 3. 创建DatagramSocket对象
		DatagramSocket socket = new DatagramSocket();
		socket.send(packet);

		// 1.创建数据报，用于接收服务器端发送的数据
		byte[] data2 = new byte[1024];
		DatagramPacket packet2 = new DatagramPacket(data2, data2.length);
		// 2.接收服务器端发送的数据
		socket.receive(packet2);
		// 3.读取数据
		String info = new String(data2, 0, packet2.getLength());
		System.out.println(info);
		socket.close();
	}

}
