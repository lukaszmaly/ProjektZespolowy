package MTGPackage;
import processing.core.*;

public class Board 
{
	PVector graveyard1;
	 PApplet parent;
	 Board(PApplet p)
	 {
		 parent=p;
	 }
	 public void display(int color,int t, int w)         //Wyswietlanie pol
	 {
		 
		// this.drawFrame(color,2*w);
		 this.drawGraveyardExile(color,w);
		 //this.drawManaFields(t+60);
		// this.drawBattlefield(t);
		 //this.drawCombat(t);
		 this.drawStack(color, w);
		 this.drawField();
		 
	 }
	 public void drawField()
	 {
		 parent.fill(255);
		 parent.stroke(255);
		 parent.rectMode(parent.CORNERS);
		 parent.rect(225, 10, parent.width-225, parent.height-10, 10, 10, 10, 10);
		 parent.rectMode(parent.CORNER);
		 
	 }
	 public void drawStack(int color, int weigth)
	 {
		 
		 parent.strokeWeight(weigth);
		 parent.stroke(color);
		 parent.fill(255);
		 parent.rect(0.83f*parent.width,0.35f*parent.height,150,200);
	 }
	 public void drawFrame(int color, int weigth)
	 {
		 parent.strokeWeight(weigth);
		 parent.stroke(color);
		 parent.line(0,0,parent.width,0);
		 parent.line(0,0,0,parent.height);
		  
		 parent.line(parent.width,0,parent.width,parent.height);
		 parent.line(0,parent.height,parent.width,parent.height);
	 }
	 
	 public void drawGraveyardExile(int color, int weight)
	 {
		 this.graveyard1=new PVector(0.07f*parent.width,0.05f*parent.height);
		 parent.stroke(color);
		 parent.strokeWeight(weight);
		 
		 parent.noFill();
		 parent.rect(0.07f*parent.width,0.05f*parent.height,0.06f*parent.width,0.125f*parent.height,7,7,7,7);
		 parent.rect(0.07f*parent.width,0.2f*parent.height,0.06f*parent.width,0.125f*parent.height,7,7,7,7);

		 parent.rect(0.07f*parent.width,0.95f*parent.height-0.125f*parent.height,0.06f*parent.width,0.125f*parent.height,7,7,7,7);
		 parent.rect(0.07f*parent.width,0.8f*parent.height-0.125f*parent.height,0.06f*parent.width,0.125f*parent.height,7,7,7,7); 
		 parent.line(0.07f*parent.width,0.05f*parent.height,0.07f*parent.width+100,0.05f*parent.height);
		 parent.fill(255,0,0);
		 parent.rect(0, 0, 250, 250);
	 }
	 
	 public void drawManaFields(int t)
	 {
		 float x1=0.2f; float x2=0.8f;
		 float y1=0.05f;float  y2=0.2f;
		 float y3=0.8f; float y4=0.95f;
	    
		 parent.fill(255);
		 parent.stroke(255);
		 parent.strokeWeight(5);
		 parent.rect(x1*parent.width,y1*parent.height,0.6f*parent.width,parent.height*0.15f,6,6,0,0);
		 parent.rect(x1*parent.width,y3*parent.height,0.6f*parent.width,parent.height*0.15f,0,0,6,6);
	    
		 parent.stroke(245,238,32,t+30);
		 parent.rect(x1*parent.width,y1*parent.height,0.6f*parent.width,parent.height*0.15f,6,6,0,0);
		 parent.rect(x1*parent.width,y3*parent.height,0.6f*parent.width,parent.height*0.15f,0,0,6,6); 
	 }
	  	 
	 public void drawBattlefield(int t)
	 {
		 float x1=0.2f; float x2=0.8f;
		 float y1=0.2f;float  y2=0.35f;
		 float y3=0.65f; float y4=0.8f;
		 int framet=20;
		 parent.noStroke();
		 parent.strokeWeight(5);
		 parent.rect(x1*parent.width,y1*parent.height,0.6f*parent.width,parent.height*0.15f);
		 parent.rect(x1*parent.width,y3*parent.height,0.6f*parent.width,parent.height*0.15f);
		  
		 parent.strokeWeight(5);
		 parent.stroke(255,255,255);
		 parent.line(x1*parent.width,y1*parent.height,x2*parent.width,y1*parent.height);
		  
		 parent.stroke(0,255,0,t+framet);
		 parent.line(x1*parent.width,y1*parent.height,x2*parent.width,y1*parent.height);
		  
		 parent.stroke(255,255,255);
		 parent.line(x1*parent.width,y2*parent.height,x2*parent.width,y2*parent.height);
		  
		 parent.stroke(0,255,0,t+framet);
		 parent.line(x1*parent.width,y2*parent.height,x2*parent.width,y2*parent.height);
		  
		 parent.stroke(255,255,255);
		 parent.line(x1*parent.width,y3*parent.height,x2*parent.width,y3*parent.height);
		  
		 parent.stroke(0,255,0,t+framet);
		 parent.line(x1*parent.width,y3*parent.height,x2*parent.width,y3*parent.height);
		  
		 parent.stroke(255,255,255);
		 parent.line(x1*parent.width,y4*parent.height,x2*parent.width,y4*parent.height);
		  
		 parent.stroke(0,255,0,t+framet);
		 parent.line(x1*parent.width,y4*parent.height,x2*parent.width,y4*parent.height);
		  
		 parent.stroke(255,255,255);
		 parent.line(x1*parent.width,y1*parent.height,x1*parent.width,y2*parent.height);
		  
		 parent.stroke(0,255,0,t+framet);
		 parent.line(x1*parent.width,y1*parent.height,x1*parent.width,y2*parent.height);
		  
		 parent.stroke(255,255,255);
		 parent.line(x2*parent.width,y1*parent.height,x2*parent.width,y2*parent.height);
		  
		 parent.stroke(0,255,0,t+framet);
		 parent.line(x2*parent.width,y1*parent.height,x2*parent.width,y2*parent.height);
		  
		 parent.stroke(255,255,255);
		 parent.line(x1*parent.width,y3*parent.height,x1*parent.width,y4*parent.height);
		  
		 parent.stroke(0,255,0,t+framet);
		 parent.line(x1*parent.width,y3*parent.height,x1*parent.width,y4*parent.height);
		  
		 parent.stroke(255,255,255);
		 parent.line(x2*parent.width,y3*parent.height,x2*parent.width,y4*parent.height);
		  
		 parent.stroke(0,255,0,t+framet);
		 parent.line(x2*parent.width,y3*parent.height,x2*parent.width,y4*parent.height);
	 }
	 
	 public void drawCombat(int t)
	 {
		 int framet=20;
		 parent.noStroke();
		 parent.strokeWeight(5);
		 parent.rect(0.2f*parent.width,0.35f*parent.height,0.6f*parent.width,parent.height*0.15f);
		 parent.rect(0.2f*parent.width,0.5f*parent.height,0.6f*parent.width,parent.height*0.15f);
	  
		 parent.strokeWeight(5);
		 parent.stroke(255,255,255);
		 parent.line(0.2f*parent.width,0.35f*parent.height,0.8f*parent.width,0.35f*parent.height);
	  
		 parent.stroke(255,0,0,t+framet);
		 parent.line(0.2f*parent.width,0.35f*parent.height,0.8f*parent.width,0.35f*parent.height);
	  
	  
		 parent.stroke(255,255,255);
		 parent.line(0.2f*parent.width,0.35f*parent.height,0.2f*parent.width,0.65f*parent.height);
	  
		 parent.stroke(255,0,0,t+framet);
		 parent.line(0.2f*parent.width,0.35f*parent.height,0.2f*parent.width,0.65f*parent.height);
	  
	  
		 parent.stroke(255,255,255);
		 parent.line(0.2f*parent.width,0.65f*parent.height,0.8f*parent.width,0.65f*parent.height);
	  
		 parent.stroke(255,0,0,t+framet);
		 parent.line(0.2f*parent.width,0.65f*parent.height,0.8f*parent.width,0.65f*parent.height);
	   
	   
	  
		 parent.stroke(255,255,255);
		 parent.line(0.8f*parent.width,0.35f*parent.height,0.8f*parent.width,0.65f*parent.height);
	  
		 parent.stroke(255,0,0,t+framet);
		 parent.line(0.8f*parent.width,0.35f*parent.height,0.8f*parent.width,0.65f*parent.height);

		 parent.stroke(255,255,255);
		 parent.line(0.2f*parent.width,0.5f*parent.height,0.8f*parent.width,0.5f*parent.height);
	  
		 parent.stroke(255,0,0,t+framet);
		 parent.line(0.2f*parent.width,0.5f*parent.height,0.8f*parent.width,0.5f*parent.height);
	 }
	 
}