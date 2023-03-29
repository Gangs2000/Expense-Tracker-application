package com.expensetracker.gui.GUI.Controller;

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

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@CrossOrigin("*")
@RequestMapping("/expense-tracker/app/gui")
public class BarGraphController {

    @Autowired ExpenseService expenseService;

    @GetMapping("/yearlyBarGraph")
    public ModelAndView renderYearlyBarGraphPage(HttpSession httpSession){
        ModelAndView modelAndView=new ModelAndView("yearlyBarGraph");
        if(httpSession.getAttribute("yearForBarGraph")==null)
            modelAndView.addObject("fetchedRecords", 0);
        else {
            List<Integer> amounts=expenseService.fetchAllMonthsAmountForTheGivenYearToDisplayGraph(httpSession);
            modelAndView.addObject("fetchedRecords", amounts.size());
            modelAndView.addObject("label","Overall amount expensed in "+httpSession.getAttribute("yearForBarGraph").toString());
            modelAndView.addObject("amountList", amounts);
        }
        return modelAndView;
    }

    @GetMapping("/monthlyBarGraph")
    public ModelAndView renderMonthlyBarGraphPage(HttpSession httpSession){
        ModelAndView modelAndView=new ModelAndView("monthlyBarGraph");
        if(httpSession.getAttribute("monthForMonthBarGraph")==null && httpSession.getAttribute("monthForYearBarGraph")==null)
            modelAndView.addObject("fetchedRecords", 0);
        else {
            Map<String, Integer> mapAmountToDay=expenseService.fetchAllDaysAmountForTheGivenMonthToDisplayGraph(httpSession);
            List<String> days=mapAmountToDay.keySet().stream().collect(Collectors.toList());
            List<Integer> amounts=mapAmountToDay.values().stream().collect(Collectors.toList());
            modelAndView.addObject("fetchedRecords", amounts.size());
            modelAndView.addObject("label","Overall amount expensed in the month of "+httpSession.getAttribute("monthForMonthBarGraph").toString()+" in "+httpSession.getAttribute("monthForYearBarGraph").toString());
            modelAndView.addObject("amountList", amounts);
            modelAndView.addObject("daysList", days);
        }
        return modelAndView;
    }

    @PostMapping(path = "/show/yearly-barGraph", consumes = "application/x-www-form-urlencoded")
    public RedirectView filterGraphByYear(HttpServletRequest httpServletRequest, HttpSession httpSession){
        RedirectView redirectView=new RedirectView("/expense-tracker/app/gui/yearlyBarGraph");
        httpSession.setAttribute("yearForBarGraph", httpServletRequest.getParameter("yearForBarGraph").toString());
        return redirectView;
    }

    @PostMapping(path = "/show/monthly-barGraph", consumes = "application/x-www-form-urlencoded")
    public RedirectView filterGraphByMonth(HttpServletRequest httpServletRequest, HttpSession httpSession){
        RedirectView redirectView=new RedirectView("/expense-tracker/app/gui/monthlyBarGraph");
        httpSession.setAttribute("monthForMonthBarGraph", httpServletRequest.getParameter("month").toString());
        httpSession.setAttribute("monthForYearBarGraph", httpServletRequest.getParameter("year").toString());
        return redirectView;
    }
}
