import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Formatter;
import java.util.LinkedList;
import java.util.Scanner;



public class FirstSet
{
	private static String OutPATH="output.txt";
	private static String InputPATH="input.txt";
	private static LinkedList<String> Production=new LinkedList<String>();//store Production rule
	private static LinkedList<Boolean> deriveLamda=new LinkedList<Boolean>();//store nonterminal can derive lamda or not
	private static LinkedList<String> nonterminal=new LinkedList<String>();//store nonterminal
	private static LinkedList<String> terminal=new LinkedList<String>();//store terminal
	private static LinkedList<String> firstSet=new LinkedList<String>();//store firstset
	private static Formatter output;
	
	public static void main(String args[]) throws FileNotFoundException
	{
		loadFile(InputPATH);//for file input
		//ScannerInput();//for system.in
		getNonterminal();
		deriveL();
		InitFirstSet();
		firstset();
		SortFirstSet();
		getTerminal();
		AddTerFirst();
		//Test();//print out result
		File outputfile=new File(OutPATH);
		output=new Formatter(outputfile);
		OutputFile();//output result to file
	}
	
	public static void loadFile(String InputPath) throws FileNotFoundException//input from file
	{
		Scanner input=new Scanner(new File(InputPath));
		while(input.hasNextLine())
		{
			Production.add(input.nextLine());
		}
		input.close();
	}
	
	public static void OutputFile()//output result to file
	{
		int count=0;
		for(int i=0;i<nonterminal.size();i++)
		{
			output.format("%s",nonterminal.get(i)+":");
			output.format("%s\r\n",firstSet.get(i));
			count=i;
		}
		count++;
		for(int i=count;i<count+terminal.size();i++)
		{
			output.format("%s",terminal.get(i-count)+":");
			output.format("%s\r\n",firstSet.get(i));
		}
		output.close();
	}
	
	public static void ScannerInput()//system.in for input
	{
		Scanner input=new Scanner(System.in);
		while(input.hasNextLine())
		{
			Production.add(input.nextLine());
		}
	}
	
	public static void Test()//print the result
	{
		int count=0;
		for(int i=0;i<nonterminal.size();i++)
		{
			System.out.print(nonterminal.get(i)+":");
			System.out.println(firstSet.get(i));
			count=i;
		}
		count++;
		for(int i=count;i<count+terminal.size();i++)
		{
			System.out.print(terminal.get(i-count)+":");
			System.out.println(firstSet.get(i));
		}
	}
	
	public static void deriveL()
	{
		boolean finish=false;//if any one of state change,not finish
		while(!finish)
		{
			finish=true;
			for(int count=0;count<Production.size();count++)//try for each production
			{
				boolean change=false;
				String rightPro=Production.get(count).substring(Production.get(count).indexOf(">")+1);//Production rule right side of->
				for(int count1=0;count1<rightPro.length();count1++)
				{
					if(rightPro.substring(0,1).matches("l"))//derive to lamda
					{
						change=true;
						break;
					}
					if(Character.isLowerCase(rightPro.charAt(count1)))//not derive to lamda
					{
						break;
					}
					else if(Character.isUpperCase(rightPro.charAt(count1)))
					{
						if(deriveLamda.get(nonterminal.indexOf(rightPro.substring(count1,count1+1))))//if this UpperCase letter can derive to lamda
						{
							if(count1==rightPro.length()-1)//and if this letter is the last
							{
								change=true;
							}
							else
							{
								continue;
							}
						}
						else//if this UpperCase letter can't derive to lamda
						{
							break;
						}
					}
				}
				if(change)//if some state change
				{
					if(!deriveLamda.get(nonterminal.indexOf(Production.get(count).substring(0,1))))
					{
						deriveLamda.set(nonterminal.indexOf(Production.get(count).substring(0,1)),true);
						finish=false;
					}
				}
			}
		}
	}
	
	public static void getNonterminal()
	{ 
		for(int count=0;count<Production.size();count++)
		{
			if(!nonterminal.contains(Production.get(count).substring(0,1)))//not repeat
			{
				nonterminal.add(Production.get(count).substring(0,1));
			}
		}
		
		for(int count=0;count<nonterminal.size();count++)//init nonterminal can't derive to lamda
		{
			deriveLamda.add(false);
		}
	}
	
	public static void getTerminal()
	{
		for(int count=0;count<Production.size();count++)//for all rules
		{
			for(int count1=0;count1<Production.get(count).length();count1++)//for all characters of rule
			{
				if(Character.isLowerCase(Production.get(count).charAt(count1)))//if is LowerCase
				{
					//if nt repeat and not lamda
					if(!terminal.contains(Production.get(count).substring(count1,count1+1))&&!Production.get(count).substring(count1,count1+1).matches("l"))
					{
						terminal.add(Production.get(count).substring(count1,count1+1));
					}
				}
			}
		}
		Collections.sort(terminal);
	}
	
	public static void InitFirstSet()
	{
		for(int count=0;count<nonterminal.size();count++)
		{
			firstSet.add("");
		}
	}
	
	public static void firstset()
	{
		boolean finish=true;
		do 
		{
			finish=true;
			boolean change=false;
			for(int count=0;count<Production.size();count++)//掃過每一條rule
			{
				String rightPro=Production.get(count).substring(Production.get(count).indexOf(">")+1);//取出箭頭右側
				for(int count1=0;count1<rightPro.length();count1++)//對右側node逐一掃描
				{
					if(Character.isLowerCase(rightPro.charAt(count1)))//terminal
					{
						if(!firstSet.get(nonterminal.indexOf(Production.get(count).substring(0,1))).contains(rightPro.substring(count1,count1+1)))//未包含
						{
							firstSet.set(nonterminal.indexOf(Production.get(count).substring(0,1)),firstSet.get(nonterminal.indexOf(Production.get(count).substring(0,1)))+","+rightPro.substring(count1,count1+1));
							change=true;	
						}
						break;
					}
					else//nonterminal
					{
						if(firstSet.get(nonterminal.indexOf(rightPro.substring(count1,count1+1))).matches(""))//後面first為空
						{

							break;
						}
						else//後面firstset不為空
						{
							String tmp[]=firstSet.get(nonterminal.indexOf(rightPro.substring(count1,count1+1))).split(",");//
							String tmpset=firstSet.get(nonterminal.indexOf(Production.get(count).substring(0,1)));//取出前面firstset
							for(int i=0;i<tmp.length;i++)
							{
								if(!firstSet.get(nonterminal.indexOf(Production.get(count).substring(0,1))).contains(tmp[i]))//如果不重複
								{
									if(!tmp[i].matches("l"))
									{
										tmpset+=","+tmp[i];
										change=true;
									}
								}
							}
							firstSet.set(nonterminal.indexOf(Production.get(count).substring(0,1)),tmpset);
							//Test();
							if(!deriveLamda.get(nonterminal.indexOf(rightPro.substring(count1,count1+1))))
							{
								break;
							}
							if(count1==rightPro.length()-1)
							{
								if(deriveLamda.get(nonterminal.indexOf(rightPro.substring(count1,count1+1))))
								{
									if(!firstSet.get(nonterminal.indexOf(Production.get(count).substring(0,1))).contains("l"))
									{
										firstSet.set(nonterminal.indexOf(Production.get(count).substring(0,1)),firstSet.get(nonterminal.indexOf(Production.get(count).substring(0,1)))+",l");
										change=true;
									}
										
								}
							}
						}
					}
				}
			}
			if(change)
			{
				finish=false;
			}
		} while (!finish);//直到沒變動才結束
		
		for(int count=0;count<firstSet.size();count++)
		{
			if(firstSet.get(count).substring(0,1).matches(","))//去除開頭,
			{
				
				firstSet.set(count,firstSet.get(count).substring(1));
			}
		}
	}
	
	public static void SortFirstSet()//sort element in firstSet
	{
		String tmp[];
		for(int count=0;count<firstSet.size();count++)
		{
			tmp=firstSet.get(count).split(",");
			Arrays.sort(tmp);
			String newString="";
			for(int i=0;i<tmp.length;i++)
			{
				newString+=tmp[i];
				if(i!=tmp.length-1)
				{
					newString+=",";
				}
			}
			firstSet.set(count,newString);
		}
	}
	
	private static void AddTerFirst()//add terminal to firstSet
	{	
		for(int count=0;count<terminal.size();count++)
		{
			firstSet.add(terminal.get(count));//terminal's firstset is itself,add terminal to its firstset
		}
	}
}
