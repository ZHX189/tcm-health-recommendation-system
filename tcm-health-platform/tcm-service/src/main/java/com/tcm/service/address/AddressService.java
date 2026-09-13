package com.tcm.service.address;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tcm.model.dto.AddressDTO;
import com.tcm.model.entity.UserAddress;

import java.util.List;

/**
 * 地址服务接口
 *
 * @author Ti
 * @since 2026-02-03
 */
public interface AddressService extends IService<UserAddress> {

    /**
     * 获取用户地址列表
     */
    List<UserAddress> getAddressList(Long userId);

    /**
     * 新增地址
     */
    void addAddress(Long userId, AddressDTO dto);

    /**
     * 更新地址
     */
    void updateAddress(Long userId, Long addressId, AddressDTO dto);

    /**
     * 删除地址
     */
    void deleteAddress(Long userId, Long addressId);

    /**
     * 设置默认地址
     */
    void setDefault(Long userId, Long addressId);
}
