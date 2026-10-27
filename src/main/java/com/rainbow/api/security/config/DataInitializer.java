package com.rainbow.api.security.config;

import com.rainbow.api.entity.Permission;
import com.rainbow.api.entity.Role;
import com.rainbow.api.entity.UserSec;
import com.rainbow.api.repository.IPermissionRepository;
import com.rainbow.api.repository.IRoleRepository;
import com.rainbow.api.repository.IUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Component
@Transactional
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final IRoleRepository roleRepository;
    private final IPermissionRepository permissionRepository;
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(
            IRoleRepository roleRepository,
            IPermissionRepository permissionRepository,
            IUserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        Permission read = getOrCreatePermission("READ");
        Permission create = getOrCreatePermission("CREATE");
        Permission update = getOrCreatePermission("UPDATE");
        Permission delete = getOrCreatePermission("DELETE");

        Role teacherRole = getOrCreateRole("TEACHER", Set.of(read, create, update, delete));
        Role studentRole = getOrCreateRole("STUDENT", Set.of(read));

        getOrCreateUser(
                "profesora@rainbow.com",
                "Admin1234!",
                "Profesora Rainbow",
                teacherRole
        );
        getOrCreateUser(
                "alumno@rainbow.com",
                "Alumno1234!",
                "Alumno Demo",
                studentRole
        );

        log.info("DataInitializer: datos iniciales verificados/creados correctamente.");
    }

    private Permission getOrCreatePermission(String permissionName) {
        return permissionRepository.findByPermissionName(permissionName)
                .orElseGet(() -> permissionRepository.save(new Permission(null, permissionName)));
    }

    private Role getOrCreateRole(String roleName, Set<Permission> permissions) {
        Role role = roleRepository.findByRole(roleName)
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setRole(roleName);
                    return newRole;
                });

        role.setPermissionsList(new HashSet<>(permissions));
        return roleRepository.save(role);
    }

    private UserSec getOrCreateUser(String username, String rawPassword, String fullName, Role role) {
        UserSec user = userRepository.findByUsername(username)
                .orElseGet(UserSec::new);

        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setFullName(fullName);
        user.setGraduated(false);
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setRolesList(new HashSet<>(Set.of(role)));

        return userRepository.save(user);
    }
}
