package com.expensetracker.restapi.Controller;

import com.expensetracker.restapi.Document.Expense;
import com.expensetracker.restapi.Service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/expense-tracker/api")
public class ExpenseController {
    @Autowired ExpenseService expenseService;

    @PostMapping(value = "/add/expense", consumes = "application/json")
    public ResponseEntity<Expense> addNewExpense(@RequestBody Expense expense){
        return new ResponseEntity<>(expenseService.addNewExpense(expense), HttpStatus.CREATED);
    }

    @PostMapping(value = "/add/missing/expense/date/{date}", consumes = "application/json")
    public ResponseEntity<Expense> addMissingExpense(@PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "dd-MM-yyyy") LocalDate date, @RequestBody Expense expense){
        return new ResponseEntity<>(expenseService.addMissingExpense(date, expense), HttpStatus.CREATED);
    }

    @GetMapping("/fetch/expense/id/{expenseId}")
    public ResponseEntity<Expense> fetchExpenseById(@PathVariable("expenseId") String userId){
        return new ResponseEntity<>(expenseService.fetchExpenseById(userId), HttpStatus.ACCEPTED);
    }

    @GetMapping("/fetch/expense/userId/{userId}/date/{localDate}")
    //This api call be used for fetching expense details for specific day or fetching expense for current day
    public ResponseEntity<List<Expense>> fetchExpeseByUserIdAndDate(@PathVariable("userId") String userId, @PathVariable("localDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE ,pattern = "dd-MM-yyyy") LocalDate date){
        return new ResponseEntity<>(expenseService.fetchExpenseByDate(userId, date), HttpStatus.ACCEPTED);
    }

    @GetMapping("/fetch/monthly-expense/userId/{userId}/month/{month}/year/{year}")
    //This api call be used for fetching expense details for specific month and year or fetching expense for current month and year
    public ResponseEntity<List<Expense>> fetchExpenseForTheMonth(@PathVariable("userId") String userId, @PathVariable("month") int month, @PathVariable("year") int year){
        return new ResponseEntity<>(expenseService.fetchExpenseForTheMonth(userId, month, year), HttpStatus.ACCEPTED);
    }

    @GetMapping("/fetch/yearly-expense/userId/{userId}/year/{year}")
    //This api call be used for fetching expense details for specific year or fetching expense for current year
    public ResponseEntity<List<Expense>> fetchExpenseForTheYear(@PathVariable("userId") String userId, @PathVariable("year") int year){
        return new ResponseEntity<>(expenseService.fetchExpenseForTheYear(userId, year), HttpStatus.ACCEPTED);
    }

    @PutMapping("/edit/expense/id/{expenseId}")
    public ResponseEntity<Expense> editSpecificExpense(@PathVariable("expenseId") String expenseId, @RequestBody Expense editExpense){
        return new ResponseEntity<>(expenseService.editExistingExpense(expenseId, editExpense), HttpStatus.OK);
    }

    @GetMapping("/get-expense/userId/{userId}/startDate/{startDate}/endDate/{endDate}")
    //This api call is used to filter records between start and end dates.
    public ResponseEntity<List<Expense>> getExpenseBetween(@PathVariable("userId") String userId, @PathVariable("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE ,pattern = "dd-MM-yyyy") LocalDate startDate, @PathVariable("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "dd-MM-yyyy") LocalDate endDate){
        return new ResponseEntity<>(expenseService.fetchExpenseTrackerBetween(userId, startDate, endDate), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/delete/expense/id/{expenseId}")
    public ResponseEntity<String> deleteSpecificExpense(@PathVariable("expenseId") String expenseId){
        return new ResponseEntity<>(expenseService.deleteExpenseById(expenseId), HttpStatus.OK);
    }
}
