package com.expensetracker.gui.GUI.Controller;

import com.expensetracker.gui.GUI.Pojo.Expense;
import com.expensetracker.gui.GUI.Service.ExpenseService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Comparator;
import java.util.List;

@Controller
@CrossOrigin("*")
@RequestMapping("/expense-tracker/app/gui")
public class FetchMonthlyExpenseController {

    @Autowired ExpenseService expenseService;

    @GetMapping("/fetchMonthlyExpense")
    public ModelAndView renderFetchMonthlyPage(HttpSession httpSession){
        ModelAndView modelAndView=new ModelAndView("fetchMonthlyExpense");
        if(httpSession.getAttribute("month")==null && httpSession.getAttribute("yearForMonthlyRequest")==null)
            modelAndView.addObject("fetchedRecords", 0);
        else {
            modelAndView.addObject("fetchedRecords", 0);
            List<Expense> filteredList=expenseService.fetchMonthlyExpenseRequest(httpSession).stream().sorted(Comparator.comparing(Expense::getTransactionDate)).toList();
            if(filteredList.size()==0)
                modelAndView.addObject("fetchedRecords", 0);
            else {
                float totalAmount=filteredList.stream().map(expense -> expense.getExpensedAmount()).reduce((a,b)->(a+b)).get();
                modelAndView.addObject("totalSum",totalAmount);
                modelAndView.addObject("fetchedRecords", filteredList.size());
                modelAndView.addObject("filteredList", filteredList);
            }
        }
        return modelAndView;
    }

    @PostMapping(path = "/filter/fetch-monthly-expense", consumes = "application/x-www-form-urlencoded")
    public RedirectView filterFetchMonthlyRequest(HttpServletRequest httpServletRequest, HttpSession httpSession){
        RedirectView redirectView=new RedirectView("/expense-tracker/app/gui/fetchMonthlyExpense");
        httpSession.setAttribute("month", httpServletRequest.getParameter("month").toString());
        httpSession.setAttribute("yearForMonthlyRequest", httpServletRequest.getParameter("year").toString());
        return redirectView;
    }

    @PostMapping(path = "/filter/fetch-monthly-expense/editExpense", consumes = "application/x-www-form-urlencoded")
    public RedirectView editExpenseRequestForFetchMonthlyRequest(HttpServletRequest httpServletRequest){
        return expenseService.editExpenseRequest(httpServletRequest, 4);
    }

    @PostMapping(path = "/filter/fetch-monthly-expense/deleteExpense", consumes = "application/x-www-form-urlencoded")
    public RedirectView deleteExpenseRequestForFetchMonthlyRequest(HttpServletRequest httpServletRequest){
        return expenseService.deleteExpenseRequest(httpServletRequest, 4);
    }
}
