package com.aegis.modules.user.controller;

import com.aegis.common.domain.vo.PageVO;
import com.aegis.common.duplicate.PreventDuplicateSubmit;
import com.aegis.common.log.BusinessType;
import com.aegis.common.log.OperationLog;
import com.aegis.common.mask.DataMask;
import com.aegis.common.validator.ValidGroup;
import com.aegis.modules.user.domain.dto.UserDTO;
import com.aegis.modules.user.domain.vo.UserVO;
import com.aegis.modules.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户接口
 *
 * @author xuesong.lei
 * @since 2025-09-04
 */
@RestController
@Tag(name = "用户接口")
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @DataMask
    @Operation(summary = "分页查询用户列表")
    @GetMapping("/pageList")
    public PageVO<UserVO> pageList(UserDTO dto) {
        return userService.pageList(dto);
    }

    @Operation(summary = "用户详情")
    @GetMapping("/detail/{id}")
    public UserVO detail(@PathVariable("id") Long id) {
        return userService.detail(id);
    }

    @Operation(summary = "修改用户状态")
    @PutMapping("/updateStatus/{id}")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "修改用户状态", businessType = BusinessType.UPDATE)
    public String updateStatus(@PathVariable("id") Long id) {
        return userService.updateStatus(id);
    }

    @Operation(summary = "删除用户")
    @DeleteMapping("/delete/{id}")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "删除用户", businessType = BusinessType.DELETE)
    public String delete(@PathVariable("id") Long id) {
        return userService.delete(id);
    }

    @Operation(summary = "新增用户")
    @PostMapping("/add")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "新增用户", businessType = BusinessType.INSERT)
    public String add(@Validated(ValidGroup.Create.class) @RequestBody UserDTO dto) {
        return userService.add(dto);
    }

    @Operation(summary = "修改用户")
    @PutMapping("/update")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "修改用户", businessType = BusinessType.UPDATE)
    public String update(@Validated(ValidGroup.Update.class) @RequestBody UserDTO dto) {
        return userService.update(dto);
    }

    @Operation(summary = "重置用户密码")
    @PutMapping("/resetPassword/{id}")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "重置用户密码", businessType = BusinessType.UPDATE)
    public String resetPassword(@PathVariable("id") Long id) {
        return userService.resetPassword(id);
    }

    @Operation(summary = "踢用户下线")
    @PutMapping("/kick/{id}")
    @PreventDuplicateSubmit
    @OperationLog(moduleTitle = "踢用户下线", businessType = BusinessType.UPDATE)
    public String kick(@PathVariable("id") Long id) {
        return userService.kick(id);
    }
}
