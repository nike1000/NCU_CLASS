import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.concurrent.*;
import java.util.List;
import java.util.LinkedList;
import java.util.ListIterator;

public class WaterSpace extends JPanel
{
	public static List <Fish> fishlist=new LinkedList<Fish>();//store fish object
	public static List <Tortoise> Torlist=new LinkedList<Tortoise>();
	private Fish newfish;
	private Tortoise newTor;
	public int function,fishcount,Torcount;
	public boolean onestop=true;
	private ListIterator< Fish > Fishiterator = fishlist.listIterator(fishlist.size());
	private ListIterator< Tortoise > Toriterator = Torlist.listIterator(Torlist.size());
	private int IDFish=0;
	private int IDTor=0;
	
	public WaterSpace()
	{
		addMouseListener(new MouseHandler());
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		
		for(Fish fishes:fishlist)
		{
			fishes.setsize(getWidth(),getHeight());//set the panel size for all fish object
			fishes.draw(g);//draw the fish for all fish object
		}
		for(Tortoise tortoises:Torlist)
		{
			tortoises.setsize(getWidth(),getHeight());//set the panel size for all tortoise object
			tortoises.draw(g);//draw the tortoise for all tortoise object
		}
		repaint();//continued update the panel by repaint itself
	}//end paintComponent

	private class MouseHandler extends MouseAdapter
	{
		public void mousePressed(MouseEvent event)
		{
			if(function==1)
			{
				newfish=new Fish(event.getX(),event.getY(),IDFish++);
				fishlist.add(newfish);//add fish object to list
				ExecutorService threadExecutor=Executors.newCachedThreadPool();//ThreadPool
				threadExecutor.execute(newfish);//add the fish object to threadPool and execute
				threadExecutor.shutdown();//shutdown threadpool
				fishcount++;
			}
			else if(function==2)
			{
				newTor=new Tortoise(event.getX(),event.getY(),IDTor++);
				Torlist.add(newTor);//add tortoise object to list
				ExecutorService threadExecutor=Executors.newCachedThreadPool();
				threadExecutor.execute(newTor);//add the tortoise object to threadPool and execute
				threadExecutor.shutdown();//shutdown threadpool
				Torcount++;
			}
			else if(function==3)
			{
				int count=0;
				for(Fish fishes:fishlist)
				{
					if(event.getX()>=fishes.x&&event.getX()<=fishes.x+fishes.size&&event.getY()>=fishes.y&&event.getY()<=fishes.y+fishes.size)
					{
						fishlist.get(count).run=false;
						fishlist.remove(count);
						fishcount--;
						break;
					}					
					count++;
				}
				count=0;
				for(Tortoise tortoise:Torlist)
				{
					if(event.getX()>=tortoise.x&&event.getX()<=tortoise.x+150&&event.getY()>=tortoise.y&&event.getY()<=tortoise.y+150)
					{
						Torlist.get(count).run=false;
						Torlist.remove(count);
						Torcount--;
						break;
					}					
					count++;
				}
			}
			else if(function==5)
			{
				for(Fish fishes:fishlist)
				//while(Fishiterator.hasPrevious())
				{
					
					if(!fishes.onewait)//if fish not wait
					{
						fishes.setwait(event.getX(), event.getY());//set wait point
					}
					else//if fish wait
					{
						fishes.startone(event.getX(),event.getY());//to check if eventpoint int pic then notify fish thread
					}
				}
				for(Tortoise tortoise:Torlist)
				{
					if(!tortoise.onewait)
					{
						tortoise.setwait(event.getX(), event.getY());
					}
					else
					{
						tortoise.startone(event.getX(),event.getY());
					}
				}
			}
			
			String string[]={"新增魚","新增烏龜","移除選取","移除全部","暫停/啟動選取","暫停/啟動全部"};//String for button function
			if(function!=0)
			Aquarium.states.setText("目前功能："+string[function-1]+"\n魚數量："+fishcount+"\n烏龜數量："+Torcount);//set state
		}//end mousePress
	}//end mousehandler
}
