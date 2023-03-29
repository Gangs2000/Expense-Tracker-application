package com.expensetracker.gui.GUI.Service;

import com.expensetracker.gui.GUI.FeignClientInterface.ExpenseFeignInterface;
import com.expensetracker.gui.GUI.Pojo.Expense;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class ExpenseService {

    @Autowired Expense expense;
    @Autowired ExpenseFeignInterface expenseFeignInterface;

    RedirectView redirectView=new RedirectView();
    public Expense fetchExpenseById(String expenseId){
        expense=expenseFeignInterface.getExpenseById(expenseId);
        return expense;
    }

    public RedirectView addExpenseRequest(HttpServletRequest httpServletRequest, HttpSession httpSession){
        expense.setNotes(httpServletRequest.getParameter("notes"));
        expense.setExpensedAmount(Float.valueOf(httpServletRequest.getParameter("amount")));
        expense.setUserId(httpSession.getAttribute("sessionEmailId").toString());
        expenseFeignInterface.addExpense(expense);
        redirectView.setUrl("/expense-tracker/app/gui/dashboard");
        return redirectView;
    }

    public RedirectView addMissingExpenseRequest(HttpServletRequest httpServletRequest, HttpSession httpSession){
        LocalDate validateDate=LocalDate.parse(httpServletRequest.getParameter("missedDate"));
        if(!validateDate.isAfter(LocalDate.now())) {
            expense.setNotes(httpServletRequest.getParameter("notes"));
            expense.setExpensedAmount(Float.valueOf(httpServletRequest.getParameter("amount")));
            expense.setUserId(httpSession.getAttribute("sessionEmailId").toString());
            expenseFeignInterface.addMissingExpense(LocalDate.parse(httpServletRequest.getParameter("missedDate")), expense);
        }
        redirectView.setUrl("/expense-tracker/app/gui/dashboard");
        return redirectView;
    }

    public RedirectView editExpenseRequest(HttpServletRequest httpServletRequest, int label){
        expense.setNotes(httpServletRequest.getParameter("notes").toString());
        expense.setExpensedAmount(Float.valueOf(httpServletRequest.getParameter("amount").toString()));
        expenseFeignInterface.editSpecificExpense(httpServletRequest.getParameter("id"), expense);
        switch (label) {
            case 1 : redirectView.setUrl("/expense-tracker/app/gui/dashboard"); break;
            case 2 : redirectView.setUrl("/expense-tracker/app/gui/fetchByDate"); break;
            case 3 : redirectView.setUrl("/expense-tracker/app/gui/fetchBetween"); break;
            case 4 : redirectView.setUrl("/expense-tracker/app/gui/fetchMonthlyExpense"); break;
            case 5 : redirectView.setUrl("/expense-tracker/app/gui/fetchYearlyExpense"); break;
        }
        return redirectView;
    }

    public RedirectView deleteExpenseRequest(HttpServletRequest httpServletRequest, int label){
        expenseFeignInterface.deleteSpecificExpense(httpServletRequest.getParameter("id").toString());
        switch (label) {
            case 1 : redirectView.setUrl("/expense-tracker/app/gui/dashboard"); break;
            case 2 : redirectView.setUrl("/expense-tracker/app/gui/fetchByDate"); break;
            case 3 : redirectView.setUrl("/expense-tracker/app/gui/fetchBetween"); break;
            case 4 : redirectView.setUrl("/expense-tracker/app/gui/fetchMonthlyExpense"); break;
            case 5 : redirectView.setUrl("/expense-tracker/app/gui/fetchYearlyExpense"); break;
        }
        return redirectView;
    }

    public List<Expense> fetchByDateRequest(HttpSession httpSession){
        LocalDate requestedDate=LocalDate.parse(httpSession.getAttribute("date").toString());
        return expenseFeignInterface.getCurrentDayExpense(httpSession.getAttribute("sessionEmailId").toString(), requestedDate);
    }

    public List<Expense> fetchBetweenDateRequest(HttpSession httpSession){
        String userId=httpSession.getAttribute("sessionEmailId").toString();
        LocalDate startDate=LocalDate.parse(httpSession.getAttribute("startDate").toString());
        LocalDate endDate=LocalDate.parse(httpSession.getAttribute("endDate").toString());
        return expenseFeignInterface.getExpenseBetweenDates(userId, startDate, endDate);
    }

    public List<Expense> fetchMonthlyExpenseRequest(HttpSession httpSession){
        String userId=httpSession.getAttribute("sessionEmailId").toString();
        int month=Integer.valueOf(httpSession.getAttribute("month").toString());
        int year=Integer.valueOf(httpSession.getAttribute("yearForMonthlyRequest").toString());
        return expenseFeignInterface.getCurrentMonthExpense(userId, month, year);
    }

    public List<Expense> fetchYearlyExpenseRequest(HttpSession httpSession){
        String userId=httpSession.getAttribute("sessionEmailId").toString();
        int year=Integer.valueOf(httpSession.getAttribute("year").toString());
        return expenseFeignInterface.getCurrentYearExpense(userId, year);
    }

    public List<Integer> fetchAllMonthsAmountForTheGivenYearToDisplayGraph(HttpSession httpSession){
        List<Integer> amounts=new LinkedList<>();
        int year=Integer.valueOf(httpSession.getAttribute("yearForBarGraph").toString());
        String userId=httpSession.getAttribute("sessionEmailId").toString();
        for(int month=1;month<=12;month++){
            List<Expense> expensedAmountInAMonth=expenseFeignInterface.getCurrentMonthExpense(userId, month, year);
            amounts.add(expensedAmountInAMonth.stream().map(expense->(int) expense.getExpensedAmount()).reduce((a,b)->(a+b)).orElse(0));
        }
        return amounts;
    }

    public Map<String, Integer> fetchAllDaysAmountForTheGivenMonthToDisplayGraph(HttpSession httpSession){
        Map<String, Integer> mapAmountToDay=new LinkedHashMap<>();
        String userId=httpSession.getAttribute("sessionEmailId").toString();
        int month=Integer.valueOf(httpSession.getAttribute("monthForMonthBarGraph").toString());
        int year=Integer.valueOf(httpSession.getAttribute("monthForYearBarGraph").toString());
        int lengthOfMonth=YearMonth.of(year, month).lengthOfMonth();
        for(int i=1;i<=lengthOfMonth;i++){
            LocalDate localDate=LocalDate.of(year, month, i);
            int amount=expenseFeignInterface.getCurrentDayExpense(userId, localDate).stream().map(expense -> (int) expense.getExpensedAmount()).reduce((a,b)->(a+b)).orElse(0);
            mapAmountToDay.put(String.valueOf(localDate), amount);
        }
        return mapAmountToDay;
    }
    
    public void clearRecordedSessions(HttpSession httpSession){
        //Method will remove all recorded sessions except user session
        httpSession.removeAttribute("date");
        httpSession.removeAttribute("startDate");
        httpSession.removeAttribute("endDate");
        httpSession.removeAttribute("month");
        httpSession.removeAttribute("yearForMonthlyRequest");
        httpSession.removeAttribute("year");
        httpSession.removeAttribute("yearForBarGraph");
        httpSession.removeAttribute("monthForMonthBarGraph");
        httpSession.removeAttribute("monthForYearBarGraph");
    }
}
