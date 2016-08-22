import java.io.File;
import java.io.FileNotFoundException;
import java.util.Formatter;
import java.util.LinkedList;
import java.util.Scanner;


public class PredictSet
{
	private static String OutPATH="output.txt";
	private static String InputPATH="input.txt";
	private static LinkedList<String> Production=new LinkedList<String>();//store Production rule
	private static LinkedList<String> firstSet=new LinkedList<String>();//store firstset
	private static LinkedList<String> followSet=new LinkedList<String>();//store followset
	private static LinkedList<String> predictSet=new LinkedList<String>();//store predictset
	private static Formatter output;
	
	public static void main(String args[]) throws FileNotFoundException
	{
		loadFile(InputPATH);//for file input
		initPredictSet();
		FindPredictSet();
		ReplaceSet();
		File outputfile=new File(OutPATH);
		output=new Formatter(outputfile);
		OutputFile();//output result to file
		
	}

	private static void initPredictSet()
	{
		for(int count=0;count<Production.size();count++)
		{
			predictSet.add("");
		}
	}

	private static void FindPredictSet() 
	{
		for(int Rulecount=0;Rulecount<Production.size();Rulecount++)
		{
			String RHS=Production.get(Rulecount).substring(Production.get(Rulecount).indexOf(">")+1);//get RHS of Production
			for(int RHScount=0;RHScount<RHS.length();RHScount++)
			{
				if(Character.isLowerCase(RHS.charAt(RHScount)))//is terminal
				{
					if(String.valueOf(RHS.charAt(RHScount)).matches("l"))//is lamda
					{
						String Foltarget=Production.get(Rulecount).substring(0,1);//get LHS of Production
						String temp="";
						for(int count=0;count<followSet.size();count++)//find the LHS's followSet
						{
							if(followSet.get(count).contains(Foltarget))
							{
								temp=followSet.get(count).substring(followSet.get(count).indexOf(":")+1);//get its followSet
								break;
							}
						}
						
						predictSet.set(Rulecount,predictSet.get(Rulecount)+temp+",");//add to predictSet
						break;
					}
					predictSet.set(Rulecount,predictSet.get(Rulecount)+String.valueOf(RHS.charAt(RHScount)+","));
					//if not lamda,add this terminal to perdictSet
					break;
				}
				else//nonterinal
				{
					String Firtarget=String.valueOf(RHS.charAt(RHScount));//get the LHS of Production
					int index=0;
					for(int count=0;count<firstSet.size();count++)//find index of its followSet
					{
						if(firstSet.get(count).contains(Firtarget))
						{
							index=count;//get index of its followSet
							break;
						}
					}
					
					String firstRHS=firstSet.get(index).substring(firstSet.get(index).indexOf(":")+1);//add to predictSet
					
					if(firstSet.get(index).contains("l"))//if firstSet contain lamda
					{
						
						String firstRight[]=firstRHS.split(",");
						String tmp="";
						for(int count=0;count<firstRight.length;count++)//add first to predictSet without lamda
						{
							if(!firstRight[count].matches("l"))
							{
								tmp+=firstRight[count]+",";
							}
						}
						predictSet.set(Rulecount,predictSet.get(Rulecount)+tmp);
						
						if(RHScount==RHS.length()-1)//if at the end of Production
						{
							String Foltarget=Production.get(Rulecount).substring(0,1);//get the LHS of Production
							String temp="";
							for(int count=0;count<followSet.size();count++)//add its followSet to predictSet
							{
								if(followSet.get(count).contains(Foltarget))
								{
									temp=followSet.get(count).substring(followSet.get(count).indexOf(":")+1);
								}
							}
							
							predictSet.set(Rulecount,predictSet.get(Rulecount)+temp+",");
							break;
						}
						continue;
					}
					else//if firstSet not contain lamda
					{
						predictSet.set(Rulecount,predictSet.get(Rulecount)+firstRHS+",");
						break;
					}
				}
			}
		}
	}
	
	public static void ReplaceSet()
	{
		for(int count=0;count<predictSet.size();count++)
		{
			String tmp=predictSet.get(count);
			predictSet.set(count,tmp.substring(0,tmp.length()-1));
		}
	}

	public static void loadFile(String InputPath) throws FileNotFoundException//input from file
	{
		Scanner input=new Scanner(new File(InputPath));
		while(input.hasNextLine())
		{
			String tmp=input.nextLine();
			if(tmp.matches(""))
			{
				break;
			}
			
			Production.add(tmp);
		}
		
		while(input.hasNextLine())
		{
			String tmp=input.nextLine();
			if(tmp.matches(""))
			{
				break;
			}
			
			firstSet.add(tmp);
		}
		
		while(input.hasNextLine())
		{
			String tmp=input.nextLine();
			if(tmp.matches(""))
			{
				break;
			}
			
			followSet.add(tmp);
		}
		input.close();
	}
	
	private static void OutputFile() 
	{
		for(int count=0;count<predictSet.size();count++)
		{
			output.format("%s:",Production.get(count));
			output.format("%s\r\n",predictSet.get(count));
		}
		output.close();
	}
}
