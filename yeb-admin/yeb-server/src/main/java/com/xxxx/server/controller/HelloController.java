package com.xxxx.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试
 *
 * @Author dingc
 * @Date 2021/12/15 20:54
 */
@RestController
public class HelloController {

	@GetMapping("hello")
	public String hello() {
		return "hello";
	}
}
