package com.globallabs.telephone;

import com.globallabs.abstractions.TelephoneFunctions;
import com.globallabs.operator.Exchange;
import com.globallabs.phonedata.TelephoneModel;
import com.globallabs.phoneexceptions.BusyPhoneException;
import com.globallabs.phoneexceptions.DialingMySelfException;
import com.globallabs.phoneexceptions.NoCommunicationPathException;
import com.globallabs.phoneexceptions.NoIncomingCallsException;
import com.globallabs.phoneexceptions.PhoneExistInNetworkException;
import com.globallabs.phoneexceptions.PhoneNotFoundException;

public class Telephone implements TelephoneFunctions {
    
    private Status status;
    
    private TelephoneModel phoneInfo;
    private Exchange exchange;
    
    private Telephone lastCall;
    
    private Telephone incomingCall;
    
    public Telephone(TelephoneModel phoneInfo, Exchange exchange) throws PhoneExistInNetworkException{
    	this.phoneInfo = phoneInfo;
    	this.exchange = exchange;
    	this.exchange.addPhoneToExchange(this);
    	this.status = Status.OFF_CALL;
    }
    
    /**
     * Get the number of the phone
     * @return
     */
    public int getId() {
    	return phoneInfo.getId();
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
    
    public void dial(final int phoneNumber) throws DialingMySelfException, PhoneNotFoundException, BusyPhoneException {
		if (phoneNumber == phoneInfo.getId()) {
			throw new DialingMySelfException("You are calling yourself");
		}
		setStatus(Status.DIALING);
		try {
			exchange.enrouteCall(phoneInfo.getId(), phoneNumber);
		} catch(Exception e) {
			setStatus(Status.OFF_CALL);
			throw e;
		}
	}

	public void dialing() throws BusyPhoneException {
		if (getStatus().equals(Status.DIALING)){
			long start = System.currentTimeMillis();
			long end = start + 10*1000;
			while (System.currentTimeMillis() < end) {
				if (getStatus().equals(Status.BUSY)) return;
				//System.out.println("Ringing");
			}
			setStatus(Status.OFF_CALL);
		} else {
			throw new BusyPhoneException("");
		}
		
	}

	public void answer() throws BusyPhoneException, NoIncomingCallsException, NoCommunicationPathException {
		if (getStatus().equals(Status.RINGING)){
			setStatus(Status.BUSY);
			exchange.openCallBetween(getId(), getIncomingCall().getId());
		} else if (getStatus().equals(Status.BUSY)) {
			throw new BusyPhoneException("You can't answer while you are in another call");
		} else {
			throw new NoIncomingCallsException("No one is calling you");
		}
	}
	
	public void hangUp() {
		
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
        return this.phoneInfo.equals(telephone.phoneInfo);
    }

    /**
     * String representation of the object
     */
    @Override
    public String toString() {
        return phoneInfo.toString();
    }
}
