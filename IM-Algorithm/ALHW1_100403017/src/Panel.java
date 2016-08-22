import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

public class Panel extends JPanel implements Runnable
{
	private Point pointArray[];
	private ArrayList<Point> convexArrayList;//store the convex vertex after do algorithm
	public int counter = 0;
	public Thread myThread;//use a thread to control speed
	public BufferedImage image = new BufferedImage(600, 600, BufferedImage.TYPE_4BYTE_ABGR);//use buffer image as plane
	public Graphics2D g2D;

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2D = image.createGraphics();//Creates a Graphics2D, which can be used to draw into this BufferedImage.
		g2D.setColor(Color.BLACK);
		g2D.setStroke(new BasicStroke(4));

		if (pointArray != null)
		{
			for (int counter = 0; counter < pointArray.length; counter++)//draw Point
			{
				g2D.fill(new Ellipse2D.Double(pointArray[counter].x - 2, pointArray[counter].y - 2, 4, 4));//shift 2 to let point basic on center,not top-left 
			}
		}

		if (convexArrayList != null)//draw edge
		{
			g2D.drawLine(convexArrayList.get(counter).x, convexArrayList.get(counter).y, convexArrayList.get(counter + 1).x, convexArrayList.get(counter + 1).y);
		}
		g.drawImage(image, 0, 0, null);//draw image to panel
	}

	public void setPoint(Point[] pointArray)//this function will be called in Frame class after click button "Produce Point"
	{
		this.pointArray = pointArray;
		repaint();
	}

	public void setConvex(ArrayList<Point> convexArrayList)//this function will be called in Frame class after click button "Start"
	{
		this.convexArrayList = convexArrayList;
		myThread = new Thread(this);
		myThread.start();
	}

	@Override
	public void run()
	{
		for (int counter = 0; counter < convexArrayList.size(); counter += 2)
		{
			this.counter = counter;
			repaint();
			try
			{
				Thread.sleep(500);//speed control,0.5s
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
}
