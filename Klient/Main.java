package MTGPackage;

import hypermedia.net.UDP;
import processing.core.*;
import java.util.*;
import MTGPackage.Effect.Type;

public class Main extends PApplet
	{
	PFont f;
	
	PImage star,edge1,edge2,edge3,edge4;
	int x1,x2,y1,y2,counter=0;
	SparkSystem ss,start;
	Board b;
	
	UDP udp; 
	ArrayList<Card> Cards=new ArrayList<Card>();
	ArrayList<Effect> Effects=new ArrayList<Effect>();
	int[][] T;
	boolean tokens=true;
	PImage skull=loadImage("skull.jpg");

	public void setup() {	
		
	    size(displayWidth,displayHeight,OPENGL);
	    background(0);
	    f=new PFont();
	    f=createFont("Arial",200,true);
	    frameRate(60);
	    skull=loadImage("skull.jpg");
	    
	    edge1=loadImage("LG.png");
	    edge2=loadImage("PG.png");
	    edge3=loadImage("PD.png");
	    edge4=loadImage("LD.png");
	    star=loadImage("star.png");
	    //skull=loadImage("star.png");
	    udp = new UDP( this,6121);
	    udp.listen( true );
	    b=new Board(this);
	  
	    Effects.add(new Effect(this));
	    //Effects.get(0).type=Type.START;
	    Effects.get(0).life=120;
	    start=new SparkSystem(new PVector(0,0),this);
	    start.sparkType='g';
	    start.r=220+(int)random(40);
	    start.g=220+(int)random(40);
		start.b=50+(int)random(40);
		start.size=10;
		//start.v=new PVector(random(-0.5f,0.5f),random(0.5f));
		//start.a=new PVector(random(-0.05f,0.05f),random(-0.05f,0.05f));
		start.v=null;
		start.a=null;
		
		start.life=100;
	    
	  }

	  public void draw() 
	  	{
		  println(frameRate);
		 
		if(counter<255) counter++;
		if(Effects.get(0).type==Type.START && Effects.get(0).life>0)	
			{
			
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
				   fill(240,240,70,150-2*e.life);
				   textAlign(CENTER);
				   text("ATMagic",width/2,height/2);
 
			   }
				
		  }
		  else
		  {
			  
			  //fill(start.r=220+(int)random(40),start.r=220+(int)random(40),start.r=(int)random(40));
			  background(0);
			  b.display(counter,40,4);
			  /*
			  beginShape();
			    texture(skull);
			    vertex(450, 500, 0, 0);
			    vertex(690, 550, skull.width, 0);
			    vertex(600, 650, skull.width, skull.height);
			    vertex(500, 600, 0, skull.height);
			    endShape();
			    */
			  noFill();
			  stroke(255);
			  strokeWeight(10);
			  /*
			  image(edge1,0,0,100,100);
			  image(edge2,width-100,0,100,100);
			  image(edge3,width-100,height-100,100,100);
			  image(edge4,0,height-100,100,100);
			  */
			  if(tokens==true)
			  {
			  image(edge1,0,0,200,200);
			  image(edge2,width-200,0,200,200);
			  image(edge3,width-200,height-200,200,200);
			  image(edge4,0,height-200,200,200);
			  }
			  /*
			  image(edge1,8,8,67,67);
			  rect(4,4,75,75);

			  image(edge2,width-71,0,67,67);
			  rect(width-71,0,67,67);
			  
			  
			  
			  //rect(width-75,height-75,75,75);
			  image(edge3,width-71,height-71,67,67);
			  
			 // rect(0,height-75,75,75);
			  image(edge4,4,height-71,67,67);
			  
			  
			  /*
			  rect(0,0,75,75);
			  image(edge1,0,0,75,75);
			  
			  
			  rect(width-75,0,75,75);
			  image(edge2,width-75,0,75,75);
			 
			  
			  rect(width-75,height-75,75,75);
			  image(edge3,width-75,height-75,75,75);
			  
			  rect(0,height-75,75,75);
			  image(edge4,0,height-75,75,75);
			  
			  */
			  
			 
   
	    if(Cards.isEmpty()==false)
	    {
	    	for(int i=0;i<Cards.size();i++)
	    	{
	    		
	    		Card c=Cards.get(i);
	    		//println(c.id+" "+c.frameRate+c.isDead);
	    		if(c.isDead==true && c.deadCounter<=0)
	    		{
	    			 Cards.remove(i);
	    		}
	    		
	    		else
	    		{
	    		stroke(255,0,0);
	    		strokeWeight(5);
	    		if(c.frame==true)
	    			c.drawEdges(50);
	    		if(c.attack==true)
	    		{
	    			noFill();
					beginShape();
					vertex(c.loc2[0].x,c.loc2[0].y);
					vertex(c.loc2[1].x,c.loc2[1].y);
					vertex(c.loc2[2].x,c.loc2[2].y);
					vertex(c.loc2[3].x,c.loc2[3].y);
					vertex(c.loc2[0].x,c.loc2[0].y);
					endShape();
					
	    		}
	    		
	    		if(c.isDead==true)
	    		{
	    			fill(0);
	    			 beginShape();
	 			    
	 			    vertex(c.loc[0].x, c.loc[0].y);
	 			    vertex(c.loc[1].x, c.loc[1].y);
	 			    vertex(c.loc[2].x, c.loc[2].y);
	 			    vertex(c.loc[3].x, c.loc[3].y);
	 			    endShape();
	    			 
	    			 beginShape();
			    texture(skull);
			    vertex(c.loc[0].x, c.loc[0].y, 0, 0);
			    vertex(c.loc[1].x, c.loc[1].y, skull.width, 0);
			    vertex(c.loc[2].x, c.loc[2].y, skull.width, skull.height);
			    vertex(c.loc[3].x, c.loc[3].y, 0, skull.height);
			    endShape();
			    
			    /*
	    			c.frameRate=4;
	    			pushMatrix();
	    			translate(c.loc[0].x,c.loc[0].y);
	    			//skull.resize((int)(c.loc[1].x-c.loc[0].x),(int)(c.loc[2].y-c.loc[1].y));
	    			
	    			tint(200-c.deadCounter);
	    			image(skull,0,0,c.width(),c.height());
	    			//image(edge4,0,0,c.width(),c.height());
	    			//image(star,0,0,c.width(),c.height());
	    			
	    			//skull.resize(100, 50);
	    			//int xCenter=(int)abs(c.loc[0].x-c.loc[1].x);
	    			//int yCenter=(int)abs(c.loc[0].y-c.loc[3].y);
	    			//image(skull,0,0);
	    			//image(skull,(c.width()/2)-(int)(0.1*c.width()),(c.height()/2)-(int)(0.1*c.height()),(float)0.2*c.width(),(float)0.2*c.height());
	    			//image(skull,c.loc[0].x+dist(c.loc[0].x,c.loc[0].y,c.loc[1].x,c.loc[1].y),0,c.width()-c.deadCounter,c.height()-(c.deadCounter*(c.height()/c.width())));
	    			popMatrix();
	    			
	    			tint(255);
	    			*/
	    		}
	    		f=createFont("Arial",12,true);
	    		textFont(f);
	    		//String s=	"locx="+c.loc[0].x+" \nlocy="+c.loc[0].y+" \nid: "+c.id+"\ndead:"+c.isDead+"\nframeLife:"+c.frameLife+"\nframeRate:"+c.frameRate+"\nsparkTime"+c.sparkTime+"\nframeCounter:"+c.frameCounter+"\ndeadCounter: "+c.deadCounter+"\nowner: "+c.owner;
				   fill(0,0,0);
				   String s=	"owner: "+c.owner+"\nlocx="+c.loc[0].x+" \nlocy="+c.loc[0].y+" \nid: "+c.id+"\ndead:"+c.isDead+"\nframeLife:"+c.frameLife+"\nframeRate:"+c.frameRate+"\nsparkTime"+c.sparkTime+"\nframeCounter:"+c.frameCounter+"\ndeadCounter: "+c.deadCounter;
				   textAlign(CENTER);
				  // text(s,(c.loc[0].x+c.loc[1].x)/2,c.loc[0].y+10+c.height());
	    		if(c.se.size()>0)
	    		{
	    			for(int j=0;j<c.se.size();j++)
	    			{
	    				SparkEdge se=c.se.get(j);	
	    				for(int k=0;k<se.ss.size();k++)
	    		    	{
	    		    		SparkSystem ss=se.ss.get(k);
	    		    		//ss.r=240;
	    		    		//ss.g=240;
	    		    		//ss.b=20;
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
case 'a':  		
			Card c=Cards.get((int)random(Cards.size()));
			c.attack=true;
			
			
			
			c.loc2[0].x=0;
			c.loc2[0].y=0;
			
			c.loc2[1].x=100;
			c.loc2[1].y=0;
			
			c.loc2[2].x=100;
			c.loc2[2].y=100;
			
			c.loc2[3].x=0;
			c.loc2[3].y=100;
			
			
			break;
case 't':   if(tokens==false) tokens=true; else tokens=false;
			break;
case 'm': 	for(int i=0;i<Cards.size();i++)
				{
				Cards.get(i).moveTo(new PVector(random(width),random(height)));
				}
			break;
case 'd':	if(Cards.isEmpty()==false)
				{
				 c=Cards.get((int)random(Cards.size()));
				c.isDead=true;
				//c.sparkTime=c.deadCounter;
				
				for(int j=0;j<c.se.size();j++)
    			{
    				SparkEdge se=c.se.get(j);	
    				PVector vel=new PVector(c.loc[0].x-b.graveyard1.x,c.loc[0].y-b.graveyard1.y);
    				PVector acc=new PVector(0.05f,0.05f);
    				
    			//	se.changeSparkType('c', 240, 20, 20, 5, vel, acc);
    				for(int k=0;k<se.ss.size();k++)
    		    	{
    		    		SparkSystem ss=se.ss.get(k);
    		    		
    		    		ss.r=240;
    		    		ss.g=255;
    		    		ss.b=20;
    		    		if(c.sparkTime>0 ) ss.addParticle();
    		    	ss.run();
    		    	}
    			}
				
				}
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
			 //Cards.add(new Card((int)(Float.parseFloat(Dane[6])*width),(int)(Float.parseFloat(Dane[7])*height),(int)(Float.parseFloat(Dane[8])*width),(int)(Float.parseFloat(Dane[9])*height),(int)(Float.parseFloat(Dane[10])*width),(int)(Float.parseFloat(Dane[11])*height),(int)(Float.parseFloat(Dane[12])*width),(int)(Float.parseFloat(Dane[13])*height),Integer.parseInt(Dane[2]),Integer.parseInt(Dane[3]),Integer.parseInt(Dane[4]),this));	/// EEE MACARENA !!!
			 Cards.add(new Card((int)(Float.parseFloat(Dane[6])),(int)(Float.parseFloat(Dane[7])),(int)(Float.parseFloat(Dane[8])),(int)(Float.parseFloat(Dane[9])),(int)(Float.parseFloat(Dane[10])),(int)(Float.parseFloat(Dane[11])),(int)(Float.parseFloat(Dane[12])),(int)(Float.parseFloat(Dane[13])),Integer.parseInt(Dane[2]),Integer.parseInt(Dane[3]),Integer.parseInt(Dane[4]),this));
			 break;
			
		 case "UPDATE": 
			 int id=Integer.parseInt(Dane[2]);
			 
			 for(int i=0;i<Cards.size();i++)
			 	{
				Card c=Cards.get(i);
				if(c.id==id) c.attack=false;
				c.r=255;
				c.g=0;
				c.b=0;
						
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
					
					c.loc[0].x=Integer.parseInt(Dane[6]);
					c.loc[0].y=Integer.parseInt(Dane[7]);
					c.loc[1].x=Integer.parseInt(Dane[8]);
					c.loc[1].y=Integer.parseInt(Dane[9]);
					c.loc[2].x=Integer.parseInt(Dane[10]);
					c.loc[2].y=Integer.parseInt(Dane[11]);
					c.loc[3].x=Integer.parseInt(Dane[12]);
					c.loc[3].y=Integer.parseInt(Dane[13]);
					
					c.drawEdges(50);
					
				 }
				
			 }
			 break;
		 case "ATTACK":
			 id=Integer.parseInt(Dane[2]);
			 for(int i=0;i<Cards.size();i++)
			 	{
				Card c=Cards.get(i);
				if(c.id==id) 
					{
					c.r=0;c.g=255;c.b=0;
					
					
					c.loc2[0].x=c.loc[0].x;
					c.loc2[0].x=c.loc[0].y;
					
					c.loc2[1].x=c.loc[1].x;
					c.loc2[1].x=c.loc[1].y;
					
					c.loc2[2].x=c.loc[2].x;
					c.loc2[2].x=c.loc[2].y;
					
					c.loc2[3].x=c.loc[3].x;
					c.loc2[3].x=c.loc[3].y;
					
					
					c.loc[0].x=Integer.parseInt(Dane[6]);
					c.loc[0].y=Integer.parseInt(Dane[7]);
					c.loc[1].x=Integer.parseInt(Dane[8]);
					c.loc[1].y=Integer.parseInt(Dane[9]);
					c.loc[2].x=Integer.parseInt(Dane[10]);
					c.loc[2].y=Integer.parseInt(Dane[11]);
					c.loc[3].x=Integer.parseInt(Dane[12]);
					c.loc[3].y=Integer.parseInt(Dane[13]);
					c.attack=true;
					//Effects.add(new Effect(this));
					//for()
					/*
					fill(255,0,0);
					beginShape();
					vertex(c.loc2[0].x-i,c.loc2[0].y-i);
					vertex(c.loc2[1].x+i,c.loc2[1].y-i);
					vertex(c.loc2[2].x+i,c.loc2[2].y+i);
					vertex(c.loc2[3].x-i,c.loc2[3].y+i);
					vertex(c.loc2[0].x-i,c.loc2[0].y-i);
					endShape();
					*/
					}
			 	}
			 		
			 		break;
			 		
		 case "BLOCK":
			 		id=Integer.parseInt(Dane[2]);
			 		int r=(int)random(5)*50;
			 		int g=(int)random(5)*50;
			 		int b=(int)random(5)*50;
			 		int attackId=Integer.parseInt(Dane[14]);
			 		for(int i=0;i<Cards.size();i++)
			 			{
			 			Card c=Cards.get(i);
			 				if(c.id==id) 
			 				{
			 					c.r=r;
			 					c.g=g;
			 					c.b=b;
			 					
			 				}
			 				if(c.id==attackId) 
			 				{
			 					c.r=r;
			 					c.g=g;
			 					c.b=b;
			 				}
			 			}
			 		break;
		 case "MARKERS":
			 if(tokens==true) tokens=false; else tokens=true;
			 break;
		 case "DEAD":
			 id=Integer.parseInt(Dane[2]);
			 for(int i=0;i<Cards.size();i++)
	 			{
	 			Card c=Cards.get(i);
	 				if(c.id==id) 
	 				{
	 					c.isDead=true;
	 					
	 				}
		 }}
	  }
	  public void mousePressed()
	  {
		  if(mouseButton == RIGHT)
		  {
		  int id=1;
		  for(int i=0;i<Cards.size();i++)
			  if(Cards.get(i).id>=id) id=Cards.get(i).id+1;
		  Cards.add(new Card(mouseX,mouseY,mouseX+100,mouseY,mouseX+100,mouseY+150,mouseX,mouseY+150,id,id,0,this));
		  }
		  if(mouseButton == LEFT)
		  {
			  int id=1;
			  for(int i=0;i<Cards.size();i++)
				  if(Cards.get(i).id>=id) id=Cards.get(i).id+1;
			  Cards.add(new Card(mouseX,mouseY,mouseX+150,mouseY-20,mouseX+160,mouseY+90,mouseX+10,mouseY+110,id,id,0,this));
		  }
	  }

public static void main(String args[]) {
    PApplet.main(new String[] { "--present", "MTGPackage.Main" });
  }
}

