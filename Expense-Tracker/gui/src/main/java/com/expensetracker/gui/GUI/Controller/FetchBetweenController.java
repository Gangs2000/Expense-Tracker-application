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
public class FetchBetweenController {

    @Autowired ExpenseService expenseService;

    @GetMapping("/fetchBetween")
    public ModelAndView renderFetchBetween(HttpSession httpSession){
        ModelAndView modelAndView=new ModelAndView("fetchBetween");
        if(httpSession.getAttribute("startDate")==null && httpSession.getAttribute("endDate")==null)
            modelAndView.addObject("fetchedRecords", 0);
        else {
            modelAndView.addObject("fetchedRecords", 0);
            List<Expense> filteredList=expenseService.fetchBetweenDateRequest(httpSession).stream().sorted(Comparator.comparing(Expense::getTransactionDate)).toList();
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

    @PostMapping(path = "/filter/fetch-between", consumes = "application/x-www-form-urlencoded")
    public RedirectView filterFetchBetweenRequest(HttpServletRequest httpServletRequest, HttpSession httpSession){
        RedirectView redirectView=new RedirectView("/expense-tracker/app/gui/fetchBetween");
        httpSession.setAttribute("startDate", httpServletRequest.getParameter("startDate").toString());
        httpSession.setAttribute("endDate", httpServletRequest.getParameter("endDate").toString());
        return redirectView;
    }

    @PostMapping(path = "/filter/fetch-between/editExpense", consumes = "application/x-www-form-urlencoded")
    public RedirectView editExpenseRequestForFetchBetweenRequest(HttpServletRequest httpServletRequest){
        return expenseService.editExpenseRequest(httpServletRequest, 3);
    }

    @PostMapping(path = "/filter/fetch-between/deleteExpense", consumes = "application/x-www-form-urlencoded")
    public RedirectView deleteExpenseRequestForFetchBetweenRequest(HttpServletRequest httpServletRequest){
        return expenseService.deleteExpenseRequest(httpServletRequest, 3);
    }
}
