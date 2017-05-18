package org.example.ws.service;

import org.example.ws.util.AsyncResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.example.ws.model.Greeting;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

/**
 * Created by ng88763 on 2/16/2016.
 */
@Service
public class EmailServiceBean implements EmailService{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public Boolean send(Greeting greeting) {
        logger.info("> Send");

        Boolean success = Boolean.FALSE;

        // Simulate Method Execution Time
        long pause = 5000;
        try{
            Thread.sleep(pause);
        } catch (Exception e){
            // do nothing
        }
        logger.info("Processing time is {} seconds.", pause/1000);
        success = Boolean.TRUE;
        logger.info("< Send");
        return success;
    }

    @Async
    @Override
    public void sendAsync(Greeting greeting) {
        logger.info("> Send Async");

        try{
            send(greeting);
        } catch (Exception e){
            logger.warn("Exception caught sending Asynchronous mail.",e);
        }
        logger.info("< send Async");
    }

    @Async
    @Override
    public Future<Boolean> sendAsyncWithResult(Greeting greeting) {
        logger.info("> Send Async with Result");

        AsyncResponse<Boolean> response = new AsyncResponse<Boolean>();

        try{
            Boolean success = send(greeting);
            response.complete(success);
        } catch (Exception e){
            logger.warn("Exception caught sending Asynchronous mail.",e);
            response.completeExceptionally(e);
        }
        logger.info("< send Async with result");
        return response;
    }
}