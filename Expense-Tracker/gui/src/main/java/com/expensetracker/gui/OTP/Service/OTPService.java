package com.expensetracker.gui.OTP.Service;

import com.expensetracker.gui.GUI.FeignClientInterface.AccountsFeignInterface;
import com.expensetracker.gui.GUI.Pojo.AccountHolders;
import com.expensetracker.gui.OTP.Pojo.OTPBucket;
import com.expensetracker.gui.OTP.RabbitMq.RabbitMqConfig;
import com.expensetracker.gui.OTP.Repository.OTPBucketRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OTPService {

    @Autowired OTPBucket otpBucket;
    @Autowired AccountHolders accountHolders;
    @Autowired RabbitTemplate rabbitTemplate;
    @Autowired OTPBucketRepository otpBucketRepository;
    @Autowired AccountsFeignInterface accountsFeignInterface;

    public void sendOTPToQueue(String emailId, String generatedPin){
        if(otpBucketRepository.existsById(emailId)) {
            otpBucket=otpBucketRepository.findById(emailId).get();
            otpBucket.setPin(generatedPin);
        }
        else {
            otpBucket.setEmailId(emailId);
            otpBucket.setPin(generatedPin);
        }
        //Producing object details into Rabbit Queue
        rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE, RabbitMqConfig.ROUTING_KEY1, otpBucket);
        otpBucketRepository.save(otpBucket);
    }

    public String verifyOTP(HttpServletRequest httpServletRequest) {
        String emailId=httpServletRequest.getParameter("emailId");
        String userName=httpServletRequest.getParameter("userName");
        String pin=httpServletRequest.getParameter("otp");
        if(otpBucketRepository.existsById(emailId)){
            otpBucket=otpBucketRepository.findById(emailId).get();
            if(otpBucket.getPin().equals(pin)){
                accountHolders.setEmailId(emailId);
                accountHolders.setUserName(userName);
                //During first time registration PIN field kept as null..
                accountHolders.setTempPin(null);
                accountHolders.setExpensedTillNow(0);
                otpBucketRepository.deleteById(emailId);
                return (accountsFeignInterface.fetchRegistrationRequestResult(accountHolders))?("registrationSuccess"):("registrationError");
            }
            return "registrationError";
        }
        return "registrationError";
    }
}
