import java.util.*;

public class DSHW1_2A_100403017
{
	static Scanner input=new Scanner(System.in);
	static List <String> scanList =new LinkedList <String>();//to store the input by every char
	static List <String> inOrderList =new LinkedList <String>();//to store the inorder changed from scanlist
	static List <String> PostList =new LinkedList <String>();//to store tje postorder changed from inorder
	static String temp="";
	static int inputcount=0;//�p��ثeinput�B�z���
	
	public static void main(String args[])
	{
		System.out.printf("Enter an inOrder:");
		String scan=input.nextLine();//scan input
		
		for(int count=0;count<scan.length();count++)
			scanList.add(String.format("%s",scan.charAt(count)));//�N���y�i�Ӫ��r��C�Ӧr�ন�r���s�Jscanlist��

		
		for(int count=0;count<scanList.size();count++)
		{
			if(scanList.get(count).equals("+")||scanList.get(count).equals("-")||scanList.get(count).equals("*")||scanList.get(count).equals("/")||scanList.get(count).equals("(")||scanList.get(count).equals(")"))
			{
				if(temp!="")//if temp not empty
				{
					inOrderList.add(temp); //�N�o�ӹB��l�P�W�ӹB��l�������Ʀr�[�Jinorderlist
					temp="";//�M��temp
				}
				inOrderList.add(scanList.get(count));//�N�B��l�����[�Jinorderlist
			}
			else
			{
				temp+=scanList.get(count);//�N�Ʀr���s�itemp
			}
		}
		if(temp!="")
			inOrderList.add(temp);//�N�̫᪺�Ʀr�[�Jinorderlist

		postorder(inOrderList);//��������
		computePost(PostList);//��ǨD��
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
				while(!stack.isEmpty()&&!compare(token,stack.peek()))//�p�Gstack���O�Ū��B�ثe�������u���v�p��stack���ݪ��u���v
				{
					PostList.add(stack.pop());//�Nstack���ݤ������X�ӥ[�ipostlist��
					if(!token.equals(")"))
					{
						inputcount--;
						System.out.printf("%-30s%-30s%-30s\n",InputBuffer(),OperatorStack(stack),OutputString());
					}
				}
				
				if(stack.empty()||!token.equals(")"))//stack�O�Ū��Τ���������")"��
				{
					stack.push(token);//�N�����[�Jstack
				}
				else
				{
					stack.pop();//���X����"("
				}
			}
			else
			{
				PostList.add(token);//�B��������[�Jpostlist
			}
		}
		while(!stack.empty())//input���y������Y�٦��ѤU�����bstack��
		{
			System.out.printf("%-30s%-30s%-30s\n",InputBuffer(),OperatorStack(stack),OutputString());
			PostList.add(stack.pop());//���Xstack��[�Jpostlist
		}
		System.out.printf("%-30s%-30s%-30s\n",InputBuffer(),OperatorStack(stack),OutputString());
	}
	
	public static boolean compare(String token,String stacktop)//�]�w����u���v
	{
		int per1=-1;//�����u���v
		int per2=-1;//stack���ݪ��u���v
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
			if(token.equals("+")||token.equals("-")||token.equals("*")||token.equals("/"))//�Y�������B��l
			{
				String result;
				result=compute(stack.pop(),stack.pop(),token);//�Nstack���ݨ⤸���P�B��l�i��B��
				stack.add(result);//�N�B�⵲�G���Jstack
			}
			else
			{
				stack.push(token);//�B�⤸�������Jstack
			}
		}
		System.out.printf("PostFix Evaluate:%s",stack.pop());//��ܵ��G
	}
	
	public static String compute(String num2,String num1,String oper)//�i��B��
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
		
		return String.format("%s",result);//�^�ǵ��G
	}
	
	public static String InputBuffer()//�^�Ǥ��Ǧ����y���p
	{
		String result="";
		for(int count=inputcount;count<inOrderList.size();count++)
		{
			result+=inOrderList.get(count);
		}
		inputcount++;
		return result;
	}
	public static String OperatorStack(Stack<String> stack)//�^��stack���e
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
	public static String OutputString()//�^�ǫ�Ǧ����p
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
