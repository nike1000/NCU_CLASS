import javax.swing.JFrame;
import java.awt.event.*;

public class ChatClientTest
{
	public static void main(String args[])
	{
		final ChatClient application;
		if(args.length==0)
			application=new ChatClient("127.0.0.1");
		else
			application=new ChatClient(args[0]);
		application.addWindowListener//視窗監聽介面
		(
			new WindowAdapter()
			{
				public void windowClosing(WindowEvent event)//關閉視窗時的監聽事件
				{
					application.exit=true;
					application.exitmessage();
				}
			}
		);
		application.setVisible(true);
		application.setSize(700, 500);
	}
	
}
