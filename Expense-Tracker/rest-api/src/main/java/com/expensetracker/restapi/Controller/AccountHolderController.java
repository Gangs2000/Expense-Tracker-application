package com.expensetracker.restapi.Controller;

import com.expensetracker.restapi.Document.AccountHolders;
import com.expensetracker.restapi.Service.AccountHoldersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/expense-tracker/api")
public class AccountHolderController {

    @Autowired AccountHoldersService accountHoldersService;

    @PostMapping("/account/registration")
    public boolean registrationRequest(@RequestBody AccountHolders accountHolders){
        return accountHoldersService.accountRegistrationRequest(accountHolders);
    }
}
