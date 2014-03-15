package intesoc;

public class MethodDetails implements java.io.Serializable{
	private Object [] args;
	private String name;
	private Class<?> returnType,argClass[];
	
	public MethodDetails(Class<?> returnType,String name,Class<?>[] argClass,Object...args){
		this.returnType=returnType;
		this.name=name;
		this.args=args;
		this.argClass=argClass;
	}
	
	public Class<?> getReturnType(){
		return returnType;
	}
	
	public String getName(){
		return name;
	}
	
	public Object[] getArguments(){
		return args;
	}
	
	public Class<?>[] getArgumentTypes(){
		return argClass;
	}
	
	public String toString(){
		StringBuffer buf=new StringBuffer("METHOD DETAILS- "+returnType.getName()+" "+getName()+"(");
		for (Class<?> c:getArgumentTypes()){
			buf.append(c.getName());
		}
		return buf.toString()+")";
	}
}