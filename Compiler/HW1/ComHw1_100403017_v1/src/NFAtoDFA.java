import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.Formatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JFileChooser;


public class NFAtoDFA
{
	private static String OutPATH="DFA.txt";
	private static String InputPATH="NFA.txt";
	private static List<String> NFATable=new LinkedList<String>();//輸入的NFA初始
	private static String Table[][];//調整過的NFA用於計算
	private static Formatter output;
	private static List<String> DFATable=new LinkedList<String>();//儲存最後的DFA
	private static String headsplit[];//儲存路徑輸入的token
	private static List<Integer> start=new LinkedList<Integer>();//DFA的第一個state
	private static List<List<Integer>> DFAallstate=new LinkedList<List<Integer>>();//儲存dfa的每組state
	
	public static void main(String args[]) throws FileNotFoundException
	{
		loadFile(InputPATH);
		File nfa=new File(OutPATH);
		output=new Formatter(nfa);
		NtoD();
		//Test();
		//TestDFA();
		OutputFile();
	}
	
	public static void loadFile(String InputPath) throws FileNotFoundException
	{
		Scanner input=new Scanner(new File(InputPath));
		
		while(input.hasNextLine())
		{
			NFATable.add(input.nextLine());
		}
		
		headsplit=NFATable.get(0).split(",");
		
		headsplit[0]=headsplit[0].split("\\(")[1];
		headsplit[headsplit.length-1]=headsplit[headsplit.length-1].split("\\)")[0];
		Table=new String[NFATable.size()][headsplit.length-1];
		for(int count=0;count<Table[0].length;count++)
		{
			Table[0][count]=headsplit[count];
		}
		
		for(int statecount=1;statecount<Table.length;statecount++)
		{
			String bodysplit[]=NFATable.get(statecount).split("\\)\\(");//split each statement of input by )(
			bodysplit[0]=bodysplit[0].split("\\(")[1];//throw the ( char in first statement  
			bodysplit[bodysplit.length-1]=bodysplit[bodysplit.length-1].split("\\)")[0];//throw the ) char in last statement
			for(int count=0;count<Table[statecount].length;count++)
			{
				Table[statecount][count]=bodysplit[count];
			}
		}
		input.close();
	}//end loadFile
		
	//not use now
	public static void OutputFile()
	{
//		try 
//		{
//			output=new Formatter(new File("DFA.txt"));
//		}
//		catch (FileNotFoundException e)
//		{
//			e.printStackTrace();
//		}
		
		String tmp="";
		for(int count=1;count<Table[0].length;count++)
		{
			tmp+=Table[0][count];
			if(count!=Table[0].length-1)
			{
				tmp+=",";
			}
		}
		output.format("(%s)\r\n",tmp);
		for(int count=0;count<DFATable.size();count++)
		{
			output.format("%s\r\n",DFATable.get(count));
		}
		output.close();
	}
	
	public static void NtoD()
	{
		StartState();
//		String headState="";
//		for(int count=0;count<Table[0].length;count++)
//		{
//			headState+=Table[count];
//			if(count!=Table[0].length-1)
//			{
//				headState+=",";
//			}
//		}
//		
//		DFATable.add(headState);
		OtherState();
	}
	
	public static void StartState()
	{		
		start.add(1);
		int count=0;
		while(count<start.size())//new state還沒滿
		{
			String tmp[]=Table[start.get(count)][0].split("0|,");//切掉,跟0
			for(int count1=0;count1<tmp.length;count1++)//把切出來的結果加入new state
			{
				if(!tmp[count1].equals(""))
				{
					int stateadd=Integer.parseInt(tmp[count1]);
					if(!start.contains(stateadd))//去除重複
					{
						start.add(Integer.parseInt(tmp[count1]));
					}
				}
			}
			count++;
		}
		DFAallstate.add(start);//第一個state加入
		Collections.sort(start);
		String startState=start.toString();
		startState=startState.replace("[","(");
		startState=startState.replace("]", ")");
		
		startState=starCheck(startState);
		DFATable.add(startState);
	}
	
	public static void OtherState()
	{
		int staterowcount=0;
		while(staterowcount<DFAallstate.size())//DFAallstate會變大,一列一列處理
		{
			//List<Integer> StateTmp=new LinkedList<Integer>();
			for(int tokencount=1;tokencount<Table[0].length;tokencount++)//根據token一行一行處理
			{
				List<Integer> StateTmp=new LinkedList<Integer>();
				int nowDFAStateSize=DFAallstate.get(staterowcount).size();
				for(int statecount=0;statecount<nowDFAStateSize;statecount++)//目前state集合裡依據每個state處理
				{
					String tmp[]=Table[DFAallstate.get(staterowcount).get(statecount)][tokencount].split(",|0");
					for(int count1=0;count1<tmp.length;count1++)//把切出來的結果加入new state
					{
						if(!tmp[count1].equals("")&&!tmp[count1].equals("*"))
						{
							int stateadd=Integer.parseInt(tmp[count1]);
							if(!StateTmp.contains(stateadd))//去除重複
							{
								StateTmp.add(Integer.parseInt(tmp[count1]));
							}
						}
					}
				}
				if(StateTmp.isEmpty())
				{
					String append=DFATable.get(staterowcount);
					DFATable.set(staterowcount,append+"(0)");
					continue;
				}
				
				//這裡處理"入"
				int lcount=0;
				while(lcount<StateTmp.size())
				{
					String tmp[]=Table[StateTmp.get(lcount)][0].split(",|0");
					for(int count1=0;count1<tmp.length;count1++)//把切出來的結果加入new state
					{
						if(!tmp[count1].equals("*")&&!tmp[count1].equals(""))
						{
							int stateadd=Integer.parseInt(tmp[count1]);
							if(!StateTmp.contains(stateadd))//去除重複
							{
								StateTmp.add(Integer.parseInt(tmp[count1]));
							}
						}
					}
					lcount++;
				}
				
				Collections.sort(StateTmp);
				String append=StateTmp.toString().replace("[", "(");//第二行之後的[處理
				append=append.replace("]", ")");
				append=starCheck(append);
				String appendtmp=DFATable.get(staterowcount);
				DFATable.set(staterowcount,appendtmp+append);
				if(!DFAallstate.contains(StateTmp))
				{
					DFAallstate.add(StateTmp);
					String State=StateTmp.toString();
					State=State.replace("[", "(");
					State=State.replace("]", ")");
					State=starCheck(State);
					DFATable.add(State);
				}
				//StateTmp.clear();
			}
			staterowcount++;
		}
	}
	
	public static void Test()
	{
		for(int row=0;row<Table.length;row++)
		{
			for(int column=0;column<Table[row].length;column++)
			{
				System.out.printf("%s\t",Table[row][column]);
			}
			System.out.println();
		}
	}
	
	public static void TestDFA()
	{
		for(int count=0;count<DFATable.size();count++)
		{
			System.out.println(DFATable.get(count));
		}
	}
	
	public static String starCheck(String state)
	{
		if(state.contains(Integer.toString(Table.length-1)))
		{
			state=state.replace("(", "(*");
		}
		return state;
	}
}


//輸出,星號,括號樣式處理