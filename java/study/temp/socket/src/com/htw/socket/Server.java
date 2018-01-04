package com.htw.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server
{

	public static void main(String[] args)
	{
		try
		{
			// 1.创建服务器端socket，并且绑定端口号
			ServerSocket serverSocket = new ServerSocket(8888);
			// 2.调用accept()方法开始监听，等待客户端的连接
			System.out.println("****服务器启动，等待客户端连接****");
			Socket socket = serverSocket.accept();
			// 3.获取输入流
			InputStream is = socket.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null)
			{
				System.out.println("服务器:" + line);
			}
			socket.shutdownInput();

			// 反馈信息
			// 4.获取输出流，响应客户端
			OutputStream os = socket.getOutputStream();
			PrintWriter pw = new PrintWriter(os);
			pw.write("server:hello");
			pw.flush();
			socket.shutdownOutput();
			// 5. 关闭资源
			pw.close();
			os.close();
			br.close();
			isr.close();
			is.close();
			socket.close();
			serverSocket.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}

	}

}
