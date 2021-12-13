package com.xxxx.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.server.pojo.Admin;
import com.xxxx.server.pojo.RespBean;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author dingc
 * @since 2021-12-12
 */
public interface IAdminService extends IService<Admin> {

	/**
	 * 登录之后返回 token
	 *
	 * @param username
	 * @param password
	 * @param request
	 * @return
	 */
	RespBean login(String username, String password, HttpServletRequest request);

	/**
	 * 根据用户名获取用户
	 *
	 * @param username
	 * @return
	 */
	Admin getAdminByUserName(String username);

}
