package com.tcm.service.user.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tcm.common.constant.CommonConstant;
import com.tcm.common.exception.BusinessException;
import com.tcm.common.result.ResultCode;
import com.tcm.mapper.SysUserMapper;
import com.tcm.model.dto.*;
import com.tcm.model.entity.SysUser;
import com.tcm.model.query.UserQuery;
import com.tcm.model.vo.LoginVO;
import com.tcm.model.vo.UserVO;
import com.tcm.service.user.SysUserService;
import com.tcm.service.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 用户服务实现
 *
 * @author Ti
 * @since 2026-02-03
 */
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(RegisterDTO dto) {
        // 校验密码
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new BusinessException("两次输入的密码不一致");
        }

        // 校验用户名是否存在
        if (existsByUsername(dto.getUsername())) {
            throw new BusinessException(ResultCode.USER_EXISTS);
        }

        // 创建用户
        SysUser user = new SysUser();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setNickname(StrUtil.isNotBlank(dto.getNickname()) ? dto.getNickname() : dto.getUsername());
        user.setPhone(dto.getPhone());
        user.setAvatar(CommonConstant.DEFAULT_AVATAR);
        user.setStatus(CommonConstant.STATUS_ENABLE);
        user.setUserType(CommonConstant.USER_TYPE_USER);
        user.setDeleted(CommonConstant.DELETED_NO);

        save(user);
    }

    @Override
    public LoginVO login(LoginDTO dto, Integer userType) {
        // 查询用户
        SysUser user = getByUsername(dto.getUsername());
        if (user == null) {
            throw new BusinessException(ResultCode.LOGIN_ERROR);
        }

        // 校验用户类型
        if (userType != null && !user.getUserType().equals(userType)) {
            throw new BusinessException(ResultCode.LOGIN_ERROR);
        }

        // 校验密码
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BusinessException(ResultCode.LOGIN_ERROR);
        }

        // 校验状态
        if (CommonConstant.STATUS_DISABLE.equals(user.getStatus())) {
            throw new BusinessException(ResultCode.USER_DISABLED);
        }

        // 更新最后登录时间
        user.setLastLoginTime(LocalDateTime.now());
        updateById(user);

        // 生成Token
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getUserType());

        return LoginVO.builder()
                .token(token)
                .userId(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .avatar(user.getAvatar())
                .userType(user.getUserType())
                .build();
    }

    @Override
    public UserVO getCurrentUser(Long userId) {
        SysUser user = getById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_EXISTS);
        }
        return convertToVO(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(Long userId, UserUpdateDTO dto) {
        SysUser user = getById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_EXISTS);
        }

        if (StrUtil.isNotBlank(dto.getNickname())) {
            user.setNickname(dto.getNickname());
        }
        if (StrUtil.isNotBlank(dto.getPhone())) {
            user.setPhone(dto.getPhone());
        }
        if (StrUtil.isNotBlank(dto.getEmail())) {
            user.setEmail(dto.getEmail());
        }
        if (dto.getGender() != null) {
            user.setGender(dto.getGender());
        }
        if (dto.getBirthday() != null) {
            user.setBirthday(dto.getBirthday());
        }

        updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updateAvatar(Long userId, String avatarUrl) {
        SysUser user = getById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_EXISTS);
        }
        user.setAvatar(avatarUrl);
        updateById(user);
        return avatarUrl;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePassword(Long userId, PasswordUpdateDTO dto) {
        // 校验新密码
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            throw new BusinessException("两次输入的新密码不一致");
        }

        SysUser user = getById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_EXISTS);
        }

        // 校验原密码
        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new BusinessException(ResultCode.OLD_PASSWORD_ERROR);
        }

        // 更新密码
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        updateById(user);
    }

    @Override
    public Page<UserVO> pageUsers(UserQuery query) {
        Page<SysUser> page = new Page<>(query.getPageNum(), query.getPageSize());

        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        
        // 支持 keyword 关键词搜索（用户名/昵称/手机号模糊匹配）
        if (StrUtil.isNotBlank(query.getKeyword())) {
            wrapper.and(w -> w
                    .like(SysUser::getUsername, query.getKeyword())
                    .or()
                    .like(SysUser::getNickname, query.getKeyword())
                    .or()
                    .like(SysUser::getPhone, query.getKeyword())
            );
        }
        
        // 支持单独字段精确搜索
        wrapper.like(StrUtil.isNotBlank(query.getUsername()), SysUser::getUsername, query.getUsername())
                .like(StrUtil.isNotBlank(query.getNickname()), SysUser::getNickname, query.getNickname())
                .like(StrUtil.isNotBlank(query.getPhone()), SysUser::getPhone, query.getPhone())
                .eq(query.getStatus() != null, SysUser::getStatus, query.getStatus())
                .eq(query.getUserType() != null, SysUser::getUserType, query.getUserType())
                .orderByDesc(SysUser::getCreateTime);

        Page<SysUser> result = page(page, wrapper);

        Page<UserVO> voPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        voPage.setRecords(result.getRecords().stream().map(this::convertToVO).toList());
        return voPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(Long userId, Integer status) {
        SysUser user = getById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_EXISTS);
        }
        user.setStatus(status);
        updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createStaff(StaffCreateDTO dto) {
        // 校验用户名是否存在
        if (existsByUsername(dto.getUsername())) {
            throw new BusinessException(ResultCode.USER_EXISTS);
        }

        // 创建员工
        SysUser user = new SysUser();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setNickname(StrUtil.isNotBlank(dto.getNickname()) ? dto.getNickname() : dto.getUsername());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        user.setAvatar(CommonConstant.DEFAULT_AVATAR);
        user.setStatus(CommonConstant.STATUS_ENABLE);
        user.setUserType(CommonConstant.USER_TYPE_STAFF);
        user.setDeleted(CommonConstant.DELETED_NO);

        save(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void registerAdmin(RegisterDTO dto) {
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new BusinessException("两次输入的密码不一致");
        }
        if (existsByUsername(dto.getUsername())) {
            throw new BusinessException(ResultCode.USER_EXISTS);
        }
        SysUser user = new SysUser();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setNickname(StrUtil.isNotBlank(dto.getNickname()) ? dto.getNickname() : dto.getUsername());
        user.setPhone(dto.getPhone());
        user.setAvatar(CommonConstant.DEFAULT_AVATAR);
        user.setStatus(CommonConstant.STATUS_ENABLE);
        user.setUserType(CommonConstant.USER_TYPE_ADMIN);
        user.setDeleted(CommonConstant.DELETED_NO);
        save(user);
    }

    /**
     * 根据用户名查询用户
     */
    private SysUser getByUsername(String username) {
        return getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username));
    }

    /**
     * 判断用户名是否存在
     */
    private boolean existsByUsername(String username) {
        return count(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username)) > 0;
    }

    /**
     * 转换为VO
     */
    private UserVO convertToVO(SysUser user) {
        UserVO vo = new UserVO();
        BeanUtil.copyProperties(user, vo);
        return vo;
    }
}
