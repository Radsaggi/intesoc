package intesoc;

import java.net.ServerSocket;
import java.net.Socket;

public final class CommunicationSystem {
	
	public static final ExceptionHandler DEFAULT_EXCEPTION_HANDLER = new ExceptionHandler(){
		public void exceptionEncountered(Throwable t,CommunicationPlug cp){
			System.err.println (t.getClass().getName()+" has occured while communicating through the plug "+cp);
			t.printStackTrace(System.err);
		}
	};
	
	private CommunicationSystem(){
	}
	
	public static void startServer(final ServerConnector conn,int port){
		if (conn==null)	throw new NullPointerException();
		try {
			ServerSocket serv=new ServerSocket(port);
			conn.setServerSocket(serv);
			new Thread(new Runnable(){
				public void run(){
					conn.run();
				}
			},"Server Thread").start();
		} catch (java.io.IOException e){
			conn.exceptionEncountered(e);
		}
	}
	
	public static CommunicationPlug startClient(Couple couple,ExceptionHandler excep,String ip,int port)throws java.net.UnknownHostException,java.io.IOException{
		Socket soc=new Socket (ip,port);
		return new CommunicationPlug(excep,soc,couple,false);
	}
}