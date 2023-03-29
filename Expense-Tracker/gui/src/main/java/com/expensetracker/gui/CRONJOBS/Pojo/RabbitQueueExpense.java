package com.expensetracker.gui.CRONJOBS.Pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component
public class RabbitQueueExpense {
    private String _id;
    private String notes;
    private float expensedAmount;
    private String userId;
    private String transactionDate;
    private String recordedTime;
}
