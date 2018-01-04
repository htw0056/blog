package com.htw.threadsocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ThreadServer implements Runnable
{
	Socket socket = null;

	public ThreadServer(Socket socket)
	{
		this.socket = socket;
	}

	@Override
	public void run()
	{
		InputStream is = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		OutputStream os = null;
		PrintWriter pw = null;
		try
		{
			// 1.获取输入流
			is = socket.getInputStream();
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null)
			{
				System.out.println("服务器:" + line);
			}
			socket.shutdownInput();

			// 反馈信息
			// 2.获取输出流，响应客户端
			os = socket.getOutputStream();
			pw = new PrintWriter(os);
			pw.write("server:hello");
			pw.flush();
			socket.shutdownOutput();
		} catch (IOException e)
		{
			e.printStackTrace();
		} finally
		{
			try
			{
				// 3. 关闭资源
				if (pw != null)
					pw.close();
				if (os != null)
					os.close();
				if (br != null)
					br.close();
				if (isr != null)
					isr.close();
				if (is != null)
					is.close();
				if (socket != null)
					socket.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}

		}
	}

}
