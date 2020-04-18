//package org.springframework.boot.goat.jdbc.tests.controller;
//
//import com.zaxxer.hikari.HikariConfig;
//import com.zaxxer.hikari.HikariDataSource;
//import org.junit.Assert;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//import javax.sql.DataSource;
//import java.sql.Connection;
//import java.sql.SQLException;
//
///**
// * Created by Administrator on 2020/4/18.
// * @ Description: springboot2.x  默认使用的是 HikariDataSource 连接池
// * @ author  山羊来了
// * @ date 2020/4/18---11:00
// */
//
//@RestController
//public class HelloController {
//
//	@Autowired
//	DataSource dataSource;
//
//	Connection connection;
//
//	// 测试地址： 	http://localhost:8080/test1
//	@GetMapping("test1")
//	public void test() throws SQLException {
//		Assert.assertEquals(com.zaxxer.hikari.HikariDataSource.class,dataSource.getClass());
//		connection = dataSource.getConnection();
//		// HikariProxyConnection@1515248124 wrapping com.mysql.jdbc.JDBC4Connection@106d77da
//		System.out.println(connection);
//		if (dataSource instanceof com.zaxxer.hikari.HikariDataSource) {
//			com.zaxxer.hikari.HikariDataSource dataSourceHK = (HikariDataSource) dataSource;
//			System.out.println(dataSourceHK);
//		}
//	}
//
//	@GetMapping("test2")
//	public void test2() throws SQLException {
//		connection.close();
//	}
//
//
//}
