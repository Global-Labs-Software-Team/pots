package com.globallabs.pots;

import org.springframework.data.jpa.repository.JpaRepository;

import com.globallabs.models.TelephoneModel;

/**
 * Interface that have the necessary functions to make
 * database operations
 */
interface TelephoneRepository extends JpaRepository<TelephoneModel, Integer>{
    
}
