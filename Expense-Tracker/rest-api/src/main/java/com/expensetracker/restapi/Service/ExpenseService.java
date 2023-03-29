package com.expensetracker.restapi.Service;

import com.expensetracker.restapi.Document.AccountHolders;
import com.expensetracker.restapi.Document.Expense;
import com.expensetracker.restapi.Exception.CustomizedException;
import com.expensetracker.restapi.Interface.ExpenseInterface;
import com.expensetracker.restapi.Repository.AccountHoldersRepository;
import com.expensetracker.restapi.Repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Service
public class ExpenseService implements ExpenseInterface {
    @Autowired Expense expense;
    @Autowired AccountHolders accountHolders;
    @Autowired ExpenseRepository expenseRepository;
    @Autowired AccountHoldersRepository accountHoldersRepository;
    @Override
    public Expense addNewExpense(Expense expense) {
        expense.set_id(UUID.randomUUID().toString());
        expense.setTransactionDate(LocalDate.now());
        expense.setRecordedTime(LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm a")));
        //Postgres DB operation
        accountHolders=accountHoldersRepository.findById(expense.getUserId()).get();
        accountHolders.setExpensedTillNow(accountHolders.getExpensedTillNow()+expense.getExpensedAmount());
        accountHoldersRepository.saveAndFlush(accountHolders);
        //MongoDB operation
        return expenseRepository.save(expense);
    }

    @Override
    public Expense addMissingExpense(LocalDate date, Expense expense) {
        expense.set_id(UUID.randomUUID().toString());
        expense.setTransactionDate(date);
        expense.setRecordedTime(LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm a")));
        //Postgres DB operation
        accountHolders=accountHoldersRepository.findById(expense.getUserId()).get();
        accountHolders.setExpensedTillNow(accountHolders.getExpensedTillNow()+expense.getExpensedAmount());
        accountHoldersRepository.saveAndFlush(accountHolders);
        //MongoDB operation
        return expenseRepository.save(expense);
    }

    @Override
    public Expense fetchExpenseById(String expenseId) {
        if(expenseRepository.existsById(expenseId))
            return expenseRepository.findById(expenseId).get();
        throw new CustomizedException("No such tracker ID available in database");
    }

    @Override
    public List<Expense> fetchExpenseByDate(String userId, LocalDate date) {
        List<Expense> fetchByUserIdAndDate=expenseRepository.findByUserIdAndTransactionDate(userId, date);
        return fetchByUserIdAndDate;
        //throw new CustomizedException("No records have been fetched for the given date. Transactions may not be recorded for the same");
    }

    @Override
    public List<Expense> fetchExpenseForTheMonth(String userId, int month, int year) {
        List<Expense> fetchOnlyMonthExpense=new LinkedList<>();
        List<Expense> fetchDetailsByUserId=expenseRepository.findByUserId(userId);
        fetchDetailsByUserId.forEach(data->{
            if(data.getTransactionDate().getMonthValue()==month && data.getTransactionDate().getYear()==year)
                fetchOnlyMonthExpense.add(data);
        });
        return fetchOnlyMonthExpense;
        //throw new CustomizedException("No transactions have been made for the given month");
    }

    @Override
    public List<Expense> fetchExpenseForTheYear(String userId, int year) {
        List<Expense> fetchOnlyYearExpense=new LinkedList<>();
        List<Expense> fetchDetailsByUserId=expenseRepository.findByUserId(userId);
        fetchDetailsByUserId.forEach(data->{
            if(data.getTransactionDate().getYear()==year)
                fetchOnlyYearExpense.add(data);
        });
        return fetchOnlyYearExpense;
        //throw new CustomizedException("No transactions have been made for the given year");
    }

    @Override
    public Expense editExistingExpense(String expenseId, Expense editExpense) {
        if(expenseRepository.existsById(expenseId)) {
            expense = expenseRepository.findById(expenseId).get();
            float currentAmount=expense.getExpensedAmount();
            expense.setNotes(editExpense.getNotes());
            expense.setExpensedAmount(editExpense.getExpensedAmount());
            float newAmount=editExpense.getExpensedAmount();
            //Postgres DB operation
            accountHolders=accountHoldersRepository.findById(expense.getUserId()).get();
            if(currentAmount>newAmount)
                accountHolders.setExpensedTillNow(accountHolders.getExpensedTillNow()-(currentAmount-newAmount));
            else if(currentAmount<newAmount)
                accountHolders.setExpensedTillNow(accountHolders.getExpensedTillNow()+(newAmount-currentAmount));
            accountHoldersRepository.saveAndFlush(accountHolders);
            //MongoDB operation
            return expenseRepository.save(expense);
        }
        throw new CustomizedException("No such tracker ID has been fetched from database");
    }

    @Override
    public List<Expense> fetchExpenseTrackerBetween(String userId, LocalDate startDate, LocalDate endDate) {
        List<Expense> trackerBetween=expenseRepository.findByUserIdAndTransactionDateBetween(userId, startDate.minusDays(1), endDate.plusDays(1));
        return trackerBetween;
        //throw new CustomizedException("No records have been tracked between the given start and end dates");
    }

    @Override
    public String deleteExpenseById(String expenseId) {
        if(expenseRepository.existsById(expenseId)){
            expense=expenseRepository.findById(expenseId).get();
            //MongoDB operation
            expenseRepository.deleteById(expenseId);
            //Postgres DB operation
            accountHolders=accountHoldersRepository.findById(expense.getUserId()).get();
            accountHolders.setExpensedTillNow(accountHolders.getExpensedTillNow()-expense.getExpensedAmount());
            accountHoldersRepository.saveAndFlush(accountHolders);
            return "Expense ID : "+expenseId+" has been deleted successfully";
        }
        throw new CustomizedException("No such expense ID is available in database");
    }
}
