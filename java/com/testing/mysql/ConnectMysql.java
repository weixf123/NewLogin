package com.testing.mysql;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class ConnectMysql {
	public Connection conn;

	// 数据库连接的地址、用户名和密码
	// ?之前是数据库的地址端口和数据库名，？后面的是连接的参数。
	public ConnectMysql() {
		try {
			FileInputStream fis = new FileInputStream(this.getClass().getResource("").getPath() + "/inter.properties");
			Properties prop = new Properties();
			prop.load(fis);
			String dbUrl = prop.getProperty("DBURL");
			String dbUser = prop.getProperty("DBUSER");
			String dbPwd = prop.getProperty("DBPWD");
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
			// 设置超时时间
			DriverManager.setLoginTimeout(10);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
