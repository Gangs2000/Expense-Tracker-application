package com.expensetracker.gui.OTP.Controller;

import com.expensetracker.gui.GUI.Pojo.AccountHolders;
import com.expensetracker.gui.GUI.Repository.AccountHoldersRepository;
import com.expensetracker.gui.OTP.Service.OTPService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Random;

@Controller
@CrossOrigin("*")
@RequestMapping("/expense-tracker/app/gui")
public class OTPController {

    @Autowired AccountHoldersRepository accountHoldersRepository;
    @Autowired AccountHolders accountHolders;
    @Autowired OTPService otpService;
    Random random=new Random();
    JSONObject jsonObject=new JSONObject();
    BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder(12);

    @PostMapping(path = "/login-account/sendOTP", consumes = "application/json")
    @ResponseBody
    public String sendOTPForLogin(@RequestBody Map<String, Object> mapper){
        String emailId=String.valueOf(mapper.get("emailId").toString());
        JSONObject jsonObject=new JSONObject();
        if(accountHoldersRepository.existsById(emailId)){
            String generatedPin=String.format("%06d", random.nextInt(99999));
            accountHolders=accountHoldersRepository.findById(emailId).get();
            accountHolders.setTempPin(bCryptPasswordEncoder.encode(generatedPin));
            accountHoldersRepository.saveAndFlush(accountHolders);
            otpService.sendOTPToQueue(emailId, generatedPin);
            jsonObject.put("status", "success");
        }
        else
            jsonObject.put("status", "failure");
        return jsonObject.toJSONString();
    }

    @PostMapping(path = "/register-account/sendOTP", consumes = "application/json")
    @ResponseBody
    public String sendOTPForAccountRegistration(@RequestBody Map<String, Object> mapper){
        String emailId=String.valueOf(mapper.get("emailId").toString());
        String generatedPin=String.format("%06d", random.nextInt(99999));
        if(accountHoldersRepository.existsById(emailId))
            jsonObject.put("status", "failure");
        else {
            otpService.sendOTPToQueue(String.valueOf(mapper.get("emailId").toString()), generatedPin);
            jsonObject.put("status", "success");
        }
        return jsonObject.toJSONString();
    }
}
