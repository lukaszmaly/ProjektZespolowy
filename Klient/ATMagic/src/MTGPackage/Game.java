package MTGPackage;
import processing.core.*;
import hypermedia.net.UDP;
import java.util.*;

import MTGPackage.Effect.Type;
public class Game {

	PApplet parent;
	Player P1,P2;
	ArrayList<Card> Cards;
	ArrayList<Effect> Effects;
	ArrayList<String> Msgs = new ArrayList<String>();
	
	PVector[] T=new PVector[4];
	
	Board board;
	Phrases fazy;
	
	int cardHeight,
		cardWidth,
		window=0,
		log=0;
	Object mutex = new Object();
	
	String[] lines;
	
	PImage 	edge1,
			edge2,
			edge3,
			edge4,
			skull,
			upArrow,
			bolt;
	
	PFont 	calibr,
			f;
	UDP udp;
	boolean tokens=true;
	
	Game(PApplet p)
	{
		parent=p;
		Cards=new ArrayList<Card>();
		Effects=new ArrayList<Effect>();
		
		board=new Board(p);
		board.game=this;
		fazy=new Phrases(p);
		
		P1=new Player(p,0,"PlayerOne");
		P2=new Player(p,1,"PlayerTwo");
		
		cardWidth=113;
		cardHeight=162;
		
		this.lines = parent.loadStrings("log.txt");
		
		edge1=parent.loadImage("LG.png"); 
		edge2=parent.loadImage("PG.png"); 
		edge3=parent.loadImage("PD.png"); 
		edge4=parent.loadImage("LD.png");
		skull = parent.loadImage("skull.jpg");
		upArrow=parent.loadImage("arrows_up.png");
		bolt=parent.loadImage("lightning-single1.png");
		
		calibr = parent.createFont("Arial", 50, true);
		f = parent.createFont("Arial", 12, true);
		
		udp = new UDP(parent, 600);
		udp.listen(true);
	}
	
	public void goThroughEffects()
	{
		if(this.Effects.isEmpty()==false)
		{
			for(int i=0;i<this.Effects.size();i++)
			{

			
			Effect e = this.Effects.get(i);
			
			e.drawEffect();
			if(e.life==0) this.Effects.remove(i);
			}
		}	
	}
	
	
	public void goThroughCards()
	{
		if (this.Cards.isEmpty() == false) 
		{
			for (int i = 0; i < this.Cards.size(); i++) 
			{
				Card c = this.Cards.get(i);
				if (c.isDead == true && c.deadCounter <= 0) this.Cards.remove(i);			
				else 
					{
						if (c.frame == true) c.drawEdges();
						if (c.se.size() > 0) 
						{
							for (int j = 0; j < c.se.size(); j++) 
							{
								SparkEdge se = c.se.get(j);
								for (int k = 0; k < se.ss.size(); k++) 
								{
									SparkSystem ss = se.ss.get(k);
									if (c.sparkTime%3==1)
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
	
	public void processMessages() {
		synchronized (mutex) {
			for (String message : this.Msgs) {

				String[] Dane = message.split(" ");
			
				

				switch (Dane[1]) {
				case "ADD":

				
					this.Cards.add(new Card((int) (Float.parseFloat(Dane[6])),
							(int) (Float.parseFloat(Dane[7])), (int) (Float
									.parseFloat(Dane[8])), (int) (Float
									.parseFloat(Dane[9])), (int) (Float
									.parseFloat(Dane[10])), (int) (Float
									.parseFloat(Dane[11])), (int) (Float
									.parseFloat(Dane[12])), (int) (Float
									.parseFloat(Dane[13])), Integer
									.parseInt(Dane[2]), Integer
									.parseInt(Dane[3]), Integer
									.parseInt(Dane[4]), parent,this));
					break;

					
				case "ADDMANA":
				{
					
					int id=Integer.parseInt(Dane[2]);
				
					char c=Dane[3].charAt(0);
					
				
					
					if(id==1) this.P1.addMana(c); else this.P2.addMana(c);
					break;
					
				}
				case "EFFECT":
				{
					
					String type=Dane[2];
					
					if(type.compareTo("BOLT")==0)
					{
						
						int playerId=Integer.parseInt(Dane[3]);
						int cardId=Integer.parseInt(Dane[4]);
						
						if(cardId>=0) this.Effects.add(new Effect(parent,Type.BOLT,10,cardId,this.cardWidth,this.cardHeight,this.Cards));
					}
					
					
					
					break;
				}
				case "SUBMANA":
				{
					char c=Dane[3].charAt(0);
					int id=Integer.parseInt(Dane[2]);
				
					if(id==1)
						{
						
						this.P1.subtractMana(c); 
						} 
					else 
					{
						
						this.P2.subtractMana(c);
					
					}
					break;
					
				}
				case "SUBMANA2":
				{
					
					int id=Integer.parseInt(Dane[2]);
					
					if(id==1)
					{
						
					
						this.P1.subtractMana(Integer.parseInt(Dane[3]),Integer.parseInt(Dane[4]),Integer.parseInt(Dane[5]),Integer.parseInt(Dane[6]),Integer.parseInt(Dane[7])); 
						
					} 
				else 
				{
					
					this.P1.subtractMana(Integer.parseInt(Dane[3]),Integer.parseInt(Dane[4]),Integer.parseInt(Dane[5]),Integer.parseInt(Dane[6]),Integer.parseInt(Dane[7])); 
				
				}
					
					break;
					
				}
				
				case "ADDLIFE":
				{
					int id=Integer.parseInt(Dane[2]);
					int q=Integer.parseInt(Dane[3]);
					if(id==1)this.P1.life+=q; else this.P2.life+=q;
					break;
					
				}
				case "SUBLIFE":
				{
					int id=Integer.parseInt(Dane[2]);
					int q=Integer.parseInt(Dane[3]);
					if(id==1)this.P1.life-=q; else this.P2.life-=q;
					break;
					
				}
				
		
				
				case "STACK":
					int id=Integer.parseInt(Dane[2]);
					 char color=Dane[3].charAt(0);
					 if(id==0) {
						 switch(color)
						 {
						 case 'R':
							 this.board.stack1.r=255;
							 this.board.stack1.g=0;
							 this.board.stack1.b=0;
							 break;
						 case 'G':
							 this.board.stack1.r=0;
							 this.board.stack1.g=255;
							 this.board.stack1.b=0;
							 break;
						 
						 case 'B':
							 this.board.stack1.r=0;
							 this.board.stack1.g=0;
							 this.board.stack1.b=255;
							 break;
						 }
					 }
						 else
							 if(id==1) {
								 switch(color)
								 {
								 case 'R':
									 this.board.stack2.r=255;
									 this.board.stack2.g=0;
									 this.board.stack2.b=0;
									 break;
								 case 'G':
									 this.board.stack2.r=0;
									 this.board.stack2.g=255;
									 this.board.stack2.b=0;
									 break;
								 
								 case 'B':
									 this.board.stack2.r=0;
									 this.board.stack2.g=0;
									 this.board.stack2.b=255;
									 break;
								 
								 
								 }
					 }
					
					break;
				case "UPDATE":
					  id = Integer.parseInt(Dane[2]);

					for (int i = 0; i < this.Cards.size(); i++) {
						Card c = this.Cards.get(i);
						

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

								dist[i] = parent.dist(c.loc[i].x, c.loc[i].y, 0, 0);

							}
							mindist = parent.min(dist);

							for (i = 0; i < 4; i++) {
								if (parent.dist(c.loc[i].x, c.loc[i].y, 0, 0) == mindist)
									c.LG = c.loc[i];
							}

							for (i = 0; i < 4; i++) {

								dist[i] = parent.dist(c.loc[i].x, c.loc[i].y, parent.width, 0);

							}

							mindist = parent.min(dist);

							for (i = 0; i < 4; i++) {
								if (parent.dist(c.loc[i].x, c.loc[i].y, parent.width, 0) == mindist)
									c.PG = c.loc[i];
							}

							for (i = 0; i < 4; i++) {

								dist[i] = parent.dist(c.loc[i].x, c.loc[i].y, parent.width,
										parent.height);

							}
							mindist = parent.min(dist);

							for (i = 0; i < 4; i++) {
								if (parent.dist(c.loc[i].x, c.loc[i].y, parent.width, parent.height) == mindist)
									c.PD = c.loc[i];
							}

							for (i = 0; i < 4; i++) {

								dist[i] = parent.dist(c.loc[i].x, c.loc[i].y, 0,
										parent.height);

							}
							mindist = parent.min(dist);
							for (i = 0; i < 4; i++) {
								if (parent.dist(c.loc[i].x, c.loc[i].y, 0, parent.height) == mindist)
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
					
					this.fazy.zmien_faze();
					break;
				case "ATTACK":
					id = Integer.parseInt(Dane[2]);
					for (int i = 0; i < this.Cards.size(); i++) {
						Card c = this.Cards.get(i);
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
					for (int i = 0; i < this.Cards.size(); i++) {
						Card c = this.Cards.get(i);
						if (c.id == attackId) {
							
							
							v2=new PVector((c.loc[0].x+c.loc[1].x+c.loc[2].x+c.loc[3].x)/4,(c.loc[0].y+c.loc[1].y+c.loc[2].y+c.loc[3].y)/4);
							
							
								

						}
					}
					for (int i = 0; i < this.Cards.size(); i++) {
						Card c = this.Cards.get(i);
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
								Effect e=new Effect(parent,Type.ARROW,-1,v1,v2);
								e.blockId=id;
								this.Effects.add(e);
								c.block=true;
							}
							else
							{
								
							}
							
						}
					}
	
					
					break;
				case "MARKERS":
					if (this.tokens == true)
						{this.tokens = false; this.window=1; this.cardWidth=Integer.parseInt(Dane[2]); this.cardHeight=Integer.parseInt(Dane[3]);}
					else
						{this.tokens = true; this.window=0;}
					break;
				case "DEAD":
					id = Integer.parseInt(Dane[2]);
					for (int i = 0; i < this.Cards.size(); i++) {
						Card c = this.Cards.get(i);
						if (c.id == id) {
							c.isDead = true;

						}
					}
				}
			}
			this.Msgs.clear();
		}
	}

}
