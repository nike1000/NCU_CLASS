import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import javax.swing.JOptionPane;

public class Algo
{
	public Set<Point> pointSet = new HashSet<Point>();//use set to store point,no repeat
	public Point pointArray[];
	public ArrayList<Point> convexArrayList=new ArrayList<Point>();

	private Random random = new Random();
	private int n;
	private int x1, y1, x2, y2;
	private double a, b;
	private double pivot;
	private boolean left = false;
	private boolean right = false;

	public void ProductPoint(int n)
	{
		int x;
		int y;
		while (pointSet.size() != n)
		{
			x = 4*(random.nextInt(101)+2);//4 times big to let it look well,not to closed
			y = 4*(random.nextInt(101)+2);//2 times shift because of the point size is 4
			pointSet.add(new Point(x, y));
		}

		pointArray = new Point[pointSet.size()];
		pointSet.toArray(pointArray);
	}

	public void ConvexAlgo()
	{
		for (int i = 0; i < pointArray.length - 1; i++)
		{
			for (int j = i + 1; j < pointArray.length; j++)
			{
				x1 = pointArray[i].x;
				y1 = pointArray[i].y;
				x2 = pointArray[j].x;
				y2 = pointArray[j].y;
				left = false;
				right = false;

				if (x2 == x1)
				{
					pivot = x2;
					for (int k = 0; k < pointArray.length; k++)
					{
						if (k == i || k == j)
						{
							continue;
						}

						if (pointArray[k].x > pivot)
						{
							right = true;
						}
						else
						{
							left = true;
						}
					}
				}
				else if (y2 == y1)
				{
					pivot = y2;
					for (int k = 0; k < pointArray.length; k++)
					{
						if (k == i || k == j)
						{
							continue;
						}

						if (pointArray[k].y > pivot)
						{
							right = true;
						}
						else
						{
							left = true;
						}
					}
				}
				else
				{
					a = (double)(y2 - y1) / (x2 - x1);
					b = (double)(x2 * y1 - x1 * y2) / (x2 - x1);
					for (int k = 0; k < pointArray.length; k++)
					{
						if (k == i || k == j)
						{
							continue;
						}

						if (a*pointArray[k].x+b-pointArray[k].y > 0)
						{
							right = true;
						}
						else
						{
							left = true;
						}
					}
				}
				if(!(left&&right))//both true means there are points in both left and right side
				{
					convexArrayList.add(new Point(x1,y1));
					convexArrayList.add(new Point(x2,y2));
				}
			}
		}
	}
}
