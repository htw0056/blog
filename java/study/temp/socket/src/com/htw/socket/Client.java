package com.htw.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client
{

	public static void main(String[] args)
	{
		try
		{
			// 1.创建客户端socket,指定服务器地址和端口号
			Socket socket = new Socket("localhost", 8888);
			// 2.获取输出流，向服务器发送信息
			OutputStream os = socket.getOutputStream();
			PrintWriter pw = new PrintWriter(os);
			pw.write("hello world");
			pw.flush();
			socket.shutdownOutput();

			// 获得服务器响应
			// 3. 获得输入流，并读取服务器端的响应信息
			InputStream is = socket.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null)
			{
				System.out.println("客户端:" + line);
			}
			socket.shutdownInput();
			// 4.关闭资源
			br.close();
			isr.close();
			is.close();
			pw.close();
			os.close();
			socket.close();

		} catch (UnknownHostException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}

	}

}
