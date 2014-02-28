package MTGPackage;
import processing.core.*;
import java.util.*;

public class SparkEdge
{
	PApplet parent;
	ArrayList<SparkSystem> ss;
	SparkEdge(int[][] T,PApplet p)
	{
		parent=p;
		this.ss=new ArrayList<SparkSystem>(); 
		int i;
		for(i=0;i<T.length;i++)
			{
			this.ss.add(new SparkSystem(new PVector(T[i][0],T[i][1]),parent)); 
			}
	}
	
	public void changeSparkType(char c,int r, int g, int b,int siz,PVector v,PVector a)
	{
		int i;
		for(i=0;i<ss.size();i++)
			{
			SparkSystem s=ss.get(i);
			s.sparkType=c;
			s.r=r;
			s.g=g;
			s.b=b;
			s.size=siz;
			if(v!=null) s.v=v.get();
			if(a!=null) s.a=a.get();
	}
}
}
