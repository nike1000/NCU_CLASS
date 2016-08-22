
public class Itype extends Instruction
{
	private String opcode;//6
	private String rs;//5
	private String rt;//5
	private String immediate;//16
	
	public Itype(String code,String type,String opcode,String rs,String rt,String immediate)
	{
		super.setCode(code);
		super.setType(type);
		this.opcode=opcode;
		this.rs=rs;
		this.rt=rt;
		this.immediate=immediate;
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
	
	public void setImmediate(String immediate)
	{
		this.immediate=immediate;
	}
	
	public String getImmediate()
	{
		return immediate;
	}
}
