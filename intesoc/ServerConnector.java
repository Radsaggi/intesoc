package intesoc;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class ServerConnector extends AbstractBean implements ExceptionHandler{
	private ServerSocket serv;
	private boolean running;
	
	/*//Override this
	boolean verifyPassword (String userName,char [] pass){
		return true;
	}*/
	
	//Override this
	public void exceptionEncountered(Throwable t, CommunicationPlug plug){
		exceptionEncountered(t);
	}
	
	public abstract Couple generateNewCouple(InetAddress ia);
	//public abstract boolean isPasswordRequired();
	public abstract void lastAdded();
	public abstract void exceptionEncountered(Throwable e);
	
	final void setServerSocket(ServerSocket serv){
		this.serv=serv;
	}
	
	final void run(){
		running=true;
		while (running){
			try {
				Socket soc=serv.accept();
				connect(soc);
			} catch (Exception e){
				exceptionEncountered(e);
			}
		}
	}
	
	private final void connect(Socket s)throws Exception {
		Couple couple=generateNewCouple(s.getInetAddress());
		CommunicationPlug cp=new CommunicationPlug(this,s,couple,true);
				
		lastAdded();
	}
	
	final void closePlug (CommunicationPlug plug){
	}
	
	public final void close()throws java.io.IOException{
		running=false;
		try {
			Thread.sleep(10);
		} catch (Exception e){ }
		
		//for (CommunicationPlug cp:) cp.close();
		
		serv.close();   
	}
	
	public final void finalize(){
		try {
			close();
		} catch (Exception e){ }
	}
}