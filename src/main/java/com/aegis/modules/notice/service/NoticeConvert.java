package com.aegis.modules.notice.service;

import com.aegis.modules.notice.domain.dto.NoticeDTO;
import com.aegis.modules.notice.domain.entity.Notice;
import com.aegis.modules.notice.domain.vo.NoticeAdminVO;
import com.aegis.modules.notice.domain.vo.NoticeVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @Author: xuesong.lei
 * @Date: 2025/9/16 22:37
 * @Description: 通知类型转换类
 */
@Mapper(componentModel = "spring")
public interface NoticeConvert {

    @Mapping(target = "targetIds", ignore = true)
    Notice toNotice(NoticeDTO dto);

    NoticeVO toNoticeVO(Notice notice);

    NoticeAdminVO toNoticeAdminVo(Notice notice);
}
