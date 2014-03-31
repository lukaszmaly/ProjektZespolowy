package MTGPackage;
import processing.core.*;

import java.util.*;

public class SparkSystem {
	PApplet parent;
	
	  ArrayList<Spark> sparks;
	  PVector source,v,a;
	  char sparkType='p';
	  int r,g,b,size,life;
	  
	  
	  SparkSystem(PVector l,PApplet p) 
	  	{
		parent=p;
	    source = l.get();
	    sparks = new ArrayList<Spark>();
	    r=0;
	    g=0;
	    b=0;
	    size=5;
	    life=45;
	    v=null;
	    a=null;
	  	}
	  	
	  public void addParticle() 
	  	{
		    sparks.add(new Spark(source,parent,a,v,life));
		}
	  
	  public void run() 
	  	{
		    for (int i = sparks.size()-1; i >= 0; i--) 
		    {
		      Spark s = sparks.get(i);
		      s.type=this.sparkType;
		      s.r=this.r;
		      s.g=this.g;
		      s.b=this.b;
		      s.size=this.size;
		      if(v!=null)  s.v=this.v.get(); 
		      if(a!=null)   s.a=this.a.get();
		      s.run();
		      if (s.isDead()) 
		      	{
		        sparks.remove(i);
		       
		      	}
		    }
		}
	
}
