package com.expensetracker.gui.GUI.Pojo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
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
