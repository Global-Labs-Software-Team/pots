package com.globallabs.phonedata;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class TelephoneModel {
    
    private @Id int id;
    
    public TelephoneModel(final int id){
    	this.id = id;
    }

    /**
     * Getter for the Id field
     * @return the identifier of the phone
     */
    public int getId() {
        return id;
    }
    
    /**
     * Compare to telephone to see if they are the same
     * 
     * @param o The object to compare
     * @return true if they are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TelephoneModel)) {
            return false;
        }
        TelephoneModel telephone = (TelephoneModel)o;
        return Objects.equals(this.id, telephone.id);
    }

    /**
     * String representation of the object
     */
    @Override
    public String toString() {
        return "Telephone (" + this.id + ")";
    }
}