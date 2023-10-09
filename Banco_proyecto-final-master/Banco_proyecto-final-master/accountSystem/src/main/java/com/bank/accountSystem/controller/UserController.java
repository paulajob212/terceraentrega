package com.bank.accountSystem.controller;

import com.bank.accountSystem.model.Role;
import com.bank.accountSystem.model.User;
import com.bank.accountSystem.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
@RequiredArgsConstructor
@Tag(name = "user", description = "the User API")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Get all users", description = "Retrieve all registered users")
    @ApiResponse(responseCode = "200", description = "Users found", content = @Content(schema = @Schema(implementation = User.class)))
    @GetMapping
    @PreAuthorize("hasAnyAuthority('admin:read', 'user:read')")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.userAll();
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Get user by ID", description = "Retrieve a user based on its ID")
    @ApiResponse(responseCode = "200", description = "User found", content = @Content(schema = @Schema(implementation = User.class)))
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('admin:read', 'user:read')")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        User user = userService.userid(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Update user by ID", description = "Update a user based on its ID")
    @ApiResponse(responseCode = "200", description = "User updated", content = @Content(schema = @Schema(implementation = User.class)))
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('admin:update', 'user:update')")
    public ResponseEntity<User> updateUser(@PathVariable Integer id, @RequestBody User updated) {
        User user = userService.userUpdate(id, updated);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Update user role by ID", description = "Update a user's role based on its ID")
    @ApiResponse(responseCode = "200", description = "User role updated", content = @Content(schema = @Schema(implementation = User.class)))
    @PutMapping("/{id}/role")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<User> updateRole(@PathVariable Integer id, @RequestParam Role newRole) {
        User updatedUser = userService.updateRole(id, newRole);
        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete user by ID", description = "Delete a user based on its ID")
    @ApiResponse(responseCode = "200", description = "User deleted")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        boolean deleted = Boolean.parseBoolean(userService.userDelete(id));
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
