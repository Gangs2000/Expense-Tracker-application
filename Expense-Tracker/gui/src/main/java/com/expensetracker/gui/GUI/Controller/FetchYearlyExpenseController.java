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
public class FetchYearlyExpenseController {
    @Autowired
    ExpenseService expenseService;

    @GetMapping("/fetchYearlyExpense")
    public ModelAndView renderFetchYearlyPage(HttpSession httpSession){
        ModelAndView modelAndView=new ModelAndView("fetchYearlyExpense");
        if(httpSession.getAttribute("year")==null)
            modelAndView.addObject("fetchedRecords", 0);
        else {
            List<Expense> filteredList=expenseService.fetchYearlyExpenseRequest(httpSession).stream().sorted(Comparator.comparing(Expense::getTransactionDate)).toList();
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

    @PostMapping(path = "/filter/fetch-yearly-expense", consumes = "application/x-www-form-urlencoded")
    public RedirectView filterFetchYearlyRequest(HttpServletRequest httpServletRequest, HttpSession httpSession){
        RedirectView redirectView=new RedirectView("/expense-tracker/app/gui/fetchYearlyExpense");
        httpSession.setAttribute("year", httpServletRequest.getParameter("year").toString());
        return redirectView;
    }

    @PostMapping(path = "/filter/fetch-yearly-expense/editExpense", consumes = "application/x-www-form-urlencoded")
    public RedirectView editExpenseRequestForFetchYearlyRequest(HttpServletRequest httpServletRequest){
        return expenseService.editExpenseRequest(httpServletRequest, 5);
    }

    @PostMapping(path = "/filter/fetch-yearly-expense/deleteExpense", consumes = "application/x-www-form-urlencoded")
    public RedirectView deleteExpenseRequestForFetchYearlyRequest(HttpServletRequest httpServletRequest){
        return expenseService.deleteExpenseRequest(httpServletRequest, 5);
    }
}
