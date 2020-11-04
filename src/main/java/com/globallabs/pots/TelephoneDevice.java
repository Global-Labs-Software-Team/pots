package com.globallabs.pots;

import com.globallabs.abstractions.TelephoneFunctions;
import com.globallabs.operator.Exchange;
import com.globallabs.phoneexceptions.*;

public class TelephoneDevice implements TelephoneFunctions {
	
	private Telephone telephone;
	private Exchange exchange;
	
	public TelephoneDevice(final Telephone phone, final Exchange exchange) throws PhoneExistInNetworkException {
		this.telephone = phone;
		this.exchange = exchange;
		this.exchange.addPhoneToExchange(this.telephone);
	}
	
	public void dial(final int phoneNumber) throws DialingMySelfException, PhoneNotFoundException, BusyPhoneException {
		if (phoneNumber == telephone.getId()) {
			throw new DialingMySelfException("You are calling yourself");
		}
		telephone.setStatus(Status.DIALING);
		try {
			exchange.enrouteCall(telephone.getId(), phoneNumber);
		} catch(Exception e) {
			telephone.setStatus(Status.OFF_CALL);
			throw e;
		}
	}

	public void dialing() throws BusyPhoneException {
		if (telephone.getStatus().equals(Status.DIALING)){
			long start = System.currentTimeMillis();
			long end = start + 10*1000;
			while (System.currentTimeMillis() < end) {
				if (telephone.getStatus().equals(Status.BUSY)) return;
				//System.out.println("Ringing");
			}
			telephone.setStatus(Status.OFF_CALL);
		} else {
			throw new BusyPhoneException("");
		}
		
	}

	public void answer() throws BusyPhoneException, NoIncomingCallsException, NoCommunicationPathException {
		if (telephone.getStatus().equals(Status.RINGING)){
			telephone.setStatus(Status.BUSY);
			exchange.openCallBetween(telephone.getId(), telephone.getIncomingCall().getId());
		} else if (telephone.getStatus().equals(Status.BUSY)) {
			throw new BusyPhoneException("You can't answer while you are in another call");
		} else {
			throw new NoIncomingCallsException("No one is calling you");
		}
	}
	
	public Telephone getPhoneInfo() {
		return this.telephone;
	}
	
	public String toString() {
		return this.telephone.toString();
	}
}
