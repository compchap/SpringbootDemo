package org.example.ws.repository;

import org.example.ws.model.Greeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

/**
 * Created by ng88763 on 2/12/2016.
 */
@Repository
public interface GreetingRepository extends CrudRepository<Greeting, BigInteger> {

}
