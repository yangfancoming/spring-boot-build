package org.springframework.boot.example;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 64274 on 2019/6/26.
 *
 * @ Description: TODO
 * @ author  山羊来了
 * @ date 2019/6/26---17:15
 */
@Controller
@RequestMapping(value = "/web", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@ResponseBody
public class WebController {

	@GetMapping(value = "/test")
	public List test() {
		List list = new ArrayList();
		list.add("这是测试");
		return list;
	}
}