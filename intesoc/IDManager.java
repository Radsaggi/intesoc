package intesoc;

import java.util.Random;

class IDManager implements Cloneable,java.io.Serializable,Comparable<IDManager>{
	private int id;
	
	//Maximum number of ids that can exist at a certain time	
	private static final int max=100000;
	
	//the int value of each id will be a index number of the existingIDs array
	//if an id exists the value existingIDs[id] will be true else it will false 
	private volatile static boolean [] existingIDs;
	private transient static Random random=new Random();
	
	static {
    	existingIDs=new boolean[max];
	}
	
	private IDManager(){
	}
	
	public static IDManager createNewID(){
		IDManager ret=new IDManager();
		for (;;) {
			ret.id=random.nextInt(max);
			if (existingIDs[ret.id]==false)
				break;
		} 
		existingIDs[ret.id]=true;
		return ret;
	}
	
	//if existingIds[id.id] is false it means that such an id does not exist
	public static boolean validate(IDManager id){
		return existingIDs[id.id];
	}
	
	public int compareTo(IDManager id){
		return this.id-id.id;
	}
	
	public boolean equals(Object o){
		if (!(o instanceof IDManager))
			return false;
		return ((IDManager)o).id==id;
	}
	
	public void destroy(){
		existingIDs[id]=false;
	}
	
	public IDManager clone(){
		IDManager a=new IDManager();
		a.id=id;
		return a;
	}
	
	public String toString(){
		return "ID :"+id;
	}
}