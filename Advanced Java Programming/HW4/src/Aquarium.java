import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class Aquarium extends JFrame implements ActionListener
{
	private JButton createFish=new JButton("新增魚");
	private JButton createTor=new JButton("新增烏龜");
	private JButton removeSelect=new JButton("移除選取");
	private JButton removeAll=new JButton("移除全部");
	private JButton stopSelect=new JButton("暫停/啟動選取");
	private JButton stopAll=new JButton("暫停/啟動全部");
	public static boolean stop=true;  
	
	public static JTextArea states=new JTextArea("目前功能：\n魚數量：0\n烏龜數量：0",3,10);
	private WaterSpace space=new WaterSpace();
	
	private GridBagLayout layout=new GridBagLayout();
	private GridBagConstraints constraints=new GridBagConstraints();
	
	public Aquarium()
	{
		super("Aquarium");
		setLayout(layout);
		states.setBackground(Color.GRAY);
		states.setForeground(Color.CYAN);
		states.setFont(new Font("Font",Font.ITALIC+Font.BOLD,14));

		constraints.fill=GridBagConstraints.BOTH;
		constraints.weightx=1;//當有額外空間時讓所有元件可變寬
		addComponent(createFish,0,0,1,1);
		addComponent(createTor,1,0,1,1);
		addComponent(removeSelect,0,1,1,1);
		addComponent(removeAll,1,1,1,1);
		addComponent(stopSelect,0,2,1,1);
		addComponent(stopAll,1,2,1,1);
		
		states.setEditable(false);
		addComponent(states,2,0,3,1);
		
		constraints.weighty=1;//當有額外空間時讓space可變高
		space.setBackground(Color.BLUE);
		addComponent(space,3,0,3,1);
		
		
		createFish.addActionListener(this);
		createTor.addActionListener(this);
		removeSelect.addActionListener(this);
		removeAll.addActionListener(this);
		stopSelect.addActionListener(this);
		stopAll.addActionListener(this);
		
		
	}//end Aquarium
	
	private void addComponent(Component component,int row,int column,int width,int height)
	{
		constraints.gridx=column;
		constraints.gridy=row;
		constraints.gridwidth=width;
		constraints.gridheight=height;
		layout.setConstraints(component,constraints);
		add(component);
	}//end addComponent
	
	public void actionPerformed(ActionEvent event)
	{
		if(event.getSource()==createFish)
		{
			space.function=1;
			states.setText("目前功能："+event.getActionCommand()+"\n魚數量："+space.fishcount+"\n烏龜數量："+space.Torcount);
		}
		else if(event.getSource()==createTor)
		{
			space.function=2;
			states.setText("目前功能："+event.getActionCommand()+"\n魚數量："+space.fishcount+"\n烏龜數量："+space.Torcount);
		}
		else if(event.getSource()==removeSelect)
		{
			space.function=3;
			states.setText("目前功能："+event.getActionCommand()+"\n魚數量："+space.fishcount+"\n烏龜數量："+space.Torcount);
		}
		else if(event.getSource()==removeAll)
		{
			space.function=4;
			for(Fish fishes:space.fishlist)
			{
				fishes.run=false;
			}
			for(Tortoise tortoise:space.Torlist)
			{
				tortoise.run=true;
			}
			space.fishlist.clear();
			space.fishcount=0;
			space.Torlist.clear();
			space.Torcount=0;
			states.setText("目前功能："+event.getActionCommand()+"\n魚數量："+space.fishcount+"\n烏龜數量："+space.Torcount);
		}
		else if(event.getSource()==stopSelect)
		{
			states.setText("目前功能："+event.getActionCommand()+"\n魚數量："+space.fishcount+"\n烏龜數量："+space.Torcount);
			space.function=5;
		}
		else if(event.getSource()==stopAll)
		{
			space.function=6;
			states.setText("目前功能："+event.getActionCommand()+"\n魚數量："+space.fishcount+"\n烏龜數量："+space.Torcount);
			if(stop)
			{
				for(Fish fishes:space.fishlist)
				{
					fishes.stop=true;//set all fish stop equal true to stop fish
				}
				for(Tortoise tortoise:space.Torlist)
				{
					tortoise.stop=true;//set all tortoise stop equal true to stop tortoise
				}
				stop=false;//set stop to change the function of next check
			}
			else
			{
				for(Fish fishes:space.fishlist)
				{
					fishes.start();//notify all fish Thread
					fishes.stop=false;
				}
				for(Tortoise tortoise:space.Torlist)
				{
					tortoise.start();//notify all tortoise Thread
					tortoise.stop=false;
				}
				stop=true;
			}//end else
		}//end else if
	}//end actionPerformed
}//end class
