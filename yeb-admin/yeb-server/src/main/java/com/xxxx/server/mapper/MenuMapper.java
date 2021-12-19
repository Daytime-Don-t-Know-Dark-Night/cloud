package com.xxxx.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxxx.server.pojo.Menu;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author dingc
 * @since 2021-12-12
 */
public interface MenuMapper extends BaseMapper<Menu> {

	/**
	 * 通过用户id查询菜单列表
	 *
	 * @param id
	 * @return
	 */
	List<Menu> getMenusByAdminId(Integer id);
}
