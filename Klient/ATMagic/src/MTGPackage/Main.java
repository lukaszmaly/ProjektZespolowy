package MTGPackage;

import hypermedia.net.UDP;
import processing.core.*;
import java.util.*;
import MTGPackage.Effect.Type;

public class Main extends PApplet
	{
	PFont f;
	
	PImage skull;
	int x1,x2,y1,y2,counter=0;
	SparkSystem ss,start;
	Board b;
	UDP udp; 
	ArrayList<Card> Cards=new ArrayList<Card>();
	ArrayList<Effect> Effects=new ArrayList<Effect>();
	int[][] T;

	public void setup() {	
		
	    size(displayWidth,displayHeight);
	    background(0);
	    f=new PFont();
	    f=createFont("Arial",200,true);
	    frameRate(60);
	    skull=loadImage("skull.jpg");
	    udp = new UDP( this,6121);
	    udp.listen( true );
	    b=new Board(this);
	  
	    Effects.add(new Effect(this));
	   // Effects.get(0).type=Type.START;
	    Effects.get(0).life=100;
	    start=new SparkSystem(new PVector(0,0),this);
	    start.sparkType='c';
	    start.r=220+(int)random(40);
	    start.g=220+(int)random(40);
		start.b=50+(int)random(40);
		start.size=10;
		start.v=null;
		start.a=null;
		
		start.life=100;
	    
	  }

	  public void draw() 
	  	{
		if(counter<255) counter++;
		if(Effects.get(0).type==Type.START && Effects.get(0).life>0)	
			{
			start.v=new PVector(0.5f,0.5f);
			start.a=new PVector(0.05f,0.05f);
			background(0);
			Effect e=Effects.get(0);
			e.life--;
			
			if(start.sparks.isEmpty()!=true) start.run();
			if(e.life>70)
			   	{				
				
				 start.source=new PVector(random(width),random(height));
				 start.addParticle();  
				 start.source=new PVector(random(width),random(height));
				 start.addParticle();  
				
			   }
			if(e.life>60)
				{
				start.source=new PVector(random(width),random(height));
				start.addParticle(); 
				
			   }
			   if(e.life>50)
			   {
				   start.source=new PVector(random(width),random(height));
				   start.addParticle(); 
			   }
			   
			   if(e.life<50)
			   {
				   textFont(f);
				   //fill(start.r=220+(int)random(40),start.r=220+(int)random(40),start.r=50+(int)random(40),200-4*Effects.get(0).life);
				   fill(240,240,70);
				   textAlign(CENTER);
				   text("ATMagic",width/2,height/2);
 
			   }
				
		  }
		  else
		  {
			  //fill(start.r=220+(int)random(40),start.r=220+(int)random(40),start.r=(int)random(40));
			  background(0);
			  b.display(counter,40,4);
   
	    if(Cards.isEmpty()==false)
	    {
	    	for(int i=0;i<Cards.size();i++)
	    	{
	    		
	    		Card c=Cards.get(i);
	    		println(c.id+" "+c.frameRate+c.isDead);
	    		if(c.isDead==true && c.deadCounter<=0)
	    		{
	    			Cards.remove(i);
	    		}
	    		else
	    		{
	    		stroke(255,0,0);
	    		strokeWeight(5);
	    		if(c.frame==true)
	    			c.drawEdges(255,0,0,50);
	    		if(c.isDead==true)
	    		{
	    			c.frameRate=4;
	    			pushMatrix();
	    			translate(c.loc[0].x,c.loc[0].y);
	    			//skull.resize((int)(c.loc[1].x-c.loc[0].x),(int)(c.loc[2].y-c.loc[1].y));
	    			
	    			tint(200-c.deadCounter);
	    			//skull.resize(100, 50);
	    			image(skull,0,0,c.width(),c.height());
	    			popMatrix();
	    			
	    			
	    		}
	    		f=createFont("Arial",10,true);
	    		textFont(f);
	    		String s=	"id: "+c.id+"\ndead:"+c.isDead+"\nframeLife:"+c.frameLife+"\nframeRate:"+c.frameRate+"\nsparkTime"+c.sparkTime+"\nframeCounter:"+c.frameCounter+"\ndeadCounter: "+c.deadCounter;
				   fill(240,240,70);
				   textAlign(CENTER);
				   text(s,(c.loc[0].x+c.loc[1].x)/2,c.loc[0].y+20);
	    		if(c.se.size()>0)
	    		{
	    			for(int j=0;j<c.se.size();j++)
	    			{
	    				SparkEdge se=c.se.get(j);	
	    				for(int k=0;k<se.ss.size();k++)
	    		    	{
	    		    		SparkSystem ss=se.ss.get(k);
	    		    		ss.r=240;
	    		    		ss.g=240;
	    		    		ss.b=20;
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
	  }
	  public void keyPressed()
	  {	  
switch(key){

case 's':  Effects.get(0).life=100;
		  fill(start.r=220+(int)random(40),start.r=220+(int)random(40),start.r=(int)random(40),200-4*Effects.get(0).life);
		  break;
case '1': 	Cards.get(1).frame=true; break;
case '2': 	Cards.get(2).frame=true; break;
case '3': 	Cards.get(3).frame=true; break;
case 'm': 	for(int i=0;i<Cards.size();i++)
				{
				Cards.get(i).moveTo(new PVector(random(width),random(height)));
				}
			break;
case 'd':	if(Cards.isEmpty()==false)
			Cards.get((int)random(Cards.size())).isDead=true;
			break;
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

