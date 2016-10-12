import java.awt.*;
import javax.swing.*;
import java.net.*;
import java.io.*;
import java.util.*;
import java.awt.event.*;
import java.util.concurrent.*;

public class ChatClient extends JFrame implements Runnable,ActionListener
{
	private JLabel label1=new JLabel("請輸入你的暱稱");
	private JLabel label2=new JLabel();
	private JLabel label3=new JLabel();
	private JTextField field1=new JTextField();
	private JTextField field2=new JTextField();
	private JTextField field3=new JTextField();
	private JTextArea area=new JTextArea();
	private JPanel panel1=new JPanel();
	private JPanel panel2=new JPanel();
	private JPanel spacePanel[]=new JPanel[4];
	private JScrollPane scroll;
	
	private Socket connection;
	private Scanner input;
	private Formatter output;
	private String host;
	private String message;
	public boolean exit=false;
	
	public ChatClient(String host)
	{
		super("Chat Room Client");
		this.host=host;
		
		panel1.setLayout(new GridLayout(2,4));
		area.setEditable(false);
		scroll=new JScrollPane(area);
		addSpacePanel();
		panel1.add(label1);
		panel1.add(field1);
		panel1.add(label2);
		area.setBackground(null);
		add(panel1,BorderLayout.NORTH);
		add(scroll,BorderLayout.CENTER);
		
		try
		{
			connection=new Socket(InetAddress.getByName(host),30000);
			input=new Scanner(connection.getInputStream());
			output=new Formatter(connection.getOutputStream());
		}
		catch(IOException ioException)
		{
			ioException.printStackTrace();
			System.exit(1);
		}
		field1.addActionListener(this);
		ExecutorService worker=Executors.newCachedThreadPool();
		worker.execute(this);
	}
	public void run()
	{
		if(input.hasNextLine())
			message=input.nextLine();//接收暱稱確認訊息
		if(exit)//若未登入即結束視窗
		{
			System.exit(0);
		}
		
		while(message.equals("Name Repeat"))//確認暱稱是否重複
		{
			label2.setText("此暱稱已有人使用");
			message=input.nextLine();
		}
		panel1.removeAll();//清除panel1版面
		remove(panel1);//清除JFrame
		remove(scroll);
		
		label1.setText("你的暱稱");//重新設置版面
		field1.setEditable(false);
		label2.setText("線上人數");
		field2.setEditable(false);
		panel1.add(label1);
		panel1.add(field1);
		panel1.add(label2);
		panel1.add(field2);
		addSpacePanel();
		area.setBackground(Color.WHITE);
		area.setLineWrap(true);//Textarea自動換行
		panel2.setLayout(new GridLayout(1,2));
		label3.setText("傳送訊息");
		panel2.add(label3);
		panel2.add(field3);
		add(panel1,BorderLayout.NORTH);
		add(new JScrollPane(area),BorderLayout.CENTER);
		add(panel2,BorderLayout.SOUTH);
		field3.addActionListener(this);
		validate();
		
		while(!exit)
		{
			if(input.hasNextLine())//等待接收訊息
				Displaymessage(input.nextLine());
		}
		System.exit(0);//結束client
	}
	public void exitmessage()//傳送結束訊息
	{
		output.format("---EXIT---\n");
		output.flush();
	}
	
	public void Displaymessage(String disMessage)
	{
		if(disMessage.equals("Number Of Member Online"))//在field2上顯示在線人數
		{
			field2.setText(String.format("%d",input.nextInt()));
			input.nextLine();
		}
		else//在area上顯示訊息
		{
			area.append(disMessage+"\n");
			area.setCaretPosition(area.getDocument().getLength());
		}
			
	}
	
	public void actionPerformed(ActionEvent event)//傳送訊息
	{
		output.format("%s\n",event.getActionCommand());
		output.flush();
		field3.setText("");
	}
	public void addSpacePanel()
	{
		for(int count=0;count<4;count++)
		{
			spacePanel[count]=new JPanel();
			panel1.add(spacePanel[count]);
		}
	}
}
