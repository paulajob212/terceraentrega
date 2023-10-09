package com.bank.accountSystem.controller;

import com.bank.accountSystem.dto.PocketRequest;
import com.bank.accountSystem.dto.TransferToPocketRequest;
import com.bank.accountSystem.model.Account;
import com.bank.accountSystem.model.Pocket;
import com.bank.accountSystem.repository.iAccountRepository;
import com.bank.accountSystem.service.PocketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/pockets")
@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
@Tag(name = "Pocket", description = "Endpoints for managing user pockets and fund transfers")
public class PocketController {

    @Autowired
    private PocketService pocketService;

    @Operation(summary = "Create a pocket", description = "Create a new pocket")
    @ApiResponse(responseCode = "200", description = "Pocket created successfully")
    @ApiResponse(responseCode = "500", description = "Error creating pocket")
    @PreAuthorize("hasAuthority('admin:create')")
    @PostMapping
    public ResponseEntity<String> createPocket(@RequestBody PocketRequest pocketRequest) {
        try {
            pocketService.createPocket(pocketRequest);
            return ResponseEntity.ok("Pocket created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating pocket");
        }
    }

    @Operation(summary = "Transfer to pocket", description = "Transfer funds to a pocket")
    @ApiResponse(responseCode = "200", description = "Successful transfer to pocket")
    @ApiResponse(responseCode = "500", description = "Error when transferring to pocket")
    @PreAuthorize("hasAuthority('admin:create')")
    @PostMapping("/transfer")
    public ResponseEntity<String> transferToPocket(@RequestBody TransferToPocketRequest transferToPocketRequest) {
        try {
            pocketService.transferToPocket(transferToPocketRequest);
            return ResponseEntity.ok("successful transfer to pocket");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error when transferring to pocket");
        }
    }
}
