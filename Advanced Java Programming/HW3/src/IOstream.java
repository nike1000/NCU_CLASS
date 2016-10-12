//100403017 kshuang

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import javax.swing.JFileChooser;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.File;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.FormatterClosedException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.LinkedList;
import java.util.Collections;
import javax.swing.JOptionPane;

public class IOstream extends JFrame implements ActionListener
{
	private JLabel label[]=new JLabel[4];//four label
	private String labelname[]={"File name:","Insert data:","Delete data:","Path:"};//label name
	
	private JTextField insertField=new JTextField();//TextField for insert
	private JTextField deleteField=new JTextField();//TextField for delete
	private JTextArea textarea=new JTextArea("Please load a file.");//TextArea to list data
	
	private JButton button[]=new JButton[6];//add six button
	private String buttonname[]={"Load","Insert","Delete","Sort","Close","Save"};//button name
	
	private JPanel panel=new JPanel();//panel to put function tool at frame top
	private JPanel panel2=new JPanel();//panel to put function tool at frame buttom
	
	private GridLayout gridlayout=new GridLayout(3,3,2,2);//layout on top panel
	private GridLayout gridlayout2=new GridLayout(1,3);//layout on buttom panel
	
	private File filename;//input file select
	private File storefile;//output file select
	private ObjectInputStream input;//input file
	private StuRecord record;
	private List <StuRecord> stulist=null;//store data as StuRecord
	private ObjectOutputStream output;//output file
	private boolean cancel=false;
	
	public IOstream()//set GUI
	{
		textarea.setEditable(false);
		for(int count=0;count<label.length;count++)
		{
			label[count]=new JLabel(labelname[count]);
		}
		for(int count=0;count<button.length;count++)
		{
			button[count]=new JButton(buttonname[count]);
			button[count].addActionListener(this);
		}
		panel.add(label[0]);
		panel.add(label[3]);
		panel.add(button[0]);
		panel.add(label[1]);
		panel.add(insertField);
		panel.add(button[1]);
		panel.add(label[2]);
		panel.add(deleteField);
		panel.add(button[2]);
		panel2.add(button[3]);
		panel2.add(button[4]);
		panel2.add(button[5]);
		
		panel.setLayout(gridlayout);
		panel2.setLayout(gridlayout2);
		
		add(panel,BorderLayout.NORTH);
		add(textarea,BorderLayout.CENTER);
		add(panel2,BorderLayout.SOUTH);
	}//end construction
	
	public void actionPerformed(ActionEvent event)
	{
		if(event.getActionCommand()=="Load")
		{
			try
			{
				loadFile();
				label[0].setText("File name:"+filename.getName());//print filename to the label
				label[3].setText("Path:"+filename.getPath());//print path to the label
				textarea.setText(PrintList());//set textarea
			}
			catch(NullPointerException nullPointerException)
			{
				JOptionPane.showMessageDialog(this,"Cancel");//show mwssage if choose cancel in JFileChooser
			}
			closeinput();//inputclose
		}//end if
		if(event.getActionCommand()=="Insert")
		{
			try
			{
				if(stulist==null)
					throw new NullPointerException();//throw exception if not loading yet
				if(!insertField.getText().matches("[A-Z][a-z]*:[0-9][0-9]*,[0-9][0-9]*")&&!insertField.getText().matches("[A-Z][a-z]*:[0-9]*,[0-9]*,"))
				{
					throw new InputMismatchException();//throw exception if input wrong
				}
				insert(insertField.getText());
				textarea.setText(PrintList());
			}
			catch(NullPointerException nullPointerException)
			{
				JOptionPane.showMessageDialog(this,"Please load a new file first.");
			}//end catch
			catch(InputMismatchException inputMismatchException)
			{
				JOptionPane.showMessageDialog(this,"The type of the data input wrong");
			}
			insertField.setText(null);
		}//end if
		if(event.getActionCommand()=="Delete")
		{
			try
			{
				sort();
				delete(deleteField.getText());
				textarea.setText(PrintList());
			}//end try
			catch(NullPointerException nullPointerException)
			{
				JOptionPane.showMessageDialog(this,"Please load a new file first.");
			}//end catch
			deleteField.setText(null);
		}//end if
		if(event.getActionCommand()=="Sort")
		{
			try
			{
				sort();
				textarea.setText(PrintList());
			}//end try
			catch(NullPointerException nullPointerException)
			{
				JOptionPane.showMessageDialog(this,"Please load a new file first.");
			}//end catch
		}//end if
		if(event.getActionCommand()=="Close")
		{
			try
			{
				if(stulist==null)
					throw new NullPointerException();
				label[0].setText("File name:");
				label[3].setText("Path:");
				stulist=null;//clean stulist
				textarea.setText(null);//clean textarea
				cancel=false;
			}//end try
			catch(NullPointerException nullPointerException)
			{
				JOptionPane.showMessageDialog(this,"Please load a new file first.");
			}//end catch
		}//end if
		if(event.getActionCommand()=="Save")
		{
			try
			{
				saveFile();
			}
			catch(NullPointerException nullPointerException)
			{
				if(cancel==true)
					JOptionPane.showMessageDialog(this,"cancel");//catch exception if choose cancel in JFileChooser
				else
					JOptionPane.showMessageDialog(this,"Please load a new file first.");//catch exception if save without loading file
			}//end catch
			catch(FileNotFoundException fileNotFoundException)
			{
				System.err.println("Error storeing file");
			}//end catch
			catch(FormatterClosedException formatterClosedException)
			{
				System.err.println("Error writing to file");
				return;
			}//end catch
			catch(NoSuchElementException elementException)
			{
				System.err.println("Invalid");
			}//end catch
		}
	}//end ActionPerformed
	
	private void loadFile() throws NullPointerException
	{
		JFileChooser filechooser=new JFileChooser();
		filechooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		filechooser.showOpenDialog(this);
		filename=filechooser.getSelectedFile();//connect selectFile to filename

		try
		{
			input=new ObjectInputStream(new FileInputStream(filename.getPath()));//input file
			stulist=new LinkedList<StuRecord>();
		}
		catch(IOException ioException)
		{
			System.err.println("Error opening File");
		}
		
		try
		{
			while(true)
			{
				record=(StuRecord)input.readObject();
				stulist.add(record);
			}//end while
		}
		catch(EOFException endOffileException)
		{
			return;
		}//end catch
		catch(ClassNotFoundException classNotFoundException)
		{
			System.err.println("Unable to create object");
		}//end catch
		catch(IOException ioException)
		{
			System.err.println("Error during read from file");
		}//end catch
	}//end loadFile
	public void closeinput()
	{
		try
		{
			if(input!=null)
				input.close();
		}
		catch(IOException ioException)
		{
			System.err.println("Error closing file");
			System.exit(1);
		}
	}

	public void saveFile() throws NullPointerException,FileNotFoundException,FormatterClosedException,NoSuchElementException
	{
		if(stulist==null)
		{
			throw new NullPointerException();
		}
		try
		{
			JFileChooser filechooser=new JFileChooser();
			filechooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			filechooser.showSaveDialog(this);
			storefile=filechooser.getSelectedFile();//connect selectFile to storefile
			output=new ObjectOutputStream(new FileOutputStream(storefile.getPath()));//connect storefile to output
		}
		catch(NullPointerException nullPointerException)
		{
			cancel=true;
		}
		catch(IOException ioException)
		{
			System.err.println("Error opening file.");
		}
		
		for(StuRecord lists:stulist)
		{
			try
			{
				output.writeObject(lists);//output stulist to file
			}
			catch(IOException ioException)
			{
				System.err.println("Error writting to file.");
			}
			
		}//end for	
		try
		{
			if(output!=null)
				output.close();//output close
		}
		catch(IOException ioException)
		{
			System.err.println("Error closing file.");
			System.exit(1);
		}
		
		JOptionPane.showMessageDialog(this,"儲存成功");
	}//saveFile
	
	public void sort()
	{
		Collections.sort(stulist);
		for(StuRecord lists:stulist)
		{
			if(lists.getScore1()>lists.getScore2())//sort the score form low to high
			{
				double temp=lists.getScore1();
				lists.setScore1(lists.getScore2());
				lists.setScore2(temp);				
			}//end if
		}//end for
	}//end sort
	
	public void insert(String string)
	{
		String temp[]=new String[3];
		temp=string.split(":|,");
		stulist.add(new StuRecord(temp[0],Double.parseDouble(temp[1]),Double.parseDouble(temp[2])));//add new data to list by line
		sort();//resort
	}//end insert

	public void delete(String string)
	{
		List <String> listtemp=new LinkedList<String>();//listtemp only store the name of data not score
		for(StuRecord lists:stulist)
		{
			listtemp.add(lists.getStuName());//add name of data to listtemp
		}//end for
		if(Collections.binarySearch(listtemp,string)>=0)
		{
			stulist.remove(Collections.binarySearch(listtemp,string));//remove the data in stulist which index the same as index of input string in listtemp
		}
		else
		{
			JOptionPane.showMessageDialog(this,"data not found");
		}
	}//end delete
	
	public String PrintList()
	{
		String string="";
		for(StuRecord lists:stulist)
		{
			string+=lists.getStuName()+"\t\t"+lists.getScore1()+"\t\t"+lists.getScore2()+"\n";//add all the data in stulist to string
		}//end for
		return string;
	}//end PrintList
}//end class
