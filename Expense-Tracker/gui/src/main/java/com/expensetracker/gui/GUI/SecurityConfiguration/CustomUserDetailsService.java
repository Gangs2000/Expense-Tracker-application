package com.expensetracker.gui.GUI.SecurityConfiguration;

import com.expensetracker.gui.GUI.Pojo.AccountHolders;
import com.expensetracker.gui.GUI.Repository.AccountHoldersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired AccountHoldersRepository accountHoldersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AccountHolders> user=accountHoldersRepository.findById(username);
        if(user.isEmpty())
            throw new UsernameNotFoundException("Given email ID is not exist..");
        return new User(user.get().getEmailId(), user.get().getTempPin(), new LinkedList<>());
    }
}
