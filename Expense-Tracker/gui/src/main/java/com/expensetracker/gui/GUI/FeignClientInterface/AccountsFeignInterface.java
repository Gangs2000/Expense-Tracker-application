package com.expensetracker.gui.GUI.FeignClientInterface;

import com.expensetracker.gui.GUI.Pojo.AccountHolders;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "account", url = "localhost:9889/expense-tracker/api")
public interface AccountsFeignInterface {
    @PostMapping("/account/registration")
    boolean fetchRegistrationRequestResult(@RequestBody AccountHolders accountHolders);
}
