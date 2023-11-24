package com.example.hostelManagementTool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/wallet")
@RestController
public class WalletController {

    @Autowired
    WalletService walletService;

    @PostMapping("/createWallet")
    public Wallet createAccount(@RequestBody Wallet wallet){
        return walletService.createWallet(wallet);
    }
}
