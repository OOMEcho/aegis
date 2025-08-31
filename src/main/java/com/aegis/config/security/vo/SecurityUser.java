package com.aegis.config.security.vo;

import com.aegis.common.constant.CommonConstants;
import com.aegis.modules.user.domain.entity.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * @Author: xuesong.lei
 * @Date: 2025/8/30 22:00
 * @Description: Security用户VO
 */
@Data
public class SecurityUser implements UserDetails {

    private final User user;

    public SecurityUser(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoleList() == null ?
                Collections.emptyList() :
                user.getRoleList().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getRoleCode()))
                        .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !CommonConstants.DISABLE_STATUS.equals(user.getStatus());
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return CommonConstants.NORMAL_STATUS.equals(user.getStatus());
    }
}
