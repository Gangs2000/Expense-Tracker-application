package com.expensetracker.gui.CRONJOBS.Controller;

import com.expensetracker.gui.CRONJOBS.Service.CronJobsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CronJobsController {

    @Autowired CronJobsService cronJobsService;

    public void triggerDuringEveryDayBeginning() throws InterruptedException {
        cronJobsService.everyDayBeginning();
    }

    public void triggerDuringEveryMonthBeginning() throws InterruptedException {
        cronJobsService.everyMonthBeginning();
    }

    public void triggerDuringEveryYearBeginning() throws InterruptedException {
        cronJobsService.everyYearBeginning();
    }
}
