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
}
