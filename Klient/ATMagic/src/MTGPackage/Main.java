package MTGPackage;

import hypermedia.net.UDP;
import processing.core.*;

import java.util.*;

import javax.management.monitor.Monitor;

import MTGPackage.Effect.Type;

public class Main extends PApplet {
	PFont 	calibr = createFont("Arial", 50, true),
			f = createFont("Arial", 12, true);
	PImage 	edge1=loadImage("LG.png"), 
			edge2=loadImage("PG.png"), 
			edge3=loadImage("PD.png"), 
			edge4=loadImage("LD.png"),
			skull = loadImage("skull.jpg"),
			upArrow=loadImage("arrows_up.png");
	int 	manaPool1=0,
			manaPool2=0,
	 		life1=20,
			life2=20,
			log=0,
			window,
			cardHeight=180,cardWidth=120;
	Player 	P1=new Player(this,1),
			P2=new Player(this,2);
	PVector[] T=new PVector[4];
	SparkSystem ss, start;	
	PVector v;
	Board b=new Board(this);
	UDP udp,udp2;
	Phrases fazy = new Phrases(this);
	ArrayList<Card> Cards = new ArrayList<Card>();
	ArrayList<Effect> Effects = new ArrayList<Effect>();
	ArrayList<String> Msgs = new ArrayList<String>();	
	//ArrayList<Player> Players = new ArrayList<Player>();
	boolean tokens = true,arrow=false;	
	float angle=PI/8;
	String[] lines = loadStrings("log.txt");
	Effect e=new Effect(this, Type.ARROW, 100,new PVector(100,100),new PVector(200,200));
	public void setup()
		{		
		size(displayWidth, displayHeight);
		background(0);
		//Players.add(new Player(this,1));
		//Players.add(new Player(this,2));
		//f = new PFont();
		//f = createFont("Arial", 40, true);
		//frameRate(60);
		//skull = loadImage("skull.jpg");

		//edge1 = loadImage("LG.png");
		//edge2 = loadImage("PG.png");
		//edge3 = loadImage("PD.png");
		//edge4 = loadImage("LD.png");
		
		// skull=loadImage("star.png");
		udp = new UDP(this, 6121);
		udp.listen(true);
		if(tokens==true) window=0; else window=1;
		//b = new Board(this);

		//Effects.add(new Effect(this));
		// Effects.get(0).type=Type.START;
		//Effects.get(0).life = 120;
		//start = new SparkSystem(new PVector(0, 0), this);
		//start.sparkType = 'g';
		//start.r = 220 + (int) random(40);
		//start.g = 220 + (int) random(40);
		//start.b = 50 + (int) random(40);
		//start.size = 10;
		// start.v=new PVector(random(-0.5f,0.5f),random(0.5f));
		// start.a=new PVector(random(-0.05f,0.05f),random(-0.05f,0.05f));
		//start.v = null;
		//start.a = null;

		//start.life = 100;
	}
	
	@Override
	public boolean sketchFullScreen() {
		// TODO Auto-generated method stub
		return true;
	}

	public void draw() {
		if(window==0)
		{
			println("@@@@@@ "+frameRate);
			background(0);
			if (tokens == true) {
				image(edge1, 0, 0, 200, 200);
				image(edge2, width - 200, 0, 200, 200);
				image(edge3, width - 200, height - 200, 200, 200);
				image(edge4, 0, height - 200, 200, 200);
			}
			textFont(calibr);
			
			
			textAlign(CENTER);
			text("Trwa kalibracja...", width / 2, height*0.35f);
			
			pushMatrix();
			translate(width/2,height*0.55f);
			strokeWeight(1);
			fill(255);
			ellipse(0,0,180,200);
			noFill( );
			for(int i=0;i<=50;i++)
			{
				stroke(240,240,70,200-4*i);
				ellipseMode(CENTER);
				ellipse(0,0,180+1f*i,200+1f*i);
				
				
			}
			popMatrix();
			processMessages();
			
			
		}
		else
			if(window==1)
		{
			processMessages();
			
		
		
		//println("there are " + lines.length + " lines");
	//	udp.send( "| ADD 0 5 1 0 830 560 880 646 754 708 705 623 |", "localhost",6121  );
		
		/*
		if(Effects.isEmpty()==false)
		{
		if (Effects.get(0).type == Type.START && Effects.get(0).life > 0) {

			background(0);
			Effect e = Effects.get(0);
			e.life--;

			if (start.sparks.isEmpty() != true)
				start.run();
			if (e.life > 70) {

				start.source = new PVector(random(width), random(height));
				start.addParticle();
				start.source = new PVector(random(width), random(height));
				start.addParticle();

			}
			if (e.life > 60) {
				start.source = new PVector(random(width), random(height));
				start.addParticle();
				start.source = new PVector(random(width), random(height));
				start.addParticle();

			}
			if (e.life > 50) {
				start.source = new PVector(random(width), random(height));
				start.addParticle();
			}

			if (e.life < 50) {
				textFont(f);
				// fill(start.r=220+(int)random(40),start.r=220+(int)random(40),start.r=50+(int)random(40),200-4*Effects.get(0).life);
				fill(240, 240, 70, 150 - 2 * e.life);
				textAlign(CENTER);
				text("ATMagic", width / 2, height / 2);

			}

		}} else {
		*/

			
			background(0);
			// b.display(counter,40,4);
			b.display(255, 40, 4,P1,P2,cardWidth,cardHeight);
			fazy.rysuj();
			//stroke(255,0,0);
			//rect(width/2, height/2,cardWidth,cardHeight);
			
			if(Effects.isEmpty()==false)
			{
				for(int i=0;i<Effects.size();i++)
				{

				
				Effect e = Effects.get(i);
				//e.v2=new PVector(mouseX, mouseY);
				e.drawEffect();
				if(e.life==0) Effects.remove(i);
				}
			}
		
			
			//if(v!=null) e.v2=v;
			//e.v1=new PVector(width/2,height/2);
				//if(e.life>0) e.drawEffect();
			/*e.drawEffect(new PVector(100,100),new PVector(200,70));
			e.drawEffect(new PVector(100,100),new PVector(200,130));
			e.drawEffect(new PVector(100,100),new PVector(50,50));
			e.drawEffect(new PVector(100,100),new PVector(50,150));
			*///e.drawEffect(new PVector(100,100),new PVector(100,50));
			//b.drawLife(P1, P2);
			//b.drawMana(P1,P2);
			f=createFont("Arial", 20);
			textFont(f);
			//fill(255);
			textAlign(CENTER);
			if(log>0) text(lines[log-1], 500,250);
		

			/*
			 * beginShape(); texture(skull); vertex(450, 500, 0, 0); vertex(690,
			 * 550, skull.width, 0); vertex(600, 650, skull.width,
			 * skull.height); vertex(500, 600, 0, skull.height); endShape();
			 */
			//noFill();
			//stroke(255);
			//strokeWeight(10);
			/*
			 * image(edge1,0,0,100,100); image(edge2,width-100,0,100,100);
			 * image(edge3,width-100,height-100,100,100);
			 * image(edge4,0,height-100,100,100);
			 */
			if (tokens == true) {
				image(edge1, 0, 0, 200, 200);
				image(edge2, width - 200, 0, 200, 200);
				image(edge3, width - 200, height - 200, 200, 200);
				image(edge4, 0, height - 200, 200, 200);
			}
			/*
			 * image(edge1,8,8,67,67); rect(4,4,75,75);
			 * 
			 * image(edge2,width-71,0,67,67); rect(width-71,0,67,67);
			 * 
			 * 
			 * 
			 * //rect(width-75,height-75,75,75);
			 * image(edge3,width-71,height-71,67,67);
			 * 
			 * // rect(0,height-75,75,75); image(edge4,4,height-71,67,67);
			 * 
			 * 
			 * /* rect(0,0,75,75); image(edge1,0,0,75,75);
			 * 
			 * 
			 * rect(width-75,0,75,75); image(edge2,width-75,0,75,75);
			 * 
			 * 
			 * rect(width-75,height-75,75,75);
			 * image(edge3,width-75,height-75,75,75);
			 * 
			 * rect(0,height-75,75,75); image(edge4,0,height-75,75,75);
			 */

				if (Cards.isEmpty() == false) 
					{
					for (int i = 0; i < Cards.size(); i++) 
							{
							Card c = Cards.get(i);
							if (c.isDead == true && c.deadCounter <= 0) 
									{
									Cards.remove(i);
									} 
							else 
									{
									//stroke(255, 0, 0);
									//strokeWeight(5);
									if (c.frame == true)	c.drawEdges();
									if (c.attack == true) 
											{
										/*
											stroke(255, 0, 0,100);
											fill(255,0,0,20);
											beginShape();
											vertex(c.loc2[0].x, c.loc2[0].y);
											vertex(c.loc2[1].x, c.loc2[1].y);
											vertex(c.loc2[2].x, c.loc2[2].y);
											vertex(c.loc2[3].x, c.loc2[3].y);
											vertex(c.loc2[0].x, c.loc2[0].y);
											endShape();
											*/
											}
									if (c.isDead == true) 
											{
							/*
							 * fill(255); c.frameRate=8; beginShape();
							 * 
							 * vertex(c.loc[0].x, c.loc[0].y);
							 * vertex(c.loc[1].x, c.loc[1].y);
							 * vertex(c.loc[2].x, c.loc[2].y);
							 * vertex(c.loc[3].x, c.loc[3].y); endShape();
							 * 
							 */
										/*strokeWeight(1);
								stroke(0);
								fill(0);
								beginShape();
								vertex(c.loc[0].x,c.loc[0].y);
								vertex(c.loc[1].x,c.loc[1].y);
								vertex(c.loc[2].x,c.loc[2].y);
								vertex(c.loc[3].x,c.loc[3].y);
								vertex(c.loc[0].x,c.loc[0].y);
								endShape();
											c.frameRate = 8;
											tint(255 - (c.deadCounter % 50) * 5);
											pushMatrix();
											translate(c.loc[0].x,c.loc[0].y);
											switch(c.direction)
											{
											case 1:
												rotate(c.asin);
												break;
												
											case 2:
												rotate(-c.asin+PI);
												break;
												
											case 3:
												rotate(c.asin+PI);
												break;
												
											case 4:
												rotate(-c.asin);
												break;
												
											default: break;
											
											}
											//rotate(c.asin);
											
											//image(skull,0,0,dist(c.loc[0].x,c.loc[0].y,c.loc[1].x,c.loc[1].y),dist(c.loc[2].x,c.loc[2].y,c.loc[1].x,c.loc[1].y));
											//popMatrix();
											//tint(255);
											
							/*
							 * beginShape(); texture(skull); vertex(c.LG.x,
							 * c.LG.y, 0, 0); vertex(c.PG.x, c.PG.y,
							 * skull.width, 0); vertex(c.PD.x, c.PD.y,
							 * skull.width, skull.height); vertex(c.LD.x,
							 * c.LD.y, 0, skull.height); endShape();
							 */
							/*
							 * beginShape(); texture(skull); vertex(c.loc[0].x,
							 * c.loc[0].y, 0, 0); vertex(c.loc[1].x, c.loc[1].y,
							 * skull.width, 0); vertex(c.loc[2].x, c.loc[2].y,
							 * skull.width, skull.height); vertex(c.loc[3].x,
							 * c.loc[3].y, 0, skull.height); endShape();
							 */

							/*
							 * c.frameRate=4; pushMatrix();
							 * translate(c.loc[0].x,c.loc[0].y);
							 * //skull.resize((
							 * int)(c.loc[1].x-c.loc[0].x),(int)(
							 * c.loc[2].y-c.loc[1].y));
							 * 
							 * tint(200-c.deadCounter);
							 * image(skull,0,0,c.width(),c.height());
							 * //image(edge4,0,0,c.width(),c.height());
							 * //image(star,0,0,c.width(),c.height());
							 * 
							 * //skull.resize(100, 50); //int
							 * xCenter=(int)abs(c.loc[0].x-c.loc[1].x); //int
							 * yCenter=(int)abs(c.loc[0].y-c.loc[3].y);
							 * //image(skull,0,0);
							 * //image(skull,(c.width()/2)-(int
							 * )(0.1*c.width()),(
							 * c.height()/2)-(int)(0.1*c.height
							 * ()),(float)0.2*c.width(),(float)0.2*c.height());
							 * /
							 * /image(skull,c.loc[0].x+dist(c.loc[0].x,c.loc[0].
							 * y
							 * ,c.loc[1].x,c.loc[1].y),0,c.width()-c.deadCounter
							 * ,
							 * c.height()-(c.deadCounter*(c.height()/c.width()))
							 * ); popMatrix();
							 * 
							 * tint(255);
							 */
											}
						//f = createFont("Arial", 12, true);
						textFont(f);
						// String s=
						// "locx="+c.loc[0].x+" \nlocy="+c.loc[0].y+" \nid: "+c.id+"\ndead:"+c.isDead+"\nframeLife:"+c.frameLife+"\nframeRate:"+c.frameRate+"\nsparkTime"+c.sparkTime+"\nframeCounter:"+c.frameCounter+"\ndeadCounter: "+c.deadCounter+"\nowner: "+c.owner;
						fill(0, 0, 0);
						String s ="r: "+c.r 
								+"\naSIN: "+c.asin
								+"\nowner: " + c.owner + "\nlocx=" + c.loc[0].x
								+ " \nlocy=" + c.loc[0].y + " \nid: " + c.id
								+ "\ndead:" + c.isDead + "\nframeLife:"
								+ c.frameLife + "\nframeRate:" + c.frameRate
								+ "\nsparkTime" + c.sparkTime
								+ "\nframeCounter:" + c.frameCounter
								+ "\ndeadCounter: " + c.deadCounter;
						//pushMatrix();
						textAlign(CENTER);
					//	text(s,(c.loc[0].x+c.loc[1].x)/2,c.loc[0].y+10+c.height());
						//popMatrix();
							if (c.se.size() > 0) 
									{
									for (int j = 0; j < c.se.size(); j++) 
											{
											SparkEdge se = c.se.get(j);
											for (int k = 0; k < se.ss.size(); k++) 
													{
													SparkSystem ss = se.ss.get(k);
									// ss.r=240;
									// ss.g=240;
									// ss.b=20;
													if (c.sparkTime > 0)
													ss.addParticle();
													ss.run();
													}
											}

									}
									if(c.sparkTime>0) c.sparkTime--;
									}
				}
					}
				
			
			}

		}
	

	public void keyPressed() {
		switch (key) {

		case 's':
			Effects.get(0).life = 100;
			fill(start.r = 220 + (int) random(40),
					start.r = 220 + (int) random(40),
					start.r = (int) random(40), 200 - 4 * Effects.get(0).life);
			break;
		case 'p':
			Card c = Cards.get((int)random(Cards.size()));
			c.sparkTime=100;
			break;
		case 'b':
			if(Cards.size()<2) break;
			else
			{
				Card c1,c2;
				c1=Cards.get(0);
				c2=Cards.get(1);
				PVector v1=new PVector((c1.loc[0].x+c1.loc[1].x+c1.loc[2].x+c1.loc[3].x)/4,(c1.loc[0].y+c1.loc[1].y+c1.loc[2].y+c1.loc[3].y)/4);
				PVector v2=new PVector((c2.loc[0].x+c2.loc[1].x+c2.loc[2].x+c2.loc[3].x)/4,(c2.loc[0].y+c2.loc[1].y+c2.loc[2].y+c2.loc[3].y)/4);
				//line(v1.x,v1.y,v2.x,v2.y);
			
				
			}
			break;
		case '1':
			Cards.get(1).frame = true;
			break;
		case '2':
			Cards.get(2).frame = true;
			break;
		case '3':
			Cards.get(3).frame = true;
			break;
		case 'a':
			 c = Cards.get((int) random(Cards.size()));
			c.attack = true;
			c.r=255;c.g=0;c.b=0;
			
			
			
			c.loc2[0].x = c.loc[0].x;
			c.loc2[0].y = c.loc[0].y;

			c.loc2[1].x = c.loc[1].x;
			c.loc2[1].y = c.loc[1].y;

			c.loc2[2].x = c.loc[2].x;
			c.loc2[2].y = c.loc[2].y;

			c.loc2[3].x = c.loc[3].x;
			c.loc2[3].y = c.loc[3].y;

			
			c.loc[0].y-=200;
			c.loc[1].y-=200;
			c.loc[2].y-=200;
			c.loc[3].y-=200;
			
			int T[]=new int[4];
			T[0]=(int)c.loc[0].y;
			T[01]=(int)c.loc[0].y;
			T[2]=(int)c.loc[0].y;
			T[3]=(int)c.loc[0].y;
			int min=min(T);
			
			PVector top=new PVector((c.loc[0].x+c.loc[1].x+c.loc[2].x+c.loc[3].x)/4,min);
			Effect e=new Effect(this, Type.ARROW,-1,top,new PVector(width/2,0));
			Effects.add(e);
			break;
		case 't':
			if (tokens == false)
				{tokens = true; window=0;}
			else
				{tokens = false;window=1;}
			break;
		case 'm':
			for (int i = 0; i < Cards.size(); i++) {
				Cards.get(i).moveTo(new PVector(random(width), random(height)));
			}
			break;
		case 'd':
			if (Cards.isEmpty() == false) {
				c = Cards.get((int) random(Cards.size()));
				c.isDead = true;
				// c.sparkTime=c.deadCounter;

				for (int j = 0; j < c.se.size(); j++) {
					SparkEdge se = c.se.get(j);
					PVector vel = new PVector(c.loc[0].x - b.graveyard1.x,
							c.loc[0].y - b.graveyard1.y);
					PVector acc = new PVector(0.05f, 0.05f);

					// se.changeSparkType('c', 240, 20, 20, 5, vel, acc);
					for (int k = 0; k < se.ss.size(); k++) {
						SparkSystem ss = se.ss.get(k);

						ss.r = 240;
						ss.g = 255;
						ss.b = 20;
						if (c.sparkTime > 0)
							ss.addParticle();
						ss.run();
					}
				}

			}
			break;
		case 'l' :
			if(lines.length>log && lines[log]!="Utworzono serwer" && lines[log]!="Zamknieto serwer") 
				udp.send( lines[log], "localhost",6121  );
			if(lines[log]=="Zamknieto serwer") {Cards.clear(); tokens=true; }
			log++;
			if(log>=lines.length) log=0;
			break;
		}
	}

	Object mutex = new Object();

	public void receive(byte[] data, String ip, int port) {
		data = subset(data, 0, data.length);
		String message = new String(data);
		println(message);
		synchronized (mutex) {
			Msgs.add(message);
		}
	}

	public void processMessages() {
		synchronized (mutex) {
			for (String message : Msgs) {

				String[] Dane = message.split(" ");
				 println("nowe");
				 for(int i=0;i<Dane.length;i++)
				 println(i+" " +Dane[i]);
			 println("*****************");
				/*
				 * gupiznak-funkcja-id-idbazy-owner-TAP-8xwspolrzedne
				 */

				switch (Dane[1]) {
				case "ADD":

					// Cards.add(new
					// Card(Integer.parseInt(Dane[6]),Integer.parseInt(Dane[7]),Integer.parseInt(Dane[8]),Integer.parseInt(Dane[9]),Integer.parseInt(Dane[10]),Integer.parseInt(Dane[11]),Integer.parseInt(Dane[12]),Integer.parseInt(Dane[13]),Integer.parseInt(Dane[2]),Integer.parseInt(Dane[3]),Integer.parseInt(Dane[4]),this));
					// Cards.add(new
					// Card((int)(Float.parseFloat(Dane[6])*width),(int)(Float.parseFloat(Dane[7])*height),(int)(Float.parseFloat(Dane[8])*width),(int)(Float.parseFloat(Dane[9])*height),(int)(Float.parseFloat(Dane[10])*width),(int)(Float.parseFloat(Dane[11])*height),(int)(Float.parseFloat(Dane[12])*width),(int)(Float.parseFloat(Dane[13])*height),Integer.parseInt(Dane[2]),Integer.parseInt(Dane[3]),Integer.parseInt(Dane[4]),this));
					// /// EEE MACARENA !!!
					Cards.add(new Card((int) (Float.parseFloat(Dane[6])),
							(int) (Float.parseFloat(Dane[7])), (int) (Float
									.parseFloat(Dane[8])), (int) (Float
									.parseFloat(Dane[9])), (int) (Float
									.parseFloat(Dane[10])), (int) (Float
									.parseFloat(Dane[11])), (int) (Float
									.parseFloat(Dane[12])), (int) (Float
									.parseFloat(Dane[13])), Integer
									.parseInt(Dane[2]), Integer
									.parseInt(Dane[3]), Integer
									.parseInt(Dane[4]), this));
					break;

					
				case "ADDMANA":
				{
					int id=Integer.parseInt(Dane[2]);
					int q=Integer.parseInt(Dane[3]);
					//if(id==1) manaPool1+=q; else manaPool2+=q;
					if(id==1) P1.manaPool+=q; else P2.manaPool+=q;
					break;
					
				}
				case "SUBMANA":
				{
					int id=Integer.parseInt(Dane[2]);
					int q=Integer.parseInt(Dane[3]);
					//if(id==1) manaPool1-=q; else manaPool2-=q;
					if(id==1)
						{
						if(P1.manaPool-q>=0) P1.manaPool-=q; else P1.manaPool=0;
						} 
					else 
					{
						if(P2.manaPool-q>=0) P2.manaPool-=q; else P2.manaPool=0;
					
					}
					break;
					
				}
				
				case "ADDLIFE":
				{
					int id=Integer.parseInt(Dane[2]);
					int q=Integer.parseInt(Dane[3]);
					if(id==1) P1.life+=q; else P2.life+=q;
					break;
					
				}
				case "SUBLIFE":
				{
					int id=Integer.parseInt(Dane[2]);
					int q=Integer.parseInt(Dane[3]);
					if(id==1) P1.life-=q; else P2.life-=q;
					break;
					
				}
				
				case "COST":
					
					b.stackCost=Integer.parseInt(Dane[3]);
					int id=Integer.parseInt(Dane[2]);
					int mana;
					if(id==1) mana=P1.manaPool;
					else  mana=P2.manaPool;
					if(b.stackCost > mana) {b.stackR=255; b.stackG=0; b.stackB=0;}
					else {b.stackR=0; b.stackG=255; b.stackB=0;}
				break;
				case "UPDATE":
					 id = Integer.parseInt(Dane[2]);

					for (int i = 0; i < Cards.size(); i++) {
						Card c = Cards.get(i);
						

						if (c.id == id) {
							/*
							 * c.x1=Integer.parseInt(Dane[6]);
							 * c.y1=Integer.parseInt(Dane[7]);
							 * c.x2=Integer.parseInt(Dane[8]);
							 * c.y2=Integer.parseInt(Dane[9]);
							 * c.x3=Integer.parseInt(Dane[10]);
							 * c.y3=Integer.parseInt(Dane[11]);
							 * c.x4=Integer.parseInt(Dane[12]);
							 * c.y4=Integer.parseInt(Dane[13]);
							 */
							
							c.attack = false;
							c.block=false;
							
							c.r = 0;
							c.g = 0;
							c.b = 255;
							
							c.loc[0].x = Integer.parseInt(Dane[6]);
							c.loc[0].y = Integer.parseInt(Dane[7]);
							c.loc[1].x = Integer.parseInt(Dane[8]);
							c.loc[1].y = Integer.parseInt(Dane[9]);
							c.loc[2].x = Integer.parseInt(Dane[10]);
							c.loc[2].y = Integer.parseInt(Dane[11]);
							c.loc[3].x = Integer.parseInt(Dane[12]);
							c.loc[3].y = Integer.parseInt(Dane[13]);
							
							c.loc2[0].x = Integer.parseInt(Dane[6]);
							c.loc2[0].y = Integer.parseInt(Dane[7]);
							c.loc2[1].x = Integer.parseInt(Dane[8]);
							c.loc2[1].y = Integer.parseInt(Dane[9]);
							c.loc2[2].x = Integer.parseInt(Dane[10]);
							c.loc2[2].y = Integer.parseInt(Dane[11]);
							c.loc2[3].x = Integer.parseInt(Dane[12]);
							c.loc2[3].y = Integer.parseInt(Dane[13]);

							float dist[] = new float[4];
							float mindist;

							for (i = 0; i < 4; i++) {

								dist[i] = dist(c.loc[i].x, c.loc[i].y, 0, 0);

							}
							mindist = min(dist);

							for (i = 0; i < 4; i++) {
								if (dist(c.loc[i].x, c.loc[i].y, 0, 0) == mindist)
									c.LG = c.loc[i];
							}

							for (i = 0; i < 4; i++) {

								dist[i] = dist(c.loc[i].x, c.loc[i].y, width, 0);

							}

							mindist = min(dist);

							for (i = 0; i < 4; i++) {
								if (dist(c.loc[i].x, c.loc[i].y, width, 0) == mindist)
									c.PG = c.loc[i];
							}

							for (i = 0; i < 4; i++) {

								dist[i] = dist(c.loc[i].x, c.loc[i].y, width,
										height);

							}
							mindist = min(dist);

							for (i = 0; i < 4; i++) {
								if (dist(c.loc[i].x, c.loc[i].y, width, height) == mindist)
									c.PD = c.loc[i];
							}

							for (i = 0; i < 4; i++) {

								dist[i] = dist(c.loc[i].x, c.loc[i].y, 0,
										height);

							}
							mindist = min(dist);
							for (i = 0; i < 4; i++) {
								if (dist(c.loc[i].x, c.loc[i].y, 0, height) == mindist)
									c.LD = c.loc[i];
							}
							if(c.loc[0].x>=c.loc[3].x && c.loc[0].y<=c.loc[3].y) c.direction=1;
					    	else if(c.loc[0].x>=c.loc[3].x && c.loc[0].y>=c.loc[3].y) c.direction=2;
					    	else if(c.loc[0].x<=c.loc[3].x && c.loc[0].y>=c.loc[3].y) c.direction=3;
					    	else c.direction=4;
							
							c.center=new PVector((c.loc[0].x+c.loc[1].x+c.loc[2].x+c.loc[3].x)/4,(c.loc[0].y+c.loc[1].y+c.loc[2].y+c.loc[3].y)/4);

							c.drawEdges();

						}

					}
					break;
					
				case "NEXTPHASE":
					
					fazy.zmien_faze();
					break;
				case "ATTACK":
					id = Integer.parseInt(Dane[2]);
					for (int i = 0; i < Cards.size(); i++) {
						Card c = Cards.get(i);
						if (c.id == id) {
							c.r = 255;
							c.g = 0;
							c.b = 0;
/*
							c.loc2[0].x = c.loc[0].x;
							c.loc2[0].y = c.loc[0].y;

							c.loc2[1].x = c.loc[1].x;
							c.loc2[1].y = c.loc[1].y;

							c.loc2[2].x = c.loc[2].x;
							c.loc2[2].y = c.loc[2].y;

							c.loc2[3].x = c.loc[3].x;
							c.loc2[3].y = c.loc[3].y;
*/
							c.loc[0].x = Integer.parseInt(Dane[6]);
							c.loc[0].y = Integer.parseInt(Dane[7]);
							c.loc[1].x = Integer.parseInt(Dane[8]);
							c.loc[1].y = Integer.parseInt(Dane[9]);
							c.loc[2].x = Integer.parseInt(Dane[10]);
							c.loc[2].y = Integer.parseInt(Dane[11]);
							c.loc[3].x = Integer.parseInt(Dane[12]);
							c.loc[3].y = Integer.parseInt(Dane[13]);
							c.attack = true;
							// Effects.add(new Effect(this));
							// for()
							/*
							 * fill(255,0,0); beginShape();
							 * vertex(c.loc2[0].x-i,c.loc2[0].y-i);
							 * vertex(c.loc2[1].x+i,c.loc2[1].y-i);
							 * vertex(c.loc2[2].x+i,c.loc2[2].y+i);
							 * vertex(c.loc2[3].x-i,c.loc2[3].y+i);
							 * vertex(c.loc2[0].x-i,c.loc2[0].y-i); endShape();
							 */
						}
					}

					break;

				case "BLOCK":
					id = Integer.parseInt(Dane[2]);
					
					PVector v1=new PVector(0,0),v2=new PVector(0,0);
					int attackId = Integer.parseInt(Dane[14]);
					for (int i = 0; i < Cards.size(); i++) {
						Card c = Cards.get(i);
						if (c.id == id) {
							c.block=true;
							
							v1=new PVector((c.loc[0].x+c.loc[1].x+c.loc[2].x+c.loc[3].x)/4,(c.loc[0].y+c.loc[1].y+c.loc[2].y+c.loc[3].y)/4);
							c.loc[0].x = Integer.parseInt(Dane[6]);
							c.loc[0].y = Integer.parseInt(Dane[7]);
							c.loc[1].x = Integer.parseInt(Dane[8]);
							c.loc[1].y = Integer.parseInt(Dane[9]);
							c.loc[2].x = Integer.parseInt(Dane[10]);
							c.loc[2].y = Integer.parseInt(Dane[11]);
							c.loc[3].x = Integer.parseInt(Dane[12]);
							c.loc[3].y = Integer.parseInt(Dane[13]);
						

						}
						if (c.id == attackId) {
							c.attack=true;
							v2=new PVector((c.loc[0].x+c.loc[1].x+c.loc[2].x+c.loc[3].x)/4,(c.loc[0].y+c.loc[1].y+c.loc[2].y+c.loc[3].y)/4);
						}
						Effect e=new Effect(this,Type.ARROW,-1,v1,v2);
						e.blockId=id;
						Effects.add(e);
						
					}
					break;
				case "MARKERS":
					if (tokens == true)
						{tokens = false; window=1; cardWidth=Integer.parseInt(Dane[2]); cardHeight=Integer.parseInt(Dane[3]);}
					else
						{tokens = true; window=0;}
					break;
				case "DEAD":
					id = Integer.parseInt(Dane[2]);
					for (int i = 0; i < Cards.size(); i++) {
						Card c = Cards.get(i);
						if (c.id == id) {
							c.isDead = true;

						}
					}
				}
			}
			Msgs.clear();
		}
	}

	public void mousePressed() {
		if (mouseButton == RIGHT) {
			int id = 1;
			for (int i = 0; i < Cards.size(); i++)
				if (Cards.get(i).id >= id)
					id = Cards.get(i).id + 1;
			//if(T[0]==null) println("@@@");
			//T[0].x=mouseX; T[0].y=mouseY;
			if(T[0]==null) {T[0]=new PVector(mouseX,mouseY);}
			else
				if(T[1]==null) {T[1]=new PVector(mouseX,mouseY);}
				else
					if(T[2]==null) {T[2]=new PVector(mouseX,mouseY);}
					else
						if(T[3]==null) 
							{
							T[3]=new PVector(mouseX,mouseY); 
							Cards.add(new Card((int)T[0].x,(int)T[0].y,(int)T[1].x,(int)T[1].y,(int)T[2].x,(int)T[2].y,(int)T[3].x,(int)T[3].y,id,id,0,this));
							T[0]=null;
							T[1]=null;
							T[2]=null;
							T[3]=null;
							}
			
						
		//	Cards.add(new Card(mouseX, mouseY, mouseX - 100, mouseY,
				//	mouseX - 100, mouseY - 150, mouseX, mouseY - 150, id, id,
				//	0, this));
		}
		if (mouseButton == LEFT) 
		{
			
			 Effects.add(new Effect(this,Type.ARROW,100,new PVector(width/2,height/2),new PVector(mouseX,mouseY)));
		//	 Effects.get(Effects.size()-1).arrowType=1;
		}
	}

	public static void main(String args[]) {
		PApplet.main(new String[] { "--present", "MTGPackage.Main" });
	}
}
