package com.rainbow.api.controller;

import com.rainbow.api.entity.Permission;
import com.rainbow.api.service.IPermissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/permissions")
public class PermissionController {

    private final IPermissionService permissionService;
    public PermissionController(IPermissionService permissionService) {
        this.permissionService = permissionService;
    }

 @GetMapping
 public ResponseEntity<List<Permission>> getAllPermissions() {
     List<Permission> permissions = permissionService.findAll();
     return ResponseEntity.ok(permissions);
 }

 @GetMapping("/{id}")
    public ResponseEntity<Permission> getPermission(@PathVariable Long id) {
        Optional <Permission> permission = permissionService.findById(id);
        return permission.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

 @PostMapping
    public ResponseEntity<Permission> createPermission(@RequestBody Permission permission) {
        Permission createdPermission = permissionService.save(permission);
        return ResponseEntity.ok(createdPermission);
    }

 @PutMapping
    public ResponseEntity<Permission> updatePermission(@RequestBody Permission permission) {
        Permission updatedPermission = permissionService.update(permission);
        return ResponseEntity.ok(updatedPermission);
    }
 @DeleteMapping
    public ResponseEntity<Void> deletePermission(@RequestParam Long id) {
        permissionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
