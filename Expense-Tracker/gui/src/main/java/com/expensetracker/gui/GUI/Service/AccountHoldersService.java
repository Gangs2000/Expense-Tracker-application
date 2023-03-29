package com.expensetracker.gui.GUI.Service;

import com.expensetracker.gui.GUI.FeignClientInterface.ExpenseFeignInterface;
import com.expensetracker.gui.GUI.Pojo.AccountHolders;
import com.expensetracker.gui.GUI.Pojo.Expense;
import com.expensetracker.gui.GUI.Repository.AccountHoldersRepository;
import com.expensetracker.gui.OTP.Service.OTPService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDate;
import java.util.List;

@Service
public class AccountHoldersService {

    @Autowired AccountHolders accountHolders;
    @Autowired OTPService otpService;
    @Autowired ExpenseFeignInterface expenseFeignInterface;
    @Autowired AccountHoldersRepository accountHoldersRepository;
    @Autowired AuthenticationProvider authenticationProvider;
    RedirectView redirectView=new RedirectView();
    ModelAndView modelAndView;
    BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder(12);

    public RedirectView validateLoginRequest(HttpServletRequest httpServletRequest, HttpSession httpSession){
        String emailId=httpServletRequest.getParameter("emailId");
        String otp=httpServletRequest.getParameter("otp");
        UsernamePasswordAuthenticationToken token=new UsernamePasswordAuthenticationToken(emailId,otp);
        if(accountHoldersRepository.existsById(emailId)){
            accountHolders=accountHoldersRepository.findById(emailId).get();
            if(bCryptPasswordEncoder.matches(otp, accountHolders.getTempPin())) {
                token.setDetails(new WebAuthenticationDetails(httpServletRequest));
                Authentication authentication= this.authenticationProvider.authenticate(token);
                SecurityContext securityContext=SecurityContextHolder.getContext();
                securityContext.setAuthentication(authentication);
                //Main line here only we are manually setting session as true
                httpServletRequest.getSession(true).setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
                httpSession.setAttribute("sessionEmailId",httpServletRequest.getParameter("emailId"));
                redirectView.setUrl("/expense-tracker/app/gui/dashboard");
                return redirectView;
            }
        }
        redirectView.setUrl("/expense-tracker/app/gui/login-failure");
        return redirectView;
    }

    public String validateRegistrationRequest(HttpServletRequest httpServletRequest){
        return otpService.verifyOTP(httpServletRequest);
    }

    public ModelAndView setCurrentDayExpenseToEvaluateDashboard(HttpSession httpSession){
        modelAndView=new ModelAndView("dashboard");
        List<Expense> currentDateExpenseList=expenseFeignInterface.getCurrentDayExpense(httpSession.getAttribute("sessionEmailId").toString(), LocalDate.now());
        float currentDay=currentDateExpenseList.stream().map(expense->expense.getExpensedAmount()).reduce((a,b)->(a+b)).orElse((float) 0);
        modelAndView.addObject("expenseList", currentDateExpenseList);
        modelAndView.addObject("currentDay", currentDay);
        return modelAndView;
    }

    public ModelAndView fetchExpenseDetails(HttpSession httpSession){
        modelAndView=new ModelAndView("expenseDetails");
        accountHolders=accountHoldersRepository.findById(httpSession.getAttribute("sessionEmailId").toString()).get();
        httpSession.setAttribute("overall", accountHolders.getExpensedTillNow());
        if(accountHolders.getExpensedTillNow()==0.0) {
            modelAndView.addObject("currentDay", 0.0);
            modelAndView.addObject("currentMonth", 0.0);
            modelAndView.addObject("currentYear", 0.0);
        }
        else {
            float currentDay, currentMonth, currentYear;
            List<Expense> currentDateExpenseList=expenseFeignInterface.getCurrentDayExpense(httpSession.getAttribute("sessionEmailId").toString(), LocalDate.now());
            currentDay=currentDateExpenseList.stream().map(expense->expense.getExpensedAmount()).reduce((a,b)->(a+b)).orElse((float) 0);
            modelAndView.addObject("currentDay", currentDay);
            List<Expense> currentMonthExpenseList=expenseFeignInterface.getCurrentMonthExpense(httpSession.getAttribute("sessionEmailId").toString(), LocalDate.now().getMonthValue(), LocalDate.now().getYear());
            currentMonth=currentMonthExpenseList.stream().map(expense -> expense.getExpensedAmount()).reduce((a,b)->(a+b)).orElse((float) 0);
            modelAndView.addObject("currentMonth", currentMonth);
            List<Expense> currentYearExpenseList=expenseFeignInterface.getCurrentYearExpense(httpSession.getAttribute("sessionEmailId").toString(), LocalDate.now().getYear());
            currentYear=currentYearExpenseList.stream().map(expense -> expense.getExpensedAmount()).reduce((a,b)->(a+b)).orElse((float) 0);
            modelAndView.addObject("currentYear", currentYear);
        }
        return modelAndView;
    }
}
