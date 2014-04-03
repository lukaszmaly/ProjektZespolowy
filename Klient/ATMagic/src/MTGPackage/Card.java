package MTGPackage;
import processing.core.*;

import java.util.*;

public class Card
{
	PApplet parent;
	
	PVector[] loc;													//Polozenie 4 rogow
	PVector LG,PG,LD,PD,center;											//Polozenie 4 rogow
	PVector[] loc2;													//Polozenie przed atakiem	
	ArrayList<SparkEdge> se;										//4 krawedzie x 10 generatorow	
	int sparkTime=20, frameLife=100,frameRate=1,deadCounter=200,direction; 	//sparking po wejsciu karty, jasnosc  frame'a	
	boolean frame,frameCounter=false,isDead=false,attack=false,block=false,isBlocked=false;		//Czy ma byc ramka, czy ma sie rozjasniac czy zaciemniac
	int r,g,b,id, db_id, owner,manaCost;
	float a=0,c=0;
	int wCost=0,bCost=0,uCost=0,rCost=0,gCost=0,power,toughness;
	float sin=0,asin=0;
	PImage skull;
	Game game;
	
	int height()
	{
		int[] Y= {(int)loc[0].y,(int)loc[1].y,(int)loc[2].y,(int)loc[3].y};
		return(parent.max(Y)-parent.min(Y));
	}
  
	int width()
	{
		int[] X= {(int)loc[0].x,(int)loc[1].x,(int)loc[2].x,(int)loc[3].x};
		return(parent.max(X)-parent.min(X));
	}
  
	void move(PVector m) 					//Przesuniecie o
	{
		this.loc[0].x+=m.x;
		this.loc[0].y+=m.y;
		this.loc[1].x+=m.x;
		this.loc[1].y+=m.y;
		this.loc[2].x+=m.x;
		this.loc[2].y+=m.y;
		this.loc[3].x+=m.x;
		this.loc[3].y+=m.y;	  
	}
  
	void moveTo(PVector m)					//Przesuniecie do
	{
		
		this.loc[1].x=m.x+this.loc[1].x-loc[0].x;
		this.loc[1].y=m.y+this.loc[1].y-loc[0].y;
		this.loc[2].x=m.x+this.loc[2].x-loc[0].x;
		this.loc[2].y=m.y+this.loc[2].y-loc[0].y;
		this.loc[3].x=m.x+this.loc[3].x-loc[0].x;
		this.loc[3].y=m.y+this.loc[3].y-loc[0].y;
		this.loc[0].x=m.x;
		this.loc[0].y=m.y;
	}
	
	void drawEdges()					//Rysowanie ramki t=transparency
	{
		if(frameLife<0) frameCounter=true;//Zaciemnianie lub pojasnianie
		if(frameLife>100) frameCounter=false;	
		if(frameCounter==true) frameLife+=frameRate; else frameLife-=frameRate;

			this.a=parent.abs(loc[0].x-loc[3].x);
			this.c=parent.dist(loc[0].x,loc[0].y,loc[3].x,loc[3].y);
			sin=a/c;
			asin=parent.asin(sin);
			PVector center=new PVector((loc[0].x+loc[1].x+loc[2].x+loc[3].x)/4,(loc[0].y+loc[1].y+loc[2].y+loc[3].y)/4);
		
			parent.noFill();
			parent.pushMatrix();
			parent.ellipseMode(parent.CENTER);
			
			parent.translate(center.x, center.y);
			
			switch(direction)
			{
			case 1:
				parent.rotate(asin);
				break;
				
			case 2:
				parent.rotate(-asin+parent.PI);
				break;
				
			case 3:
				parent.rotate(asin+parent.PI);
				break;
				
			case 4:
				parent.rotate(-asin);
				break;
				
			default: break;
			
			}
		
			for(int i=50;i>0;i--)
			{
				
				parent.noFill();
				parent.stroke(this.r,this.g,this.b,(int)(parent.sqrt(frameLife*i)));
				parent.ellipse(0,0,150-2*i,200-2*i);
				//parent.ellipse(0,0,100+2*i,150+2*i);
				
			}
			
		
			parent.popMatrix();
			
			if(this.attack==true)
			{
				PVector returnCenter=new PVector((loc2[0].x+loc2[1].x+loc2[2].x+loc2[3].x)/4,(loc2[0].y+loc2[1].y+loc2[2].y+loc2[3].y)/4);
			
					parent.fill(240,240,70,(100f*frameLife*0.01f));
					parent.ellipseMode(parent.CENTER);
					//parent.stroke(240,240,70,(100-4*i)*frameLife*0.01f);
					parent.stroke(240,240,70,(100)*frameLife*0.01f);
					parent.ellipse(returnCenter.x, returnCenter.y, 100, 100);
			
			}
			
			
			parent.strokeWeight(1);
			parent.stroke(255);
			parent.fill(255);
			parent.beginShape();
			parent.vertex(loc[0].x,loc[0].y);
			parent.vertex(loc[1].x,loc[1].y);
			parent.vertex(loc[2].x,loc[2].y);
			parent.vertex(loc[3].x,loc[3].y);
			parent.vertex(loc[0].x,loc[0].y);
			parent.endShape();
			 
			parent.pushMatrix();
			parent.translate(this.loc[3].x,this.loc[3].y);
			
			
			switch(direction)
			{
			case 1:
				parent.rotate(asin);
				break;
				
			case 2:
				parent.rotate(-asin+parent.PI);
				break;
				
			case 3:
				parent.rotate(asin+parent.PI);
				break;
				
			case 4:
				parent.rotate(-asin);
				break;
				
			default: break;
			}
			parent.translate(-0.1f*game.cardWidth,0.1f*game.cardHeight);
			parent.noStroke();
			parent.fill(255);
			parent.ellipse(0,0,0.25f*game.cardHeight,0.25f*game.cardWidth);
			parent.noFill();
			for(int i=10;i>0;i--)
			{
				parent.stroke(0,0,255,i*10);
				parent.ellipse(0,0,0.25f*game.cardHeight+10-1*i,0.25f*game.cardWidth+10-1*i);
				
			}
				game.f=parent.createFont("Arial", 28);
			parent.textFont(game.f);
			parent.fill(255,0,0);
			parent.textAlign(parent.CENTER,parent.CENTER);
			parent.text(this.power, 0,-3);
			parent.translate(game.cardWidth*1.1f,0);
			//parent.stroke(0,0,255);
			parent.noStroke();
			parent.fill(255);
			parent.ellipse(0,0,0.25f*game.cardHeight,0.25f*game.cardWidth);
			parent.noFill();
			for(int i=10;i>0;i--)
			{
				parent.stroke(255,0,0,i*10);
				parent.ellipse(0,0,0.25f*game.cardHeight+10-1*i,0.25f*game.cardWidth+10-1*i);
				
			}
			
			parent.fill(0,0,255);
			parent.text(this.toughness, 0,-3);
			parent.popMatrix();
			
			//}
		if(this.isDead==true)
		{
			this.deadCounter--;
			parent.strokeWeight(1);
			parent.stroke(0);
			parent.fill(0);

			this.frameRate = 8;
			parent.tint(200- (this.deadCounter),255);
			
						parent.pushMatrix();
						parent.translate(this.loc[0].x,this.loc[0].y);
						switch(this.direction)
						{
						case 1:
							parent.rotate(this.asin);
							break;
							
						case 2:
							parent.rotate(-this.asin+parent.PI);
							break;
							
						case 3:
							parent.rotate(this.asin+parent.PI);
							break;
							
						case 4:
							parent.rotate(-this.asin);
							break;
							
						default: break;
						
						}
						
						
						parent.image(skull,0,0,parent.dist(this.loc[0].x,this.loc[0].y,this.loc[1].x,this.loc[1].y),parent.dist(this.loc[2].x,this.loc[2].y,this.loc[1].x,this.loc[1].y));
						parent.popMatrix();
						parent.tint(255);
			
		}

  }

	int[][] divideEdge(int section,int n) 	//odcinek 1..4, ilosc podzialow
	    {
	    	if(section >4 || section<1) 
	    		{
	    		return null;
	    		}
	    	int xx1=0;
	    	int xx2=0;
	    	int yy1=0;
	    	int yy2=0;
	    	if(section==1)
			  	{
		  		xx1=(int)this.loc[0].x;
		  		xx2=(int)this.loc[1].x;
		  		yy1=(int)this.loc[0].y;
		  		yy2=(int)this.loc[1].y;
			  	}
	    	if(section==2)
	    		{
	    		xx1=(int)this.loc[1].x;
		  		xx2=(int)this.loc[2].x;
		  		yy1=(int)this.loc[1].y;
		  		yy2=(int)this.loc[2].y;
	    		}
	    	if(section==3)
	    		{
	    	
	    		
	    		xx1=(int)this.loc[2].x;
		  		xx2=(int)this.loc[3].x;
		  		yy1=(int)this.loc[2].y;
		  		yy2=(int)this.loc[3].y;
	    		}
	    	if(section==4)
	    		{
	    		
	    		
	    		xx1=(int)this.loc[3].x;
		  		xx2=(int)this.loc[0].x;
		  		yy1=(int)this.loc[3].y;
		  		yy2=(int)this.loc[0].y;
	    		}
	  
	    	int[] PointsX=new int[n];
	    	int[] PointsY=new int[n];
		
	    	int xDif=parent.abs(xx1-xx2);
	    	int yDif=parent.abs(yy1-yy2);
	    	int mx=parent.min(xx1,xx2);
	    	int my=parent.min(yy1,yy2);

	    	if(mx==xx1 && my==yy1)
	    		{
	    		for(int i=0;i<n;i++)
	    			{
	    			PointsX[i]=(xDif/n)*(i+1)+mx;
	    			PointsY[i]=(yDif/n)*(i+1)+my;
	    			}
	    		}
	  
	    	if(mx==xx1 && my==yy2)
	    		{
	    		for(int i=0;i<n;i++)
	    			{
	    			PointsX[i]=(xDif/n)*(i+1)+mx;
	    			PointsY[i]=yy1-(yDif/n)*(i+1);
	    			}
	    		}
	  
	    	if(mx==xx2 && my==yy1)
	    		{
	    		for(int i=0;i<n;i++)
	    			{
	    			PointsX[i]=(xDif/n)*(i+1)+mx;
	    			PointsY[i]=yy2-(yDif/n)*(i+1);
	    			}
	    		}
	  
	    	if(mx==xx2 && my==yy2)
	    		{
	    		for(int i=0;i<n;i++)
	    			{
	    			PointsX[i]=(xDif/n)*(i+1)+mx;
	    			PointsY[i]=(yDif/n)*(i+1)+my;
	    			}
	    		}
	  
	    	int[][] Points=new int[n][2];
	    	for(int i=0;i<n;i++)
	    		{
	    		Points[i][0]=PointsX[i];
	    		Points[i][1]=PointsY[i];
	    		}
	    	return Points;
	    }
  
    Card(int x1,int y1,int x2,int y2,int x3,int y3,int x4,int y4,int id,int db_id,int owner,PApplet p,Game gm)
    {
    	parent=p;
    	isDead=false;
    	frame=true;							//czy ma byc ramka
    	loc=new PVector[4];	
    	loc2=new PVector[4];	//lokalizacja
    	r=0;g=0;b=255;
    	skull = parent.loadImage("xbones-black.png");
    	this.game=gm;
    	
    	
    	loc[0]=new PVector(x1,y1);
    	loc[1]=new PVector(x2,y2);
    	loc[2]=new PVector(x3,y3);
    	loc[3]=new PVector(x4,y4);
    	
    	
    	
    	loc2[0]=new PVector(x1,y1);
    	loc2[1]=new PVector(x2,y2);
    	loc2[2]=new PVector(x3,y3);
    	loc2[3]=new PVector(x4,y4);
    	
    	if(loc[0].x>=loc[3].x && loc[0].y<=loc[3].y) direction=1;
    	else if(loc[0].x>=loc[3].x && loc[0].y>=loc[3].y) direction=2;
    	else if(loc[0].x<=loc[3].x && loc[0].y>=loc[3].y) direction=3;
    	else direction=4;
    	
    	center=new PVector((loc[0].x+loc[1].x+loc[2].x+loc[3].x)/4,(loc[0].y+loc[1].y+loc[2].y+loc[3].y)/4);

	    
		float dist[]=new float[4];
		float mindist;
		int i;
    	for(i=0;i<4;i++)
    	{
    		
    		dist[i]=parent.dist(this.loc[i].x, this.loc[i].y, 0, 0);
    		
    	}
    	mindist=parent.min(dist);
    	
    	for(i=0;i<4;i++)
    	{
    		if(parent.dist(this.loc[i].x, this.loc[i].y, 0, 0)==mindist) {LG=loc[i]; break;}
    	}
    	i++;
    	PG=loc[i%4];
    	i++;
    	PD=loc[i%4];
    	i++;
    	LD=loc[i%4];
    	
	    this.id=id;
	    this.db_id=db_id;
	    this.owner=owner;
	    se=new ArrayList<SparkEdge>();

	    int[][] T=this.divideEdge(1, 10);		//dzielenie i tworzenie krawedzi z generatorami
	    se.add(new SparkEdge(T,p));
	    T=this.divideEdge(2, 10);
	    se.add(new SparkEdge(T,p));
	    T=this.divideEdge(3, 10);
	    se.add(new SparkEdge(T,p));
	    T=this.divideEdge(4, 10);
	    se.add(new SparkEdge(T,p));

	    PVector acc=null;
	    PVector vel=null;
	  
	    this.manaCost=2;
	    for(i=0;i<se.size();i++)
	    	{
	    	//parent.println("SE");
	    	SparkEdge e=se.get(i);
	    
	    	e.changeSparkType('c',255,0,0,6,vel,acc);
	    	}
	    
    }

}
