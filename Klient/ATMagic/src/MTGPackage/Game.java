package MTGPackage;
import processing.core.*;
import hypermedia.net.UDP;

import java.util.*;

import MTGPackage.Effect.Type;
public class Game {

	PApplet parent;
	
	Player P1,P2;
	Player ActivePlayer;
	ArrayList<Card> Cards;
	ArrayList<Effect> Effects;
	ArrayList<String> Msgs = new ArrayList<String>();
	ArrayList<PImage> fires2=new ArrayList<PImage>();
	ArrayList<PImage> fires=new ArrayList<PImage>();
	ArrayList<PImage> dbCards=new ArrayList<PImage>();
	char GameType;
	PVector[] T=new PVector[4];
	
	Board board;
	Phrases fazy;
	
	int cardHeight,
		cardWidth,
		window=0,
		log=0,
		drawCounter,
		cardsInDB=24;
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
		ActivePlayer=P2;
		
		cardWidth=100;
		cardHeight=145;
		
		this.lines = parent.loadStrings("log.txt");
		drawCounter=0;
		edge1=parent.loadImage("LG.png"); 
		edge2=parent.loadImage("PG.png"); 
		edge3=parent.loadImage("PD.png"); 
		edge4=parent.loadImage("LD.png");
		skull = parent.loadImage("skull.jpg");
		upArrow=parent.loadImage("arrows_up.png");
		//bolt=parent.loadImage("lightning-single1.png");
		
		calibr = parent.createFont("Arial", 50, true);
		f = parent.createFont("Arial", 12, true);
		
		String s="AnimatedFire2/fire1_0";
		for(int i=51;i<=99;i++)
		{
			PImage img=parent.loadImage(s+"0"+i+".png");
			parent.println(s+"0"+i+".png");
			fires2.add(img);
		}
		for(int i=100;i<=125;i++)
		{
			PImage img=parent.loadImage(s+i+".png");
			parent.println(s+i+".png");
			fires2.add(img);
		}
		
		 s="AnimatedFire/animatedfire";
		for(int i=1;i<=24;i++)
		{
			PImage img=parent.loadImage(s+i+".png");
			//println(s+i+".png");
			fires.add(img);
		}
		s="Cards/";
		for(int i=0;i<cardsInDB;i++)
		{
			PImage img=parent.loadImage(s+i+".jpg");
			dbCards.add(img);
		}
		udp = new UDP(parent, 600);
		udp.listen(true);
	}
	
	public void removeById(int id)
	{
		for(int i=0;i<Effects.size();i++)
		{
			Effect e=Effects.get(i);
			if(e.blockId1==id || e.blockId2==id)
			{
				this.Effects.remove(i);
			}
		}
	}
	public void editEffectByID(int id,float x,float y)
	{
		for(int i=0;i<this.Effects.size();i++)
		{
			Effect e = this.Effects.get(i);
			if(e.blockId1==id)
			{
				
				e.v1 = new PVector(x,y);
			}
			if(e.blockId2==id)
			{
				
				e.v2 = new PVector(x,y);
			}
		}
		
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
//////////////////////////////////////////////////////////////////////////////////////	
	public void processMessages() {
		synchronized (mutex) {
			//parent.println(this.Msgs.size());
			for (String message : this.Msgs) {

				String[] Dane = message.split(" ");
			
				

				switch (Dane[1]) {
				case "ACTIVEPLAYER":
					 int id=Integer.parseInt(Dane[2]);
					 if(id==1)
						 this.ActivePlayer=this.P1;
					 else 
						 this.ActivePlayer=this.P2;
				break;
					
				
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
									.parseInt(Dane[4]), parent,this,-1,-1));
					break;

					
				case "ADDMANA":
				{
					
					 id=Integer.parseInt(Dane[2]);
				
					char c=Dane[3].charAt(0);
					
				
					
					if(id==1) this.P1.addMana(c); else this.P2.addMana(c);
					break;
					
				}
				
				case "ADDLIFE":
				
					 id=Integer.parseInt(Dane[2]);
					int q=Integer.parseInt(Dane[3]);
					if(id==1)this.P1.life+=q; else this.P2.life+=q;
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
							
							c.power=Integer.parseInt(Dane[14]);
							c.toughness=Integer.parseInt(Dane[15]);
							
							if(c.loc[0].x>=c.loc[3].x && c.loc[0].y<=c.loc[3].y) c.direction=1;
					    	else if(c.loc[0].x>=c.loc[3].x && c.loc[0].y>=c.loc[3].y) c.direction=2;
					    	else if(c.loc[0].x<=c.loc[3].x && c.loc[0].y>=c.loc[3].y) c.direction=3;
					    	else c.direction=4;
							c.attack = true;
					
							
							PVector center = new PVector((c.loc[0].x+c.loc[1].x+c.loc[2].x+c.loc[3].x)/4,(c.loc[0].y+c.loc[1].y+c.loc[2].y+c.loc[3].y)/4);
							editEffectByID(c.id,center.x,center.y);
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
							c.blockedId=attackId;
							v1=new PVector((c.loc[0].x+c.loc[1].x+c.loc[2].x+c.loc[3].x)/4,(c.loc[0].y+c.loc[1].y+c.loc[2].y+c.loc[3].y)/4);
							
							c.loc[0].x = Integer.parseInt(Dane[6]);
							c.loc[0].y = Integer.parseInt(Dane[7]);
							c.loc[1].x = Integer.parseInt(Dane[8]);
							c.loc[1].y = Integer.parseInt(Dane[9]);
							c.loc[2].x = Integer.parseInt(Dane[10]);
							c.loc[2].y = Integer.parseInt(Dane[11]);
							c.loc[3].x = Integer.parseInt(Dane[12]);
							c.loc[3].y = Integer.parseInt(Dane[13]);
							v1=new PVector((c.loc[0].x+c.loc[1].x+c.loc[2].x+c.loc[3].x)/4,(c.loc[0].y+c.loc[1].y+c.loc[2].y+c.loc[3].y)/4);
							c.power=Integer.parseInt(Dane[15]);
							c.toughness=Integer.parseInt(Dane[16]);
							
							if(c.loc[0].x>=c.loc[3].x && c.loc[0].y<=c.loc[3].y) c.direction=1;
					    	else if(c.loc[0].x>=c.loc[3].x && c.loc[0].y>=c.loc[3].y) c.direction=2;
					    	else if(c.loc[0].x<=c.loc[3].x && c.loc[0].y>=c.loc[3].y) c.direction=3;
					    	else c.direction=4;
							
							if(c.block==false)
							{
								Effect e=new Effect(parent,this,Type.ARROW,-1,v1,v2);
								e.blockId1=id;
								e.blockId2=c.blockedId;
								this.Effects.add(e);
								c.block=true;
							}
							else
							{
								PVector center = new PVector((c.loc[0].x+c.loc[1].x+c.loc[2].x+c.loc[3].x)/4,(c.loc[0].y+c.loc[1].y+c.loc[2].y+c.loc[3].y)/4);
								editEffectByID(c.id,center.x,center.y);
							}
						
							
						}
					
				
	}
					
					break;
				case "CARDONSTACK":
					int playerId=Integer.parseInt(Dane[2]);
					int cardId=Integer.parseInt(Dane[3]);
					if(playerId==1)
					{
						this.board.stack1.cardOnStack=true;
						this.board.stack1.cardOnStackId=cardId;
					}
					else
					{
						this.board.stack2.cardOnStack=true;
						this.board.stack2.cardOnStackId=cardId;
					}
					break;
				case "DAMAGE":
					id=Integer.parseInt(Dane[2]);
					 q=Integer.parseInt(Dane[3]);
					 Effects.add(new Effect(parent, this, Type.DAMAGE, 50, id));
					Effect ef= Effects.get(Effects.size()-1);
					ef.q=q;
					break;
					
				case "DEAD":
					id = Integer.parseInt(Dane[2]);
					for (int i = 0; i < this.Cards.size(); i++) {
						Card c = this.Cards.get(i);
						if (c.id == id) {
							c.isDead = true;
							this.Effects.add(new Effect(parent,this,Type.DEATH,100,0));
							c.deadCounter=100;
							//c.sparkTime=15;
							removeById(id);
							 PVector acc=null;
							    PVector vel=null;
							  for( i=0;i<c.se.size();i++)
						    	{
						    	
						    	SparkEdge e=c.se.get(i);
						    //c.sparkTime=15;
						    	e.changeSparkType('c',0,0,0,6,vel,acc);
						    	}
						}
					}
					
				case "DRAW":
				{
					id=Integer.parseInt(Dane[2]);
					 String s=Dane[3];
					 s="Draw\n"+s;
					 if(id==1)
					 {
						 //this.board.lib1.r=(int)parent.random(255);
						 //this.board.lib1.g=(int)parent.random(255);
						 //this.board.lib1.b=(int)parent.random(255);
						 this.Effects.add(new Effect(parent,this,120,this.board.lib1.position,new PVector(0,0),40, 0, 255, 0,s));
					 }
					 if(id==2)
					 {
						 this.Effects.add(new Effect(parent,this,120,this.board.lib2.position,new PVector(0,0),40, 0, 255, 0,s));
					 }
				}
				
		break;
					
				case "EFFECT":
				{
					
					String type=Dane[2];
					
					if(type.compareTo("BOLT")==0)
					{
						
						 playerId=Integer.parseInt(Dane[3]);
						 cardId=Integer.parseInt(Dane[4]);
						
						if(cardId>=0) this.Effects.add(new Effect(parent,this,Type.BOLT,24,cardId,this.cardWidth,this.cardHeight,this.Cards));
					}
					else
					if(type.compareTo("SPEAR")==0)
					{
						PVector v=null,u=null;
						 playerId=Integer.parseInt(Dane[3]);
						 cardId=Integer.parseInt(Dane[4]);
						if(playerId==1)
						{
							u=new PVector(parent.width/2,parent.height);
							v=new PVector(parent.width-100,0);
							this.Effects.add(new Effect(parent,this, Type.SPEAR, 20,u,v));
						}
						if(playerId==2)
						{
							u=new PVector(parent.width/2,0);
							v=new PVector(parent.width-100,parent.height);
							this.Effects.add(new Effect(parent,this, Type.SPEAR, 20,u,v));
						}
						//if(cardId>=0) this.Effects.add(new Effect(parent,this,Type.BOLT,16,cardId,this.cardWidth,this.cardHeight,this.Cards));
					}
					else
					if(type.compareTo("BOOST")==0)
					{
						
						 cardId=Integer.parseInt(Dane[4]);
						this.Effects.add(new Effect(parent,this,Type.BOOST,50,cardId));
						
						
					}
					else
						if(type.compareTo("REDUCTION")==0)
					{
							
							 cardId=Integer.parseInt(Dane[4]);
							this.Effects.add(new Effect(parent,this,Type.REDUCTION,50,cardId));
					}
						else if(type.compareTo("FIRE")==0)
						{
							cardId=Integer.parseInt(Dane[4]);
							this.Effects.add(new Effect(parent,this,Type.FIRE,48,cardId));
						}
						else if(type.compareTo("FIRE2")==0)
						{
							cardId=Integer.parseInt(Dane[4]);
							this.Effects.add(new Effect(parent,this,Type.FIRE2,74,cardId));
						}
					
					
					
					break;
				}
					
				case "MARKERS":
					char ch=Dane[4].charAt(0);
					 id=Integer.parseInt(Dane[5]);
					 this.GameType=ch;
					 if(P1.id==id)
					 {
						 P1.isLocal=true;
						 P2.isLocal=false;
					 }
					 else
					 {
						 P2.isLocal=true;
						 P1.isLocal=false; 
					 }
					 
					if (this.tokens == true)
						{
						this.tokens = false; 
						if (this.GameType=='S')
							this.window=1;
						else window=2;
						}
					else
						{
						this.tokens=true;
						this.window=0;
						}

						this.cardWidth=Integer.parseInt(Dane[2]); 
						this.cardHeight=Integer.parseInt(Dane[3]);
						
					
					break;
					
				case "NEXTPHASE":
					
					this.fazy.zmien_faze();
					break;
				
				
				case "SUBMANA":
				{
					 char c=Dane[3].charAt(0);
					 id=Integer.parseInt(Dane[2]);
				
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
					
					 id=Integer.parseInt(Dane[2]);
					
					if(id==1)
					{
						
					
						this.P1.subtractMana(Integer.parseInt(Dane[3]),Integer.parseInt(Dane[4]),Integer.parseInt(Dane[5]),Integer.parseInt(Dane[6]),Integer.parseInt(Dane[7])); 
						
					} 
				else 
				{
					
					this.P2.subtractMana(Integer.parseInt(Dane[3]),Integer.parseInt(Dane[4]),Integer.parseInt(Dane[5]),Integer.parseInt(Dane[6]),Integer.parseInt(Dane[7])); 
				
				}
					
					break;
					
				}
				
				
				case "SUBLIFE":
				{
					 id=Integer.parseInt(Dane[2]);
					 q=Integer.parseInt(Dane[3]);
					if(id==1)this.P1.life-=q; else this.P2.life-=q;
					break;
					
				}
				case "SCRY":
				{
					id=Integer.parseInt(Dane[2]);
					 String s=Dane[3];
					 s="Scry\n"+s;
					 if(id==1)
					 {
						 //this.board.lib1.r=(int)parent.random(255);
						 //this.board.lib1.g=(int)parent.random(255);
						 //this.board.lib1.b=(int)parent.random(255);
						// parent.pushMatrix();
						// parent.rotate(parent.PI);
						 this.Effects.add(new Effect(parent,this,120,this.board.lib1.position,new PVector(0,0),35, 255, 0, 0,s));
						// parent.popMatrix();
					 }
					 if(id==2)
					 {
						 this.Effects.add(new Effect(parent,this,120,this.board.lib2.position,new PVector(0,0),35, 255, 0, 0,s));					 }
				}
				break;
		
				
				case "STACK":
					 id=Integer.parseInt(Dane[2]);
					 char color=Dane[3].charAt(0);
					 if(id==1) {
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
							 this.board.stack1.cardOnStack=false;
							 break;
						 }
					 }
						 else
							 if(id==2) {
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
									 this.board.stack2.cardOnStack=false;
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
							if(c.attack==true || c.block==true)
							{
								removeById(c.id);
							}
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
							
							c.loc2[0].x = c.loc[0].x;
							c.loc2[0].y = c.loc[0].y;
							c.loc2[1].x = c.loc[1].x;
							c.loc2[1].y = c.loc[1].y;
							c.loc2[2].x = c.loc[2].x;
							c.loc2[2].y = c.loc[2].y;
							c.loc2[3].x = c.loc[3].x;
							c.loc2[3].y = c.loc[3].y;
							
							c.power=Integer.parseInt(Dane[14]);
							c.toughness=Integer.parseInt(Dane[15]);

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

							//c.drawEdges();
							break;

						}

					}
					break;
				
				

				
				
				
				}
			}
			this.Msgs.clear();
		}
	}

}
