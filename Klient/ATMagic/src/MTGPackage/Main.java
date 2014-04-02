package MTGPackage;

import hypermedia.net.UDP;
import processing.core.*;

import java.nio.file.attribute.AttributeView;
import java.util.*;

import javax.management.monitor.Monitor;

import MTGPackage.Effect.Type;

public class Main extends PApplet {

	Game game=new Game(this);

	UDP udp,udp2;

//	String[] lines = loadStrings("log.txt");

	public void setup()
		{		
		size(displayWidth, displayHeight);
		background(0);
		
		
		
		udp = new UDP(this, 600);
		udp.listen(true);
	//	if(tokens==true) game.window=0; else game.window=1;
		
	}
	
	@Override
	public boolean sketchFullScreen() {
		// TODO Auto-generated method stub
		return true;
	}

	public void draw() 
	{
		if(game.window==0)
		{
			
			background(0);
			if (game.tokens == true) {
				image(game.edge1, 0, 0, 200, 200);
				image(game.edge2, width - 200, 0, 200, 200);
				image(game.edge3, width - 200, height - 200, 200, 200);
				image(game.edge4, 0, height - 200, 200, 200);
			}
			textFont(game.calibr);
			
			
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
			if(game.window==1)
		{
			processMessages();
			
		
		
		

			
			background(0);
			
			game.board.display(255, 40, 4,game.P1,game.P2,game.cardWidth,game.cardHeight);
			
			game.fazy.rysuj();
		
			
		
		
	
			game.f=createFont("Arial", 20);
			textFont(game.f);
			//fill(255);
			textAlign(CENTER);
			if(game.log>0) text(game.lines[game.log-1], 500,250);
		

			if (game.tokens == true) {
				image(game.edge1, 0, 0, 200, 200);
				image(game.edge2, width - 200, 0, 200, 200);
				image(game.edge3, width - 200, height - 200, 200, 200);
				image(game.edge4, 0, height - 200, 200, 200);
			}
			

				if (game.Cards.isEmpty() == false) 
					{
					for (int i = 0; i < game.Cards.size(); i++) 
							{
							Card c = game.Cards.get(i);
						//	println("Card");
							if (c.isDead == true && c.deadCounter <= 0) 
									{
									game.Cards.remove(i);
									} 
							else 
									{
									
									if (c.frame == true)	c.drawEdges();
									if (c.attack == true) 
											{
									
											}
									if (c.isDead == true) 
											{
											}
							
						textFont(game.f);
						
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
						
						textAlign(CENTER);
					
							if (c.se.size() > 0) 
									{
								println("A");
									for (int j = 0; j < c.se.size(); j++) 
											{
											SparkEdge se = c.se.get(j);
											for (int k = 0; k < se.ss.size(); k++) 
													{
													SparkSystem ss = se.ss.get(k);
									
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
				
				if(game.Effects.isEmpty()==false)
				{
					for(int i=0;i<game.Effects.size();i++)
					{

					
					Effect e = game.Effects.get(i);
					
					e.drawEffect();
					if(e.life==0) game.Effects.remove(i);
					}
				}
				
					}
	}

		
	

	public void keyPressed() {
		switch (key) {

	
		case 'p':
			Card c = game.Cards.get((int)random(game.Cards.size()));
			c.sparkTime=100;
			break;
		case 'b':
			if(game.Cards.size()<2) break;
			else
			{
				Card c1,c2;
				c1=game.Cards.get(0);
				c2=game.Cards.get(1);
				PVector v1=new PVector((c1.loc[0].x+c1.loc[1].x+c1.loc[2].x+c1.loc[3].x)/4,(c1.loc[0].y+c1.loc[1].y+c1.loc[2].y+c1.loc[3].y)/4);
				PVector v2=new PVector((c2.loc[0].x+c2.loc[1].x+c2.loc[2].x+c2.loc[3].x)/4,(c2.loc[0].y+c2.loc[1].y+c2.loc[2].y+c2.loc[3].y)/4);
				
			
				
			}
			break;
		case '1':
			game.Cards.get(1).frame = true;
			break;
		case '2':
			game.Cards.get(2).frame = true;
			break;
		case '3':
			game.Cards.get(3).frame = true;
			break;
		case 'a':
			 c = game.Cards.get((int) random(game.Cards.size()));
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
			game.Effects.add(e);
			break;
		case 't':
			if (game.tokens == false)
				{game.tokens = true; game.window=0;}
			else
				{game.tokens = false;game.window=1;}
			break;
		case 'm':
			for (int i = 0; i < game.Cards.size(); i++) {
				game.Cards.get(i).moveTo(new PVector(random(width), random(height)));
			}
			break;
		case 'd':
			if (game.Cards.isEmpty() == false) {
				c = game.Cards.get((int) random(game.Cards.size()));
				c.isDead = true;
			

				for (int j = 0; j < c.se.size(); j++) {
					SparkEdge se = c.se.get(j);
					PVector vel = new PVector(c.loc[0].x - game.board.graveyard1.x,
							c.loc[0].y - game.board.graveyard1.y);
					PVector acc = new PVector(0.05f, 0.05f);

					
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
			if(game.lines.length>game.log && game.lines[game.log]!="Utworzono serwer" && game.lines[game.log]!="Zamknieto serwer") 
				udp.send( game.lines[game.log], "localhost",6121  );
			if(game.lines[game.log]=="Zamknieto serwer") {game.Cards.clear(); game.tokens=true; }
			game.log++;
			if(game.log>=game.lines.length) game.log=0;
			break;
		}
	}

	Object mutex = new Object();

	public void receive(byte[] data, String ip, int port) {
		data = subset(data, 0, data.length);
		String message = new String(data);
		println(message);
		synchronized (mutex) {
			game.Msgs.add(message);
		}
	}

	public void processMessages() {
		synchronized (mutex) {
			for (String message : game.Msgs) {

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

				
					game.Cards.add(new Card((int) (Float.parseFloat(Dane[6])),
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
				
					char c=Dane[3].charAt(0);
					
				
					
					if(id==1) game.P1.addMana(c); else game.P2.addMana(c);
					break;
					
				}
				case "EFFECT":
				{
					
					String type=Dane[2];
					
					if(type.compareTo("BOLT")==0)
					{
						
						int playerId=Integer.parseInt(Dane[3]);
						int cardId=Integer.parseInt(Dane[4]);
						
						if(cardId>=0) game.Effects.add(new Effect(this,Type.BOLT,8,cardId,game.cardWidth,game.cardHeight,game.Cards));
					}
					
					
					
					break;
				}
				case "SUBMANA":
				{
					char c=Dane[3].charAt(0);
					int id=Integer.parseInt(Dane[2]);
				
					if(id==1)
						{
						
						game.P1.subtractMana(c); 
						} 
					else 
					{
						
						 game.P2.subtractMana(c);
					
					}
					break;
					
				}
				case "SUBMANA2":
				{
					
					int id=Integer.parseInt(Dane[2]);
					
					if(id==1)
					{
						
					
						game.P1.subtractMana(Integer.parseInt(Dane[3]),Integer.parseInt(Dane[4]),Integer.parseInt(Dane[5]),Integer.parseInt(Dane[6]),Integer.parseInt(Dane[7])); 
						
					} 
				else 
				{
					
					game.P1.subtractMana(Integer.parseInt(Dane[3]),Integer.parseInt(Dane[4]),Integer.parseInt(Dane[5]),Integer.parseInt(Dane[6]),Integer.parseInt(Dane[7])); 
				
				}
					
					break;
					
				}
				
				case "ADDLIFE":
				{
					int id=Integer.parseInt(Dane[2]);
					int q=Integer.parseInt(Dane[3]);
					if(id==1)game.P1.life+=q; else game.P2.life+=q;
					break;
					
				}
				case "SUBLIFE":
				{
					int id=Integer.parseInt(Dane[2]);
					int q=Integer.parseInt(Dane[3]);
					if(id==1)game.P1.life-=q; else game.P2.life-=q;
					break;
					
				}
				
				case "COST":
				
				
					int id=Integer.parseInt(Dane[2]);
					int mana;
					if(id==1) mana=game.P1.manaPool;
					else  mana=game.P2.manaPool;
					if(game.board.stackCost > mana) {game.board.stack1R=255; game.board.stack1G=0; game.board.stack1B=0;}
					else {game.board.stack1R=0; game.board.stack1G=255; game.board.stack1B=0;}
				break;
				
				case "STACK":
					 id=Integer.parseInt(Dane[2]);
					 char color=Dane[3].charAt(0);
					 if(id==1) {
						 switch(color)
						 {
						 case 'R':
							 game.board.stack1R=255;
							 game.board.stack1G=0;
							 game.board.stack1B=0;
							 break;
						 case 'G':
							 game.board.stack1R=0;
							 game.board.stack1G=255;
							 game.board.stack1B=0;
							 break;
						 
						 case 'B':
							 game.board.stack1R=0;
							 game.board.stack1G=0;
							 game.board.stack1B=255;
							 break;
						 
						 
						 }
					 }
					
					break;
				case "UPDATE":
					  id = Integer.parseInt(Dane[2]);

					for (int i = 0; i < game.Cards.size(); i++) {
						Card c = game.Cards.get(i);
						

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
					
					game.fazy.zmien_faze();
					break;
				case "ATTACK":
					id = Integer.parseInt(Dane[2]);
					for (int i = 0; i < game.Cards.size(); i++) {
						Card c = game.Cards.get(i);
						if (c.id == id) {
							c.r = 255;
							c.g = 0;
							c.b = 0;
/*
				
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
					
						}
					}

					break;

				case "BLOCK":
				
					id = Integer.parseInt(Dane[2]);
					
					PVector v1=new PVector(0,0),v2=new PVector(0,0);
					int attackId = Integer.parseInt(Dane[14]);
					for (int i = 0; i < game.Cards.size(); i++) {
						Card c = game.Cards.get(i);
						if (c.id == attackId) {
							
							
							v2=new PVector((c.loc[0].x+c.loc[1].x+c.loc[2].x+c.loc[3].x)/4,(c.loc[0].y+c.loc[1].y+c.loc[2].y+c.loc[3].y)/4);
							
							
								

						}
					}
					for (int i = 0; i < game.Cards.size(); i++) {
						Card c = game.Cards.get(i);
						if (c.id == id) {
							
							v1=new PVector((c.loc[0].x+c.loc[1].x+c.loc[2].x+c.loc[3].x)/4,(c.loc[0].y+c.loc[1].y+c.loc[2].y+c.loc[3].y)/4);
							c.loc[0].x = Integer.parseInt(Dane[6]);
							c.loc[0].y = Integer.parseInt(Dane[7]);
							c.loc[1].x = Integer.parseInt(Dane[8]);
							c.loc[1].y = Integer.parseInt(Dane[9]);
							c.loc[2].x = Integer.parseInt(Dane[10]);
							c.loc[2].y = Integer.parseInt(Dane[11]);
							c.loc[3].x = Integer.parseInt(Dane[12]);
							c.loc[3].y = Integer.parseInt(Dane[13]);
							
							
							if(c.block==false)
							{
								Effect e=new Effect(this,Type.ARROW,-1,v1,v2);
								e.blockId=id;
								game.Effects.add(e);
								c.block=true;
							}
							else
							{
								
							}
							
						}
					}
	
					
					break;
				case "MARKERS":
					if (game.tokens == true)
						{game.tokens = false; game.window=1; game.cardWidth=Integer.parseInt(Dane[2]); game.cardHeight=Integer.parseInt(Dane[3]);}
					else
						{game.tokens = true; game.window=0;}
					break;
				case "DEAD":
					id = Integer.parseInt(Dane[2]);
					for (int i = 0; i < game.Cards.size(); i++) {
						Card c = game.Cards.get(i);
						if (c.id == id) {
							c.isDead = true;

						}
					}
				}
			}
			game.Msgs.clear();
		}
	}

	public void mousePressed() {
		if (mouseButton == RIGHT) {
			
			
			
			int id = 1;
			for (int i = 0; i < game.Cards.size(); i++)
				if (game.Cards.get(i).id >= id)
					id = game.Cards.get(i).id + 1;
			
			if(game.T[0]==null) {game.T[0]=new PVector(mouseX,mouseY);}
			else
				if(game.T[1]==null) {game.T[1]=new PVector(mouseX,mouseY);}
				else
					if(game.T[2]==null) {game.T[2]=new PVector(mouseX,mouseY);}
					else
						if(game.T[3]==null) 
							{
							game.T[3]=new PVector(mouseX,mouseY); 
							game.Cards.add(new Card((int)game.T[0].x,(int)game.T[0].y,(int)game.T[1].x,(int)game.T[1].y,(int)game.T[2].x,(int)game.T[2].y,(int)game.T[3].x,(int)game.T[3].y,id,id,0,this));
							game.T[0]=null;
							game.T[1]=null;
							game.T[2]=null;
							game.T[3]=null;
							}
			
						
	
		}
		if (mouseButton == LEFT) 
		{
			
			 game.Effects.add(new Effect(this,Type.BOLT,8,1,game.cardWidth,game.cardHeight,game.Cards));
	
		}
	}

	public static void main(String args[]) {
		PApplet.main(new String[] { "--present", "MTGPackage.Main" });
	}
}
