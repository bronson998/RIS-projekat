package com.projekat.ris.security;

import com.projekat.ris.model.Role;
import com.projekat.ris.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class StoreUserDetails implements UserDetails {
    private final Long id;
    private final String username;   // koristimo User.username za Basic Auth kredencijale
    private final String password;
    private final Collection<GrantedAuthority> authorities;
    private final boolean enabled;

    public static StoreUserDetails fromUser(User u) {
        Set<GrantedAuthority> auths = u.getRoles().stream()
                .map(Role::getName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

        boolean enabled = true;
        return new StoreUserDetails(
                u.getId(),
                u.getUsername(),
                u.getPassword(),
                auths,
                enabled
        );
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return enabled; }
}
