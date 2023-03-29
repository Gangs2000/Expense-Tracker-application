package com.expensetracker.restapi.Repository;

import com.expensetracker.restapi.Document.Expense;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends MongoRepository<Expense, String> {
    List<Expense> findByUserIdAndTransactionDateBetween(String userId, LocalDate startDate, LocalDate endDate);
    List<Expense> findByUserIdAndTransactionDate(String userId, LocalDate date);
    List<Expense> findByUserId(String userId);
}
