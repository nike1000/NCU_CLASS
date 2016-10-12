import javax.swing.JFrame;

public class GUIpainterTest {
	public static void main(String args[])
	{
		GUIpainter guipainter=new GUIpainter();
		guipainter.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		guipainter.setSize(500,500);
		guipainter.setVisible(true);
	}
}
