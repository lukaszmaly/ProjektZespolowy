package MTGPackage;
import processing.core.*;
import java.util.*;

public class SparkSystem {
	PApplet parent;
	
	  ArrayList<Spark> sparks;
	  PVector source;
	  char sparkType='p';
	  int r,g,b,size;
	  
	  SparkSystem(PVector l,PApplet p) 
	  	{
		parent=p;
	    source = l.get();
	    sparks = new ArrayList<Spark>();
	    r=0;
	    g=0;
	    b=0;
	    size=5;
	  	}
	
	  public void addParticle() 
	  	{
		    sparks.add(new Spark(source,parent));
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
		      s.run();
		      if (s.isDead()) 
		      	{
		        sparks.remove(i);
		       
		      	}
		    }
		}
	
}
