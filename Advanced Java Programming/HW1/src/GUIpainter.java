import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.Color;


public class GUIpainter extends JFrame{
	private JPanel functionpanel;
	private JComboBox ComboBox;
	private String functionname[]={"筆刷","直線","橢圓形","矩形","圓角矩形"};
	private JLabel comboLabel;
	
	private JRadioButton JRBArray[]={new JRadioButton("小",true),new JRadioButton("中",false),new JRadioButton("大",false)};
	private ButtonGroup radioGroup;
	private JLabel radioLabel;
	
	private JCheckBox fullBox;
	
	private JButton JBArray[]={new JButton("前景色"),new JButton("背景色"),new JButton("清除畫面")};
	
	private JPanel mousePanel;
	private JLabel statusBar;
	
	
	public GUIpainter()
	{
		super("小畫家");
		functionpanel=new JPanel();
		//This is ComboBox
		ComboBox=new JComboBox(functionname);
		ComboBox.setMaximumRowCount(3);//set rollcount of combobox
		ComboBox.addActionListener(		//addListener for comboBox by inner Class
				new ActionListener()
				{
					public void actionPerformed(ActionEvent event)
					{
						JOptionPane.showMessageDialog(null, "你點選了"+functionname[ComboBox.getSelectedIndex()]);
					}
				}
				);
		comboLabel=new JLabel("[繪圖工具]");//Label for tooltip
		functionpanel.add(comboLabel);//add comboLabel to functionalpanel
		functionpanel.add(ComboBox);//add comboBox to functionalpanel

		//This is RadioButton
		radioGroup=new ButtonGroup();
		RadioButtonHandler handler=new RadioButtonHandler();
		radioLabel=new JLabel("[筆刷大小]");//label for tooltip
		functionpanel.add(radioLabel);//add radioLabel to functionpanel
		for(int count=0;count<JRBArray.length;count++)
		{
			radioGroup.add(JRBArray[count]);//add radioButton to radioGroup
			JRBArray[count].addActionListener(handler);//addActionListener for radioButton
			functionpanel.add(JRBArray[count]);//add JRBAray to panel2
		}		
		
		fullBox=new JCheckBox("填滿");
		functionpanel.add(fullBox);//add fullBox to functionpanel
		fullBox.addActionListener(	//addActionListener for fullBox by inner Class
				new ActionListener()
				{
					private String chooce="取消";
					public void actionPerformed(ActionEvent event)
					{
							chooce=fullBox.isSelected()?"選擇":"取消";
							JOptionPane.showMessageDialog(null,"你"+chooce+"了填滿的訊息視窗");
					}
				}
				);
		
		//This is Button
		ButtonHandler bhandler=new ButtonHandler();
		for(int count=0;count<JBArray.length;count++)
		{
			JBArray[count].addActionListener(bhandler);//addActionListener for Button
			functionpanel.add(JBArray[count]);//add Button to functionpanel
		}

		functionpanel.setLayout(new GridLayout(0,1));//setLayout for functional by grid
		add(functionpanel,BorderLayout.WEST);//add functionpanel to JFrame by Border
		
		
		mousePanel=new JPanel();
		add(mousePanel,BorderLayout.CENTER);//add mousePanel to JFrame
		
		statusBar=new JLabel("Mouse outside panel");
		add(statusBar,BorderLayout.SOUTH);//add statusBar to JFrame
		
		MouseHandler mhandler=new MouseHandler();
		mousePanel.addMouseMotionListener(mhandler);
		
		for(int count=0;count<JBArray.length;count++)
		{
			JBArray[count].setBackground(Color.ORANGE);//setBackGroung for Button
			JRBArray[count].setBackground(Color.ORANGE);//setBackGroung for radioButton
		}
		
		ComboBox.setBackground(Color.ORANGE);//setBackgroung for ComboBox
		fullBox.setBackground(Color.ORANGE);//setBackgroung for fullBox
		functionpanel.setBackground(Color.ORANGE);//setBackgroung for functionpanel
	}
	//Handler for radioButton
	private class RadioButtonHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			JOptionPane.showMessageDialog(null,"你選擇了"+event.getActionCommand()+"筆刷");
		}
	}
	//Handler for Button
	private class ButtonHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			JOptionPane.showMessageDialog(null,event.getActionCommand());
		}
	}
	//Handler for Mouse
	private class MouseHandler extends MouseAdapter
	{
		public void mouseMoved(MouseEvent event)
		{
			statusBar.setText(String.format("Clicked at [%d,%d]",event.getX(),event.getY()));
		}
		
	}
}
