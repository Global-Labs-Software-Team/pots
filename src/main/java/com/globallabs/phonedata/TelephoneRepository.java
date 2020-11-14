package com.globallabs.phonedata;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface that have the necessary functions to make
 * database operations.
 */
public interface TelephoneRepository extends JpaRepository<TelephoneModel, Integer> {
    
}
