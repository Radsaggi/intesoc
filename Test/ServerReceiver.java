package Test;

import intesoc.*;
import javax.swing.JTextArea;

public class ServerReceiver implements Receiver {
		private JTextArea ta;
		
		public ServerReceiver (JTextArea ta){
			this.ta=ta;
		}
	
		public void sendString(String a){
			ta.append("Client : "+a+"\n");
		}
	}