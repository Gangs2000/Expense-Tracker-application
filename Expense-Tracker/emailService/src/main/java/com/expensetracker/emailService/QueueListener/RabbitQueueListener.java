package com.expensetracker.emailService.QueueListener;

import com.expensetracker.emailService.Pojo.Expense;
import com.expensetracker.emailService.Pojo.OTPBucket;
import com.expensetracker.emailService.RabbitMq.RabbitMqConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class RabbitQueueListener {
    @Autowired JavaMailSender javaMailSender;
    SimpleMailMessage simpleMailMessage=new SimpleMailMessage();

    @RabbitListener(queues = RabbitMqConfig.QUEUE1)
    public void OTPQueueListener(OTPBucket otpBucket){
        simpleMailMessage.setFrom("evento.bookconfirmation@gmail.com");
        simpleMailMessage.setTo(otpBucket.getEmailId());
        simpleMailMessage.setSubject("Expense Tracker app - OTP to verify your account");
        simpleMailMessage.setText(" Please enter the below mentioned One Time Password in OTP to verify your account.."
                +"\n \n OTP : "+otpBucket.getPin()
                +"\n \n Don't share this OTP with anyone..");
        javaMailSender.send(simpleMailMessage);
        System.out.println("Email has been sent to "+otpBucket.getEmailId());
    }

    @RabbitListener(queues = RabbitMqConfig.QUEUE2)
    public void dailyEmailTriggerTask(List<Expense> listOfExpenses){
        simpleMailMessage.setSubject("Daily Report - Daily expense Report for previous day - "+ LocalDate.now().minusDays(1));
        this.emailTemplate(simpleMailMessage, listOfExpenses);
    }

    @RabbitListener(queues = RabbitMqConfig.QUEUE3)
    public void monthlyEmailTriggerTask(List<Expense> listOfExpenses){
        String monthName=listOfExpenses.stream().findFirst().map(expense -> LocalDate.parse(expense.getTransactionDate())).get().getMonth().name();
        simpleMailMessage.setSubject("Monthly Report - Monthly expense Report for previous month - "+monthName);
        this.emailTemplate(simpleMailMessage, listOfExpenses);
    }

    @RabbitListener(queues = RabbitMqConfig.QUEUE4)
    public void yearlyEmailTriggerTask(List<Expense> listOfExpenses){
        int year=listOfExpenses.stream().findFirst().map(expense -> LocalDate.parse(expense.getTransactionDate())).get().getYear();
        simpleMailMessage.setSubject("Yearly Report - Yearly expense Report for previous year - "+year);
        this.emailTemplate(simpleMailMessage, listOfExpenses);
    }

    public void emailTemplate(SimpleMailMessage simpleMailMessage, List<Expense> listOfExpenses){
        simpleMailMessage.setFrom("evento.bookconfirmation@gmail.com");
        String emailId=listOfExpenses.stream().findFirst().map(expense -> expense.getUserId()).get();
        simpleMailMessage.setTo(emailId);
        String emailPlainText="\n Please find the below list of expenses : ";
        for(Expense expense : listOfExpenses){
            emailPlainText+="\n \n Transaction ID   : "+expense.get_id();
            emailPlainText+="\n Notes            : "+expense.getNotes();
            emailPlainText+="\n Expensed amount  : "+expense.getExpensedAmount();
            emailPlainText+="\n Transaction date : "+expense.getTransactionDate();
            emailPlainText+="\n Transaction time : "+expense.getRecordedTime();
            emailPlainText+="\n \n ------------------------------------------------------------------------------";
        }
        simpleMailMessage.setText(emailPlainText);
        javaMailSender.send(simpleMailMessage);
        System.out.println("Email has been sent to "+emailId);
    }
}
