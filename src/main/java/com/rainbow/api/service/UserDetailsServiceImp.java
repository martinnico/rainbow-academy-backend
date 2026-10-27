package com.rainbow.api.service;

import com.rainbow.api.dto.AuthLoginRequestDTO;
import com.rainbow.api.dto.AuthResponseDTO;
import com.rainbow.api.entity.UserSec;
import com.rainbow.api.repository.IUserRepository;
import com.rainbow.api.utils.JwtUtils;
import jakarta.validation.Valid;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

    private final IUserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    public UserDetailsServiceImp(IUserRepository userRepository, JwtUtils jwtUtils, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //Usersec Necesitamos devolverlo en formato UserDetails
        //traer nuestro usuario de la base de datos
        UserSec userSec = userRepository.findUserEntityByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuerio no encontrado : " + username));

        // creamos una lista para los permisos de nuestro usuario
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

        //traer roles y convertirlos en SimpleGrantedAuthority
        userSec.getRolesList()
                .forEach(role -> authorityList.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRole()))));

        //traer permisos y convertirlos en SimpleGrantedAuthority
        userSec.getRolesList().stream()
                .flatMap(role -> role.getPermissionsList().stream())
                .forEach(permission -> authorityList.add(new SimpleGrantedAuthority(permission.getPermissionName())));
        return new User(
                userSec.getUsername(),
                userSec.getPassword(),
                userSec.isEnabled(),
                userSec.isAccountNonExpired(),
                userSec.isCredentialsNonExpired(),
                userSec.isAccountNonLocked(),
                authorityList);
    }

    public AuthResponseDTO loginUser(@Valid AuthLoginRequestDTO authLoginRequest) {
        // recuperar el usuario y contraseña
        String username = authLoginRequest.username();
        String password = authLoginRequest.password();

        Authentication authentication = this.authenticate(username, password);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = jwtUtils.createToken(authentication);
        AuthResponseDTO authResponseDTO = new AuthResponseDTO(username,"Login exitoso", accessToken, true);
        return authResponseDTO;
    }

    public Authentication authenticate (String username, String password){
        UserDetails userDetails = this.loadUserByUsername(username);
        if (userDetails == null || !passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Usuario o contraseña incorrectos");
        }

        return new UsernamePasswordAuthenticationToken(username, userDetails.getPassword(), userDetails.getAuthorities());
    }
}