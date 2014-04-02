package MTGPackage;
import java.util.ArrayList;

import processing.core.*;


public class Effect 
{
	public enum Type{START, BOLT, SPARKLES, FRAME, ARROW}
	
	PApplet parent;
	int life;
	Type type;
	PImage img;
	PImage l1,l2,l3,l4;
	PVector v1, v2;
	int blockId;
	int arrowType=0;
	int cardId;
	int cardWidth, cardHeight;
	ArrayList<Card> Cards;
	
	Effect(PApplet p,Type t,int life)
	{
		parent=p;
		this.life=life;
		this.type=t;
		
	}
	
	
	Effect(PApplet p,Type t,int life,PVector v1,PVector v2)
	{
		parent=p;
		this.v1=v1;
		this.v2=v2;
		this.life=life;
		this.type=t;
		this.cardHeight=cardHeight;
		this.cardWidth=cardWidth;
		if(t==Type.ARROW)
		{
			img=parent.loadImage("arrows_up.png");
		}
		
	}
	
	Effect(PApplet p,Type t,int life,int cardID,int cardWidth, int cardHeight,ArrayList<Card> c)
	{
		parent=p;
		this.v1=v1;
		this.v2=v2;
		this.life=life;
		this.type=t;
		this.cardId=cardID;
		this.cardHeight=cardHeight;
		this.cardWidth=cardWidth;
		this.Cards=c;
	
		if(t==Type.BOLT)
		{
			l1=parent.loadImage("lightning1.png");
			l2=parent.loadImage("lightning2.png");
			l3=parent.loadImage("lightning3.png");
			l4=parent.loadImage("lightning4.png");
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
	   
	    
	    
	    parent.pushMatrix();
	    parent.translate(v2.x, v2.y);
	 
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
		else
		if(this.type==Type.BOLT)
		{

			Card c;
			for (int i = 0; i < Cards.size(); i++) {
				c = Cards.get(i);
				
				if (c.id == cardId) {
				
					
					
				
			
			
		
		    if(life>0) life--;
		   
			parent.pushMatrix();
			
			parent.translate(c.loc[0].x,c.loc[0].y);
			switch(c.direction)
			{
			case 1:
				parent.rotate(c.asin);
				break;
				
			case 2:
				parent.rotate(-c.asin+parent.PI);
				break;
				
			case 3:
				parent.rotate(c.asin+parent.PI);
				break;
				
			case 4:
				parent.rotate(-c.asin);
				break;
				
			default: break;
			
			}
			
			
	
			if((int)(life/2)%4==0)
			parent.image(l1, -cardWidth/2, -cardHeight/2,cardWidth*2f,cardHeight*2f);
			else
				if((int)(life/2)%4==1)
					parent.image(l2, -cardWidth/2, -cardHeight/2,cardWidth*2f,cardHeight*2f);
				else
					if((int)(life/2)%4==2)
						parent.image(l3, -cardWidth/2, -cardHeight/2,cardWidth*2f,cardHeight*2f);
					else
						if((int)(life/2)%4==3)
							parent.image(l4, -cardWidth/2, -cardHeight/2,cardWidth*2f,cardHeight*2f);
			parent.popMatrix();
			}
			}

		}
	}
	
}

