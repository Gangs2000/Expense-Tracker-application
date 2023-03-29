package com.expensetracker.gui.CRONJOBS.Service;

import com.expensetracker.gui.CRONJOBS.Pojo.RabbitQueueExpense;
import com.expensetracker.gui.GUI.FeignClientInterface.ExpenseFeignInterface;
import com.expensetracker.gui.GUI.Pojo.AccountHolders;
import com.expensetracker.gui.GUI.Pojo.Expense;
import com.expensetracker.gui.GUI.Repository.AccountHoldersRepository;
import com.expensetracker.gui.OTP.RabbitMq.RabbitMqConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Service
public class CronJobsService {
    @Autowired AccountHoldersRepository accountHoldersRepository;
    @Autowired ExpenseFeignInterface expenseFeignInterface;
    @Autowired RabbitTemplate rabbitTemplate;

    @Scheduled(cron = "0 5 0 * * *")
    public void everyDayBeginning() throws InterruptedException {
        //Fetching all user details from account holders database to send email of current day expense..
        List<AccountHolders> getAllAccountHolders=accountHoldersRepository.findAll();
        //Fetching today date..
        LocalDate prevDate = LocalDate.now().minusDays(1);
        for(AccountHolders accountHolder : getAllAccountHolders) {
            String userId = accountHolder.getEmailId();
            List<Expense> listOfExpenses = expenseFeignInterface.getCurrentDayExpense(userId, prevDate);
            if (listOfExpenses.size() != 0) {
                List<RabbitQueueExpense> modifiedRabbitQueueExpenseList=this.changeLocalDateToString(listOfExpenses);
                rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE, RabbitMqConfig.ROUTING_KEY2, modifiedRabbitQueueExpenseList);
                Thread.sleep(3000);
            }
        }
    }

    @Scheduled(cron = "0 5 0 1 * *")
    public void everyMonthBeginning() throws InterruptedException {
        //Fetching all user details from account holders database to send email of previous month expense..
        List<AccountHolders> getAllAccountHolders=accountHoldersRepository.findAll();
        LocalDate prevDate=LocalDate.now().minusDays(1);
        //Fetching prevMonth..
        int prevMonth=prevDate.getMonthValue();
        //Fetching currentYear..
        int currentYear=prevDate.getYear();
        for(AccountHolders accountHolder : getAllAccountHolders){
            String userId=accountHolder.getEmailId();
            List<Expense> listOfExpenses = expenseFeignInterface.getCurrentMonthExpense(userId, prevMonth, currentYear);
            if(listOfExpenses.size()!=0){
                List<RabbitQueueExpense> modifiedRabbitQueueExpenseList=this.changeLocalDateToString(listOfExpenses);
                rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE, RabbitMqConfig.ROUTING_KEY3, modifiedRabbitQueueExpenseList);
                Thread.sleep(3000);
            }
        }
    }

    @Scheduled(cron = "0 5 0 1 1 *")
    public void everyYearBeginning() throws InterruptedException {
        //Fetching all user details from account holder database to send email of previous year expense..
        List<AccountHolders> getAllAccountHolders=accountHoldersRepository.findAll();
        LocalDate prevDate=LocalDate.now().minusDays(1);
        //Fetching prevYear..
        int prevYear=prevDate.getYear();
        for(AccountHolders accountHolder : getAllAccountHolders){
            String userId=accountHolder.getEmailId();
            List<Expense> listOfExpenses = expenseFeignInterface.getCurrentYearExpense(userId, prevYear);
            if(listOfExpenses.size()!=0){
                List<RabbitQueueExpense> modifiedRabbitQueueExpenseList=this.changeLocalDateToString(listOfExpenses);
                rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE, RabbitMqConfig.ROUTING_KEY4, modifiedRabbitQueueExpenseList);
                Thread.sleep(3000);
            }
        }
    }

    public List<RabbitQueueExpense> changeLocalDateToString(List<Expense> listOfExpenses){
        List<RabbitQueueExpense> modifiedRabbitQueueExpenseList = new LinkedList<>();
        listOfExpenses.forEach(object->{
            RabbitQueueExpense rabbitQueueExpense=new RabbitQueueExpense();
            rabbitQueueExpense.set_id(object.get_id());
            rabbitQueueExpense.setUserId(object.getUserId());
            rabbitQueueExpense.setNotes(object.getNotes());
            rabbitQueueExpense.setExpensedAmount(object.getExpensedAmount());
            rabbitQueueExpense.setTransactionDate(String.valueOf(object.getTransactionDate()));
            rabbitQueueExpense.setRecordedTime(object.getRecordedTime());
            modifiedRabbitQueueExpenseList.add(rabbitQueueExpense);
        });
        return modifiedRabbitQueueExpenseList;
    }
}
