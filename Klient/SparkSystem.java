package MTGPackage;
import processing.core.*;
import java.util.*;

public class SparkSystem {
	PApplet parent;
	
	  ArrayList<Spark> sparks;
	  PVector source;

	  SparkSystem(PVector l,PApplet p) 
	  	{
		parent=p;
	    source = l.get();
	    sparks = new ArrayList<Spark>();
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
		      s.run();
		      if (s.isDead()) 
		      	{
		        sparks.remove(i);
		       
		      	}
		    }
		}
	
}
