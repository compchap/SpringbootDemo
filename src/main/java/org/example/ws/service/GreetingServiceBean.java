package org.example.ws.service;

import org.example.ws.model.Greeting;
import org.example.ws.repository.GreetingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.Collection;

/**
 * Created by ng88763 on 2/11/2016.
 */

@Service
@Transactional(propagation = Propagation.SUPPORTS,
                readOnly = true)
public class GreetingServiceBean implements GreetingService {

    @Autowired
    private GreetingRepository greetingRepository;
/*

    private static BigInteger nextId;
    private static Map<BigInteger, Greeting> greetingMap;

    private static Greeting save(Greeting greeting) {
        if(greetingMap == null) {
            greetingMap = new HashMap<BigInteger, Greeting>();
            nextId = BigInteger.ONE;
        }
        //if Update...
        if(greeting.getId() != null){
            Greeting oldGreeting = greetingMap.get(greeting.getId());
            if(oldGreeting == null) {
                return null;
            }
            greetingMap.remove(greeting.getId());
            greetingMap.put(greeting.getId(), greeting);
            return greeting;
        }
        //if create...
        greeting.setId(nextId);
        nextId = nextId.add(BigInteger.ONE);
        greetingMap.put(greeting.getId(), greeting);
        return greeting;
    }

    private static Boolean remove(BigInteger id){
        Greeting deletedGreeting = greetingMap.remove(id);
        if(deletedGreeting == null) {
            return false;
        }
        return true;
    }

    static {
        Greeting g1 = new Greeting();
        g1.setText("Hellow");
        save(g1);

        Greeting g2 = new Greeting();
        g2.setText("Hola Motu!");
        save(g2);
    }
*/

    @Override
    public Collection<Greeting> findAll() {
//        Collection<Greeting> greetings =  greetingMap.values();
        Collection<Greeting> greetings = (Collection<Greeting>) greetingRepository.findAll();
        return greetings;
    }

    @Override
    @Cacheable(
            value = "greetings",
            key = "#id")
    public Greeting findOne(BigInteger id) {
//        Greeting greeting = greetingMap.get(id);
        Greeting greeting = greetingRepository.findOne(id);
        return greeting;
    }

    @Override
    @Transactional(
            propagation = Propagation.REQUIRED,
            readOnly = false)
    @CachePut(
            value = "greetings",
            key = "#result.id")
    public Greeting create(Greeting greeting) {
//        Greeting savedGreeting = save(greeting);
        if(greeting.getId() != null){
            // cannot create the greeting with specified Id
            return null;
        }
        Greeting savedGreeting = greetingRepository.save(greeting);
        // Illustrates Rollback

        if(savedGreeting.getId() == BigInteger.valueOf(4L)){
            throw new RuntimeException("ROll me back!!");
        }
        return savedGreeting;
    }

    @Override
    @Transactional(
            propagation = Propagation.REQUIRED,
            readOnly = false)
    @CachePut(
            value = "greetings",
            key = "#greeting.id")
    public Greeting update(Greeting greeting) {
//        Greeting updatedGreeting = save(greeting);

        Greeting greetingPersisted = findOne(greeting.getId());
        if(greetingPersisted == null) {
        // cannot update the Greeting that hasn't been persisted in database
            return null;
        }
        Greeting updatedGreeting = greetingRepository.save(greeting);
        return updatedGreeting;
    }

    @Override
    @Transactional(
            propagation = Propagation.REQUIRED,
            readOnly = false)
    @CacheEvict(
            value = "greetings",
            key = "#id")
    public void delete(BigInteger id) {
//        remove(id);
        greetingRepository.delete(id);
    }

    @Override
    @CacheEvict(
            value = "greetings",
            allEntries = true)
    public void evictCache(){

    }
}
