package MTGPackage;
import processing.core.*;

import java.util.*;

public class Card
{
	PApplet parent;
	
	PVector[] loc;													//Polozenie 4 rogow
	PVector LG,PG,LD,PD,center;											//Polozenie 4 rogow
	PVector[] loc2;	
	//Polozenie przed atakiem	
	ArrayList<SparkEdge> se;										//4 krawedzie x 10 generatorow	
	int sparkTime,blockedId=-1, frameLife=100,frameRate=1,deadCounter=40,direction; 	//sparking po wejsciu karty, jasnosc  frame'a	
	boolean frame,frameCounter=false,isDead=false,attack=false,block=false,isBlocked=false;		//Czy ma byc ramka, czy ma sie rozjasniac czy zaciemniac
	int r,g,b,id, db_id, owner,manaCost;
	float a=0,c=0;
	int power=-1,toughness=-1;
	float sin=0,asin=0;
	PFont f;
	PImage skull;
	Game game;
	
	void drawEdges()					//Rysowanie ramki t=transparency
	{
		if(frameLife<0) frameCounter=true;//Zaciemnianie lub pojasnianie
		if(frameLife>100) frameCounter=false;	
		if(frameCounter==true) frameLife+=frameRate; else frameLife-=frameRate;

			this.a=parent.abs(loc[0].x-loc[3].x);
			this.c=parent.dist(loc[0].x,loc[0].y,loc[3].x,loc[3].y);
			sin=a/c;
			asin=parent.asin(sin);
		
			parent.noFill();
			parent.pushMatrix();
			
			parent.translate(this.center.x, this.center.y);
			
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
			parent.stroke(r,g,b,40);
			parent.strokeWeight(5);
		
			parent.rectMode(parent.CENTER);
			parent.imageMode(parent.CENTER);
			if((this.owner==1 && game.P1.isLocal==true) ||(this.owner==2 && game.P2.isLocal==true))
			parent.rect(0,0,game.cardWidth*1.2f,game.cardHeight*1.1f,20,20,20,20);
			else parent.image(game.dbCards.get(this.db_id), 0, 0,game.cardWidth,game.cardHeight);
		
			parent.imageMode(parent.CORNER);

			parent.popMatrix();
			
			if(this.attack==true)
			{
				PVector returnCenter=new PVector((loc2[0].x+loc2[1].x+loc2[2].x+loc2[3].x)/4,(loc2[0].y+loc2[1].y+loc2[2].y+loc2[3].y)/4);
				//PVector returnCenter=new PVector((loc2[0].x+loc2[1].x+loc2[2].x+loc2[3].x)/4+200,(loc2[0].y+loc2[1].y+loc2[2].y+loc2[3].y)/4);
				//parent.tint(255,(frameLife)+75);
				parent.pushMatrix();
				parent.translate(returnCenter.x-game.cardWidth/2, returnCenter.y-game.cardWidth/2);
				parent.image(game.ret,0,0,game.cardWidth,game.cardWidth);
				parent.tint(255);
				parent.popMatrix();
	
			}
			if((this.owner==1 && game.P1.isLocal==true) ||(this.owner==2 && game.P2.isLocal==true))

			{
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
			}
	
			parent.strokeWeight(1);
			if(this.toughness!=-1 && this.power!=-1)
			{ 
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
			//parent.stroke(20);
			//parent.ellipse(0,0,0.25f*game.cardHeight+10,0.25f*game.cardWidth+10);

			for(int i=10;i>0;i--)
			{
				parent.stroke(255,0,0,i*10);
				parent.ellipse(0,0,0.25f*game.cardHeight+10-1*i,0.25f*game.cardWidth+10-1*i);
				
			}
			
				
			parent.textFont(game.stats);
			parent.fill(255,0,0);
			parent.textAlign(parent.CENTER,parent.CENTER);
			parent.text(this.power, 0,-3);
			parent.translate(game.cardWidth*1.2f,0);
			
			parent.noStroke();
			parent.fill(255);
			parent.ellipse(0,0,0.25f*game.cardHeight,0.25f*game.cardWidth);
			parent.noFill();
			for(int i=10;i>0;i--)
			{
				parent.stroke(0,0,255,i*10);
				parent.ellipse(0,0,0.25f*game.cardHeight+10-1*i,0.25f*game.cardWidth+10-1*i);
				
			}
			
			parent.fill(0,0,255);
			parent.text(this.toughness, 0,-3);
			parent.popMatrix();
			}
			
		if(this.isDead==true)
		{	
			this.deadCounter--;
			
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
  
    Card(int x1,int y1,int x2,int y2,int x3,int y3,int x4,int y4,int id,int db_id,int owner,PApplet p,Game gm,int power, int toughness)
    {
    	parent=p;
    	isDead=false;
    	frame=true;	

    	this.sparkTime=10; 
    	loc=new PVector[4];	
    	loc2=new PVector[4];	
    	r=0;g=255;b=0;
    
    	this.game=gm;
    	this.power=power;
    	this.toughness=toughness;
    	
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
	  
	    for(int i=0;i<se.size();i++)
	    	{
	   
	    	SparkEdge e=se.get(i);
	    
	    	e.changeSparkType('c',40,30,210,6,vel,acc);
	    	}
	    
    }

}
