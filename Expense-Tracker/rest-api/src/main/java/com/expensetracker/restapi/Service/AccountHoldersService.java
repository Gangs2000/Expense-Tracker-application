package com.expensetracker.restapi.Service;

import com.expensetracker.restapi.Document.AccountHolders;
import com.expensetracker.restapi.Interface.AccountHoldersInterface;
import com.expensetracker.restapi.Repository.AccountHoldersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountHoldersService implements AccountHoldersInterface {

    @Autowired AccountHoldersRepository accountHoldersRepository;

    @Override
    public boolean accountRegistrationRequest(AccountHolders accountHolders) {
        if(!accountHoldersRepository.existsById(accountHolders.getEmailId())) {
            accountHoldersRepository.saveAndFlush(accountHolders);
            return true;
        }
        return false;
    }
}
