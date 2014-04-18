package MTGPackage;
import java.util.ArrayList;

import processing.core.*;


public class Effect 
{
	public enum Type{START, BOLT, SPARKLES, FRAME, ARROW, TEXT, FIRE, SPEAR,BOOST,REDUCTION}
	
	PApplet parent;
	int life,textR,textG,textB,initLife=20;
	int textSize;
	String text;
	Type type;
	Game g;
	int blockId1=-1,blockId2=-1;
	PImage 	img,
			lightning,
			arrow;
	PImage 	l1,l2,l3,l4,
			f1,f2,f3,f4,f5,f6;
	PVector v1, v2;
	int blockId;
	int arrowType=0;
	int cardId;
	int cardWidth, cardHeight;
	PFont f=new PFont();
	ArrayList<Card> Cards;
	
	Effect(PApplet p,Type t,int life)
	{
		parent=p;
		this.life=life;
		this.type=t;
		
	}
	Effect(PApplet p,Game G,Type t,int life,int cardId)
	{
		parent=p;
		this.life=life;
		this.g=G;
		this.type=t;
		this.cardId=cardId;
		if(t==Type.BOOST)
		arrow=parent.loadImage("arrows_up.png");
		if(t==Type.REDUCTION)
			arrow=parent.loadImage("arrows_down.png");
		this.initLife=life;
	
		
	}

	
	Effect(PApplet p,Game G,int life,PVector pos,PVector move,int size, int r, int g, int b,String text)
	{
		parent=p;
		this.text=text;
		this.type=type.TEXT;
		this.v1=pos;
		this.life=life;
		this.textR=r;
		this.textG=g;
		this.textB=b;
		this.g=G;
		this.textSize=size;
		
	}
	
	Effect(PApplet p,Game G,Type t,int life,PVector v1,PVector v2)
	{
		parent=p;
		this.v1=v1;
		this.v2=v2;
		this.life=life;
		this.type=t;
		this.g=G;
		this.cardHeight=cardHeight;
		this.cardWidth=cardWidth;
		lightning=parent.loadImage("arrows_up.png");
		if(t==Type.ARROW)
		{
			img=parent.loadImage("arrows_up.png");
		}
		if(t==Type.SPEAR)
		{
			img=parent.loadImage("fire8.png");
		}
		
	}
	
	Effect(PApplet p,Game G,Type t,int life,int cardID,int cardWidth, int cardHeight,ArrayList<Card> c)
	{
		parent=p;
		this.v1=v1;
		this.v2=v2;
		this.life=life;
		this.type=t;
		this.g=G;
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
		if(t==Type.FIRE)
		{
			f1=parent.loadImage("fire11.png");
			f2=parent.loadImage("fire16.png");
			f3=parent.loadImage("fire17.png");
			f4=parent.loadImage("fire18.png");
			f5=parent.loadImage("fire19.png");
			f6=parent.loadImage("fire21.png");
			
		}
		
	}
	
	public void drawEffect()
	{
		if(this.type==Type.ARROW)
		{
			if(arrowType==1) img=parent.loadImage("arrows_down.png");
			
	    float d=parent.dist(v1.x, v1.y, v2.x, v2.y);
	    //if(life>0) life--;
	    
	    int h=(int)d;
	    int w=(int)(d/5);
	    if(w>80) w=80;
	    float sin=(parent.abs(v1.x-v2.x))/d;
	    float asin=parent.asin(sin);
	    parent.println(asin);
	   // parent.stroke(0,0,255);
	   
	    
	    
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
		if(this.type==Type.SPEAR)
		{
			
			
	    float d=parent.dist(v1.x, v1.y, v2.x, v2.y);
	    if(life>0) life--;
	    
	    int h=(int)d;
	    int w=(int)(d/1.5);
	  //  int w=h;
	   // if(w>80) w=80;
	    float sin=(parent.abs(v1.x-v2.x))/d;
	    float asin=parent.asin(sin);
	 //   parent.println(asin);
	    parent.stroke(0,0,255);
    parent.pushMatrix();
	    parent.translate(v2.x, v2.y);
	 
	 if(v1.x<=v2.x && v1.y<=v2.y)  
	 {
		 parent.rotate(parent.PI-asin);
	
	   // parent.image(img,-w/2,0,w,d);
	 }
	 else
		 if(v1.x<=v2.x && v1.y>=v2.y)  
		 {
			 parent.rotate(asin);
			 
		   // parent.image(img,-w/2,0,w,d);
		 } 
		 else
			 if(v1.x>=v2.x && v1.y>=v2.y)  
			 {
				 parent.rotate(-asin);
				
			    //parent.image(img,-w/2,0,w,d);
			 }
			 else
				 if(v1.x>=v2.x && v1.y<=v2.y)  
				 {
					 parent.rotate(parent.PI+asin);
					
				   // parent.image(img,-w/2,0,w,d);
				 }
	 float l=(life/(float)initLife);
	// parent.println(life+" "+initLife+" "+l);
			 parent.tint(255,(255*l));
	 parent.image(img,-w/2,l*h,w,d*2l);
	 parent.tint(255);
	 
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
			
			
	
			if((int)(life)%4==0)
			parent.image(l1, -cardWidth/2, -cardHeight/2,cardWidth*2f,cardHeight*2f);
			else
				if((int)(life)%4==1)
					parent.image(l2, -cardWidth/2, -cardHeight/2,cardWidth*2f,cardHeight*2f);
				else
					if((int)(life)%4==2)
						parent.image(l3, -cardWidth/2, -cardHeight/2,cardWidth*2f,cardHeight*2f);
					else
						if((int)(life)%4==3)
							parent.image(l4, -cardWidth/2, -cardHeight/2,cardWidth*2f,cardHeight*2f);
			parent.popMatrix();
			}
			}

		}
		else if(this.type==type.TEXT)
		{
			//parent.println("kk"+parent);
			f=parent.createFont("Comic Sans", this.textSize);
			//f=parent.createFont("Comic Sans", 22,true);
			
		 	parent.textFont(f);
			parent.fill(this.textR,this.textG,this.textB);
			parent.textAlign(parent.CENTER);
			
			parent.pushMatrix();
		//	parent.translate(parent.width*g.,10);
			//v1
			parent.translate(v1.x*parent.width,v1.y*parent.height);
			//parent.println("####:"+v1.y);
			//parent.rotate(parent.PI);
			//parent.text(String.valueOf("5"),0,0);
			parent.println(this.text+" "+ v1.x*parent.width+" "+v1.y*parent.height);
			parent.text(this.text,0,0);
			parent.popMatrix();
			life--;
			//parent.text(String.valueOf(P2.life),parent.width*0.86f,parent.height-10);
			
		}
		else if(this.type==Type.BOOST)
		{
			for (int i = 0; i < g.Cards.size(); i++) {
				Card c = g.Cards.get(i);
				if (c.id == cardId) 
				{
				v1=c.center;
				if(this.life>0) this.life--;
			
			parent.pushMatrix();
			parent.translate(v1.x, v1.y);
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
			
			int halfinit=initLife/2;
			
			if(life>(initLife/2))
			{
				int life2=initLife-life;
				float l=life2/(float)halfinit;
				int tint=(int)(l*255);
			//	parent.println(life2+"   "+l+"   "+tint);
				parent.tint(255,tint);
				//parent.image(arrow, 0, 0,50,50);
				
			}
			else
			{
				float l=life/(float)halfinit;
				int tint=(int)(l*255);
				//parent.println(life+"   "+l+"   "+tint);
				parent.tint(255,tint);
				//parent.image(arrow, 0, 0,50,50);
			}
			parent.translate(0,(initLife/4)-(initLife-life)/2);
			parent.image(arrow, -15, -15,30,30);
			parent.image(arrow, -55, -15,30,30);//oryg
			parent.image(arrow, 25, -15,30,30);
			
			parent.image(arrow, -15, -10,30,30);
				parent.image(arrow, -55, -10,30,30);
				parent.image(arrow, 25, -10,30,30);
				
				parent.image(arrow, -15, -5,30,30);
				parent.image(arrow, -55, -5,30,30);
				parent.image(arrow, 25, -5,30,30);
			
		//	parent.image(arrow, -15, 5,30,30);
		//	parent.image(arrow, -55, 5,30,30);
		//	parent.image(arrow, 25, 5,30,30);
			
		//	parent.image(arrow, -15, -5,30,30);
		//	parent.image(arrow, -55, -5,30,30);
		//	parent.image(arrow, 25, -5,30,30);
			
			parent.image(arrow, -35, 20,30,30);//oryg
			parent.image(arrow, 5, 20,30,30);
			
			parent.image(arrow, -35, 25,30,30);
			parent.image(arrow, 5, 25,30,30);
			
			parent.image(arrow, -35, 30,30,30);
			parent.image(arrow, 5, 30,30,30);
			
			parent.image(arrow, -35, -55,30,30);//oryg
			parent.image(arrow, 5, -55,30,30);
			
			parent.image(arrow, -35, -50,30,30);
			parent.image(arrow, 5, -50,30,30);
			
			parent.image(arrow, -35, -45,30,30);
			parent.image(arrow, 5, -45,30,30);
			
			
			parent.tint(255);
			parent.popMatrix();
			break;
			
				}
				
			}
			
		}
		else if(this.type==Type.REDUCTION)
		{
			for (int i = 0; i < g.Cards.size(); i++) {
				Card c = g.Cards.get(i);
				if (c.id == cardId) 
				{
				v1=c.center;
				if(this.life>0) this.life--;
			
			parent.pushMatrix();
			parent.translate(v1.x, v1.y);
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
			
			int halfinit=initLife/2;
			//img=parent.loadImage("arrows_down.png");
			if(life>(initLife/2))
			{
				int life2=initLife-life;
				float l=life2/(float)halfinit;
				int tint=((int)(l*255))/4;
			//	parent.println(life2+"   "+l+"   "+tint);
				parent.tint(255,tint);
				//parent.image(arrow, 0, 0,50,50);
				
			}
			else
			{
				float l=life/(float)halfinit;
				int tint=((int)(l*255))/4;
				//parent.println(life+"   "+l+"   "+tint);
				parent.tint(255,tint);
				//parent.image(arrow, 0, 0,50,50);
			}
			parent.translate(0,-(initLife/4)+(initLife-life)/2);
			parent.image(arrow, -15, -15,30,30);
			parent.image(arrow, -55, -15,30,30);//oryg
			parent.image(arrow, 25, -15,30,30);
			
			parent.image(arrow, -15, -10,30,30);
				parent.image(arrow, -55, -10,30,30);
				parent.image(arrow, 25, -10,30,30);
				
				parent.image(arrow, -15, -5,30,30);
				parent.image(arrow, -55, -5,30,30);
				parent.image(arrow, 25, -5,30,30);
			
		//	parent.image(arrow, -15, 5,30,30);
		//	parent.image(arrow, -55, 5,30,30);
		//	parent.image(arrow, 25, 5,30,30);
			
		//	parent.image(arrow, -15, -5,30,30);
		//	parent.image(arrow, -55, -5,30,30);
		//	parent.image(arrow, 25, -5,30,30);
			
			parent.image(arrow, -35, 20,30,30);//oryg
			parent.image(arrow, 5, 20,30,30);
			
			parent.image(arrow, -35, 25,30,30);
			parent.image(arrow, 5, 25,30,30);
			
			parent.image(arrow, -35, 30,30,30);
			parent.image(arrow, 5, 30,30,30);
			
			parent.image(arrow, -35, -55,30,30);//oryg
			parent.image(arrow, 5, -55,30,30);
			
			parent.image(arrow, -35, -50,30,30);
			parent.image(arrow, 5, -50,30,30);
			
			parent.image(arrow, -35, -45,30,30);
			parent.image(arrow, 5, -45,30,30);
			
			
			parent.tint(255);
			parent.popMatrix();
			break;
			
				}
				
			}
			
		}
		else if (this.type==Type.FIRE)
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
			parent.translate(-0.01f*cardWidth,-0.05f*cardHeight);
			/*
			if((int)(life)%4==0)
			parent.image(f1, 0, 0,cardWidth,cardHeight);
			else
				if((int)(life)%4==1)
				{
					parent.tint(255, 127);
					parent.image(f1, 0, 0,cardWidth,cardHeight);
					parent.tint(255);
					parent.image(f2, 0, 0,cardWidth,cardHeight);
				}
				else
					if((int)(life)%4==2)
					{
						parent.tint(255, 127);
						parent.image(f2, 0, 0,cardWidth,cardHeight);
						parent.tint(255);
						parent.image(f3,0, 0,cardWidth,cardHeight);
					}
					else
						if((int)(life)%4==3)
						{
							parent.tint(255, 127);
							parent.image(f3, 0, 0,cardWidth,cardHeight);
							parent.tint(255);
							parent.image(f4, 0, 0,cardWidth,cardHeight);
						}
						*/
			
				
				/*
				if(life>25 && life<=30)
					{
						parent.tint(255, 127);
						parent.image(f1, 0, 0,cardWidth,cardHeight);
						parent.tint(255);
						parent.image(f2, 0, 0,cardWidth,cardHeight);
					}
					else
						if(life>20 && life<=25)
						{	parent.tint(255,50);
							parent.image(f1, 0, 0,cardWidth,cardHeight);
							parent.tint(255, 127);
							parent.image(f2, 0, 0,cardWidth,cardHeight);
							parent.tint(255);
							parent.image(f3,0, 0,cardWidth,cardHeight);
						}
						else
							if(life>15 && life<=20)
							{
								parent.tint(255,50);
								parent.image(f2, 0, 0,cardWidth,cardHeight);
								parent.tint(255, 127);
								parent.image(f3, 0, 0,cardWidth,cardHeight);
								parent.tint(255);
								parent.image(f4, 0, 0,cardWidth,cardHeight);
							}
							else
								if(life>10 && life<=15)
								{
									parent.tint(255,50);
									parent.image(f3, 0, 0,cardWidth,cardHeight);
									parent.tint(255, 127);
									parent.image(f4, 0, 0,cardWidth,cardHeight);
									parent.tint(255);
									parent.image(f5, 0, 0,cardWidth,cardHeight);
								}
								else
									if(life>5 && life<=10)
									{
										parent.tint(255,50);
										parent.image(f4, 0, 0,cardWidth,cardHeight);
										parent.tint(255, 127);
										parent.image(f5, 0, 0,cardWidth,cardHeight);
										parent.tint(255);
										parent.image(f6, 0, 0,cardWidth,cardHeight);
									}
									else
										if(life>0 && life<=5)
										{
											parent.tint(255, 127);
											parent.image(f5, 0, 0,cardWidth,cardHeight);
											parent.tint(255);
											parent.image(f6, 0, 0,cardWidth,cardHeight);
										}
										*/
			if(life>25 && life<=30)
			{
				parent.tint(255, (30-life)*50);
				parent.image(f2, -10, 0,cardWidth,cardHeight);
			}
			else
				if(life>20 && life<=25)
				{
					parent.tint(255, life*5);
					parent.image(f2, -10, 0,cardWidth,cardHeight);
					
					parent.tint(255, (25-life)*50);
					parent.image(f3, 0, 0,cardWidth,cardHeight);
				}
				else
					if(life>15 && life<=20)
					{
						parent.tint(255, life*10);
						parent.image(f3, 0, 0,cardWidth,cardHeight);
						
						parent.tint(255, (20-life)*50);
						parent.image(f4, 0, 0,cardWidth,cardHeight);
					}
					else
						if(life>10 && life<=15)
						{
							parent.tint(255, life*15);
							parent.image(f4, 0, 0,cardWidth,cardHeight);
							
							parent.tint(255, (15-life)*50);
							parent.image(f3, 0, 0,cardWidth,cardHeight);
						}
						else
							if(life>5 && life<=10)
							{
								parent.tint(255, life*20);
								parent.image(f3, 0, 0,cardWidth,cardHeight);
								
								parent.tint(255, (10-life)*50);
								parent.image(f2, -10, 0,cardWidth,cardHeight);
							}
							else
								if(life>0 && life<=5)
								{
									parent.tint(255, life*25);
									parent.image(f2, -10, 0,cardWidth,cardHeight);
								}
			//parent.tint(255, (30-life)*10);
			//parent.image(f6, 0, 0,cardWidth,cardHeight);
			parent.tint(255);
			parent.popMatrix();
			}
			}

		}
			
	}
	
}

