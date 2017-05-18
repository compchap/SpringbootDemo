package org.example.ws.service;

import org.example.ws.model.Greeting;

import java.math.BigInteger;
import java.util.Collection;

/**
 * Created by ng88763 on 2/11/2016.
 */
public interface GreetingService {

    Collection<Greeting> findAll();

    public Greeting findOne(BigInteger id);

    public Greeting create(Greeting greeting);

    public Greeting update(Greeting greeting);

    public void delete(BigInteger id);

    void evictCache();
}
