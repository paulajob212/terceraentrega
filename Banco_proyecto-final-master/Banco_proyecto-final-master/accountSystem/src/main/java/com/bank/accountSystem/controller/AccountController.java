package com.bank.accountSystem.controller;

import com.bank.accountSystem.dto.DepositRequest;
import com.bank.accountSystem.dto.TransferRequest;
import com.bank.accountSystem.model.Account;
import com.bank.accountSystem.model.Pocket;
import com.bank.accountSystem.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
@Tag(name = "Account", description = "Endpoints for managing accounts and transactions")
public class AccountController {
    @Autowired
    private AccountService accountService;

    //1.0 OBTENER TODAS LAS CUENTAS
    @Operation(summary = "Get all accounts", description = "Retrieve all accounts")
    @ApiResponse(responseCode = "200", description = "Accounts retrieved successfully")
    @ApiResponse(responseCode = "204", description = "No accounts found")
    @PreAuthorize("hasAnyAuthority('admin:read', 'user:read')")
    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts(){
        List<Account> accounts = accountService.getAllAccounts();
        if (accounts.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(accounts);
    }

    //1.1 OBTENER BOLSILLOS DE CADA CUENTA
    @Operation(summary = "Get pockets by account number", description = "Retrieve pockets associated with an account")
    @ApiResponse(responseCode = "200", description = "Pockets retrieved successfully")
    @GetMapping("/{accountNumber}/pockets")
    @PreAuthorize("hasAnyAuthority('admin:read', 'user:read')")
    public ResponseEntity<List<Pocket>> getPocketsByAccountNumber(@PathVariable String accountNumber) {
        List<Pocket> pockets = accountService.getPocketsByAccountNumber(accountNumber);
        return ResponseEntity.ok(pockets);
    }

    //1.2 APERTURA DE CUENTA
    @Operation(summary = "Open a new account", description = "Open a new bank account")
    @ApiResponse(responseCode = "200", description = "Account created successfully")
    @PreAuthorize("hasAuthority('admin:create')")
    @PostMapping
    public ResponseEntity<Account> saveAccount(@RequestBody Account account){
        Account savedAccount = accountService.saveAccount(account);
        return ResponseEntity.ok(savedAccount);
    }

    //1.3 DEPOSITAR EN UNA CUENTA
    @Operation(summary = "Deposit to an account", description = "Deposit funds into a specified account")
    @ApiResponse(responseCode = "200", description = "Deposit successful")
    @ApiResponse(responseCode = "400", description = "Invalid request or insufficient funds")
    @ApiResponse(responseCode = "404", description = "Account not found")
    @PreAuthorize("hasAnyAuthority('admin:create' ,'user:create')")
    @PostMapping("/{accountNumber}/deposit")
    public ResponseEntity<String> realizarDeposito(@PathVariable("accountNumber") String accountNumber, @RequestBody DepositRequest depositRequest) {
        String resultDeposit = accountService.deposit(accountNumber, depositRequest.getAmount());
        if (resultDeposit.equals("Account doesn´t exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resultDeposit);
        }
        return ResponseEntity.ok(resultDeposit);
    }

    //1.4 TRANSFERENCIA A OTRA CUENTA
    @Operation(summary = "Transfer funds to another account", description = "Transfer funds from one account to another")
    @ApiResponse(responseCode = "200", description = "Transfer successful")
    @ApiResponse(responseCode = "400", description = "Invalid request or insufficient funds")
    @ApiResponse(responseCode = "404", description = "One or both accounts not found")
    @PreAuthorize("hasAnyAuthority('admin:create', 'user:create')")
    @PostMapping("/transfer")
    public ResponseEntity<String> makeTransfer(@RequestBody TransferRequest transferRequest) {
        String resultTransfer = accountService.makeTransfer(transferRequest);
        if (resultTransfer.equals("One or both accounts do not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resultTransfer);
        } else if (resultTransfer.equals("Insufficient balance in the source account")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultTransfer);
        }
        return ResponseEntity.ok(resultTransfer);
    }

    //1.5 CONSULTAR CUENTA
    @Operation(summary = "Get account by account number", description = "Retrieve account details by its account number")
    @ApiResponse(responseCode = "200", description = "Account found")
    @ApiResponse(responseCode = "404", description = "Account not found")
    @PreAuthorize("hasAnyAuthority('admin:read', 'user:read')")
    @GetMapping("/{accountNumber}")
    public ResponseEntity<Account> findByAccountNumber(@PathVariable String accountNumber){
        Account account = accountService.findByAccountNumber(accountNumber);
        if (account == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(account);
    }

    //1.6 ELIMINAR CUENTA
    @Operation(summary = "Delete an account", description = "Delete an account by its account number")
    @ApiResponse(responseCode = "200", description = "Account successfully deleted")
    @ApiResponse(responseCode = "404", description = "Account not found")
    @PreAuthorize("hasAuthority('admin:delete')")
    @DeleteMapping("/{accountNumber}")
    public ResponseEntity<String> deleteAccount(@PathVariable String accountNumber){
        try{
            accountService.deleteAccountByAccNumber(accountNumber);
            return ResponseEntity.ok("Account successfully deleted");
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
