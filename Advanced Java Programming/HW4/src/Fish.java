import java.util.*;
import java.util.Timer;
import java.awt.image.*;
import java.awt.*;
import javax.swing.*;


public class Fish extends JPanel implements Runnable 
{
	public int ID;
	public int x,y,size,kind,direct,speed,width,heigh;
	private int waitx,waity;//set waitpoint
	private Image image;
	private Random random=new Random();
	private String pic[]={"1.png","2.png","3.png","4.png","5.png","6.png"};
	public boolean stop=false;//All stop
	public boolean onewait=false;//One Stop
	private Timer timer=new Timer();
	private timertask task=new timertask();
	private boolean directup;
	public boolean run=true;
	private boolean collectionx=false;
	private boolean collectiony=false;
	
	public Fish(int x,int y,int ID)
	{
		this.ID=ID;
		this.x=x;//set start point of x
		this.y=y;//set start point of y
		size=random.nextInt(90)+60;
		kind=random.nextInt(3);
		direct=random.nextInt(2)+1;
		speed=random.nextInt(20)+15;
		directup=random.nextBoolean();
		image=getToolkit().getImage(getClass().getResource(direct==1?pic[kind*2]:pic[kind*2+1]));//set pic to image
		timer.schedule(task, 3000,3000);
	}
	public synchronized void run()
	{
		while(run)
		{
			try
			{
				Thread.sleep(speed);//set sleep time for speed control
			}
			catch(InterruptedException exception)
			{
				System.out.print("Error");
			}
			
			if(y>heigh-size)
			{
				y--;
			}
			if(y<=0)
			{
				y++;
			}
			
			if(direct==1)//use if to change x to right or left
			{
				x++;
			}
			else
			{
				x--;
			}
			if(directup)
			{
				y--;
			}
			else
			{
				y++;
			}
			
			if((x>=width-size&&direct==1)||(x<=0&&direct==2))//if pic move to edge
			{
				direct=direct%2+1;//change direct
				image=getToolkit().getImage(getClass().getResource(direct==1?pic[kind*2]:pic[kind*2+1]));//change image
			}//碰邊反彈
			if(y<=0||y>=heigh-size)
			{
				directup=!directup;
			}
			
			if(stop)//if stop from stopAll is true
			{
				try
				{
					onewait=true;
					wait();//stop Fish Thread
				}
				catch(InterruptedException exception)
				{
					System.out.print("Error");
				}
			}//end if
			
			
			if(!onewait&&waitx>x&&waitx<x+size&&waity>y&&waity<y+size)//if waitpoint in the pic
			{
				try
				{
					onewait=true;//set onewait equal true to let function 5 of WaterSpace's listener next check execute notify
					wait();
				}
				catch(InterruptedException exception)
				{
					System.out.print("Error");
				}	
			}
			else//if waitpoint not in pic
			{
				waitx=0;//set waitx=0
				waity=0;//set waity=0
			}
			
			for(Fish fishes:WaterSpace.fishlist)
			{
				if(Math.abs(getcenterx()-fishes.getcenterx())<=(size+fishes.size)/2&&Math.abs(getcentery()-fishes.getcentery())<=(size+fishes.size)/2&&ID!=fishes.ID)
				{
					if(x+size>=fishes.x||x<=fishes.x+fishes.size)
						collectionx=true;
					if(y+size>=fishes.y||y<=fishes.y+fishes.size)
						collectiony=true;
					
					if((x+size>=fishes.x||x<=fishes.x+fishes.size)&&collectionx)
					{
						if(direct==fishes.direct)
						{
							if(speed>fishes.speed)
							{
								direct=direct%2+1;//change direct
								image=getToolkit().getImage(getClass().getResource(direct==1?pic[kind*2]:pic[kind*2+1]));//change image
							}
							else
							{
								fishes.direct=fishes.direct%2+1;//change direct
								fishes.image=getToolkit().getImage(getClass().getResource(fishes.direct==1?fishes.pic[fishes.kind*2]:fishes.pic[fishes.kind*2+1]));//change image
							}
						}//change two fish's direct only if their direct the different
						else
						{
							direct=direct%2+1;//change direct
							image=getToolkit().getImage(getClass().getResource(direct==1?pic[kind*2]:pic[kind*2+1]));//change image
							fishes.direct=fishes.direct%2+1;//change direct
							fishes.image=getToolkit().getImage(getClass().getResource(fishes.direct==1?fishes.pic[fishes.kind*2]:fishes.pic[fishes.kind*2+1]));//change image
							
						}
						collectionx=false;
					}
					if((y+size>=fishes.y||y<=fishes.y+fishes.size)&&collectiony)
					{
						if(directup==fishes.directup)
						{
							if(speed>fishes.speed)
								directup=!directup;
							else
								fishes.directup=!fishes.directup;
						}
						else
						{
							directup=!directup;
							fishes.directup=!fishes.directup;
						}
						collectiony=false;
					}
				}
				
			}
			
		}
	}
	
	public void draw(Graphics g)//drawImage
	{
		g.drawImage(image,x,y,size,size,this);
	}
	
	public void setsize(int x,int y)//set WaterSpace size
	{
		width=x;
		heigh=y;
	}
	
	public synchronized void start()//To Allstart
	{
		notify();
		onewait=false;
		setwait(0,0);
	}
	
	public synchronized void startone(int x,int y)
	{
		if(onewait&&x>this.x&&x<this.x+size&&y>this.y&&y<this.y+size)//if thread is stoped  and check in pic
		{
			setwait(0,0);
			notify();//Start
			onewait=false;////set onewait equal false to let function 5 of WaterSpace's listener next check execute wait
			stop=false;
			Aquarium.stop=true;
		}
	}
	
	public void setwait(int x,int y)//set onewait point
	{
		waitx=x;
		waity=y;
	}
	public int getcenterx()
	{
		return x+size/2;
	}
	public int getcentery()
	{
		return y+size/2;
	}
	
	private class timertask extends TimerTask
	{
		public void run()
		{
			direct=random.nextInt(2)+1;
			speed=random.nextInt(10)+10;
			image=getToolkit().getImage(getClass().getResource(direct==1?pic[kind*2]:pic[kind*2+1]));
		}
	}
}
