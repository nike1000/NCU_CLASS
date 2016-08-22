
public class Rtype extends Instruction
{
	private String opcode;//6
	private String rs;//5
	private String rt;//5
	private String rd;//5
	private String shamt;//5
	private String funct;//6
	
	public Rtype(String code,String type,String opcode,String rs,String rt,String rd,String shamt,String funct)
	{
		super.setCode(code);
		super.setType(type);
		this.opcode=opcode;
		this.rs=rs;
		this.rt=rt;
		this.rd=rd;
		this.shamt=shamt;
		this.funct=funct;
	}
	
	public void setOpcode(String opcode)
	{
		this.opcode=opcode;
	}
	
	public String getOpcode()
	{
		return opcode;
	}
	
	public void setRs(String rs)
	{
		this.rs=rs;
	}
	
	public String getRs()
	{
		return rs;
	}
	
	public void setRt(String rt)
	{
		this.rt=rt;
	}
	
	public String getRt()
	{
		return rt;
	}
	
	public void setRd(String rd)
	{
		this.rd=rd;
	}
	
	public String getRd()
	{
		return rd;
	}
	
	public void setShamt(String shamt)
	{
		this.shamt=shamt;
	}
	
	public String getShamt()
	{
		return shamt;
	}
	
	public void setFunct(String funct)
	{
		this.funct=funct;
	}
	
	public String getFunct()
	{
		return funct;
	}
}
