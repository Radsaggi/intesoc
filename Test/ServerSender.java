package Test;

import intesoc.*;

import java.awt.BorderLayout;
import javax.swing.*;

public class ServerSender extends ServerConnector {
	private JTabbedPane tabbedPane;
	private int count=0;
	
	public ServerSender (){
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				JFrame frame=new JFrame ("Server");
				frame.setSize(400,400);
				frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				frame.setLocationRelativeTo(null);
				frame.setContentPane(tabbedPane=new JTabbedPane());
				frame.setVisible(true);
			}
		});
		
	}

	public Couple generateNewCouple(java.net.InetAddress a){

		SenderImpl sender=new SenderImpl();
		JTextArea ta=sender.getTextArea();
		
		JPanel pan=new JPanel(new BorderLayout());
		pan.add(new JScrollPane(ta),BorderLayout.CENTER);
		pan.add(sender.getTextField(),BorderLayout.SOUTH);
		
		tabbedPane.addTab("Client "+count++,pan);
		Couple c=new Couple(sender,new ServerReceiver(ta),null);
		
		return c;
	}

	public void lastAdded(){
		System.out.println ("last added");
	}
	
	public void exceptionEncountered(Throwable t){
		t.printStackTrace();
	}
	
	public static void main (String[] args) {
		CommunicationSystem.startServer(new ServerSender(),59648);
	}
}