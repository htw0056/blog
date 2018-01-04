package com.htw.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPServer
{

	public static void main(String[] args) throws IOException
	{
		// 1.创建服务器端DatagramSocket,指定端口
		DatagramSocket socket = new DatagramSocket(8888);
		// 2.创建数据报，用于接收客户端发送的数据
		byte[] data = new byte[1024];
		DatagramPacket packet = new DatagramPacket(data, data.length);
		// 3.接收客户端发送的数据
		socket.receive(packet);
		// 4.读取数据
		String info = new String(data, 0, packet.getLength());
		System.out.println(info);
		// 服务器端反馈信息
		// 1.定义服务器的地址，端口号，数据
		InetAddress inetAddress =packet.getAddress();
		int port = packet.getPort();
		byte[] data2 = "hello!".getBytes();
		// 2.创建数据报，包含发送的数据信息
		DatagramPacket packet2 = new DatagramPacket(data2, data2.length, inetAddress, port);
		socket.send(packet2);
		socket.close();
	}

}
