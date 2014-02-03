package MTGPackage;
import processing.core.*;
import java.util.*;

public class Card
{
	PApplet parent;
  int x1, y1, x2, y2, x3, y3, x4, y4;
  ArrayList<SparkEdge> se;
  int sparkTime=40;
  int id;
  int db_id;
  int owner;
  
  int height()
  {
	
	int[] Y= {y1,y2,y3,y4};
	return(parent.max(Y)-parent.min(Y));
  }
  
  int width()
  {
	int[] X= {x1,x2,x3,x4};
	return(parent.max(X)-parent.min(X));
	
	
  }
  
  void drawEdges(int c)
  {
	  /*
	  parent.strokeWeight(4);
	  int counter=c%5;
	  parent.println(counter);
	  for(int i=1;i<5;i++)
	  {
	  int[][] section=this.divideEdge(i, 5);
	
	  
	  parent.println(section[counter][0]);
	  if(counter==4) parent.line(section[counter][0],section[counter][1] , section[counter+1][0],section[counter+1][1]);
		  parent.line(section[counter][0],section[counter][1] , section[counter+1][0],section[counter+1][1]);
	  
		  
	  parent.line(section[counter][0],section[counter][1] , section[counter+1][0],section[counter+1][1]);
	  }
	*/
	  parent.line(x1, y1, x2, y2);
	  parent.line(x2, y2, x3, y3);
	  parent.line(x3, y3, x4, y4);
	  parent.line(x4, y4, x1, y1);
  }
  
    int[][] divideEdge(int section,int n) //odcinek 1..4, ilosc podzialow
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
  
  parent.stroke(0);
  parent.strokeWeight(2);
  
  parent.stroke(255);
  parent.strokeWeight(7);
  
  int[][] T=this.divideEdge(1, 5);
  
 
  se.add(new SparkEdge(T,p));
  T=this.divideEdge(2, 5);
  
 
 se.add(new SparkEdge(T,p));
 T=this.divideEdge(3, 5);
 
 
 se.add(new SparkEdge(T,p));
 T=this.divideEdge(4, 5);
/*
 se.add(new SparkEdge(T,p));
*/
  
  }

}
