import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;

public class BufferDrawerPanel extends JPanel
{
	public int function=0;
	public int size=4;
	public float dash[]={20};
	public boolean full;
	public int x1,y1,x2,y2;
	public Color ColorChooce=Color.BLACK;
	public Color BackColorChooce;
	private Point point[]=new Point[10000];
	private int pointcount;
	private int count;
	public BufferedImage image=new BufferedImage(800,700,BufferedImage.TYPE_4BYTE_ABGR);
	public Graphics2D g2D;
	public LinkedList<BufferedImage> imageList=new LinkedList<BufferedImage>();
	public boolean stepback=false;
	public boolean drag=true;
	public boolean draw=false;
	public boolean drawimage=false;
	
	public final int pen=0;
	public final int line=1;
	public final int oval=2;
	public final int rect=3;
	public final int roundrect=4;
	public final int eraser=5;
	
	int counter=0;
	
	public BufferedImage imagestore;
	
	public BufferDrawerPanel()
	{
		
		addMouseMotionListener(new MouseMotionAdapter());
		addMouseListener(new MouseMotionAdapter());
	}
	
	public void paintComponent(Graphics g)
	{
		
		super.paintComponent(g);
			
		Graphics2D g2d = ( Graphics2D ) g;
		g2D=image.createGraphics();
		g2d.setStroke(new BasicStroke(size));
		g2d.setColor(ColorChooce);
		g2D.setStroke(new BasicStroke(size));
		g2D.setColor(ColorChooce);
		g.drawImage(image,0,0,null);
			
		if(draw)
		{
			if(!stepback)
			{
				if(function==pen)
				{
					for(count=0;point[count+1]!=null;count++)
					{
						if(drag)
						{
							g2d.drawLine(point[count].x, point[count].y, point[count+1].x, point[count+1].y);
						}
						else
						{
							g2D.drawLine(point[count].x, point[count].y, point[count+1].x, point[count+1].y);
						}
					}
				}
				else if(function==line)
				{
					if(!full)
					{
						g2D.setStroke(new BasicStroke(size,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND,10,dash,0));
						g2d.setStroke(new BasicStroke(size,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND,10,dash,0));
					}
					if(drag)
					{
						g2d.draw(new Line2D.Double(x1,y1,x2,y2));
					}
					else
					{
						g2D.draw(new Line2D.Double(x1,y1,x2,y2));
					}		
				}
				else if(function==oval)
				{
					if(full)
					{
						if(drag)
						{
							g2d.fill(new Ellipse2D.Double(Math.min(x1,x2),Math.min(y1,y2),Math.abs(x1-x2),Math.abs(y1-y2)));
						}
						else
						{
							g2D.fill(new Ellipse2D.Double(Math.min(x1,x2),Math.min(y1,y2),Math.abs(x1-x2),Math.abs(y1-y2)));
						}
					}
					else
					{
						if(drag)
						{
							g2d.draw(new Ellipse2D.Double(Math.min(x1,x2),Math.min(y1,y2),Math.abs(x1-x2),Math.abs(y1-y2)));
						}
						else
						{
							g2D.draw(new Ellipse2D.Double(Math.min(x1,x2),Math.min(y1,y2),Math.abs(x1-x2),Math.abs(y1-y2)));
						}
					}
				}
				else if(function==rect)
				{
					if(full)
					{
						if(drag)
						{
							g2d.fill(new Rectangle2D.Double(Math.min(x1,x2),Math.min(y1,y2),Math.abs(x1-x2),Math.abs(y1-y2)));
						}
						else
						{
							g2D.fill(new Rectangle2D.Double(Math.min(x1,x2),Math.min(y1,y2),Math.abs(x1-x2),Math.abs(y1-y2)));
						}
						
					}
					else
					{
						if(drag)
						{
							g2d.draw(new Rectangle2D.Double(Math.min(x1,x2),Math.min(y1,y2),Math.abs(x1-x2),Math.abs(y1-y2)));
						}
						else
						{
							g2D.draw(new Rectangle2D.Double(Math.min(x1,x2),Math.min(y1,y2),Math.abs(x1-x2),Math.abs(y1-y2)));
						}
					}
					
				}
				else if(function==roundrect)
				{
					if(full)
					{
						if(drag)
						{
							g2d.fill(new RoundRectangle2D.Double(Math.min(x1,x2),Math.min(y1,y2),Math.abs(x1-x2),Math.abs(y1-y2),100,100));
						}
						else
						{
							g2D.fill(new RoundRectangle2D.Double(Math.min(x1,x2),Math.min(y1,y2),Math.abs(x1-x2),Math.abs(y1-y2),100,100));
						}
						
					}
					else
					{
						if(drag)
						{
							g2d.draw(new RoundRectangle2D.Double(Math.min(x1,x2),Math.min(y1,y2),Math.abs(x1-x2),Math.abs(y1-y2),100,100));
						}
						else
						{
							g2D.draw(new RoundRectangle2D.Double(Math.min(x1,x2),Math.min(y1,y2),Math.abs(x1-x2),Math.abs(y1-y2),100,100));
						}
						
					}
					
				}
				else if(function==eraser)
				{
					g2D.setColor(Color.WHITE);
					g2d.setColor(Color.WHITE);
					for(count=0;point[count+1]!=null;count++)
					{
						if(drag)
						{
							g2d.drawLine(point[count].x, point[count].y, point[count+1].x, point[count+1].y);
						}
						else
						{
							g2D.drawLine(point[count].x, point[count].y, point[count+1].x, point[count+1].y);
						}
					}
				}
			}
			if((function==pen||function==eraser)&&!drag)
			{
				int counter=0;
				while(point[counter]!=null)
					point[counter++]=null;
				pointcount=0;
			}
			if(drawimage)
			{
				g.drawImage(image,0,0,null);
				drawimage=false;
			}
			stepback=false;
		}
		draw=false;
	}
	
	private class MouseMotionAdapter extends MouseAdapter
	{
		public void mousePressed(MouseEvent event)
		{
			draw=true;
			if(function==pen)
			{
				point[pointcount++]=event.getPoint();
			}
			else
			{
				x1=event.getX();
				y1=event.getY();
			}
			
		}//end MousePress
		public void mouseReleased(MouseEvent event)
		{
			draw=true;
			x2=event.getX();
			y2=event.getY();
			
			imagestore=new BufferedImage(800,700,BufferedImage.TYPE_4BYTE_ABGR);
			imagestore.setData(image.getData());
			imageList.add(imagestore);
			drag=false;
			drawimage=true;
			repaint();
		}//end MouseRelease
		public void mouseDragged(MouseEvent event)
		{
			draw=true;
			if(function==pen||function==eraser)
			{
				point[pointcount++]=event.getPoint();
			}
			else
			{
				x2=event.getX();
				y2=event.getY();
			}
			drag=true;
			repaint();
		}//end MouseDrag
	}//end MouseMotionAdapter
}
