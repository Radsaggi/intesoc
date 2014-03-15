package intesoc;

public class Couple {
	protected Sender send;
	protected Receiver rec;
	protected Object identifier;
	
	public Couple(Sender send, Receiver rec,Object identifier){
		this.send=send;
		this.rec=rec;
		this.identifier=identifier;
	}
	
	public Sender getSender(){
		return send;
	}
	
	public Receiver getReceiver(){
		return rec;
	}
	
	public Object getIdentifier(){
		return identifier;
	}
	
	public void setIdentifier(Object o){
		identifier=o;
	}
}