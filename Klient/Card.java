package MTGPackage;
import processing.core.*;

import java.util.*;

public class Card
{
	PApplet parent;
	PVector[] loc;							//Polozenie 4 rogow
	int x1, y1, x2, y2, x3, y3, x4, y4;		//Polozenie 4 rogow
	ArrayList<SparkEdge> se;				//4 krawedzie x 10 generatorow
	int sparkTime=20, frameLife=0; 			//sparking po wejsciu karty, jasnosc  frame'a
	int id, db_id, owner;
	boolean frame,frameCounter=false;		//Czy ma byc ramka, czy ma sie rozjasniac czy zaciemniac

  
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
		this.loc[0].x=m.x;
		this.loc[0].y=m.y;
		this.loc[1].x=m.x;
		this.loc[1].y=m.y;
		this.loc[2].x=m.x;
		this.loc[2].y=m.y;
		this.loc[3].x=m.x;
		this.loc[3].y=m.y;
	}
	
	void drawEdges(int r,int g, int b,int t)//Rysowanie ramki t=transparency
	{
		if(frameLife==0) frameCounter=true;//Zaciemnianie lub pojasnianie
		if(frameLife>100) frameCounter=false;	
		if(frameCounter==true) frameLife++; else frameLife--;

		parent.stroke(r,g,b,(int)(frameLife*1.5));
		parent.strokeWeight(6);
		parent.noFill();
		
		parent.beginShape();
		parent.vertex(loc[0].x,loc[0].y);
		parent.vertex(loc[1].x,loc[1].y);
		parent.vertex(loc[2].x,loc[2].y);
		parent.vertex(loc[3].x,loc[3].y);
		parent.vertex(loc[0].x,loc[0].y);
		parent.endShape();
		
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
	  		xx1=this.x1;
	  		xx2=this.x2;
	  		yy1=this.y1;
	  		yy2=this.y2;
		  	}
    	if(section==2)
    		{
    		xx1=this.x2;
    		xx2=this.x3;
    		yy1=this.y2;
    		yy2=this.y3;
    		}
    	if(section==3)
    		{
    		xx1=this.x3;
    		xx2=this.x4;
    		yy1=this.y3;
    		yy2=this.y4;
    		}
    	if(section==4)
    		{
    		xx1=this.x4;
    		xx2=this.x1;
    		yy1=this.y4;
    		yy2=this.y1;
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

  
    Card(int x1,int y1,int x2,int y2,int x3,int y3,int x4,int y4,int id,int db_id,int owner,PApplet p)
    {
    	parent=p;
    	frame=true;							//czy ma byc ramka
    	loc=new PVector[4];					//lokalizacja
	  
    	loc[0]=new PVector(x1,y1);
    	loc[1]=new PVector(x2,y2);
    	loc[2]=new PVector(x3,y3);
    	loc[3]=new PVector(x4,y4);
	 
    	this.x1=x1;
	    this.x2=x2;
	    this.x3=x3;
	    this.x4=x4;
	     
	    this.y1=y1;
	    this.y2=y2;
	    this.y3=y3;
	    this.y4=y4;

	    this.id=id;
	    this.db_id=db_id;
	    this.owner=owner;
	    se=new ArrayList<SparkEdge>();

	    int[][] T=this.divideEdge(1, 5);		//dzielenie i tworzenie krawedzi z generatorami
	    se.add(new SparkEdge(T,p));
	    T=this.divideEdge(2, 5);
	    se.add(new SparkEdge(T,p));
	    T=this.divideEdge(3, 5);
	    se.add(new SparkEdge(T,p));
	    T=this.divideEdge(4, 5);
	    se.add(new SparkEdge(T,p));

	   // PVector acc=new PVector(0.1f,0.1f);
	   // PVector vel=new PVector(2,2);
	    PVector vel=null;
	    PVector acc=null;
	    
	    for(int i=0;i<se.size();i++)
	    	{
	    	SparkEdge e=se.get(i);
	    	e.changeSparkType('e',220,225,80,5,vel,acc);
	    	}
    }

}
