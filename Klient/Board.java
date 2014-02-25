package MTGPackage;
import processing.core.*;

public class Board {
	 PApplet parent;

	 
	 
	 Board(PApplet p)
	 {
		 parent=p;
 
		 
	 }
	  public void display(int color,int t, int w)
	  {
		 
			  
		  this.drawFrame(color,2*w);
		  this.drawGraveyardExile(color,w);
		  this.drawManaFields(t+60);
		  this.drawBattlefield(t);
		  this.drawCombat(t);
		
		  
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
		 
		 parent.stroke(color);
		 parent.strokeWeight(weight);
		 
		 parent.noFill();
		parent.rect(0.07f*parent.width,0.05f*parent.height,0.06f*parent.width,0.125f*parent.height,7,7,7,7);
		parent.rect(0.07f*parent.width,0.2f*parent.height,0.06f*parent.width,0.125f*parent.height,7,7,7,7);

		parent.rect(0.07f*parent.width,0.95f*parent.height-0.125f*parent.height,0.06f*parent.width,0.125f*parent.height,7,7,7,7);
		parent.rect(0.07f*parent.width,0.8f*parent.height-0.125f*parent.height,0.06f*parent.width,0.125f*parent.height,7,7,7,7); 
	 }
	 
	 public void drawManaFields(int t)
	 {
	     
	       float x1=0.2f; float x2=0.8f;
	    float y1=0.05f;float  y2=0.2f;
	    float y3=0.8f; float y4=0.95f;
	    //int t=100; //transparency
	   // parent.fill(0);
	    //parent.noFill();
	    parent.fill(255);
	    parent.noStroke();
	  //  parent.fill(255,258,32,t);
	    parent.stroke(255);
	    parent.strokeWeight(5);
	    parent.rect(x1*parent.width,y1*parent.height,0.6f*parent.width,parent.height*0.15f,6,6,0,0);
	    parent.rect(x1*parent.width,y3*parent.height,0.6f*parent.width,parent.height*0.15f,0,0,6,6);
	    
	    parent.stroke(245,238,32,t+30);
	    parent.rect(x1*parent.width,y1*parent.height,0.6f*parent.width,parent.height*0.15f,6,6,0,0);
	    parent.rect(x1*parent.width,y3*parent.height,0.6f*parent.width,parent.height*0.15f,0,0,6,6);
	  
	    parent.strokeWeight(5);
	    parent.stroke(255,255,255);
	  //  parent.line(x1*parent.width,y1*parent.height,x2*parent.width,y1*parent.height);
	  
	    parent.stroke(245,238,32,t+30);   // parent.line(x1*parent.width,y1*parent.height,x2*parent.width,y1*parent.height);
	  
	    parent.stroke(255,255,255);
	  //  parent.line(x1*parent.width,y2*parent.height,x2*parent.width,y2*parent.height);
	  
	    parent.stroke(245,238,32,t+30);   // parent.line(x1*parent.width,y2*parent.height,x2*parent.width,y2*parent.height);
	  
	    parent.stroke(255,255,255);
	  //  parent.line(x1*parent.width,y3*parent.height,x2*parent.width,y3*parent.height);
	  
	    parent.stroke(245,238,32,t+30);   // parent.line(x1*parent.width,y3*parent.height,x2*parent.width,y3*parent.height);
	  
	    parent.stroke(255,255,255);
	   // parent.line(x1*parent.width,y4*parent.height,x2*parent.width,y4*parent.height);
	  
	    parent.stroke(245,238,32,t+30);//    parent.line(x1*parent.width,y4*parent.height,x2*parent.width,y4*parent.height);
	  
	    parent.stroke(255,255,255);
	 //   parent.line(x1*parent.width,y1*parent.height,x1*parent.width,y2*parent.height);
	  
	    parent.stroke(245,238,32,t+30);   // parent.line(x1*parent.width,y1*parent.height,x1*parent.width,y2*parent.height);
	  
	    parent.stroke(255,255,255);
	    //parent.line(x2*parent.width,y1*parent.height,x2*parent.width,y2*parent.height);
	  
	    parent.stroke(245,238,32,t+30);  //  parent.line(x2*parent.width,y1*parent.height,x2*parent.width,y2*parent.height);
	  
	    parent.stroke(255,255,255);
	   // parent.line(x1*parent.width,y3*parent.height,x1*parent.width,y4*parent.height);
	  
	    parent.stroke(245,238,32,t+30);   // parent.line(x1*parent.width,y3*parent.height,x1*parent.width,y4*parent.height);
	  
	    parent.stroke(255,255,255);
	    //parent.line(x2*parent.width,y3*parent.height,x2*parent.width,y4*parent.height);
	  
	    parent.stroke(245,238,32,t+30);    //parent.line(x2*parent.width,y3*parent.height,x2*parent.width,y4*parent.height);
	  
	 
	   // parent.rect(x1*parent.width,y1*parent.height,0.6f*parent.width,parent.height*0.15f,6,6,6,6);
	 
	    }
	  
	 
	 public void drawBattlefield(int t)
	 {
		 
		      
		       float x1=0.2f; float x2=0.8f;
		    float y1=0.2f;float  y2=0.35f;
		    float y3=0.65f; float y4=0.8f;
		    //int t=40; //transparency
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
	      //int t=40; //transparency
	      int framet=20;
	      //stroke(255,0,0,t+30);
	      parent.noStroke();
	    //  parent.fill(219,42,11,t);
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