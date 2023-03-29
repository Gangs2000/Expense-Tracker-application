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
public class FetchByDateController {

    @Autowired ExpenseService expenseService;

    @GetMapping("/fetchByDate")
    public ModelAndView renderFetchByDate(HttpSession httpSession){
        ModelAndView modelAndView=new ModelAndView("fetchByDate");
        if(httpSession.getAttribute("date")==null)
            modelAndView.addObject("fetchedRecords", 0);
        else {
            List<Expense> filteredList=expenseService.fetchByDateRequest(httpSession).stream().sorted(Comparator.comparing(Expense::getTransactionDate)).toList();
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

    @PostMapping(path = "/filter/fetch-by-date", consumes = "application/x-www-form-urlencoded")
    public RedirectView filterFetchByDate(HttpServletRequest httpServletRequest, HttpSession httpSession){
        RedirectView redirectView=new RedirectView("/expense-tracker/app/gui/fetchByDate");
        httpSession.setAttribute("date", httpServletRequest.getParameter("date").toString());
        return redirectView;
    }

    @PostMapping(path = "/filter/fetch-by-date/editExpense", consumes = "application/x-www-form-urlencoded")
    public RedirectView editExpenseRequestForFetchByDateRequest(HttpServletRequest httpServletRequest){
        return expenseService.editExpenseRequest(httpServletRequest, 2);
    }

    @PostMapping(path = "/filter/fetch-by-date/deleteExpense", consumes = "application/x-www-form-urlencoded")
    public RedirectView deleteExpenseRequestForFetchByDateRequest(HttpServletRequest httpServletRequest){
        return expenseService.deleteExpenseRequest(httpServletRequest, 2);
    }
}
