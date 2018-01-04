package com.htw.hive.jdbctest;

import java.util.List;

import org.apache.hadoop.hive.service.HiveClient;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;

public class HIveThriftClient {
	public static void main(String[] args) throws Exception {
		// 创建Socket连接
		final TSocket tSocket = new TSocket("192.168.170.102", 10000);
		// 创建一个协议
		final TProtocol tProtocol = new TBinaryProtocol(tSocket);
		// 创建hive client
		final HiveClient client = new HiveClient(tProtocol);
		// 打开Socket
		tSocket.open();
		// 执行Hql
		client.execute("desc t1");
		// 返回结果
		List<String> columns = client.fetchAll();
		for (String col : columns) {
			System.out.println(col);
		}
	}
}
