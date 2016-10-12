import javax.swing.JFrame;


public class BufferDrawerTest
{
	public static void main(String args[])
	{
		BufferDrawerFrame application=new BufferDrawerFrame();
		application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		application.setSize(800, 700);
		application.setResizable(false);
		application.setVisible(true);
	}
}
