package org.springframework.boot.goat.undertow.tests;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.handlers.resource.PathResourceManager;

import java.io.File;

/**
 * Created by Administrator on 2020/5/14.
 *
 * @ Description: 项目启动后 访问： http://localhost:8080/
 * @ author  山羊来了
 * @ date 2020/5/14---0:17
 */
public class FileServer {
	public static void main(String[] args) {
		File file = new File("/");
		Undertow server = Undertow.builder().addHttpListener(8080, "localhost")
				.setHandler(Handlers.resource(new PathResourceManager(file.toPath(), 100)).setDirectoryListingEnabled(true))
				.build();
		server.start();
	}
}