package com.expensetracker.gui.GUI.Controller;

import com.expensetracker.gui.GUI.Pojo.Expense;
import com.expensetracker.gui.GUI.Service.AccountHoldersService;
import com.expensetracker.gui.GUI.Service.ExpenseService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@CrossOrigin("*")
@RequestMapping("/expense-tracker/app/gui")
public class RouteController {

    @Autowired AccountHoldersService accountHoldersService;
    @Autowired ExpenseService expenseService;
    @GetMapping("/")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/menuBar")
    public String renderMenuBar(){
        return "menuBar";
    }

    @GetMapping("/dashboard")
    public ModelAndView dashBoard(HttpSession httpSession){
        expenseService.clearRecordedSessions(httpSession);
        return accountHoldersService.setCurrentDayExpenseToEvaluateDashboard(httpSession);
    }

    @GetMapping("/expenseDetails")
    public ModelAndView renderExpenseDetailsPage(HttpSession httpSession){
        return accountHoldersService.fetchExpenseDetails(httpSession);
    }

    @GetMapping("/fetch/expenseId/{expenseId}")
    @ResponseBody
    public Expense fetchId(@PathVariable("expenseId") String expenseId){
        return expenseService.fetchExpenseById(expenseId);
    }

    @PostMapping(path = "/addExpense", consumes = "application/x-www-form-urlencoded")
    public RedirectView addExpense(HttpServletRequest httpServletRequest, HttpSession httpSession){
        return expenseService.addExpenseRequest(httpServletRequest, httpSession);
    }

    @PostMapping(path = "/addMissingExpense", consumes = "application/x-www-form-urlencoded")
    public RedirectView addMissingExpense(HttpServletRequest httpServletRequest, HttpSession httpSession){
        return expenseService.addMissingExpenseRequest(httpServletRequest, httpSession);
    }

    @PostMapping(path = "/editExpense", consumes = "application/x-www-form-urlencoded")
    public RedirectView editExpense(HttpServletRequest httpServletRequest){
        return expenseService.editExpenseRequest(httpServletRequest, 1);
    }

    @PostMapping(path = "/deleteExpense", consumes = "application/x-www-form-urlencoded")
    public RedirectView deleteExpense(HttpServletRequest httpServletRequest){
        return expenseService.deleteExpenseRequest(httpServletRequest, 1);
    }

    @PostMapping(path = "/login-request", consumes = "application/x-www-form-urlencoded")
    public RedirectView loginRequest(HttpServletRequest httpServletRequest, HttpSession httpSession){
        return accountHoldersService.validateLoginRequest(httpServletRequest, httpSession);
    }

    @PostMapping(path = "/registration-request", consumes = "application/x-www-form-urlencoded")
    public String accountRegistration(HttpServletRequest httpServletRequest){
        return accountHoldersService.validateRegistrationRequest(httpServletRequest);
    }

    @GetMapping("/login-failure")
    public String loginFailure(){
        return "loginError";
    }

    @GetMapping("/logout-success")
    public String logoutPage(HttpSession httpSession){
        return "logout";
    }
}
