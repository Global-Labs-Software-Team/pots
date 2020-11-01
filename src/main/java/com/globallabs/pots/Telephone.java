package com.globallabs.pots;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Telephone {
    
    private @Id int id;
    
    private Status status;
    
    private Telephone lastCall;
    
    private Telephone incomingCall;
    
    public Telephone(final int id){
    	this.id = id;
    	this.status = Status.OFF_CALL;
    }

    /**
     * Getter for the Id field
     * @return the identifier of the phone
     */
    public int getId() {
        return id;
    }
    
    /**
     * Getter for the status
     * @return the status of the phone
     */
    public Status getStatus() {
    	return status;
    }
    
    /**
     * Set a new status to the phone
     * @param newStatus
     */
    public void setStatus(final Status newStatus) {
    	this.status = newStatus;
    }
    
    /**
     * Get the phone that you were in a call, also
     * if you are in a call, the phone you are connected with
     * @return the last phone you called
     */
    public Telephone getLastCall() {
    	return lastCall;
    }
    
    /**
     * Set the phone of your last call or you current call
     * @param phone the last phone you called or you are in a call with
     */
    public void setLastCall(final Telephone phone) {
    	this.lastCall = phone;
    }
    
    /**
     * Get the phone that is calling you.
     * @return the phone calling you, null if there is nobody calling you
     */
    public Telephone getIncomingCall() {
    	return incomingCall;
    }
    
    /**
     * Set the phone that is calling you
     * @param phone
     */
    public void setIncomingCall(final Telephone phone) {
    	incomingCall = phone;
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
        if (!(o instanceof Telephone)) {
            return false;
        }
        Telephone telephone = (Telephone)o;
        return Objects.equals(this.id, telephone.id);
    }

    /**
     * String representation of the object
     */
    @Override
    public String toString() {
        return "Telephone{" + "id=" + this.id + "}";
    }
}
