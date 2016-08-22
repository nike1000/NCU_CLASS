import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

import javax.swing.JFileChooser;

public class Pipeline
{
	private static boolean debug=false;
	private static Scanner input;//input file
	private static List <Instruction> cmdlist=null;//store data as string
	private static Map<String,String> RtypeMap=new HashMap<String,String>();
	private static Map<String,String> ItypeMap=new HashMap<String,String>();
	private static int memory[]=new int[5];
	private static int register[]=new int[9];
	
	public static void main(String args[])
	{	
		initMap();
		initMem();
		initReg();
		//load file ,store instruction in my own object
		try
		{
			loadFile();
		}
		catch (FileNotFoundException | NullPointerException | NoSuchElementException | IllegalStateException e)
		{
			e.printStackTrace();
		}
		
		//convert instruction that people can read
		for(int count=0;count<cmdlist.size();count++)
		{
			Instruction cmd=cmdlist.get(count);
			if(cmd.getType().equals("R"))
			{
				String instruction=RtypeMap.get(cmd.getFunct());
				int rs=Integer.parseInt(cmd.getRs(),2);
				int rt=Integer.parseInt(cmd.getRt(),2);
				int rd=Integer.parseInt(cmd.getRd(),2);
				
				System.out.printf("%-5s $%d, $%d, $%d\n",instruction,rd,rs,rt);
			}
			else if(cmd.getType().equals("I"))
			{
				String instruction=ItypeMap.get(cmd.getOpcode());
				int rs=Integer.parseInt(cmd.getRs(),2);
				int rt=Integer.parseInt(cmd.getRt(),2);
				int immediate=Integer.parseInt(cmd.getImmediate(),2);
				String immediate_hex=String.format("0x%2s",Integer.toString(immediate,16)).replace(' ','0');
				
				if(instruction.equals("beq"))//beq
				{
					
					System.out.printf("%-5s $%d, $%d, %s\n",instruction,rs,rt,immediate_hex);
				}
				else//lw sw
				{
					System.out.printf("%-5s $%d, %s($%d)\n",instruction,rt,immediate_hex,rs);
				}
			}
			else//J-type,not in project this time
			{
				
			}
		}
		
		//starting pipeline
		pipelineProcess();
	}
	
	public static void initMap()//binary opcode to String mapping
	{
		RtypeMap.put("100000","add");
		RtypeMap.put("100010","sub");
		RtypeMap.put("100100","and");
		RtypeMap.put("100101","or");
		RtypeMap.put("101010","slt");
		ItypeMap.put("100011","lw");
		ItypeMap.put("101011","sw");
		ItypeMap.put("000100","beq");
	}
	
	public static void initMem()
	{
		memory[0]=9;
		memory[1]=6;
		memory[2]=4;
		memory[3]=2;
		memory[4]=5;
	}
	
	public static void initReg()
	{
		register[0]=0;
		register[1]=5;
		register[2]=8;
		register[3]=6;
		register[4]=7;
		register[5]=5;
		register[6]=1;
		register[7]=2;
		register[8]=4;
	}
	
	private static void loadFile() throws FileNotFoundException,NullPointerException,NoSuchElementException,IllegalStateException
	{
		JFileChooser filechooser=new JFileChooser();
		filechooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		filechooser.showOpenDialog(null);
		cmdlist=new LinkedList<Instruction>();
		input=new Scanner(new File(filechooser.getSelectedFile().getPath()));//input file

		while(input.hasNextLine())
		{
			cmdlist.add(checkCmdType(input.nextLine()));//add instruction to cmdlist line by line
		}//end while
	}//end loadFile
	
	public static Instruction checkCmdType(String instruction)//split the string ,return object that store instruction info
	{
		String opcode=instruction.substring(0,6);
		
		if(debug)
			System.out.printf("%s ",opcode);
		
		if(opcode.equals("000000"))//R-type
		{
			String rs=instruction.substring(6,11);
			String rt=instruction.substring(11,16);
			String rd=instruction.substring(16,21);
			String shamt=instruction.substring(21, 26);
			String funct=instruction.substring(26,32);
			
			if(debug)
				System.out.printf("%s %s %s %s %s\n",rs,rt,rd,shamt,funct);
			
			return new Rtype(instruction,"R",opcode,rs,rt,rd,shamt,funct);
		}
		else if(opcode.equals(000010)||opcode.equals(000011))//J-type
		{
			String target=instruction.substring(6,32);
			return new Jtype(instruction,"J",opcode,target);
		}
		else//I-type
		{
			String rs=instruction.substring(6,11);
			String rt=instruction.substring(11,16);
			String immediate=instruction.substring(16,32);
			
			if(debug)
				System.out.printf("%s %s %s\n",rs,rt,immediate);
			
			return new Itype(instruction,"I",opcode,rs,rt,immediate);
		}
	}
	
	public static void pipelineProcess()
	{
		//這邊把每個pipe當做一個stage，所以總共是4個，最前面那個指令會從cmdlist抓，還沒抓時回會放在pipe中(IF/ID)
		//前面stage的rs,rt等直接從指令抓
		Instruction pathInstr[]=new Instruction[4];//放目前各stage的instruction
		String ctrlSignal[]={"000000000","000000000","000000000","000000000"};//其實只有後三個stage會用到，但方便對應，還是宣告為4
		String aluResult[]={"0","0","0","0"};//其實只有後兩個stage會用到
		String dataMemory[]=new String[4];//其實只有最後的stage會用到
		int ALURs=0,ALURt=0;
		int pc=0,CC=1;//program counter and clock cycle
		
		/*
		 * *ctrlSignal每個資料是9位數的二進位,由左到右表示
		 * 0  Regdst
		 * 1  ALUOp1
		 * 2  ALUOp2
		 * 3  ALUSrc
		 * 4  Branch
		 * 5  Memread
		 * 6  Memwrite
		 * 7  Regwrite
		 * 8  MemtoReg
		 */
		boolean EOF=false;
		while(true)
		{
			if(EOF)
			{
				break;
			}
			
			//MEM/WB--------------------------------------------------------
			if(ctrlSignal[3].substring(7,8).equals("1"))//Regwrite
			{
				int MEMWBrd;
				if(ctrlSignal[3].substring(8,9).equals("1"))//MemtoReg,lw
				{
					MEMWBrd=Integer.parseInt(pathInstr[3].getRt(),2);
					register[MEMWBrd]=Integer.parseInt(dataMemory[3]);//將memory的資料writeback
				}
				else
				{
					MEMWBrd=Integer.parseInt(pathInstr[3].getRd(),2);
					register[MEMWBrd]=Integer.parseInt(aluResult[3]);//將ALU計算的資料writeback
				}
				
				if(MEMWBrd==0)//$0永遠為0
				{
					register[0]=0;
				}
				else//檢查datahazard
				{
					if(pathInstr[1]!=null)
					{
						int IDEXrs=Integer.parseInt(pathInstr[1].getRs(),2);
						int IDEXrt=Integer.parseInt(pathInstr[1].getRt(),2);
						
						if(!pathInstr[1].getOpcode().equals("100011"))//除lw外都可能有rt hazard
						{
							if(IDEXrt==MEMWBrd)
							{
								ALURt=register[MEMWBrd];
							}
						}
						
						if(IDEXrs==MEMWBrd)//rs hazard
						{
							ALURs=register[MEMWBrd];
						}
					}
				}
			}
			
			pathInstr[3]=null;
			ctrlSignal[3]="000000000";
			aluResult[3]="0";
			dataMemory[3]="000000000";
			//EX/MEM-------------------------------------------------------
			
			if(ctrlSignal[2].substring(6,7).equals("1"))//Memwrite sw
			{
				int memAddress=Integer.parseInt(aluResult[2])/4;
				int index=Integer.parseInt(pathInstr[2].getRt(),2);
				memory[memAddress]=register[index];
			}
			else if(ctrlSignal[2].substring(5,6).equals("1"))//Memread
			{
				int memAddress=Integer.parseInt(aluResult[2])/4;
				dataMemory[3]=Integer.toString(memory[memAddress]);
			}
			else//直接來自ALU
			{
				aluResult[3]=aluResult[2];
			}
			
			//處理hazard
			if(ctrlSignal[2].substring(7,8).equals("1"))//EX/MEM.RegWrite
			{
				int EXMEMrd=0;
				if(pathInstr[1]!=null)
				{
					int IDEXrs=Integer.parseInt(pathInstr[1].getRs(),2);//ID/EX的rs
					int IDEXrt=Integer.parseInt(pathInstr[1].getRt(),2);//ID/EX的rt
					
					if(pathInstr[2].getType().equals("R"))//取得rd
					{
						EXMEMrd=Integer.parseInt(pathInstr[2].getRd(),2);
					}
					else if(pathInstr[2].getOpcode().equals("100011"))//lw
					{
						EXMEMrd=Integer.parseInt(pathInstr[2].getRt(),2);
					}
					
					if(EXMEMrd!=0)
					{
						if(!pathInstr[1].getOpcode().equals("100011"))//ID/EX stage除lw外都可能有rt hazard
						{
							if(IDEXrt==EXMEMrd)//有hazard
							{
								//add、sub、and、or、slt和lw都拉回去
								if(pathInstr[2].getOpcode().equals("100011"))//EX/MEM stage指令是lw
								{
									ALURt=Integer.parseInt(dataMemory[3]);//指令lw,資料從memory來
								}
								else
								{
									ALURt=Integer.parseInt(aluResult[3]);
								}
							}
						}
						
						if(IDEXrs==EXMEMrd)//ID/EX stage都可能有rs hazard
						{
							if(pathInstr[2].getOpcode().equals("100011"))//EX/MEM stage指令是lw
							{
								ALURs=Integer.parseInt(dataMemory[3]);//指令lw,資料從memory來
							}
							else
							{
								ALURs=Integer.parseInt(aluResult[3]);
							}
						}
					}
				}
				
			}
			
			pathInstr[3]=pathInstr[2];
			ctrlSignal[3]=ctrlSignal[2];
			pathInstr[2]=null;
			ctrlSignal[2]="000000000";
			aluResult[2]="0";
			//ID/EX---------------------------------------------------------------
			
			if(pathInstr[1]!=null)
			{
				if(pathInstr[1].getType().equals("R"))
				{
					if(RtypeMap.get(pathInstr[1].getFunct()).equals("add"))
					{
						aluResult[2]=Integer.toString(ALURs+ALURt);
					}
					if(RtypeMap.get(pathInstr[1].getFunct()).equals("sub"))
					{
						aluResult[2]=Integer.toString(ALURs-ALURt);
					}
					if(RtypeMap.get(pathInstr[1].getFunct()).equals("and"))
					{
						int result=Integer.parseInt(Integer.toBinaryString(ALURs),2)&Integer.parseInt(Integer.toBinaryString(ALURt),2);
						aluResult[2]=Integer.toString(result);
					}
					if(RtypeMap.get(pathInstr[1].getFunct()).equals("or"))
					{
						int result=Integer.parseInt(Integer.toBinaryString(ALURs),2)|Integer.parseInt(Integer.toBinaryString(ALURt),2);
						aluResult[2]=Integer.toString(result);
					}
					if(RtypeMap.get(pathInstr[1].getFunct()).equals("slt"))
					{
						if(ALURs<ALURt)
						{
							aluResult[2]="0";
						}
						else
						{
							aluResult[2]="1";
						}
					}
				}
				else if(pathInstr[1].getType().equals("I"))
				{
					if(ItypeMap.get(pathInstr[1].getOpcode()).equals("beq"))
					{
						if(pathInstr[1].getRs().equals(pathInstr[1].getRt()))
						{
							pc+=Integer.parseInt(pathInstr[1].getImmediate(),2);
						}
					}
					else//lw sw直接計算位置
					{
						aluResult[2]=Integer.toString(ALURs+Integer.parseInt(pathInstr[1].getImmediate(),2));
					}
				}
				else//j type
				{
					
				}
			}
			
			//處理stall
			if(ctrlSignal[1].substring(5,6).equals("1"))//memRead
			{
				
				if(pathInstr[0]!=null)
				{
					int IDEXRt=Integer.parseInt(pathInstr[1].getRt(),2);
					int IFIDRs=Integer.parseInt(pathInstr[0].getRs(),2);
					int IFIDRt=Integer.parseInt(pathInstr[0].getRt(),2);
					
					if(IDEXRt==IFIDRs||IDEXRt==IFIDRt)//stall
					{
						ctrlSignal[1]="000000000";
						continue;
					}
				}
			}
			
			ALURs=0;ALURt=0;
			pathInstr[2]=pathInstr[1];
			ctrlSignal[2]=ctrlSignal[1];
			ctrlSignal[1]="000000000";
			pathInstr[1]=null;
			aluResult[1]="0";
			
			//IF/ID---------------------------------------------------------------
			
			if(pathInstr[0]!=null)
			{
				ALURs=register[Integer.parseInt(pathInstr[0].getRs(),2)];
				ALURt=register[Integer.parseInt(pathInstr[0].getRt(),2)];
				
				//產生control signal
				if(pathInstr[0].getType().equals("R"))
				{
					ctrlSignal[1]="110000010";
				}
				else if(pathInstr[0].getType().equals("I"))
				{
					if(ItypeMap.get(pathInstr[0].getOpcode()).equals("lw"))
					{
						ctrlSignal[1]="000101011";
					}
					if(ItypeMap.get(pathInstr[0].getOpcode()).equals("sw"))
					{
						ctrlSignal[1]="000100100";
						
					}
					if(ItypeMap.get(pathInstr[0].getOpcode()).equals("beq"))
					{
						ctrlSignal[1]="001010000";
					}
				}
				else
				{
					
				}
				
				pathInstr[1]=pathInstr[0];
				
				if(pathInstr[0].getOpcode().equals("000100"))//處理beq
				{
					int IFIDRs=Integer.parseInt(pathInstr[0].getRs(),2);
					int IFIDRt=Integer.parseInt(pathInstr[0].getRt(),2);
					if(register[IFIDRs]==register[IFIDRt])//如果要branch
					{
						pc+=Integer.parseInt(pathInstr[0].getImmediate(),2);
						pathInstr[0]=null;
						continue;
					}
				}
			}
			
			pathInstr[1]=pathInstr[0];
			
			//--------------------------------------------------------------------
			if(pc<cmdlist.size())
			{
				pathInstr[0]=cmdlist.get(pc);
			}
			else
			{
				pathInstr[0]=null;
			}
			pc++;
			
			
			System.out.printf("CC:%d\n\n",CC++);
			
			System.out.printf("Register:\n");
			System.out.printf("$0: %d\t$1: %d\t$2: %d\n",register[0],register[1],register[2]);
			System.out.printf("$3: %d\t$4: %d\t$5: %d\n",register[3],register[4],register[5]);
			System.out.printf("$6: %d\t$7: %d\t$8: %d\n\n",register[6],register[7],register[8]);
			
			System.out.printf("Data Memory:\n");
			System.out.printf("00:\t%d\n",memory[0]);
			System.out.printf("04:\t%d\n",memory[1]);
			System.out.printf("08:\t%d\n",memory[2]);
			System.out.printf("12:\t%d\n",memory[3]);
			System.out.printf("16:\t%d\n\n",memory[4]);
			
			System.out.printf("IF/ID :\n");
			System.out.printf("%-20s%d\n","PC",pc*4);
			System.out.printf("%-20s%s\n\n","Instruction",pathInstr[0]==null?"00000000000000000000000000000000":pathInstr[0].getCode());
			
			System.out.printf("ID/EX :\n");
			System.out.printf("%-20s%d\n","ReadData1",ALURs);
			System.out.printf("%-20s%d\n","ReadData2",ALURt);
			if(pathInstr[1]==null)
			{
				System.out.printf("%-20s%d\n","sign_ext",0);
			}
			else
			{
				System.out.printf("%-20s%d\n","sign_ext",pathInstr[1].getType().equals("I")?Integer.parseInt(pathInstr[1].getImmediate(),2):0);
			}
			
			System.out.printf("%-20s%d\n","Rs",pathInstr[1]==null?0:Integer.parseInt(pathInstr[1].getRs(),2));
			System.out.printf("%-20s%d\n","Rt",pathInstr[1]==null?0:Integer.parseInt(pathInstr[1].getRt(),2));
			if(pathInstr[1]!=null&&pathInstr[1].getRd()==null)
			{
				System.out.printf("%-20s%d\n","Rd",0);
			}
			else
			{
				System.out.printf("%-20s%d\n","Rd",pathInstr[1]==null?0:Integer.parseInt(pathInstr[1].getRd(),2));
			}
			System.out.printf("Control signals\t%s\n\n",ctrlSignal[1]);
			
			System.out.printf("EX/MEM :\n");
			System.out.printf("%-20s%s\n","ALUOut",aluResult[2]);
			System.out.printf("%-20s%d\n","WriteData",pathInstr[2]==null?0:register[Integer.parseInt(pathInstr[2].getRt(),2)]);
			System.out.printf("%-20s%d\n","Rt",pathInstr[2]==null?0:Integer.parseInt(pathInstr[2].getRt(),2));
			if(ctrlSignal[2]==null)
			{
				System.out.printf("%-20s%s\n\n","Control signals","00000");
			}
			else
			{
				System.out.printf("%-20s%s\n\n","Control signals",ctrlSignal[2].substring(4,9));
			}
			
			System.out.printf("MEM/WB :\n");
			if(ctrlSignal[3]==null)
			{
				System.out.printf("%-20s%d\n","ReadData",0);
				System.out.printf("%-20s%d\n","ALUOut",0);
				System.out.printf("%-20s%s\n\n","Control signals","00");
			}
			else
			{
				System.out.printf("%-20s%s\n","ReadData",ctrlSignal[3].substring(5,6).equals("1")?dataMemory[3]:0);
				System.out.printf("%-20s%s\n","ALUOut",ctrlSignal[3].substring(6,7).equals("1")?0:aluResult[3]);
				System.out.printf("%-20s%s\n\n","Control signals",ctrlSignal[3].substring(7,9));
			}
			
			System.out.printf("=============================================================\n");
			
			if(pathInstr[0]==null&&pathInstr[1]==null&&pathInstr[2]==null&&pathInstr[3]==null)
			{
				EOF=true;
			}
		}
	}
}
