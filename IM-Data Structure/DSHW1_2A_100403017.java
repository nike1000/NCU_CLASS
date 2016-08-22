import java.util.*;

public class DSHW1_2A_100403017
{
	static Scanner input=new Scanner(System.in);
	static List <String> scanList =new LinkedList <String>();//to store the input by every char
	static List <String> inOrderList =new LinkedList <String>();//to store the inorder changed from scanlist
	static List <String> PostList =new LinkedList <String>();//to store tje postorder changed from inorder
	static String temp="";
	static int inputcount=0;//計算目前input處理到哪
	
	public static void main(String args[])
	{
		System.out.printf("Enter an inOrder:");
		String scan=input.nextLine();//scan input
		
		for(int count=0;count<scan.length();count++)
			scanList.add(String.format("%s",scan.charAt(count)));//將掃描進來的字串每個字轉成字元存入scanlist中

		
		for(int count=0;count<scanList.size();count++)
		{
			if(scanList.get(count).equals("+")||scanList.get(count).equals("-")||scanList.get(count).equals("*")||scanList.get(count).equals("/")||scanList.get(count).equals("(")||scanList.get(count).equals(")"))
			{
				if(temp!="")//if temp not empty
				{
					inOrderList.add(temp); //將這個運算子與上個運算子之間的數字加入inorderlist
					temp="";//清掉temp
				}
				inOrderList.add(scanList.get(count));//將運算子直接加入inorderlist
			}
			else
			{
				temp+=scanList.get(count);//將數字先存進temp
			}
		}
		if(temp!="")
			inOrderList.add(temp);//將最後的數字加入inorderlist

		postorder(inOrderList);//中序轉後序
		computePost(PostList);//後序求值
	}
	
	public static void postorder(List<String> list)
	{
		Stack<String> stack=new Stack<String>();
		System.out.printf("%-30s%-30s%-30s\n","InputBuffer","OperatorStack","OutputString");
		for(String token:list)
		{
			System.out.printf("%-30s%-30s%-30s\n",InputBuffer(),OperatorStack(stack),OutputString());
			if(token.equals("+")||token.equals("-")||token.equals("*")||token.equals("/")||token.equals("(")||token.equals(")"))
			{
				while(!stack.isEmpty()&&!compare(token,stack.peek()))//如果stack不是空的且目前元素的優先權小於stack頂端的優先權
				{
					PostList.add(stack.pop());//將stack頂端元素打出來加進postlist中
					if(!token.equals(")"))
					{
						inputcount--;
						System.out.printf("%-30s%-30s%-30s\n",InputBuffer(),OperatorStack(stack),OutputString());
					}
				}
				
				if(stack.empty()||!token.equals(")"))//stack是空的或元素不等於")"時
				{
					stack.push(token);//將元素加入stack
				}
				else
				{
					stack.pop();//推出元素"("
				}
			}
			else
			{
				PostList.add(token);//運算員直接加入postlist
			}
		}
		while(!stack.empty())//input掃描結束後若還有剩下元素在stack中
		{
			System.out.printf("%-30s%-30s%-30s\n",InputBuffer(),OperatorStack(stack),OutputString());
			PostList.add(stack.pop());//推出stack後加入postlist
		}
		System.out.printf("%-30s%-30s%-30s\n",InputBuffer(),OperatorStack(stack),OutputString());
	}
	
	public static boolean compare(String token,String stacktop)//設定比較優先權
	{
		int per1=-1;//元素優先權
		int per2=-1;//stack頂端的優先權
		if(token.equals("+")||token.equals("-"))
		{
			per1=1;
		}
		if(token.equals("*")||token.equals("/"))
		{
			per1=2;
		}
		if(token.equals(")"))
		{
			per1=0;
		}
		if(token.equals("("))
		{
			per1=3;
		}
		
		if(stacktop.equals("+")||stacktop.equals("-"))
		{
			per2=1;
		}
		if(stacktop.equals("*")||stacktop.equals("/"))
		{
			per2=2;
		}
		if(stacktop.equals("("))
		{
			per2=0;
		}
		if(per1==0&&per2==0)
			return true;
		if(per1>per2)
			return true;
		else
			return false;
	}
	
	public static void computePost(List<String> postlist)
	{
		Stack <String>stack=new Stack<String>();
		for(String token:postlist)
		{
			if(token.equals("+")||token.equals("-")||token.equals("*")||token.equals("/"))//若元素為運算子
			{
				String result;
				result=compute(stack.pop(),stack.pop(),token);//將stack頂端兩元素與運算子進行運算
				stack.add(result);//將運算結果推入stack
			}
			else
			{
				stack.push(token);//運算元直接推入stack
			}
		}
		System.out.printf("PostFix Evaluate:%s",stack.pop());//顯示結果
	}
	
	public static String compute(String num2,String num1,String oper)//進行運算
	{
		Double result=0.0;
		if(oper.equals("+"))
		{
			result=Double.parseDouble(num1)+Double.parseDouble(num2);
		}
		else if(oper.equals("-"))
		{
			result=Double.parseDouble(num1)-Double.parseDouble(num2);
		}
		else if(oper.equals("*"))
		{
			result=Double.parseDouble(num1)*Double.parseDouble(num2);
		}
		else if(oper.equals("/"))
		{
			result=Double.parseDouble(num1)/Double.parseDouble(num2);
		}
		
		return String.format("%s",result);//回傳結果
	}
	
	public static String InputBuffer()//回傳中序式掃描情況
	{
		String result="";
		for(int count=inputcount;count<inOrderList.size();count++)
		{
			result+=inOrderList.get(count);
		}
		inputcount++;
		return result;
	}
	public static String OperatorStack(Stack<String> stack)//回傳stack內容
	{
		String result="";
		if(stack.empty())
			return result="EMPTY";
		else
		{
			for(String stacks:stack)
			{
				result+=stacks;
			}
			return result;
		}
	}
	public static String OutputString()//回傳後序式情況
	{
		String result="";
		for(String output:PostList)
		{
			result+=output;
			result+=" ";
		}
		if(result.equals(""))
			return "EMPTY";
		else
			return result;
	}
}
