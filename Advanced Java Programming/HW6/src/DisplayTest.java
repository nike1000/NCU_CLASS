import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.event.*;
import javax.swing.*;
import javax.swing.table.*;

public class DisplayTest extends JFrame 
{
	private JPanel upPanel=new JPanel();
	private JLabel fieledLabel=new JLabel("Fieled Name:");
	private String combolist[]={"MemberID","Name","Phone","E_mail","Sex"};
	private JComboBox fieledComboBox=new JComboBox(combolist);
	private JTextField fieledField=new JTextField(30);
	private JButton FindButton=new JButton("Find");
	private int comboChooce;
	
	private JPanel leftPanel=new JPanel();
	private JLabel IDLabel=new JLabel("MemeberID:");
	public static JTextField IDField=new JTextField();
	private JLabel NameLabel=new JLabel("Name:");
	public static JTextField NameField=new JTextField();
	private JLabel PhoneLabel=new JLabel("Phone Number:");
	public static JTextField PhoneField=new JTextField();
	private JLabel EmailLabel=new JLabel("E_mail:");
	public static JTextField EmailField=new JTextField();
	private JLabel SexLabel=new JLabel("Sex:");
	public static JTextField SexField=new JTextField();
	
	private JTable resultTable;
	
	
	private JPanel downPanel=new JPanel();
	private JButton BrowseButton=new JButton("Browse All Entyies");
	private JButton InsertButton=new JButton("Insert New Entry");
	private JButton UpdateButton=new JButton("Update");
	private JButton DeleteButton=new JButton("delete");
	private JPanel spacePanel=new JPanel();
	
	private PersonQueries personQuery;
	private ListSelectionModel lsm;
	
	
	public DisplayTest()
	{
		super("Member");
		setLayout(new BorderLayout());

		personQuery=new PersonQueries();
		resultTable=new JTable(personQuery);//將JTable註冊為TableModelEvent的監聽者,而TableModelEvent由PersonQuies產生
		
		
		upPanel.setBorder(BorderFactory.createTitledBorder("Find an entry by a field"));
		upPanel.add(fieledLabel);
		upPanel.add(fieledComboBox);
		upPanel.add(fieledField);
		upPanel.add(FindButton);
		
		leftPanel.setLayout(new GridLayout(5,2));
		IDField.setEditable(false);
		leftPanel.add(IDLabel);
		leftPanel.add(IDField);
		leftPanel.add(NameLabel);
		leftPanel.add(NameField);
		leftPanel.add(PhoneLabel);
		leftPanel.add(PhoneField);
		leftPanel.add(EmailLabel);
		leftPanel.add(EmailField);
		leftPanel.add(SexLabel);
		leftPanel.add(SexField);
		spacePanel.add(leftPanel,BorderLayout.NORTH);
		
		downPanel.add(BrowseButton);
		downPanel.add(InsertButton);
		downPanel.add(UpdateButton);
		downPanel.add(DeleteButton);
		
		add(upPanel,BorderLayout.NORTH);
		add(spacePanel,BorderLayout.WEST);
		add(new JScrollPane(resultTable),BorderLayout.CENTER);
		add(downPanel,BorderLayout.SOUTH);
		
		setResizable(false);//JFrame大小不可變更
		setVisible(true);
		setSize(800,500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		ActionHandler Actionhandler=new ActionHandler();
		BrowseButton.addActionListener(Actionhandler);
		FindButton.addActionListener(Actionhandler);
		fieledComboBox.addActionListener(Actionhandler);
		InsertButton.addActionListener(Actionhandler);
		DeleteButton.addActionListener(Actionhandler);
		UpdateButton.addActionListener(Actionhandler);
		lsm=resultTable.getSelectionModel();
		lsm.addListSelectionListener(new listSelectionHandler());
		//TableRowSorter<PersonQueries> sorter=new TableRowSorter<PersonQueries>(personQuery);
		//resultTable.setRowSorter(sorter);
	}
	
	public static void main(String args[])
	{
		new DisplayTest();
	}

	private class ActionHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			if(event.getSource()==BrowseButton)
			{
				personQuery.BrowseAll();
			}
			else if(event.getSource()==FindButton)
			{
				personQuery.Find(fieledField.getText(),comboChooce);
			}
			else if(event.getSource()==fieledComboBox)
			{
				comboChooce=fieledComboBox.getSelectedIndex();
			}
			else if(event.getSource()==InsertButton)
			{
				personQuery.Insert(NameField.getText(), PhoneField.getText(), EmailField.getText(), SexField.getText());
			}
			else if(event.getSource()==DeleteButton)
			{
				personQuery.Delete();
			}
			else if(event.getSource()==UpdateButton)
			{
				personQuery.Update();
			}
		}
	}//end inner class
	
	private class listSelectionHandler implements ListSelectionListener
	{
		public void valueChanged(ListSelectionEvent event)
		{
			IDField.setText(resultTable.getValueAt(resultTable.getSelectedRow(),0).toString());
			NameField.setText(resultTable.getValueAt(resultTable.getSelectedRow(),1).toString());
			PhoneField.setText(resultTable.getValueAt(resultTable.getSelectedRow(),2).toString());
			EmailField.setText(resultTable.getValueAt(resultTable.getSelectedRow(),3).toString());
			SexField.setText(resultTable.getValueAt(resultTable.getSelectedRow(),4).toString());
		}
	}
}//end class
