package org.example.ws.service;

import org.example.ws.model.Greeting;

import java.util.concurrent.Future;

/**
 * Created by ng88763 on 2/15/2016.
 */
public interface EmailService {

    // Send Email Synchronously
    Boolean send(Greeting greeting);

    void sendAsync(Greeting greeting);

    // Send Email Asynchronously and Return a response
    // Future Interface provides methods while allows exceptions return
    // JDK 8 an implementation of FUTURE interface, CompletableFuture
    Future<Boolean> sendAsyncWithResult(Greeting greeting);
}
