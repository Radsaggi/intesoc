package intesoc;

import java.io.*;
import java.net.Socket;
import java.lang.reflect.Method;
import java.util.concurrent.Semaphore;

public final class CommunicationPlug {
	private ExceptionHandler conn;
	private Socket soc;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private Couple couple;
	
	private Semaphore transferUsePermit;
	private Semaphore transferChangePermit;
	private AbstractTransfer transfer;
	
	private volatile boolean running;
	private boolean writeFirst;
	
	CommunicationPlug(ExceptionHandler conn,Socket soc,Couple couple,boolean writeFirst) throws IOException {
		this.conn=conn;
		this.soc=soc;
		oos = new ObjectOutputStream (soc.getOutputStream());
		ois = new ObjectInputStream (soc.getInputStream());
		this.couple=couple;
		couple.getSender().setCommunicationPlug(this);
		
		transferChangePermit=new Semaphore(1);
		transferUsePermit=new Semaphore(1);
		transfer=null;
		
		running=true;
		this.writeFirst=writeFirst;
		final CommunicationPlug cp=this;
		Thread t=new Thread (new Runnable(){
			public void run(){
				cp.run();
			}
		});
		t.setPriority(Thread.MAX_PRIORITY);
		t.start();
	}
	
	private void run(){
		
		while (running){
			if (writeFirst){
				write();
				if (!running)			break;
				read();
			} else {
				read();
				if (!running)			break;
				write();
			}
		}
	}
	
	private void read(){
		//Reading from InputStream
		try {
			final Object o=ois.readObject();
			final CommunicationPlug cp=this;			
			new Thread (new Runnable(){
				public void run(){
					cp.processObject(o);
				}
			}).start();
		} catch (Exception e){
			conn.exceptionEncountered(e,this);
				try {
				close();
			} catch (IOException ioe){ }
		}
	}
	
	private void write(){
		//Writing to OutputStream
			try {				
				try {
					transferUsePermit.acquire();
				} catch (InterruptedException e){ } //never thrown
					if (transfer==null)
						transfer=new NullTransfer();
					//To ensure memory space and that the objects are properly serialised
					oos.reset();			
				 	oos.writeObject(transfer);
			} catch (IOException e){
				conn.exceptionEncountered(e,this);
				try {
					close();
				} catch (IOException ioe){ }
			} finally {
				//Reset transfer to NullTransfer
				transfer=null;
				transferUsePermit.release();
					
				
				//allow transfer to get Changed now if it was changed
				if (transferChangePermit.availablePermits()==0)
					transferChangePermit.release();
			}
	}
	
	private void processObject(Object o){
		try {
			MethodTransfer mt=(MethodTransfer)o;
			//System.out.println ("method transfer");
			MethodDetails md=mt.getMethodDetails();
			
			Class<?> c=couple.getReceiver().getClass();
			try {
				Method m=c.getDeclaredMethod(md.getName(),md.getArgumentTypes());
				Object ob=m.invoke(couple.getReceiver(),md.getArguments());
				
				changeTransferTo(new ReturnTransfer(mt,ob));
			} catch(Exception e){
				changeTransferTo(new ExceptionTransfer(mt.getID(),e));
			}
			return;
		} catch (ClassCastException e){	}
		try {
			try {
				ReturnTransfer rt=(ReturnTransfer)o;
				//System.out.println ("Return transfer");
				Sender.Returner<Object> retu=couple.getSender().getReturner(rt.getID());
				retu.getCountDownLatch().countDown();
				retu.getExchanger().exchange(rt.getReturnedValue());
				rt.getID().destroy();
			} catch(InterruptedException e){
				conn.exceptionEncountered(e,this);
			}
			return;
		} catch (ClassCastException e){ }
		try {
			ExceptionTransfer et=(ExceptionTransfer) o;
			conn.exceptionEncountered(et.getException(),this);
			et.getID().destroy();
			return;
		} catch (ClassCastException e){ }
		if (!(o instanceof NullTransfer))
			conn.exceptionEncountered(new NullPointerException ("Unresolved object received - "+o),this);
	}
	
	public void changeTransferTo(AbstractTransfer at){
		if (at instanceof NullTransfer)
			return;
		synchronized( this ) {
			try {
				//check if transferChangePermit is available
				transferChangePermit.acquire();		
				transferUsePermit.acquire();
				
				transfer=at;
			} catch (InterruptedException e){	
			} finally {
				transferUsePermit.release();
			}
		}		
	}
	
	public void close()throws IOException{
		running = false;
		try {
			Thread.sleep(10);
		} catch (Exception e){ }
		oos.close();
		ois.close();
		soc.close();
		
		if (conn instanceof ServerConnector)
			((ServerConnector)conn).closePlug(this);
	}
	
	public boolean isRunning(){
		return running;
	}
	
	public String getDetails(){
		return "InetAddress"+soc.getInetAddress().toString();
	}
	
	public Object getIdentifier(){
		return couple.getIdentifier();
	}
	
	public void finalize(){
		try {
			close();
		} catch (Exception e){ }
	}
}