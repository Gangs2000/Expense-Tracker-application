package com.expensetracker.gui.GUI.Pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component
public class Expense {
    private String _id;
    private String notes;
    private float expensedAmount;
    private String userId;
    private @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate transactionDate;
    private String recordedTime;
}
