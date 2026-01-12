package com.aegis.modules.notice.mapper;

import com.aegis.modules.notice.domain.dto.NoticeUserDTO;
import com.aegis.modules.notice.domain.entity.NoticeUser;
import com.aegis.modules.notice.domain.vo.NoticeVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: xuesong.lei
 * @Date: 2025-09-16 21:39:42
 * @Description: 针对表【t_notice_user(通知接收记录表)】的数据库操作Mapper
 * @Entity: com.aegis.modules.notice.domain.entity.NoticeUser
 */
public interface NoticeUserMapper extends BaseMapper<NoticeUser> {

    /**
     * 查询所有用户ID
     *
     * @return 用户ID列表
     */
    List<Long> selectAllUserIds();

    /**
     * 根据角色ID列表查询用户ID列表
     *
     * @param ids 角色ID列表
     * @return 用户ID列表
     */
    List<Long> selectUserIdsByRoleIds(List<Long> ids);

    /**
     * 根据部门ID列表查询用户ID列表
     *
     * @param ids 部门ID列表
     * @return 用户ID列表
     */
    List<Long> selectUserIdsByDeptIds(List<Long> ids);

    /**
     * 分页查询通知公告列表
     *
     * @param dto    查询参数
     * @param userId 用户ID
     * @return 通知公告列表
     */
    List<NoticeVO> pageList(@Param("dto") NoticeUserDTO dto, @Param("userId") Long userId);
}




