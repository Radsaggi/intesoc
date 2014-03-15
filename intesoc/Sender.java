package intesoc;

import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Exchanger;

//Write out the method that you require 
//these method should just call the invoke method that does all the background work
public abstract class Sender {
	private TreeMap<IDManager,Returner<Object>> map;
	private CommunicationPlug plug;
	
	final static class Returner<D>{
		private CountDownLatch latch;
		private Exchanger<D> exchanger;
		
		public Returner(CountDownLatch latch,Exchanger<D> exchanger){
			this.latch=latch;
			this.exchanger=exchanger;
		}
		
		public CountDownLatch getCountDownLatch(){
			return latch;
		}
		
		public Exchanger<D> getExchanger(){
			return exchanger;
		}
	}
	
	protected Sender(){
		map=new TreeMap<IDManager,Returner<Object>>();
	}
	
	final void setCommunicationPlug(CommunicationPlug cp){
		plug=cp;
	}
	
	protected final Object invokeMethod(MethodDetails md){
		IDManager id;
		Returner<Object> ret=new Returner<Object>(new CountDownLatch(1),new Exchanger<Object>());
		map.put(id=IDManager.createNewID(),ret);
		plug.changeTransferTo(new MethodTransfer(id,md));
		try {
			ret.getCountDownLatch().await();
			return ret.getExchanger().exchange(null);
		} catch (Exception e){
			return null;
		}
	}
	
	final Returner<Object> getReturner(IDManager id){
		return map.get(id);
	}
}