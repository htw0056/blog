package com.htw.threadsocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server
{

	public static void main(String[] args)
	{
		ServerSocket serverSocket = null;
		try
		{
			// 1.创建服务器端socket，并且绑定端口号
			serverSocket = new ServerSocket(8888);
			System.out.println("****服务器启动，等待客户端连接****");
			// 2. 循环监听
			while (true)
			{
				Socket socket = serverSocket.accept();
				new Thread(new ThreadServer(socket)).start();
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}

	}

}
