package com.tcm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tcm.model.entity.UserAddress;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户地址Mapper
 *
 * @author Ti
 * @since 2026-02-03
 */
@Mapper
public interface UserAddressMapper extends BaseMapper<UserAddress> {
}
