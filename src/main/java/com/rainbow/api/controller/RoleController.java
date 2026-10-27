package com.rainbow.api.controller;

import com.rainbow.api.entity.Permission;
import com.rainbow.api.entity.Role;
import com.rainbow.api.service.IPermissionService;
import com.rainbow.api.service.IRoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final IRoleService roleService;
    private final IPermissionService permissionService;

    public RoleController(IRoleService roleService , IPermissionService permissionService) {
        this.roleService = roleService;
        this.permissionService = permissionService;
    }

    @GetMapping()
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> roles = roleService.findAll();
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable Long id) {
        return roleService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<Role> createRole(@RequestBody Role role) {
        Set<Permission> permList = new HashSet<Permission>();
        Permission readPermission;

        // recuperamos las permissions por su ID
        for (Permission permission : role.getPermissionsList()) {
            readPermission = permissionService.findById(permission.getId()).orElse(null);
            if (readPermission != null) {
                // si encuentra alguna, la guarda en la lista
                permList.add(readPermission);
            }
        }
        role.setPermissionsList(permList);
        Role createdRole = roleService.save(role);
        return ResponseEntity.ok(createdRole);
    }

    @PutMapping()
    public ResponseEntity<Role> updateRole(@RequestBody Role role) {
        Role updatedRole = roleService.update(role);
        return ResponseEntity.ok(updatedRole);
}

    @DeleteMapping()
    public ResponseEntity<Void> deleteRole(@RequestParam Long id) {
        roleService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
