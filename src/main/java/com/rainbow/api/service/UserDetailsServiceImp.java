package com.rainbow.api.service;

import com.rainbow.api.dto.AuthLoginRequestDTO;
import com.rainbow.api.dto.AuthRegisterRequestDTO;
import com.rainbow.api.dto.AuthResponseDTO;
import com.rainbow.api.entity.Role;
import com.rainbow.api.entity.UserSec;
import com.rainbow.api.exception.ResourceNotFoundException;
import com.rainbow.api.repository.IRoleRepository;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    public UserDetailsServiceImp(IUserRepository userRepository, IRoleRepository roleRepository, JwtUtils jwtUtils, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
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
        String username = authLoginRequest.username();
        String password = authLoginRequest.password();

        Authentication authentication = this.authenticate(username, password);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = jwtUtils.createToken(authentication);
        return new AuthResponseDTO(username, "Login exitoso", accessToken, true);
    }

    public AuthResponseDTO registerUser(@Valid AuthRegisterRequestDTO authRegisterRequest) {
        String username = authRegisterRequest.username();

        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        Role studentRole = roleRepository.findByRole("STUDENT")
                .orElseThrow(() -> new ResourceNotFoundException("Role STUDENT no encontrado"));

        UserSec newUser = UserSec.builder()
                .username(username)
                .fullName(authRegisterRequest.fullName())
                .password(passwordEncoder.encode(authRegisterRequest.password()))
                .graduated(false)
                .enabled(true)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .rolesList(new HashSet<>(Set.of(studentRole)))
                .build();

        userRepository.save(newUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                username,
                newUser.getPassword(),
                getAuthorities(newUser)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = jwtUtils.createToken(authentication);
        return new AuthResponseDTO(username, "Registro exitoso", accessToken, true);
    }

    public Authentication authenticate (String username, String password){
        UserDetails userDetails = this.loadUserByUsername(username);
        if (userDetails == null || !passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Usuario o contraseña incorrectos");
        }

        return new UsernamePasswordAuthenticationToken(username, userDetails.getPassword(), userDetails.getAuthorities());
    }

    private List<SimpleGrantedAuthority> getAuthorities(UserSec userSec) {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        userSec.getRolesList().forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRole()))));
        userSec.getRolesList().stream()
                .flatMap(role -> role.getPermissionsList().stream())
                .forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission.getPermissionName())));
        return authorities;
    }
}