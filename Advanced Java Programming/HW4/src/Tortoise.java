import java.util.*;
import java.util.Timer;
import java.awt.image.*;
import java.awt.*;
import javax.swing.*;
 

public class Tortoise extends JPanel implements Runnable
{
	public int x,y,direct,speed,width,heigh;
	private int waitx,waity;
	private Image image;
	private Random random=new Random();
	public boolean stop=false;//All stop
	public boolean onewait=false;//One Stop
	private Timer timer=new Timer();
	private timertask task=new timertask();
	public boolean run=true;
	public int sizex=150;
	public int sizey=80;
	public int ID;
	
	
	public Tortoise(int x,int y,int ID)
	{
		this.ID=ID;
		this.x=x;
		this.y=y;
		direct=random.nextInt(2)+1;
		speed=random.nextInt(10)+10;
		image=getToolkit().getImage(getClass().getResource(direct==1?"w.png":"w2.png"));
		timer.schedule(task, 3000, 3000);
		
	}
	public synchronized void run()
	{
		//timer.schedule(,);
		while(run&&(heigh==0||y<=(heigh-sizey)))//while pic not at buttom
		{
			try
			{
				Thread.sleep(10);
			}
			catch(InterruptedException exception)
			{
				System.out.print("Error");
			}
			y++;
			
			if(stop)
			{
				try
				{
					onewait=true;
					wait();
				}
				catch(InterruptedException exception)
				{
					System.out.print("Error");
				}
				stop=false;
			}
			
			if(!onewait&&waitx>x&&waitx<x+sizex&&waity>y&&waity<y+sizex)
			{
				try
				{
					onewait=true;
					wait();
				}
				catch(InterruptedException exception)
				{
					System.out.print("Error");
				}	
			}
			else
			{
				waitx=0;
				waity=0;
			}
		}
		while(run)
		{
			y=heigh-80;
			try
			{
				Thread.sleep(speed);
			}
			catch(InterruptedException exception)
			{
				System.out.print("Error");
			}
			if(direct==1)
			{
				x++;
			}
			else
			{
				x--;
			}
			
			if((x>=(width-sizex)&&direct==1)||(x<=0&&direct==2))
			{
				direct=direct%2+1;
				image=getToolkit().getImage(getClass().getResource(direct==1?"w.png":"w2.png"));
			}
			
			if(stop)
			{
				try
				{
					onewait=true;
					wait();
				}
				catch(InterruptedException exception)
				{
					System.out.print("Error");
				}
			}
			
			if(!onewait&&waitx>x&&waitx<x+sizex&&waity>y&&waity<y+sizey)
			{
				try
				{
					onewait=true;
					wait();
				}
				catch(InterruptedException exception)
				{
					System.out.print("Error");
				}	
			}
			else
			{
				waitx=0;
				waity=0;
			}
			for(Tortoise tortoise:WaterSpace.Torlist)
			{
				if((x+sizex>=tortoise.x&&x<=tortoise.x+tortoise.sizex)&&ID!=tortoise.ID)
				{
					if(direct==tortoise.direct)
					{
						if(speed>tortoise.speed)
						{
							direct=direct%2+1;//change direct
							image=getToolkit().getImage(getClass().getResource(direct==1?"w.png":"w2.png"));//change image
						}
						else
						{
							tortoise.direct=tortoise.direct%2+1;//change direct
							tortoise.image=getToolkit().getImage(getClass().getResource(tortoise.direct==1?"w.png":"w2.png"));//change image
						}
					}
					else
					{
						
						direct=direct%2+1;//change direct
						image=getToolkit().getImage(getClass().getResource(direct==1?"w.png":"w2.png"));//change image
						tortoise.direct=tortoise.direct%2+1;//change direct
						tortoise.image=getToolkit().getImage(getClass().getResource(tortoise.direct==1?"w.png":"w2.png"));//change image
					}
				}
			}
		}
	}
	
	public void draw(Graphics g)
	{
		g.drawImage(image,x,y,this);
	}
	
	public void setsize(int x,int y)
	{
		width=x;
		heigh=y;
	}
	
	public synchronized void start()
	{
		notify();
		onewait=false;
		setwait(0,0);
	}
	
	public synchronized void startone(int x,int y)
	{
		if(onewait&&x>this.x&&x<this.x+150&&y>this.y&&y<this.y+150)
		{
			setwait(0,0);
			notify();
			onewait=false;
			stop=false;
			Aquarium.stop=true;
		}
	}
	
	public void setwait(int x,int y)
	{
		waitx=x;
		waity=y;
	}
	
	private class timertask extends TimerTask
	{
		public void run()
		{
			direct=random.nextInt(2)+1;
			speed=random.nextInt(10)+10;
			image=getToolkit().getImage(getClass().getResource(direct==1?"w.png":"w2.png"));
		}
	}
}
