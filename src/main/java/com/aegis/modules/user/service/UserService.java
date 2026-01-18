package com.aegis.modules.user.service;

import com.aegis.common.domain.vo.PageVO;
import com.aegis.modules.user.domain.dto.UserDTO;
import com.aegis.modules.user.domain.vo.UserVO;

/**
 * @Author: xuesong.lei
 * @Date: 2025/9/4 22:50
 * @Description: 用户业务层
 */
public interface UserService {

    /**
     * 分页列表
     *
     * @param dto 查询参数
     * @return 用户分页列表
     */
    PageVO<UserVO> pageList(UserDTO dto);

    /**
     * 详情
     *
     * @param id 用户ID
     * @return 用户详情
     */
    UserVO detail(Long id);

    /**
     * 修改用户状态
     *
     * @param id 用户ID
     * @return 响应消息
     */
    String updateStatus(Long id);

    /**
     * 删除用户
     *
     * @param id 用户ID
     * @return 响应消息
     */
    String delete(Long id);

    /**
     * 新增用户
     *
     * @param dto 用户DTO
     * @return 响应消息
     */
    String add(UserDTO dto);

    /**
     * 修改用户
     *
     * @param dto 用户DTO
     * @return 响应消息
     */
    String update(UserDTO dto);

    /**
     * 重置密码
     *
     * @param id 用户ID
     * @return 响应消息
     */
    String resetPassword(Long id);

    /**
     * 踢用户下线
     *
     * @param id 用户ID
     * @return 响应消息
     */
    String kick(Long id);
}
