package Test;


import intesoc.*;
import javax.swing.*;

public class SenderImpl extends Sender implements java.awt.event.ActionListener{
		private JTextArea ta;
		private JTextField tf;

		public SenderImpl (){
			ta=new JTextArea();
			ta.setEditable(false);
			tf=new JTextField ();
			tf.addActionListener(this);
		}

		public JTextArea getTextArea(){
			return ta;
		}
		
		public JTextField getTextField(){
			return tf;
		}
		
		public void actionPerformed(java.awt.event.ActionEvent ae){
			final String str=tf.getText();
			tf.setText("");
			new Thread(new Runnable(){
				public void run(){
					sendString(str);
				}
			}).start();
		}

		public void sendString (String arg){
			ta.append("Server : "+arg+"\n");
			invokeMethod(new MethodDetails(Void.class,"sendString",new Class[]{String.class},arg));
		}
	}