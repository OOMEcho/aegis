package com.aegis.modules.user.service;

import com.aegis.modules.user.domain.dto.UserDTO;
import com.aegis.modules.user.domain.entity.User;
import com.aegis.modules.user.domain.vo.UserVO;
import org.mapstruct.Mapper;

/**
 * 用户信息转换类
 *
 * @author xuesong.lei
 * @since 2025/9/14 11:29
 */
@Mapper(componentModel = "spring")
public interface UserConvert {

    UserVO toUserVo(User user);

    User toUser(UserDTO dto);
}
