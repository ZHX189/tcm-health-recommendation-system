package com.tcm.service.user;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tcm.model.dto.*;
import com.tcm.model.entity.SysUser;
import com.tcm.model.query.UserQuery;
import com.tcm.model.vo.LoginVO;
import com.tcm.model.vo.UserVO;

/**
 * 用户服务接口
 *
 * @author Ti
 * @since 2026-02-03
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 用户注册
     */
    void register(RegisterDTO dto);

    /**
     * 用户登录
     */
    LoginVO login(LoginDTO dto, Integer userType);

    /**
     * 获取当前用户信息
     */
    UserVO getCurrentUser(Long userId);

    /**
     * 更新用户信息
     */
    void updateUser(Long userId, UserUpdateDTO dto);

    /**
     * 更新用户头像
     */
    String updateAvatar(Long userId, String avatarUrl);

    /**
     * 修改密码
     */
    void updatePassword(Long userId, PasswordUpdateDTO dto);

    /**
     * 分页查询用户
     */
    Page<UserVO> pageUsers(UserQuery query);

    /**
     * 禁用/启用用户
     */
    void updateStatus(Long userId, Integer status);

    /**
     * 创建员工
     */
    void createStaff(StaffCreateDTO dto);

    /**
     * 管理员注册
     */
    void registerAdmin(RegisterDTO dto);
}
