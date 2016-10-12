import javax.swing.JFrame;

public class ChatServerTest
{
	public static void main(String args[])
	{
		ChatServer application=new ChatServer();
		application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		application.setVisible(true);
		application.setSize(700, 500);
		application.connectWaiting();
	}
}
