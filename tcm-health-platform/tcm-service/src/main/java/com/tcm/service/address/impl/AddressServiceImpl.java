package com.tcm.service.address.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tcm.common.constant.CommonConstant;
import com.tcm.common.exception.BusinessException;
import com.tcm.mapper.UserAddressMapper;
import com.tcm.model.dto.AddressDTO;
import com.tcm.model.entity.UserAddress;
import com.tcm.service.address.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 地址服务实现
 *
 * @author Ti
 * @since 2026-02-03
 */
@Service
@RequiredArgsConstructor
public class AddressServiceImpl extends ServiceImpl<UserAddressMapper, UserAddress> implements AddressService {

    @Override
    public List<UserAddress> getAddressList(Long userId) {
        return list(new LambdaQueryWrapper<UserAddress>()
                .eq(UserAddress::getUserId, userId)
                .orderByDesc(UserAddress::getIsDefault)
                .orderByDesc(UserAddress::getCreateTime));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addAddress(Long userId, AddressDTO dto) {
        UserAddress address = new UserAddress();
        BeanUtil.copyProperties(dto, address);
        address.setUserId(userId);
        address.setDeleted(CommonConstant.DELETED_NO);

        // 如果设置为默认，则取消其他默认
        if (Integer.valueOf(1).equals(dto.getIsDefault())) {
            cancelDefault(userId);
        }

        // 如果是第一个地址，自动设为默认
        long count = count(new LambdaQueryWrapper<UserAddress>().eq(UserAddress::getUserId, userId));
        if (count == 0) {
            address.setIsDefault(1);
        }

        save(address);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAddress(Long userId, Long addressId, AddressDTO dto) {
        UserAddress address = getById(addressId);
        if (address == null || !address.getUserId().equals(userId)) {
            throw new BusinessException("地址不存在");
        }

        BeanUtil.copyProperties(dto, address, "id", "userId", "createTime", "deleted");

        // 如果设置为默认，则取消其他默认
        if (Integer.valueOf(1).equals(dto.getIsDefault())) {
            cancelDefault(userId);
        }

        updateById(address);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAddress(Long userId, Long addressId) {
        UserAddress address = getById(addressId);
        if (address == null || !address.getUserId().equals(userId)) {
            throw new BusinessException("地址不存在");
        }
        removeById(addressId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setDefault(Long userId, Long addressId) {
        UserAddress address = getById(addressId);
        if (address == null || !address.getUserId().equals(userId)) {
            throw new BusinessException("地址不存在");
        }

        // 取消其他默认
        cancelDefault(userId);

        // 设置当前为默认
        address.setIsDefault(1);
        updateById(address);
    }

    /**
     * 取消用户所有默认地址
     */
    private void cancelDefault(Long userId) {
        lambdaUpdate()
                .eq(UserAddress::getUserId, userId)
                .eq(UserAddress::getIsDefault, 1)
                .set(UserAddress::getIsDefault, 0)
                .update();
    }
}
