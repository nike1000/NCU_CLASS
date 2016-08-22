import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;


public class DFAtoken 
{
	private static String InputPATH="DFA.txt";
	private static List<String[]>Table=new LinkedList<String[]>();//扣除第1列和第1行的DFA內部table
	private static List<String>columnHeadList=new LinkedList<String>();//to store every state of DFA
	private static List<String>rowHeadList=new LinkedList<String>();//to store token in first row
	private static String inTable[];
	
	public static void main(String args[]) throws FileNotFoundException
	{
		loadFile(InputPATH);
		//Test();
		tokenTest();
	}
	
	public static void loadFile(String InputPath) throws FileNotFoundException
	{
		Scanner input=new Scanner(new File(InputPath));
		
		if(input.hasNextLine())
		{
			String tmp=input.nextLine();
			tmp=tmp.replace("(","");//去除(
			tmp=tmp.replace(")","");//去除)
			String tmpArray[]=tmp.split(",");//以,分割
			for(String tmpadd:tmpArray)
			{
				rowHeadList.add(tmpadd);
			}
		}
		while(input.hasNextLine())
		{
			inTable=new String[rowHeadList.size()];
			String tmp=input.nextLine();
			tmp=tmp.replace(")(",")-(");//用)-(取代)(
			String tmpArray[]=tmp.split("-");//以-分割
			columnHeadList.add(tmpArray[0]);
			for(int count=0;count<inTable.length;count++)
			{
				inTable[count]=tmpArray[count+1];
			}
			Table.add(inTable);
		}
	}
	
	public static void tokenTest()
	{
		Scanner tokeninput=new Scanner(System.in);
		String token=tokeninput.nextLine();//讀取input的token
		char tokenArray[]=token.toCharArray();
		int toRow=0;
		int tocolumn=0;
		String nextState="";
		boolean error=false;
		for(int count=0;count<tokenArray.length;count++)
		{
			
			tocolumn=rowHeadList.indexOf(Character.toString(tokenArray[count]));
			if(tocolumn==-1)
			{
				System.out.print("error");
				error=true;
				break;
			}
			nextState=Table.get(toRow)[tocolumn];
			if(columnHeadList.contains(nextState))
			{
				toRow=columnHeadList.indexOf(nextState);
			}
			else
			{
				System.out.print("error");
				error=true;
				break;
			}
		}
		if(!error)
		{
			if(nextState.contains("*"))
			{
				System.out.print("valid");
			}
			else
			{
				System.out.print("error");
			}
		}
	}
	
	public static void Test()
	{
		for(int count=0;count<Table.size();count++)
		{
			for(int count1=0;count1<Table.get(count).length;count1++)
			{
				System.out.print(Table.get(count)[count1]);
			}
			System.out.println();
		}
		System.out.println(rowHeadList);
		System.out.println(columnHeadList);
		
	}
}
