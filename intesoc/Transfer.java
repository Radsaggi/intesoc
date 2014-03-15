package intesoc;

abstract class AbstractTransfer implements java.io.Serializable{
	protected IDManager id;
	
	public IDManager getID(){
		return id;
	}
}

class MethodTransfer extends AbstractTransfer implements java.io.Serializable{
	private MethodDetails details;
	
	public MethodTransfer (IDManager id,MethodDetails deatils){
		this.id=id.clone();
		this.details=deatils;
	}
	
	public MethodDetails getMethodDetails(){
		return details;
	}
}

class NullTransfer extends AbstractTransfer{
}

class ReturnTransfer extends AbstractTransfer {
	Object retrunedValue;
	
	public ReturnTransfer(MethodTransfer mt,Object val){
		this.id=mt.getID().clone();
		retrunedValue=val;
	}
	
	public Object getReturnedValue(){
		return retrunedValue;
	}
}

class ExceptionTransfer extends AbstractTransfer{
	private Exception e;
	
	public ExceptionTransfer(IDManager id,Exception e){
		this.id=id.clone();
		this.e=e;
	}
	
	public Exception getException(){
		return e;
	}	
}