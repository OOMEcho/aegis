package com.aegis.config.security.customize;

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
 * Security用户VO
 *
 * @author xuesong.lei
 * @since 2025/8/30 22:00
 */
@Data
public class SecurityUser implements UserDetails {

    private final User user;

    public SecurityUser(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 返回用户的权限编码列表（perm_code）
        return user.getPermCodeList() == null ?
                Collections.emptyList() :
                user.getPermCodeList().stream()
                        .filter(permCode -> permCode != null && !permCode.isEmpty())
                        .distinct()
                        .map(SimpleGrantedAuthority::new)
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
    public boolean isEnabled() {
        return CommonConstants.NORMAL_STATUS.equals(user.getStatus());
    }
}
