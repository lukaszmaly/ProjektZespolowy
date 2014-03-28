package MTGPackage;
import processing.core.*;


public class Effect 
{
	public enum Type{START, BOLT, SPARKLES, FRAME, ARROW}
	
	PApplet parent;
	int life;
	Type type;
	PImage img;
	PVector v1, v2;
	int blockId;
	int arrowType=0;
	
	Effect(PApplet p,Type t,int life,PVector v1,PVector v2)
	{
		parent=p;
		this.v1=v1;
		this.v2=v2;
		this.life=life;
		this.type=t;
		if(t==Type.ARROW)
		{
			img=parent.loadImage("arrows_up.png");
		}
	}
	
	public void drawEffect()
	{
		if(this.type==Type.ARROW)
		{
			if(arrowType==1) img=parent.loadImage("arrows_down.png");
	    float d=parent.dist(v1.x, v1.y, v2.x, v2.y);
	    if(life>0) life--;
	    
	    int h=(int)d;
	    int w=(int)(d/5);
	    if(w>80) w=80;
	    float sin=(parent.abs(v1.x-v2.x))/d;
	    float asin=parent.asin(sin);
	    parent.println(asin);
	    parent.stroke(0,0,255);
	    //parent.line(v1.x, v1.y, v2.x, v2.y);
	    
	    
	    parent.pushMatrix();
	    parent.translate(v2.x, v2.y);
	  //  parent.rotate(parent.radians(135));
	 if(v1.x<=v2.x && v1.y<=v2.y)  
	 {
		 parent.rotate(parent.PI-asin);
	  
	    parent.image(img,-w/2,0,w,d);
	 }
	 else
		 if(v1.x<=v2.x && v1.y>=v2.y)  
		 {
			 parent.rotate(asin);
		  
		    parent.image(img,-w/2,0,w,d);
		 } 
		 else
			 if(v1.x>=v2.x && v1.y>=v2.y)  
			 {
				 parent.rotate(-asin);
			  
			    parent.image(img,-w/2,0,w,d);
			 }
			 else
				 if(v1.x>=v2.x && v1.y<=v2.y)  
				 {
					 parent.rotate(parent.PI+asin);
				  
				    parent.image(img,-w/2,0,w,d);
				 }
	    parent.popMatrix();

	}
	}
	
}

