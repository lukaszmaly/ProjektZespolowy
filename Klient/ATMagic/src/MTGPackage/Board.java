package MTGPackage;
import processing.core.*;

public class Board 
{
	PVector graveyard1;
	PApplet parent;
	int stackR=0, stackG=0,stackB=255;
	int stackCost=9999;
	PFont f;
	PImage img;
	
	Board(PApplet p)
	 {
		 parent=p;
		 img = parent.loadImage("mana-symbols.png");
	 }
	 
	 public void display(int color,int t, int w, Player P1,Player P2,int width, int height)         //Wyswietlanie pol
	 {
		 
		 this.drawFrame(color,2*w);
		 this.drawGraveyardExile(color,w,width,height);
		 //this.drawManaFields(t+60);
		// this.drawBattlefield(t);
		 //this.drawCombat(t);
		 this.drawStack(color, w,width,height);
		 this.drawField();
		 this.drawLife(P1, P2);
		 this.drawMana(P1, P2);
		 this.drawMarkersDetector();
		 
		 
	 }
	 
	 public void drawField()
	 {
		 parent.fill(255);
		 parent.stroke(255);
		 parent.rectMode(parent.CORNERS);
		 parent.rect(225, 10, parent.width-225, parent.height-10, 10, 10, 10, 10);
		 parent.rectMode(parent.CORNER);
		 parent.line(231,10,parent.width-231, 10);
		 
	 }
	   
	 public void drawMarkersDetector()
	 {
		 
		 parent.stroke(255);
		 parent.strokeWeight(1);
		 parent.fill(255);
		 parent.rect(0.88f*parent.width,0.13f*parent.height,100,100,20,20,20,20);
		 parent.rect(0.88f*parent.width,0.74f*parent.height,100,100,20,20,20,20);
		
		 
		 parent.noFill();
		 for(int i=0;i<25;i++)
		 {
			 parent.stroke(255-i*10);
			 
			 parent.rect(0.88f*parent.width-i,0.13f*parent.height-i,100+2*i,100+2*i,20,20,20,20);
			 parent.rect(0.88f*parent.width-i,0.74f*parent.height-i,100+2*i,100+2*i,20,20,20,20);
			
		 }
	 }
	 
	 public void drawStack(int color, int weigth,int width, int height)
	 {
	
		 parent.strokeWeight(1);
		 parent.stroke(stackR,stackG, stackB );
		 
	
		 
parent.rectMode(parent.CENTER);
		 
		 parent.fill(255);
		 int i;
		 for(i=20;i>0;i--)
		 {
			 parent.stroke(stackR,stackG, stackB,20+i*8);
			 parent.rect(parent.width*0.915f,parent.height/2,width*1.5f-2*i,height*1.5f-2*i,100,100,100,100);
			 parent.noFill();
		 }
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
	 
	 public void drawGraveyardExile(int color, int weight,int width, int height)
	 {
		 this.graveyard1=new PVector(0.07f*parent.width,0.05f*parent.height);
		 parent.stroke(color);
		 parent.strokeWeight(weight);
		 parent.fill(255);
		 parent.rect(0.035f*parent.width,0.07f*parent.height,1.1f*width,1.1f*height,20,20,20,20);
		 parent.rect(0.035f*parent.width,0.68f*parent.height,1.1f*width,1.1f*height,20,20,20,20);
		 //parent.rect(0.035f*parent.width,0.68f*parent.height,0.1f*parent.width,0.25f*parent.height,20,20,20,20);
		 
		 parent.noFill();
		 for(int i=0;i<25;i++)
		 {
			 parent.stroke(255-i*10);
			 //parent.rect(0.035f*parent.width-i,0.07f*parent.height-i,0.1f*parent.width+i,0.25f*parent.height+i,20,20,20,20);
			 //parent.rect(0.035f*parent.width-i,0.07f*parent.height-i,0.1f*parent.width+2*i,0.25f*parent.height+2*i,20,20,20,20);
			 //parent.rect(0.035f*parent.width-i,0.68f*parent.height-i,0.1f*parent.width+2*i,0.25f*parent.height+2*i,20,20,20,20);
			 parent.rect(0.035f*parent.width-i,0.07f*parent.height-i,1.1f*width+2*i,1.1f*height+2*i,20,20,20,20);
			 parent.rect(0.035f*parent.width-i,0.68f*parent.height-i,1.1f*width+2*i,1.1f*height+2*i,20,20,20,20);
		 }
			 /*
		 parent.rect(0.07f*parent.width,0.05f*parent.height,0.06f*parent.width,0.125f*parent.height);
		 parent.rect(0.07f*parent.width,0.2f*parent.height,0.06f*parent.width,0.125f*parent.height);

		 parent.rect(0.07f*parent.width,0.95f*parent.height-0.125f*parent.height,0.06f*parent.width,0.125f*parent.height);
		 parent.rect(0.07f*parent.width,0.8f*parent.height-0.125f*parent.height,0.06f*parent.width,0.125f*parent.height); 
		 //parent.line(0.07f*parent.width,0.05f*parent.height,0.07f*parent.width+100,0.05f*parent.height);
		*/ 
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
	 
	 public void drawLife(Player P1,Player P2)
	 {		f=parent.createFont("Comic Sans", 50);
		 	parent.textFont(f);
			parent.fill(255,0,0);
			parent.textAlign(parent.CENTER);
			
			parent.pushMatrix();
			parent.translate(parent.width*0.85f,10);
			parent.rotate(parent.PI);
			parent.text(String.valueOf(P1.life),0,0);
			parent.popMatrix();
			
			parent.text(String.valueOf(P2.life),parent.width*0.85f,parent.height-10);
			
	 
	 }
	 
	 public void drawMana(Player P1,Player P2)
	 {
		 
			f=parent.createFont("Comic Sans", 25,true);
		 	parent.textFont(f);
			//parent.fill(0,0,255);
		 	parent.fill(255);
			parent.textAlign(parent.CENTER);
			
			parent.pushMatrix();
			parent.translate(parent.width*0.99f,40);
			parent.rotate(parent.PI);
			parent.image(img,0,0,150,30);
			parent.text("W",14,-5);
			parent.text("U",44,-5);
			parent.text("B",74,-5);
			parent.text("R",104,-5);
			parent.text("G",134,-5);
			//parent.text(String.valueOf(P1.manaPool),0,0);
			parent.popMatrix();
			//parent.image(img,500,500);
			parent.pushMatrix();
			parent.translate(parent.width*0.99f-150,parent.height-40);
			parent.image(img,0,0,150,30);
			parent.text("W",14,-5);
			parent.text("U",44,-5);
			parent.text("B",74,-5);
			parent.text("R",104,-5);
			parent.text("G",134,-5);
			parent.popMatrix();
			
			
			//parent.text(String.valueOf(P2.manaPool), parent.width*0.95f,parent.height-25);
			
		 /*
		 	f=parent.createFont("Comic Sans", 50);
		 	parent.textFont(f);
			parent.fill(0,0,255);
			parent.textAlign(parent.CENTER);
			parent.text(String.valueOf(P1.manaPool), parent.width*0.95f,50);
			parent.text(String.valueOf(P2.manaPool), parent.width*0.95f,parent.height-25);
			*/
	 }
}