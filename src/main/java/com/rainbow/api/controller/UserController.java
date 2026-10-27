package com.rainbow.api.controller;

import com.rainbow.api.entity.Role;
import com.rainbow.api.entity.UserSec;
import com.rainbow.api.service.IRoleService;
import com.rainbow.api.service.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final IUserService userService;
    private final IRoleService roleService;
    public UserController(IUserService userService, IRoleService roleService) {
        this.roleService = roleService;
        this.userService = userService;
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

        //encriptamos la contraseña antes de guardarla
        userSec.setPassword(userService.encryptPassword(userSec.getPassword()));

        // recuperamos los roles por su ID
        for (Role role : userSec.getRolesList()) {
            readRole = roleService.findById(role.getId()).orElse(null);
            if (readRole != null) {
                // si encuentra alguno, lo guarda en la lista
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
}
