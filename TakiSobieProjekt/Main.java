package MTGPackage;

import hypermedia.net.UDP;
import processing.core.*;

import java.util.*;


public class Main extends PApplet
	{
	int x1,x2,y1,y2;
	PImage img;
	SparkSystem ss;
	Board b;
	UDP udp; 
	int counter;
	ArrayList<Card> Cards=new ArrayList<Card>();

	int[][] T;
	
		
	
	public void setup() {	
		
	    size(displayWidth,displayHeight);
	    frameRate(60);
	    udp = new UDP( this,6121);
	    udp.listen( true );
	    b=new Board(this);
	    
	  }

	  public void draw() {
		  //counter++;
	    stroke(100);
	    background(0);
	    b.display();
	    
	    if(Cards.isEmpty()==false)
	    {
	    	for(int i=0;i<Cards.size();i++)
	    	{
	    		Card c=Cards.get(i);
	    		
	    		img=loadImage(c.db_id+".jpg");
	    		int h=img.height;
	    		int w=img.width;
	    		
	    		float scaleX=((float)c.height())/h;
	    		float scaleY=((float)c.width())/w;
	    		pushMatrix();
	    		translate(c.x1, c.y1);
	    		scale(scaleY,scaleX);
	    		image(img, 0, 0);
	    		
	    		popMatrix();

	    		stroke(255,0,0);
	    		strokeWeight(5);	    		
	    	//	if(counter%2==0)
	    		//	c.drawEdges(counter);
	    		if(c.se.size()>0)
	    		{
	    			for(int j=0;j<c.se.size();j++)
	    			{
	    				SparkEdge se=c.se.get(j);	
	    				for(int k=0;k<se.ss.size();k++)
	    		    	{
	    		    		SparkSystem ss=se.ss.get(k);
	    		    		if(c.sparkTime>0 && millis()%2==0) ss.addParticle();
	    		    	ss.run();
	    		    	}
	    			}
	    			
	    		}
	    		c.sparkTime--;
	    	}
	    	
	    }
	  //  if(counter>100) counter=0;
	    
	    }
	
	  public void receive( byte[] data, String ip, int port ) {	 
		  data = subset(data, 0, data.length);
		  String message = new String( data );
		 String[] Dane= message.split(" ");
		 println("nowe");
		 for(int i=0;i<Dane.length;i++)
		  println(i+" " +Dane[i]);
		 /*
		  gupiznak-funkcja-id-idbazy-owner-TAP-8xwspolrzedne 
		  
		  */
		 
		 
		 switch(Dane[1])
		 {
		 case "ADD": 
			 
		//		 Cards.add(new Card(Integer.parseInt(Dane[6]),Integer.parseInt(Dane[7]),Integer.parseInt(Dane[8]),Integer.parseInt(Dane[9]),Integer.parseInt(Dane[10]),Integer.parseInt(Dane[11]),Integer.parseInt(Dane[12]),Integer.parseInt(Dane[13]),Integer.parseInt(Dane[2]),Integer.parseInt(Dane[3]),Integer.parseInt(Dane[4]),this));
			 Cards.add(new Card((int)(Float.parseFloat(Dane[6])*width),(int)(Float.parseFloat(Dane[7])*height),(int)(Float.parseFloat(Dane[8])*width),(int)(Float.parseFloat(Dane[9])*height),(int)(Float.parseFloat(Dane[10])*width),(int)(Float.parseFloat(Dane[11])*height),(int)(Float.parseFloat(Dane[12])*width),(int)(Float.parseFloat(Dane[13])*height),Integer.parseInt(Dane[2]),Integer.parseInt(Dane[3]),Integer.parseInt(Dane[4]),this));	/// EEE MACARENA !!!
			 break;
			
		 case "UPDATE": 
			 int id=Integer.parseInt(Dane[2]);
			 for(int i=0;i<Cards.size();i++)
			 {
				 Card c=Cards.get(i);
				 if(c.id==id) 
				 {
					 c.x1=Integer.parseInt(Dane[6]);
					c.y1=Integer.parseInt(Dane[7]);
					
					 c.x2=Integer.parseInt(Dane[8]);
						c.y2=Integer.parseInt(Dane[9]);
						 c.x3=Integer.parseInt(Dane[10]);
							c.y3=Integer.parseInt(Dane[11]);
							 c.x4=Integer.parseInt(Dane[12]);
								c.y4=Integer.parseInt(Dane[13]);
								c.drawEdges(counter);
								
				 }
			 }
			 break;
		 }
	  }
	  public void mousePressed()
	  {
		  Cards.add(new Card(mouseX,mouseY,mouseX+100,mouseY,mouseX+100,mouseY+150,mouseX,mouseY+150,1,1,0,this));
		  
	  }

public static void main(String args[]) {
    PApplet.main(new String[] { "--present", "MTGPackage.Main" });
  }
}

