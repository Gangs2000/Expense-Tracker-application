package com.expensetracker.restapi.Interface;

import com.expensetracker.restapi.Document.Expense;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseInterface {
    Expense addNewExpense(Expense expense);
    Expense addMissingExpense(LocalDate date, Expense expense);
    Expense fetchExpenseById(String expenseId);
    List<Expense> fetchExpenseByDate(String userId, LocalDate date);
    List<Expense> fetchExpenseForTheMonth(String userId, int month, int year);
    List<Expense> fetchExpenseForTheYear(String userId, int year);
    Expense editExistingExpense(String expenseId, Expense editExpense);
    List<Expense> fetchExpenseTrackerBetween(String userId, LocalDate startDate, LocalDate endDate);
    String deleteExpenseById(String expenseId);
}
