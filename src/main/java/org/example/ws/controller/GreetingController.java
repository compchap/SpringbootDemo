package org.example.ws.controller;

import org.example.ws.model.Greeting;
import org.example.ws.service.EmailService;
import org.example.ws.service.GreetingService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;

import java.math.BigInteger;
import java.util.Collection;
import java.util.concurrent.Future;

/**
 * Created by ng88763 on 2/9/2016.
 */
@RestController
@RequestMapping("api")
public class GreetingController {

    @Autowired
    private GreetingService greetingService;

    @Autowired
    private EmailService emailService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //to inject instance of Greeting Service in Controller class
    //Use interface type for Dependency injection rather than implementation Class
    //this follows programming by contract models and ensures that only public methods are exposed

    @RequestMapping(
            value="greetings",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<Greeting>> getGreetings() {

//        Collection<Greeting> greetings =  greetingMap.values();
        Collection<Greeting> greetings =  greetingService.findAll();
        return new ResponseEntity<Collection<Greeting>>(greetings,HttpStatus.OK);
    }

    @RequestMapping(
            value = "/greeting/{id}/data" ,
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Greeting> getGreeting(@PathVariable("id") BigInteger id)throws Exception{

        logger.info("inside getGreeting() ");
//        Greeting greeting = greetingMap.get(id);
        Greeting greeting = greetingService.findOne(id);
        if(greeting == null){
            return new ResponseEntity<Greeting>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Greeting>(greeting, HttpStatus.OK);
    }

    @RequestMapping(
            value = "/greeting",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Greeting> createGreeting(@RequestBody Greeting greeting){
//        Greeting savedGreeting = save(greeting);
        Greeting savedGreeting = greetingService.create(greeting);
        return new ResponseEntity<Greeting>(savedGreeting, HttpStatus.CREATED);

    }

    @RequestMapping(
            value = "/greeting/{id}",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Greeting> updateGreeting(@RequestBody Greeting greeting) {
//        Greeting updatedGreeting = save(greeting);
        Greeting updatedGreeting = greetingService.update(greeting);
        if(updatedGreeting == null){
            return new ResponseEntity<Greeting>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
         return new ResponseEntity<Greeting>(updatedGreeting,HttpStatus.OK);
    }

    @RequestMapping(
            value = "/greeting/{id}",
            method = RequestMethod.DELETE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Greeting> deleteGreeting(@PathVariable("id") BigInteger id,
                                                   @RequestBody Greeting greeting){
//        boolean deleted = delete(id);
        greetingService.delete(id);
     /*
        if(!deleted) {
            return new ResponseEntity<Greeting>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
     */
        return new ResponseEntity<Greeting>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(
            value = "/greetings/{id}/send",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Greeting> sendGreeting(@PathVariable("id") BigInteger id,
                                                 @RequestParam(value ="wait",defaultValue = "false") boolean waitForAsyncResult){

        logger.info("> Send Greeting");
        Greeting greeting = null;
        try{
            greeting = greetingService.findOne(id);
            if(greeting == null) {
                logger.info("< Send Greeting");
                return new ResponseEntity<Greeting>(HttpStatus.NOT_FOUND);
            }

            if(waitForAsyncResult){
                Future<Boolean> asyncResponse = emailService.sendAsyncWithResult(greeting);
                Boolean emailSent = asyncResponse.get();
                logger.info("- greeting email sent? {}",emailSent);
            }else {
                emailService.sendAsync(greeting);
            }
        }catch (Exception e) {
            logger.error("-- An error while sending the greeting.",e);
            return new ResponseEntity<Greeting>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        logger.info("< Send Greeting");
        return new ResponseEntity<Greeting>(greeting, HttpStatus.OK);
    }
}
