package org.springframework.boot.goat.jdbc.tests.controller;

import com.zaxxer.hikari.HikariConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;

/**
 * Created by Administrator on 2020/4/18.
 *
 * @ Description: TODO
 * @ author  山羊来了
 * @ date 2020/4/18---23:01
 */
@RestController
@RequestMapping("/test")
public class TestController {

	@Autowired
	@Qualifier("primaryDataSource")
	private DataSource primaryDataSource;

	@Autowired @Qualifier("secondaryDataSource")
	private DataSource secondaryDataSource;

	// 测试地址： 	http://localhost:8080/test/test1
	@GetMapping("test1")
	public void test1(){
		System.out.println(primaryDataSource);
		System.out.println(secondaryDataSource);
	}

	@Autowired
	HikariConfig hikariConfig;

	// 测试地址： 	http://localhost:8080/test/test2
	@GetMapping("test2")
	public void test2(){
		System.out.println("池中最小空闲连接数量：" + hikariConfig.getMinimumIdle());
		System.out.println("池中最大连接数（包括空闲和正在使用的连接）：" + hikariConfig.getMaximumPoolSize());
		System.out.println("是否自动提交池中返回的连接：" + hikariConfig.isAutoCommit());
		System.out.println("空闲时间：" + hikariConfig.getIdleTimeout());
		System.out.println("连接池中连接的最大生命周期：" + hikariConfig.getMaxLifetime());
		System.out.println("连接超时时间：" + hikariConfig.getConnectionTimeout());
	}
}
