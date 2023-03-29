package com.expensetracker.restapi.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Component
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Expense {
    @Id
    private String _id;
    private String notes;
    private float expensedAmount;
    private String userId;
    private @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate transactionDate;
    private String recordedTime;
}
