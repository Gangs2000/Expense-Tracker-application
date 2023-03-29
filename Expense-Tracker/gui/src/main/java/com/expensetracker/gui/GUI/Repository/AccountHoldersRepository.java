package com.expensetracker.gui.GUI.Repository;

import com.expensetracker.gui.GUI.Pojo.AccountHolders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountHoldersRepository extends JpaRepository<AccountHolders, String> {

}
