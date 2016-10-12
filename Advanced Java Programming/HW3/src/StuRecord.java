import java.io.Serializable;

public class StuRecord implements Serializable,Comparable
{
	private String stuname;
	private double score1;
	private double score2;
	
	public StuRecord()
	{
		this("",0.0,0.0);
	}
	public StuRecord(String stuname,double score1,double score2)
	{
		setStuName(stuname);
		setScore1(score1);
		setScore2(score2);
	}
	
	public int compareTo(Object Record)
	{
		return stuname.compareTo(((StuRecord)Record).getStuName());
	}
	
	public void setStuName(String stuname)
	{
		this.stuname=stuname;
	}
	public String getStuName()
	{
		return stuname;
	}
	public void setScore1(double score1)
	{
		this.score1=score1;
	}
	public double getScore1()
	{
		return score1;
	}
	public void setScore2(double score2)
	{
		this.score2=score2;
	}
	public double getScore2()
	{
		return score2;
	}
}
