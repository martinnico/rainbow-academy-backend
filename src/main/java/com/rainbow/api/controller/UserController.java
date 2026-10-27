package com.rainbow.api.controller;

import com.rainbow.api.dto.UserProfileResponseDTO;
import com.rainbow.api.entity.Role;
import com.rainbow.api.entity.UserSec;
import com.rainbow.api.exception.ResourceNotFoundException;
import com.rainbow.api.repository.IUserRepository;
import com.rainbow.api.service.IRoleService;
import com.rainbow.api.service.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final IUserService userService;
    private final IRoleService roleService;
    private final IUserRepository userRepository;

    public UserController(IUserService userService, IRoleService roleService, IUserRepository userRepository) {
        this.roleService = roleService;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping()
    public ResponseEntity<List<UserSec>> getAllUsers() {
        List<UserSec> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserSec> getUserById(@PathVariable Long id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<UserSec> createUser(@RequestBody UserSec userSec) {
        Set<Role> roleList = new HashSet<Role>();
        Role readRole;

        userSec.setPassword(userService.encryptPassword(userSec.getPassword()));

        for (Role role : userSec.getRolesList()) {
            readRole = roleService.findById(role.getId()).orElse(null);
            if (readRole != null) {
                roleList.add(readRole);
            }
        }
        if (!roleList.isEmpty()) {
            userSec.setRolesList(roleList);
            UserSec createdUser = userService.save(userSec);
            return ResponseEntity.ok(createdUser);
        }
        return null;
    }

    @PutMapping()
    public ResponseEntity<UserSec> updateUser(@RequestBody UserSec userSec) {
        UserSec updatedUser = userService.update(userSec);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping()
    public ResponseEntity<Void> deleteUser(@RequestParam Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/students")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<List<UserProfileResponseDTO>> getStudents() {
        List<UserProfileResponseDTO> students = userRepository.findAll().stream()
                .filter(user -> user.getRolesList().stream().anyMatch(role -> "STUDENT".equalsIgnoreCase(role.getRole())))
                .map(this::toProfile)
                .toList();
        return ResponseEntity.ok(students);
    }

    @PatchMapping("/{id}/graduation")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<UserProfileResponseDTO> updateGraduation(@PathVariable Long id, @RequestParam boolean graduated) {
        UserSec user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));

        user.setGraduated(graduated);
        UserSec updatedUser = userRepository.save(user);
        return ResponseEntity.ok(toProfile(updatedUser));
    }

    private UserProfileResponseDTO toProfile(UserSec user) {
        Set<String> roles = user.getRolesList().stream()
                .map(role -> role.getRole())
                .collect(java.util.stream.Collectors.toSet());

        return new UserProfileResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getFullName(),
                user.isGraduated(),
                roles
        );
    }
}
