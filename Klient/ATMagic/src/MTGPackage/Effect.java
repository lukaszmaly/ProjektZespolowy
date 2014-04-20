package MTGPackage;
import java.util.ArrayList;

import processing.core.*;


public class Effect 
{
	public enum Type{	START, 
						BOLT, 
						SPARKLES, 
						DAMAGE, 
						ARROW, 
						TEXT, 
						FIRE, 
						FIRE2,
						SPEAR,
						BOOST,
						REDUCTION,
						FIRESHIELD,
						DEATH,
						SCRY}
	
	PApplet parent;
	int life,textR,textG,textB,initLife=20,startingcounter;
	int textSize,q;
	boolean rotate=false;
	String text;
	Type type;
	Game g;
	int blockId1=-1,blockId2=-1;
	PImage 	img,
			lightning,
			arrow,
			damage,
			fireshield,
			death,
			scry;
	PImage 	l1,l2,l3,l4,
			f1,f2,f3,f4,f5,f6;
	
	
	PVector v1, v2;
	int blockId;
	int arrowType=0;
	int cardId;
	int cardWidth, cardHeight;
	PFont f=new PFont();
	//ArrayList<Card> Cards;
	
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
		else if(t==Type.REDUCTION)
			arrow=parent.loadImage("arrows_down.png");
		else if(t==Type.DAMAGE)
			damage=parent.loadImage("Blood.png");
		else if(t==Type.FIRESHIELD)
		{
			fireshield=parent.loadImage("Fire_Shield.png");
		}
		else if(t==Type.DEATH)
		{
			death=parent.loadImage("skull2.png");
			//death=parent.loadImage("FlamedSkull.png");
		}
	
		this.initLife=life;
	
		startingcounter=g.drawCounter;
	}

	
	Effect(PApplet p,Game G,int life,PVector pos,PVector move,int size, int r, int g, int b,String text,boolean rotated)
	{
		parent=p;
		this.text=text;
		this.type=type.TEXT;
		this.v1=pos;
		this.v2=move;
		this.life=life;
		this.textR=r;
		this.textG=g;
		this.textB=b;
		this.g=G;
		this.textSize=size;
		this.initLife=life;
		this.rotate=rotated;
		
	}
	
	Effect(PApplet p,Game G,Type t,int life,PVector v1,PVector v2,boolean r)
	{
		parent=p;
		this.v1=v1;
		this.v2=v2;
		this.life=life;
		this.initLife=life;
		this.type=t;
		this.g=G;
		this.cardHeight=cardHeight;
		this.cardWidth=cardWidth;
		lightning=parent.loadImage("arrows_up.png");
		if(t==Type.ARROW)
		{
			//img=parent.loadImage("arrows_up.png");
			img=parent.loadImage("sword.png");
		}
		if(t==Type.SPEAR)
		{
			img=parent.loadImage("fire8.png");
		}
		if(t==Type.SCRY)
		{	this.rotate=r;
			scry=parent.loadImage("scry.png");
		}
		
	}
	
	Effect(PApplet p,Game G,Type t,int life,int cardID,int cardWidth, int cardHeight,ArrayList<Card> c)
	{
		parent=p;
		this.v1=v1;
		this.v2=v2;
		this.life=life;
		this.initLife=life;
		this.type=t;
		this.g=G;
		this.cardId=cardID;
		this.cardHeight=cardHeight;
		this.cardWidth=cardWidth;
		//this.Cards=c;
	
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
		// parent.rotate(parent.PI/4);
	    //parent.image(img,-w/2,0,w,d);
	 }
	 else
		 if(v1.x<=v2.x && v1.y>=v2.y)  
		 {
			 parent.rotate(asin);
			// parent.rotate(parent.PI/4);
		   // parent.image(img,-w/2,0,w,d);
		 } 
		 else
			 if(v1.x>=v2.x && v1.y>=v2.y)  
			 {
				 parent.rotate(-asin);
				 //parent.rotate(parent.PI/4);
			  //  parent.image(img,-w/2,0,w,d);
			 }
			 else
				 if(v1.x>=v2.x && v1.y<=v2.y)  
				 {
					 parent.rotate(parent.PI+asin);
					//parent.rotate(parent.PI/4);
				   // parent.image(img,-w/2,0,w,d);
				 }
	// parent.rotate(-parent.PI/8);
	 parent.image(img,-w/2,0,w,d);
	    parent.popMatrix();

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
		
		else
			if(this.type==Type.BOLT)
			{

				Card c;
				for (int i = 0; i < g.Cards.size(); i++) {
					c = g.Cards.get(i);
					
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
int halfinit=initLife/2;
				
				if(life>(initLife*0.75f))
				{
					int life2=initLife-life;
					float l=life2/(float)halfinit;
					int tint=(int)(l*255);
					parent.tint(255,tint);
					
				}
				else
					if(life<(initLife*0.25f))
				{
					float l=life/(float)halfinit;
					int tint=(int)(l*255);
					parent.tint(255,tint);
				}
					else
						parent.tint(255);

						
						
				if(life>(initLife*0.75f))
				parent.image(l1, -cardWidth/2, -cardHeight/2,cardWidth*2f,cardHeight*2f);
				else
					if(life>(initLife*0.5f) && life<initLife*0.75f)
						parent.image(l2, -cardWidth/2, -cardHeight/2,cardWidth*2f,cardHeight*2f);
					else
						if(life>(initLife*0.25f) && life< initLife*0.5f)
							parent.image(l3, -cardWidth/2, -cardHeight/2,cardWidth*2f,cardHeight*2f);
						else
							parent.image(l4, -cardWidth/2, -cardHeight/2,cardWidth*2f,cardHeight*2f);

					
				parent.tint(255);
				
		/*
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
				*/
				parent.popMatrix();
				}
				}

			}
			else if (this.type==Type.DAMAGE)
			{
				Card c;
				for (int i = 0; i < g.Cards.size(); i++) {
					c = g.Cards.get(i);
					
					if (c.id == cardId) {

			    if(life>0) life--;
			   
				parent.pushMatrix();
				
				//parent.translate(c.loc[0].x,c.loc[0].y);
				parent.translate(c.center.x,c.center.y);
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
					parent.tint(255,tint);
					
				}
				else
				{
					float l=life/(float)halfinit;
					int tint=(int)(l*255);
					parent.tint(255,tint);
				}
				float size;
				if(this.q<3)
				{
					 size=q/3f;
				}
				else
				{
					 size=1.0f;
				}
				parent.imageMode(parent.CENTER);
				//parent.image(damage, 0.5f*size*g.cardWidth, 0.5f*size*g.cardHeight,g.cardWidth*size,g.cardHeight*size);
				parent.image(damage,0, 0,g.cardWidth*size,g.cardHeight*size);

				parent.println(size);
				parent.popMatrix();
				
				parent.imageMode(parent.CORNER);
				parent.tint(255);
				}
				}	
			}
			else if(this.type==Type.DEATH)
			{
				Card c;
				for (int i = 0; i < g.Cards.size(); i++) {
					c = g.Cards.get(i);
					
					if (c.id == cardId) {

			    
			   
				parent.pushMatrix();
				
				parent.translate(c.center.x,c.center.y);
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
					parent.tint(255,tint);
					
				}
				else
				{
					float l=life/(float)halfinit;
					int tint=(int)(l*255);
					parent.tint(255,tint);
				}
				parent.imageMode(parent.CENTER);
				parent.image(death, 0,0,g.cardWidth,g.cardHeight);
				parent.imageMode(parent.CORNER);
				parent.tint(255);
				parent.popMatrix();
				if(this.life>0)this.life--;
				break;
					}
				}
			}
			
		
			else if (this.type==Type.FIRE)
			{
				Card c;
				for (int i = 0; i < g.Cards.size(); i++) {
					c = g.Cards.get(i);
					
					if (c.id == cardId) {

			    
			   
				parent.pushMatrix();
				
				parent.translate(c.center.x,c.center.y);
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
				/*
				int halfinit=initLife/2;
				
				if(life>(initLife/2))
				{
					int life2=initLife-life;
					float l=life2/(float)halfinit;
					int tint=(int)(l*255);
					parent.tint(255,tint);
					
				}
				else
				{
					float l=life/(float)halfinit;
					int tint=(int)(l*255);
					parent.tint(255,tint);
				}
				float size;
				if(this.q<3)
				{
					 size=q/3f;
				}
				else
				{
					 size=1.0f;
				}
				*/
				//parent.image(damage, g.cardWidth/2-(size/2)*g.cardWidth,g.cardHeight/2-(size/2)*g.cardHeight, g.cardWidth*size,g.cardHeight*size);
				//parent.image(damage, 0.5f*size*g.cardWidth, 0.5f*size*g.cardHeight,g.cardWidth*size,g.cardHeight*size);
				//parent.println(size);
				int imgNum=23;
				if(initLife-life>=0 && initLife-life<48)
				 imgNum=(initLife-life)/2;
				parent.println(imgNum+"^");
				//if(g.drawCounter-startingcounter<23)
				// imgNum=g.drawCounter-startingcounter;
				
				parent.imageMode(parent.CENTER);
				parent.image(g.fires.get(imgNum), 0,0);
				parent.imageMode(parent.CORNER);
				parent.popMatrix();
				parent.tint(255);
				if(life>0) life--;
				}
				}
				
				
/*
				Card c;
				for (int i = 0; i < g.Cards.size(); i++) {
					c = g.Cards.get(i);
					
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
				/*
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
	*/
			}
		
			else if(this.type==Type.FIRE2)
			{
				
					Card c;
					for (int i = 0; i < g.Cards.size(); i++) {
						c = g.Cards.get(i);
						
						if (c.id == cardId) {

				    
				   
					parent.pushMatrix();
					
					parent.translate(c.center.x,c.center.y);
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
						
						
						
						
						
						int imgNum=74;
						if(initLife-life>=0 && initLife-life<74)
						 imgNum=(initLife-life);
						
						if(imgNum>50)
							parent.tint(255,(74-imgNum)*10);
						
						if(imgNum<25)
							parent.tint(255,(imgNum)*10);
						
						parent.println(imgNum+"^");
						//if(g.drawCounter-startingcounter<23)
						// imgNum=g.drawCounter-startingcounter;
						
						parent.imageMode(parent.CENTER);
						parent.image(g.fires2.get(imgNum), 0,0);
						parent.imageMode(parent.CORNER);
						parent.popMatrix();
						parent.tint(255);
						if(life>0) life--;
						
					}
			}
		
			}
		
			else if (this.type==Type.FIRESHIELD)
			{
				Card c;
				for (int i = 0; i < g.Cards.size(); i++) {
					c = g.Cards.get(i);
					
					if (c.id == cardId) {
						parent.pushMatrix();
						parent.imageMode(parent.CENTER);
						int halfinit=initLife/2;
						
						if(life>(initLife/2))
						{
							int life2=initLife-life;
							float l=life2/(float)halfinit;
							int tint=(int)(l*255);
							parent.tint(255,tint);
							
						}
						else
						{
							float l=life/(float)halfinit;
							int tint=(int)(l*255);
							parent.tint(255,tint);
						}
						parent.translate(c.center.x, c.center.y);
						float angle=life/(float)initLife;
						//float angle2=(life+20)/(float)(initLife+40);
						//float angle3=(life+40)/(float)(initLife+40);
						parent.println("angl:   "+angle);
						parent.rotate(parent.PI*angle*10);
						parent.image(fireshield, 0, 0,g.cardWidth*2*(1-angle),g.cardWidth*2f*(1-angle));
				
						//parent.image(fireshield, 0, 0,g.cardWidth*2*(1-angle2),g.cardWidth*2*(1-angle2));

						//parent.image(fireshield, 0, 0,g.cardWidth*2*(1-angle3),g.cardWidth*2*(1-angle3));

						parent.imageMode(parent.CORNER);
						parent.popMatrix();
						if(life>0)life--;
						parent.tint(255);
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
		else if(this.type==Type.SCRY)
		{
			
			if(this.life>0) this.life--;
			
			parent.pushMatrix();
			parent.translate(v1.x*parent.width, v1.y*parent.height);
	
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
			float h=(scry.height/(float)scry.width);
			parent.println("h:"+h);
			parent.imageMode(parent.CENTER);
			//parent.image(scry, 0, 0,g.cardWidth,g.cardWidth*(scry.height/scry.width));
			if(this.rotate=true)
				parent.rotate(parent.PI);
			parent.image(scry, 0, 0,g.cardWidth,h*g.cardWidth);
			parent.imageMode(parent.CORNER);
			parent.tint(255);
			parent.popMatrix();
			
		
		}
		else if(this.type==Type.SPEAR)
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
	// float l=(life/(float)initLife);
	// parent.println(life+" "+initLife+" "+l);
			// parent.tint(255,(255*l));
	 int halfinit=initLife/2;
	 float l;
		if(life>(initLife/2))
		{
			int life2=initLife-life;
			 l=life2/(float)halfinit;
			int tint=(int)(l*400);
			parent.tint(255,tint);
			
		}
		else
		{
			 l=life/(float)halfinit;
			int tint=(int)(l*400);
			parent.tint(255,tint);
		}
	 parent.image(img,-w/2,((life/initLife)*h)+life*4,w,d+life*4);
	 parent.tint(255);
	 
	    parent.popMatrix();

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
			int halfinit=initLife/2;
			//img=parent.loadImage("arrows_down.png");
			if(life>(initLife/2))
			{
				int life2=initLife-life;
				float l=life2/(float)halfinit;
				int tint=((int)(l*255));
			//	parent.println(life2+"   "+l+"   "+tint);
				//parent.tint(255,tint);
				parent.fill(this.textR,this.textG,this.textB,2*tint);
				//parent.image(arrow, 0, 0,50,50);
				parent.println("   "+tint);
				
			}
			else
			{
				float l=life/(float)halfinit;
				int tint=((int)(l*255));
				//parent.println(life+"   "+l+"   "+tint);
				//parent.tint(255,tint);
				//parent.image(arrow, 0, 0,50,50);
				parent.fill(this.textR,this.textG,this.textB,2*tint);
				parent.println("   "+tint);
			}
			v1.x=v1.x+v2.x;
			v1.y=v1.y+v2.y;
			parent.translate(v1.x*parent.width,v1.y*parent.height);
			//parent.println("####:"+v1.y);
			//parent.rotate(parent.PI);
			//parent.text(String.valueOf("5"),0,0);
		//	parent.println(this.text+" "+ v1.x*parent.width+" "+v1.y*parent.height);
			if(this.rotate==true)
				{
				parent.rotate(parent.PI);
				parent.translate(0, 20);
				}
			parent.text(this.text,0,0);
			parent.popMatrix();
			life--;
			//parent.text(String.valueOf(P2.life),parent.width*0.86f,parent.height-10);
			
		}
		
		
	
			
	}
	
}

