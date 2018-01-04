package com.htw.hive.jdbctest;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Hello world!
 *
 */
public class HIveJDBCDemo {
	public static void main(String[] args) {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		String sql = "select * from t1";

		try {
			conn = HIveJDBCUtils.getConnection();
			st = conn.createStatement();
			rs = st.executeQuery(sql);

			while (rs.next()) {
				String a = rs.getString(1);
				double b = rs.getDouble(2);

				System.out.println("a:" + a + "\tb:" + b);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			HIveJDBCUtils.release(conn, st, rs);
		}
	}
}
