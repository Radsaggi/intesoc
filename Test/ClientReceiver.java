package Test;

import intesoc.*;

public class ClientReceiver implements Receiver {
	public void sendString(String args){
		System.out.println ("Server : "+args+"\n");
	}
}