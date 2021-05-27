package org.springframework.boot.goat.mvc.tests;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by 64274 on 2019/6/26.
 *
 * @ Description: TODO
 * @ author  山羊来了
 * @ date 2019/6/26---19:03
 */
@RestController
@RequestMapping("/test")
public class MyController {

	// 测试地址： http://localhost:8080/test/hello
	@RequestMapping("/hello")
	public String hello() {
		return "hello world";
	}
}
