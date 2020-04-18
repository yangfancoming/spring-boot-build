package org.springframework.boot.goat.jdbc.tests;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by 64274 on 2019/6/26.
 * @ author  山羊来了
 * @ date 2019/6/26---19:02
 *
 * 项目启动后 查看控制台：
 * 2020-04-18 11:13:07.884  INFO 8580 --- [ main] o.s.j.e.a.AnnotationMBeanExporter
 * : Located MBean 'dataSource': registering with JMX server as MBean [com.zaxxer.hikari:name=dataSource,type=HikariDataSource]
 */
@SpringBootApplication
public class MyTestJDBCApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyTestJDBCApplication.class, args);
	}
}
