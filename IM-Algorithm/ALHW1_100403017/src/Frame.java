/*
 * 100403017 HKS 2015.05.21
 */

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Frame extends JFrame
{
	private Algo variable=new Algo();
	// UI component
	private JLabel hintlabel = new JLabel("輸入整數n，3<n<50:");
	private JTextField inputField = new JTextField();
	private JButton pointBtn = new JButton("Produce Point");
	private JButton startBtn = new JButton("Start");
	private JPanel toolPanel = new JPanel();
	private Panel convexPanel = new Panel();
	private Handler handler = new Handler();

	private int n;
	

	public Frame()
	{
		toolPanel.setLayout(new GridLayout(1, 0));
		toolPanel.add(hintlabel);
		toolPanel.add(inputField);
		toolPanel.add(pointBtn);
		toolPanel.add(startBtn);
		setLayout(new BorderLayout());
		add(convexPanel, BorderLayout.CENTER);
		add(toolPanel, BorderLayout.SOUTH);
		
		//listener
		pointBtn.addActionListener(handler);
		startBtn.addActionListener(handler);
		startBtn.setEnabled(false);
	}

	private class Handler implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent event)
		{
			if (event.getSource() == pointBtn)
			{
				try
				{
					n = Integer.parseInt(inputField.getText());
					if (n < 3 || n > 50)//input range
					{
						JOptionPane.showMessageDialog(null, "Out of range,please input the number again.");
					}
					else
					{
						variable.ProductPoint(n);//random get n point
						convexPanel.setPoint(variable.pointArray);//set point to panel
						startBtn.setEnabled(true);
						pointBtn.setEnabled(false);
					}

				}
				catch (Exception e)//input type check
				{
					JOptionPane.showMessageDialog(null, "Error,please input the number again.");
				}
			}
			else if (event.getSource() == startBtn)
			{
				variable.ConvexAlgo();//algorithm
				convexPanel.setConvex(variable.convexArrayList);//set edge to panel
				startBtn.setEnabled(false);
			}
		}
	}
}
