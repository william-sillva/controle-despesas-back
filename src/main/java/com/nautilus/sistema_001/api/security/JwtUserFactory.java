package com.nautilus.sistema_001.api.security;

import com.nautilus.sistema_001.api.entities.Users;
import com.nautilus.sistema_001.api.enums.ProfileEnum;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

public class JwtUserFactory {

    private JwtUserFactory() {
    }

    /**
     * Converte e gera um JwtUser com base nos dados de um funcionário.
     * @return JwtUser
     */
    public static JwtUser create(Users users) {
        return new JwtUser(users.getId(), users.getEmail(),
                users.getPassword(),
                mapToGrantedAuthorities(users.getProfile()));
    }

    /**
     * Converte o perfil do usuário para o formato utilizado pelo Spring Security.
     * @return List<GrantedAuthority>
     */
    private static List<GrantedAuthority> mapToGrantedAuthorities(ProfileEnum profileEnum) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(profileEnum.toString()));

        return authorities;
    }
}
