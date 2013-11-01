package Test;

import intesoc.*;
import java.io.*;
public class ClientSender extends Sender implements Runnable {
	private static CommunicationPlug cp;
	
	public ClientSender (){
		new Thread(this).start();
	}
	
	public void sendString (String args){
		System.out.println ("Client : "+args+"\n");
		invokeMethod(new MethodDetails(Void.class,"sendString",new Class[]{String.class},args));
	}
	
	public void run(){
		BufferedReader br= new BufferedReader (new InputStreamReader (System.in));
		System.out.println ("Enter message to send");
		String str="";
		while (!str.equals("close")){
			try {
				str=br.readLine();
				sendString(str);
			} catch (Exception e){ }			
		}
		try {
			cp.close();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main (String[] args)throws Exception {
		cp=CommunicationSystem.startClient(new Couple(new ClientSender(),new ClientReceiver(),null),CommunicationSystem.DEFAULT_EXCEPTION_HANDLER,"localhost",59648);
	}
}