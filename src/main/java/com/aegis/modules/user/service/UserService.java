package com.aegis.modules.user.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: xuesong.lei
 * @Date: 2025/9/4 22:50
 * @Description: 用户业务层
 */
public interface UserService {

    String refreshToken(HttpServletRequest request, HttpServletResponse response);
}
