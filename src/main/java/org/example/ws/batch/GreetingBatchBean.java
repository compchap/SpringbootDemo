package org.example.ws.batch;

import org.example.ws.model.Greeting;
import org.example.ws.service.GreetingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Created by ng88763 on 2/15/2016.
 */
@Profile("batch")
@Component
public class GreetingBatchBean {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private GreetingService greetingService;

//    @Scheduled(cron = "0,30 * * * * *")
    public void cronJob(){
        logger.info("<cronJob");

//        Add Scheduled Logic here
        Collection<Greeting> greetings = greetingService.findAll();
        logger.info("There are {} greeting in the datastore, ", greetings.size());
        logger.info(">cronjob");
    }

//    @Scheduled(initialDelay = 5000, fixedRate = 15000)
    public void fixedRateJobWithInitialDelay() {
        logger.info("> Fixed Rate JOb with Initial Delay");
        // Add Scheduled lOgic here
        // Simulate Job Processing Time

        long pause = 5000;
        long start = System.currentTimeMillis();
        do {
            if(start+pause < System.currentTimeMillis())
                break;
        } while (true);
        logger.info("Processing time was {} seconds.", pause/1000);
        logger.info("< Fixed Rate JOb with Initial Delay");
    }

//    @Scheduled(initialDelay = 5000, fixedDelay = 15000)
    public void fixedDelayJobWithInitialDelay() {
        logger.info("> Fixed Rate JOb with Initial Delay");
        // Add Scheduled lOgic here
        // Simulate Job Processing Time

        long pause = 5000;
        long start = System.currentTimeMillis();
        do {
            if(start+pause < System.currentTimeMillis())
                break;
        } while (true);
        logger.info("Processing time was {} seconds.", pause/1000);
        logger.info("< Fixed Rate JOb with Initial Delay");
    }

}
