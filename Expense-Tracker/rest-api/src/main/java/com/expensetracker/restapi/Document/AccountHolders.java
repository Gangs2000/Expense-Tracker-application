package com.expensetracker.restapi.Document;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AccountHolders {
    @Id
    private String emailId;
    private String userName;
    private String tempPin;
    private float expensedTillNow;
}
