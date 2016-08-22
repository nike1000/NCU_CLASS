
public class Jtype extends Instruction
{
	private String opcode;//6
	private String target;//26
	
	public Jtype(String code,String type,String opcode,String target)
	{
		super.setCode(code);
		super.setType(type);
		this.opcode=opcode;
		this.target=target;	
	}
	
	public void setOpcode(String opcode)
	{
		this.opcode=opcode;
	}
	
	public String getOpcode()
	{
		return opcode;
	}
	
	public void setTarget(String target)
	{
		this.target=target;
	}
	
	public String getTarget()
	{
		return target;
	}
}
