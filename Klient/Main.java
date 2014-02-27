package MTGPackage;

import hypermedia.net.UDP;
import processing.core.*;

import java.util.*;

import MTGPackage.Effect.Type;


public class Main extends PApplet
	{
	PFont f;
	int x1,x2,y1,y2;
	
	PImage img;
	SparkSystem ss,start;
	Board b;
	UDP udp; 
	int counter=0;
	ArrayList<Card> Cards=new ArrayList<Card>();
	ArrayList<Effect> Effects=new ArrayList<Effect>();

	int[][] T;
	
		
	
	public void setup() {	
		
	    size(displayWidth,displayHeight);
	    background(0);
	    f=new PFont();
	    f=createFont("Arial",200,true);
	    frameRate(60);
	    udp = new UDP( this,6121);
	    udp.listen( true );
	    b=new Board(this);
	  
	  Effects.add(new Effect(this));
	  Effects.get(0).type=Type.START;
	  Effects.get(0).life=100;
	  start=new SparkSystem(new PVector(0,0),this);
  start.sparkType='e';
	  start.r=220+(int)random(40);
	  
			  start.g=220+(int)random(40);
					  start.b=50+(int)random(40);
					  start.size=12;
					  
					 // start.v=new PVector(random(0.2f)-0.1f,random(0.2f)-0.1f);
					 // start.a=new PVector(random(-5f,5f),random(-5,5));
					  start.v=new PVector(0.3f,0.3f);
					  start.a=new PVector(3f,3f);
					 start.life=40;
					 
						
	    
	  }

	  public void draw() {
		 if(counter<255) counter++;
		  
			   
		  if(Effects.get(0).type==Type.START && Effects.get(0).life>0)
			
		  {
			  background(0);
			// fill(start.r=220+(int)random(40),start.r=220+(int)random(40),start.r=0+(int)random(40),200-4*Effects.get(0).life);

			  Effects.get(0).life--;
			  if(start.sparks.isEmpty()!=true) start.run();
			   
			   if(Effects.get(0).life>70)
			   {
				   
				 start.source=new PVector(random(width),random(height));
				 start.addParticle();  
				 start.source=new PVector(random(width),random(height));
				 start.addParticle();  
				 start.source=new PVector(random(width),random(height));
				 start.addParticle(); 
				 
				
			   }
			   if(Effects.get(0).life>60)
			   {
				   
				   start.source=new PVector(random(width),random(height));
					 start.addParticle(); 
					 start.source=new PVector(random(width),random(height));
					 start.addParticle();
			   }
			   if(Effects.get(0).life>50)
			   {
				   
				   start.source=new PVector(random(width),random(height));
					 start.addParticle(); 
					 
					 
			   }
			   
			   if(Effects.get(0).life<50)
			   {
				   textFont(f);
				   fill(start.r=220+(int)random(40),start.r=220+(int)random(40),start.r=50+(int)random(40),200-4*Effects.get(0).life);

				   textAlign(CENTER);
				   text("ATMagic",width/2,height/2);
				  
				 
			   }
				
		  }
		  else
		  {
			  fill(start.r=220+(int)random(40),start.r=220+(int)random(40),start.r=(int)random(40));

				  background(0);
				  b.display(counter,40,4);
				  

		  
	    
			  
	    
	    if(Cards.isEmpty()==false)
	    {
	    	for(int i=0;i<Cards.size();i++)
	    	{
	    		Card c=Cards.get(i);
	    		stroke(255,0,0);
	    		strokeWeight(5);
	    		if(c.frame==true)
	    			c.drawEdges(255,0,0,50);
	    		//img=loadImage(c.db_id+".jpg");
	    		//int h=img.height;
	    	//	int w=img.width;
	    		
	    	//	float scaleX=((float)c.height())/h;
	    	//	float scaleY=((float)c.width())/w;
	    	//	pushMatrix();
	    	//	translate(c.x1, c.y1);
	    	//	scale(scaleY,scaleX);
	    	//	image(img, 0, 0);
	    		
	    	//	popMatrix();

	    			    		
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
	    		    		//ss.sparkType='e';
	    		    		//ss.r=0;
	    		    		//ss.g=0;
	    		    		//ss.b=250;
	    		    		println(ss.source.x,",",ss.source.y);
	    		    		//if(c.sparkTime>0 && millis()%2==0) ss.addParticle();
	    		    		if(c.sparkTime>0 ) ss.addParticle();
	    		    	ss.run();
	    		    	}
	    			}
	    			
	    		}
	    		c.sparkTime--;
	    	}
	    	
	    }
	 
	    
	    }
	  }
	  public void keyPressed()
	  {
		  
		 // loop();
		  
		  
switch(key){
			

case 's':  Effects.get(0).life=100;
		  fill(start.r=220+(int)random(40),start.r=220+(int)random(40),start.r=(int)random(40),200-4*Effects.get(0).life);
		  break;
case '1': 	Cards.get(1).frame=true; break;
case '2': 	Cards.get(2).frame=true; break;
case '3': 	Cards.get(3).frame=true; break;
//case 'm': 	Cards.get(1).move(new PVector(50,50));

			
			
			

		  
	  }
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
								c.drawEdges(255,0,0,50);
								
				 }
			 }
			 break;
		 }
	  }
	  public void mousePressed()
	  {
		  int id=1;
		  for(int i=0;i<Cards.size();i++)
			  if(Cards.get(i).id>=id) id=Cards.get(i).id+1;
		  Cards.add(new Card(mouseX,mouseY,mouseX+100,mouseY,mouseX+100,mouseY+150,mouseX,mouseY+150,id,id,0,this));
		  
	  }

public static void main(String args[]) {
    PApplet.main(new String[] { "--present", "MTGPackage.Main" });
  }
}

