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
						SCRY,
						DRAW,
						SPEAR2}
	
	PApplet parent;
	int life,textR,textG,textB,initLife=40;
	int textSize,q;
	boolean rotate=false;
	String text;
	Type type;
	Game g;
	int blockId1=-1,blockId2=-1;

	PVector v1, v2;
	int blockId;	
	int cardId;
	PFont f=new PFont();
	
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
		this.initLife=life;
	
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
		f=parent.createFont("Comic Sans", this.textSize);
		
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
		this.rotate=r;
	
	}

	
	public void drawEffect()
	{
		if(this.type==Type.ARROW)
		{			
	    float d=parent.dist(v1.x, v1.y, v2.x, v2.y);
	    
	    
	    int h=(int)d;
	    int w=(int)(d/5);
	    if(w>80) w=80;
	    float sin=(parent.abs(v1.x-v2.x))/d;
	    float asin=parent.asin(sin);
	    parent.println(asin);

	    parent.pushMatrix();
	    parent.translate(v2.x, v2.y);
	 
	 if(v1.x<=v2.x && v1.y<=v2.y)  
	 {
		 parent.rotate(parent.PI-asin);

	 }
	 else
		 if(v1.x<=v2.x && v1.y>=v2.y)  
		 {
			 parent.rotate(asin);

		 } 
		 else
			 if(v1.x>=v2.x && v1.y>=v2.y)  
			 {
				 parent.rotate(-asin);

			 }
			 else
				 if(v1.x>=v2.x && v1.y<=v2.y)  
				 {
					 parent.rotate(parent.PI+asin);

				 }

	 parent.image(g.sword2,-w/2,0,w,d);
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
				parent.tint(255,tint);
	
			}
			else
			{
				float l=life/(float)halfinit;
				int tint=(int)(l*255);
				parent.tint(255,tint);
			}
			parent.translate(0,(initLife/4)-(initLife-life)/2);
			parent.image(g.boostImg, -15, -15,30,30);
			parent.image(g.boostImg, -55, -15,30,30);
			parent.image(g.boostImg, 25, -15,30,30);
			
			parent.image(g.boostImg, -15, -10,30,30);
				parent.image(g.boostImg, -55, -10,30,30);
				parent.image(g.boostImg, 25, -10,30,30);
				
				parent.image(g.boostImg, -15, -5,30,30);
				parent.image(g.boostImg, -55, -5,30,30);
				parent.image(g.boostImg, 25, -5,30,30);
			

			
			parent.image(g.boostImg, -35, 20,30,30);//oryg
			parent.image(g.boostImg, 5, 20,30,30);
			
			parent.image(g.boostImg, -35, 25,30,30);
			parent.image(g.boostImg, 5, 25,30,30);
			
			parent.image(g.boostImg, -35, 30,30,30);
			parent.image(g.boostImg, 5, 30,30,30);
			
			parent.image(g.boostImg, -35, -55,30,30);//oryg
			parent.image(g.boostImg, 5, -55,30,30);
			
			parent.image(g.boostImg, -35, -50,30,30);
			parent.image(g.boostImg, 5, -50,30,30);
			
			parent.image(g.boostImg, -35, -45,30,30);
			parent.image(g.boostImg, 5, -45,30,30);
			
			
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
				parent.image(g.l1, -g.cardWidth/2, -g.cardHeight/2,g.cardWidth*2f,g.cardHeight*2f);
				else
					if(life>(initLife*0.5f) && life<initLife*0.75f)
						parent.image(g.l2, -g.cardWidth/2, -g.cardHeight/2,g.cardWidth*2f,g.cardHeight*2f);
					else
						if(life>(initLife*0.25f) && life< initLife*0.5f)
							parent.image(g.l3, -g.cardWidth/2, -g.cardHeight/2,g.cardWidth*2f,g.cardHeight*2f);
						else
							parent.image(g.l4, -g.cardWidth/2, -g.cardHeight/2,g.cardWidth*2f,g.cardHeight*2f);

					
				parent.tint(255);
				
		
				parent.popMatrix();
				break;
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
				PVector center = new PVector((c.loc[0].x+c.loc[1].x+c.loc[2].x+c.loc[3].x)/4,(c.loc[0].y+c.loc[1].y+c.loc[2].y+c.loc[3].y)/4);
				c.center=center;
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
				parent.image(g.damage,0, 0,g.cardWidth*size,g.cardHeight*size);

				parent.println(size);
				parent.popMatrix();
				
				parent.imageMode(parent.CORNER);
				parent.tint(255);
				break;
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
				parent.image(g.death, 0,0,g.cardWidth,g.cardHeight);
				parent.imageMode(parent.CORNER);
				parent.tint(255);
				parent.popMatrix();
				if(this.life>0)this.life--;
				break;
					}
				}
			}
			else if(this.type==Type.DRAW)
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
					parent.tint(255,tint);
					
				}
				else
				{
					float l=life/(float)halfinit;
					int tint=(int)(l*255);
					parent.tint(255,tint);
				}
				float h=(g.draw.height/(float)g.draw.width);
				parent.println("h:"+h);
				parent.imageMode(parent.CENTER);
				if(this.rotate==true)
					parent.rotate(parent.PI);
				parent.image(g.draw, 0, 0,g.cardWidth,h*g.cardWidth);
				parent.imageMode(parent.CORNER);
				parent.tint(255);
				parent.popMatrix();
				
			
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
				
				int imgNum=23;
				if(initLife-life>=0 && initLife-life<48)
				 imgNum=(initLife-life)/2;
				
				parent.imageMode(parent.CENTER);
				parent.image(g.fires.get(imgNum), 0,0);
				parent.imageMode(parent.CORNER);
				parent.popMatrix();
				parent.tint(255);
				if(life>0) life--;
				break;
				}
				}
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
						
						parent.imageMode(parent.CENTER);
						parent.image(g.fires2.get(imgNum), 0,0);
						parent.imageMode(parent.CORNER);
						parent.popMatrix();
						parent.tint(255);
						if(life>0) life--;
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
			if(life>(initLife/2))
			{
				int life2=initLife-life;
				float l=life2/(float)halfinit;
				int tint=((int)(l*255))/4;
				parent.tint(255,tint);
				
			}
			else
			{
				float l=life/(float)halfinit;
				int tint=((int)(l*255))/4;
				parent.tint(255,tint);
			}
			parent.translate(0,-(initLife/4)+(initLife-life)/2);
			parent.image(g.reductionImg, -15, -15,30,30);
			parent.image(g.reductionImg, -55, -15,30,30);//oryg
			parent.image(g.reductionImg, 25, -15,30,30);
			
			parent.image(g.reductionImg, -15, -10,30,30);
				parent.image(g.reductionImg, -55, -10,30,30);
				parent.image(g.reductionImg, 25, -10,30,30);
				
				parent.image(g.reductionImg, -15, -5,30,30);
				parent.image(g.reductionImg, -55, -5,30,30);
				parent.image(g.reductionImg, 25, -5,30,30);
			
			parent.image(g.reductionImg, -35, 20,30,30);//oryg
			parent.image(g.reductionImg, 5, 20,30,30);
			
			parent.image(g.reductionImg, -35, 25,30,30);
			parent.image(g.reductionImg, 5, 25,30,30);
			
			parent.image(g.reductionImg, -35, 30,30,30);
			parent.image(g.reductionImg, 5, 30,30,30);
			
			parent.image(g.reductionImg, -35, -55,30,30);//oryg
			parent.image(g.reductionImg, 5, -55,30,30);
			
			parent.image(g.reductionImg, -35, -50,30,30);
			parent.image(g.reductionImg, 5, -50,30,30);
			
			parent.image(g.reductionImg, -35, -45,30,30);
			parent.image(g.reductionImg, 5, -45,30,30);
			
			
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
				parent.tint(255,tint);
				
			}
			else
			{
				float l=life/(float)halfinit;
				int tint=(int)(l*255);
				parent.tint(255,tint);
			}
			float h=(g.scry.height/(float)g.scry.width);
			parent.println("h:"+h);
			parent.imageMode(parent.CENTER);
			if(this.rotate==true)
				parent.rotate(parent.PI);
			parent.image(g.scry, 0, 0,g.cardWidth,h*g.cardWidth);
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

	    float sin=(parent.abs(v1.x-v2.x))/d;
	    float asin=parent.asin(sin);
	    parent.stroke(0,0,255);
	    parent.pushMatrix();
	    parent.translate(v2.x, v2.y);
	 
	 if(v1.x<=v2.x && v1.y<=v2.y)  
	 {
		 parent.rotate(parent.PI-asin);
	
	 }
	 else
		 if(v1.x<=v2.x && v1.y>=v2.y)  
		 {
			 parent.rotate(asin);
			 
		 } 
		 else
			 if(v1.x>=v2.x && v1.y>=v2.y)  
			 {
				 parent.rotate(-asin);
				
			 }
			 else
				 if(v1.x>=v2.x && v1.y<=v2.y)  
				 {
					 parent.rotate(parent.PI+asin);
					
				 }

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
	 parent.image(g.spear,-w/2,((life/initLife)*h)+life*4,w,d+life*4);
	 parent.tint(255);
	 
	    parent.popMatrix();

	}
		else if(this.type==Type.SPEAR2)
		{
			float d=parent.dist(v1.x, v1.y, v2.x, v2.y);
		    if(life>0) life--;
		    

		    float sin=(parent.abs(v1.x-v2.x))/d;
		    float asin=parent.asin(sin);
				parent.pushMatrix();
				
				parent.translate(v1.x,v1.y);
				if(v1.x<=v2.x && v1.y<=v2.y)  
				 {
					 parent.rotate(parent.PI-asin);
				 }
				 else
					 if(v1.x<=v2.x && v1.y>=v2.y)  
					 {
						 parent.rotate(asin);
						 
					 } 
					 else
						 if(v1.x>=v2.x && v1.y>=v2.y)  
						 {
							 parent.rotate(-asin);
						 }
						 else
							 if(v1.x>=v2.x && v1.y<=v2.y)  
							 {
								 parent.rotate(parent.PI+asin);
								
							 }
					
					parent.rotate(parent.PI);
					parent.translate(0,(1-(life/(float)initLife))*d);
					
					
					int imgNum=74;
					if(initLife-life>=0 && initLife-life<74)
					 imgNum=(initLife-life);
					
					if(imgNum>50)
						parent.tint(255,(74-imgNum)*10);
					
					if(imgNum<25)
						parent.tint(255,(imgNum)*10);
					
				
					parent.imageMode(parent.CENTER);
					parent.image(g.fires2.get(imgNum), 0,0);
					parent.imageMode(parent.CORNER);
					parent.popMatrix();
					parent.tint(255);
					if(life>0) life--;
					
				}
	
		else if(this.type==type.TEXT)
		{
			f=parent.createFont("Comic Sans", this.textSize);
			
		 	parent.textFont(f);
			parent.fill(this.textR,this.textG,this.textB);
			parent.textAlign(parent.CENTER);
			
			parent.pushMatrix();
			
			int halfinit=initLife/2;
			if(life>(initLife/2))
			{
				int life2=initLife-life;
				float l=life2/(float)halfinit;
				int tint=((int)(l*255));
				parent.fill(this.textR,this.textG,this.textB,2*tint);
				parent.println("   "+tint);
				
			}
			else
			{
				float l=life/(float)halfinit;
				int tint=((int)(l*255));
				parent.fill(this.textR,this.textG,this.textB,2*tint);
				parent.println("   "+tint);
			}
			v1.x=v1.x+v2.x;
			v1.y=v1.y+v2.y;
			parent.translate(v1.x*parent.width,v1.y*parent.height);

			if(this.rotate==true)
				{
				parent.rotate(parent.PI);
				parent.translate(0, 20);
				}
			parent.text(this.text,0,0);
			parent.popMatrix();
			life--;
			
		}		
	}
	
}

