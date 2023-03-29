package com.expensetracker.restapi.Repository;

import com.expensetracker.restapi.Document.AccountHolders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountHoldersRepository extends JpaRepository<AccountHolders, String> {

}
