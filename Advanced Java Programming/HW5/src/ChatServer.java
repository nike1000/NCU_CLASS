import java.awt.*;
import javax.swing.*;
import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.awt.event.*;

public class ChatServer extends JFrame implements ActionListener
{
	private JLabel label1=new JLabel("線上人數",SwingConstants.RIGHT);
	private JLabel label2=new JLabel("廣播",SwingConstants.RIGHT);
	private JTextField field1=new JTextField("0");
	private JTextField field2=new JTextField();
	private JTextArea area1=new JTextArea("<System>聊天室已上線\n",10,15);
	private JTextArea area2=new JTextArea(10,25);
	private JPanel panel=new JPanel();
	private ServerSocket server;
	private LinkedList<Member> memberlist=new LinkedList<Member>();
	private ExecutorService chatRoom;
	private int IDcount;//使用者ID
	private int numOfmember;//線上人數
	
	public ChatServer()
	{
		super("Chat Room Server");
		chatRoom=Executors.newCachedThreadPool();
		
		try
		{
			server=new ServerSocket(30000,20);
		}
		catch(IOException ioException)
		{
			ioException.printStackTrace();
			System.exit(1);
		}
		
		field1.setEditable(false);
		area1.setLineWrap(true);//Textarea自動換行
		area2.setLineWrap(true);
		area1.setEditable(false);//Textarea不可編輯
		area2.setEditable(false);
		panel.setLayout(new GridLayout(1,4));
		panel.add(label1);
		panel.add(field1);
		panel.add(label2);
		panel.add(field2);
		
		add(panel,BorderLayout.NORTH);
		add(new JScrollPane(area1),BorderLayout.CENTER);
		add(new JScrollPane(area2),BorderLayout.EAST);
		field2.addActionListener(this);
		
		
	}//end ChatServer
	public void connectWaiting()
	{
		while(IDcount<20)
		{
			try
			{
				memberlist.add(new Member(server.accept(),IDcount++));//等待連線
				chatRoom.execute(memberlist.get(memberlist.size()-1));
			}
			catch(IOException ioException)
			{
				ioException.printStackTrace();
				System.exit(1);
			}
		}
	}//end connectWaiting
	
	public void actionPerformed(ActionEvent event)//廣播事件
	{
		displayMessage("***System Brocast***:"+event.getActionCommand());
		field2.setText("");
		outputMessageToAll("***System Brocast***:"+event.getActionCommand()+"\n");
	}
	
	public void displayMessage(String message)//在area1顯示訊息
	{
		area1.append(message+"\n");
		area1.setCaretPosition(area1.getDocument().getLength());//area1卷軸自動往下
	}
	
	public void memberStatus()//在area2顯示在線的client
	{
		area2.setText("");
		for(Member member:memberlist)
		{
			if(member.name!=null)
				area2.append(member.name+"(ID:"+member.ID+")\n");
		}
	}
	
	public void outputMessageToAll(String message)//將訊息傳給client
	{
		for(Member member:memberlist)
		{
			if(member.name!=null)//client還沒輸入名稱登入時無法接收訊息
			{
				member.output.format("%s",message);
				member.output.flush();
			}
		}
	}
	
		
	private class Member implements Runnable
	{
		private Socket connection;
		private Scanner input;
		private Formatter output;
		private String name;
		private int ID;
		private String message;
		private boolean namepass=true;
		private int index;
		
		public Member(Socket socket,int ID)
		{
			connection=socket;
			this.ID=ID;
			try
			{
				input=new Scanner(connection.getInputStream());
				output=new Formatter(connection.getOutputStream());
			}
			catch(IOException ioException)
			{
				ioException.printStackTrace();
				System.exit(1);
			}
		}
		public void run()
		{
			do
			{
				message=input.nextLine();//接收暱稱
				namepass=true;
				for(Member member:memberlist)//檢查暱稱是否重複
				{
					if(message.equals(member.name))
					{
						output.format("Name Repeat\n");//告訴client暱稱重複
						output.flush();
						namepass=false;
						break;
					}
				}
			}while(!namepass);
			
			if(!message.equals("---EXIT---"))//!如果client還沒輸入名稱就關閉
			{
				output.format("Successful\n");//告訴client暱稱可使用
				output.flush();
				name=message;//在Member設定client暱稱
				displayMessage("<server>client:"+connection.getLocalAddress()+"連線建立ID:"+ID+"已上線");
				outputMessageToAll("<server>clientID:"+ID+"進入聊天室\n");//系統:登入通知
				memberStatus();//在serever設定member連線狀態
				numOfmember++;
				field1.setText(String.format("%d",numOfmember));//在server設定上線人數
				outputMessageToAll("Number Of Member Online\n"+numOfmember+"\n");//告知client線上人數
				
				message=input.nextLine();
				while(!message.equals("---EXIT---"))
				{
					displayMessage("["+name+"]說:"+message);
					outputMessageToAll("["+name+"]說:"+message+"\n");//將client訊息發送
					message=input.nextLine();
				}
				displayMessage("<Server> ClientID:"+ID+"已終止連線");
				outputMessageToAll("<Server> ClientID:"+ID+"已經離開聊天室\n");//系統:離線通知
				numOfmember--;
				field1.setText(String.format("%d",numOfmember));//在server設定上線人數
				outputMessageToAll("Number Of Member Online\n"+numOfmember+"\n");//告知client線上人數
				
			}
			
			for(Member member:memberlist)//尋找被刪除的member index
			{
				if(member.ID==ID)
				{
					index=memberlist.indexOf(member);
				}
			}
			memberlist.remove(index);//從list中刪除member
			memberStatus();
		}
	}
}
