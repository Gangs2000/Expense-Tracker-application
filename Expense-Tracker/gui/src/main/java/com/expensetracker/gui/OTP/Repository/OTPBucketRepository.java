package com.expensetracker.gui.OTP.Repository;

import com.expensetracker.gui.OTP.Pojo.OTPBucket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OTPBucketRepository extends JpaRepository<OTPBucket,String> {

}
