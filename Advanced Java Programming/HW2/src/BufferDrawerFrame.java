import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.*;

public class BufferDrawerFrame extends JFrame
{
	private JLabel label1=new JLabel("[繪圖工具]");
	private String comboString[]={"筆刷","直線","橢圓","矩形","圓角矩形","橡皮擦"};
	private JComboBox comboBox=new JComboBox(comboString);
	private JLabel label2=new JLabel("[筆刷粗細]");
	private JRadioButton Rbuttonsmall=new JRadioButton("小",true);
	private JRadioButton Rbuttonmiddum=new JRadioButton("中",false);
	private JRadioButton Rbuttonbig=new JRadioButton("大",false);
	private ButtonGroup radioGroup=new ButtonGroup();
	private JCheckBox fullcheck=new JCheckBox("填滿");
	private JButton button1=new JButton("前景色");
	private JButton button2=new JButton("背景色");
	private JButton button3=new JButton("清除全部");
	private JButton button4=new JButton("儲存");
	private JButton button5=new JButton("開啟檔案");
	private JButton button6=new JButton("上一步");
	private JLabel stateslabel=new JLabel("staes");
	private JPanel Toolpanel=new JPanel();
	private BorderLayout borderlayout=new BorderLayout();
	private BufferDrawerPanel drawpanel=new BufferDrawerPanel();
	private MouseMotionAdapter mousehandler=new MouseMotionAdapter();
	private JFileChooser fileChooser=new JFileChooser();
	private File filename;
	private File storefile;
	private BufferedImage input;
	
	
	public Color ColorChooce=Color.GRAY;
	public Color BackColorChooce=Color.GRAY;
	
	private Handler handler=new Handler();
	
	public BufferDrawerFrame()
	{
		super("Drawer");
		radioGroup.add(Rbuttonsmall);
		radioGroup.add(Rbuttonmiddum);
		radioGroup.add(Rbuttonbig);

		Toolpanel.setLayout(new GridLayout(0,1));
		Toolpanel.add(label1);
		Toolpanel.add(comboBox);
		Toolpanel.add(label2);
		Toolpanel.add(Rbuttonsmall);
		Toolpanel.add(Rbuttonmiddum);
		Toolpanel.add(Rbuttonbig);
		Toolpanel.add(fullcheck);
		Toolpanel.add(button1);
		Toolpanel.add(button2);
		Toolpanel.add(button3);
		Toolpanel.add(button4);
		Toolpanel.add(button5);
		Toolpanel.add(button6);
		
		setLayout(new BorderLayout());
		add(Toolpanel,borderlayout.WEST);
		add(stateslabel,borderlayout.SOUTH);
		add(drawpanel,borderlayout.CENTER);
		
		comboBox.addActionListener(handler);
		Rbuttonsmall.addActionListener(handler);
		Rbuttonmiddum.addActionListener(handler);
		Rbuttonbig.addActionListener(handler);
		fullcheck.addActionListener(handler);
		button1.addActionListener(handler);
		button2.addActionListener(handler);
		button3.addActionListener(handler);
		button4.addActionListener(handler);
		button5.addActionListener(handler);
		button6.addActionListener(handler);
		drawpanel.addMouseMotionListener(mousehandler);	
		drawpanel.setBackground(Color.WHITE);
	}
	private class Handler implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			if(event.getSource()==comboBox)
			{
				drawpanel.function=comboBox.getSelectedIndex();
			}
			else if(event.getSource()==Rbuttonsmall)
			{
				drawpanel.size=4;
			}
			else if(event.getSource()==Rbuttonmiddum)
			{
				drawpanel.size=8;
			}
			else if(event.getSource()==Rbuttonbig)
			{
				drawpanel.size=12;
			}
			else if(event.getSource()==fullcheck)
			{
				if(fullcheck.isSelected())
				{
					drawpanel.full=true;
				}
				else
				{
					drawpanel.full=false;
				}
			}
			else if(event.getSource()==button1)
			{
				ColorChooce=JColorChooser.showDialog(null, "Choose a color", Color.GRAY);
				drawpanel.ColorChooce=ColorChooce;
				button1.setBackground(ColorChooce);
			}
			else if(event.getSource()==button2)
			{
				BackColorChooce=JColorChooser.showDialog(null,"Choose a color", Color.GRAY);
				drawpanel.BackColorChooce=BackColorChooce;
				button2.setBackground(BackColorChooce);
				drawpanel.setBackground(BackColorChooce);
				drawpanel.draw=true;
				drawpanel.repaint();
			}
			else if(event.getSource()==button3)
			{
				drawpanel.image=null;
				drawpanel.image=new BufferedImage(800,700,BufferedImage.TYPE_4BYTE_ABGR_PRE);
				button2.setBackground(null);
				drawpanel.setBackground(null);
				drawpanel.x1=0;
				drawpanel.y1=0;
				drawpanel.x2=0;
				drawpanel.y2=0;
				drawpanel.imageList.add(new BufferedImage(800,700,BufferedImage.TYPE_4BYTE_ABGR));
				drawpanel.draw=true;
				drawpanel.repaint();
				
			}
			else if(event.getSource()==button4)
			{
				OutputPic();
				try
				{
					BufferedImage IOimage=new BufferedImage(drawpanel.getWidth(),drawpanel.getHeight(),BufferedImage.TYPE_4BYTE_ABGR);
					Graphics gIO=IOimage.createGraphics();
					drawpanel.paint(gIO);
					ImageIO.write(IOimage,"png",storefile);
				}
				catch(IOException ioException)
				{
					System.err.println("Error");
				}
			}
			else if(event.getSource()==button5)
			{
				if(drawpanel.g2D==null)
					System.out.print("null\n");
				InputPic();
				try
				{
					input = ImageIO.read(filename);
				}
				catch(IOException ioException)
				{
					System.err.println("Error");
				}
				
				drawpanel.g2D.drawImage(input,0,0,null);
				drawpanel.draw=true;
				drawpanel.repaint();
			}
			else if(event.getSource()==button6)
			{
				if(drawpanel.imageList.isEmpty()==false)
				{
					drawpanel.image=drawpanel.imageList.get(drawpanel.imageList.size()-1);
					drawpanel.imageList.remove(drawpanel.imageList.size()-1);
				}
				drawpanel.stepback=true;
				drawpanel.draw=true;
				drawpanel.repaint();
			}
		}
	}
	
	public void InputPic()
	{
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.showOpenDialog(this);
		filename=fileChooser.getSelectedFile();
	}
	public void OutputPic()
	{
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.showSaveDialog(this);
		storefile=fileChooser.getSelectedFile();//connect selectFile to storefile		
	}
	
	private class MouseMotionAdapter extends MouseAdapter
	{
		public void mouseMoved(MouseEvent event)
		{
			stateslabel.setText(String.format("Mouse at [%d,%d]",event.getX(),event.getY()));
		}
	}
}
