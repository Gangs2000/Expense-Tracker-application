package com.expensetracker.gui.GUI.FeignClientInterface;

import com.expensetracker.gui.GUI.Pojo.Expense;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@FeignClient(value = "expense", url = "localhost:9889/expense-tracker/api")
public interface ExpenseFeignInterface {
    @GetMapping("/fetch/expense/userId/{userId}/date/{localDate}")
    List<Expense> getCurrentDayExpense(@PathVariable("userId") String userId, @PathVariable("localDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE ,pattern = "dd-MM-yyyy") LocalDate date);
    @GetMapping("/fetch/monthly-expense/userId/{userId}/month/{month}/year/{year}")
    List<Expense> getCurrentMonthExpense(@PathVariable("userId") String userId, @PathVariable("month") int month, @PathVariable("year") int year);
    @GetMapping("/fetch/yearly-expense/userId/{userId}/year/{year}")
    List<Expense> getCurrentYearExpense(@PathVariable("userId") String userId, @PathVariable("year") int year);
    @GetMapping("/get-expense/userId/{userId}/startDate/{startDate}/endDate/{endDate}")
    List<Expense> getExpenseBetweenDates(@PathVariable("userId") String userId, @PathVariable("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE ,pattern = "dd-MM-yyyy") LocalDate startDate, @PathVariable("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "dd-MM-yyyy") LocalDate endDate);
    @GetMapping("/fetch/expense/id/{expenseId}")
    Expense getExpenseById(@PathVariable("expenseId") String expenseId);
    @PostMapping("/add/expense")
    void addExpense(@RequestBody Expense expense);
    @PostMapping("/add/missing/expense/date/{date}")
    void addMissingExpense(@PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "dd-MM-yyyy") LocalDate date, @RequestBody Expense expense);
    @PutMapping("/edit/expense/id/{expenseId}")
    void editSpecificExpense(@PathVariable("expenseId") String expenseId, @RequestBody Expense editExpense);
    @DeleteMapping("/delete/expense/id/{expenseId}")
    void deleteSpecificExpense(@PathVariable("expenseId") String expenseId);
}
