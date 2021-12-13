package com.xxxx.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxxx.server.pojo.Admin;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dingc
 * @since 2021-12-12
 */
// @Repository, 加上之后, impl层不会再报错
public interface AdminMapper extends BaseMapper<Admin> {

}
