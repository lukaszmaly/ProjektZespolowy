package MTGPackage;

import hypermedia.net.UDP;
import processing.core.*;

import java.nio.file.attribute.AttributeView;
import java.util.*;

import javax.management.monitor.Monitor;

import MTGPackage.Effect.Type;

public class Main extends PApplet {

	public Game game=new Game(this);
	int counter=5;
	PVector v=null,u=null;
	PImage img;
	ArrayList<PImage> imgs=new ArrayList<PImage>();
	//Game game;
	public void setup()
		{		
		size(displayWidth, displayHeight);

		background(0);		
		}
	@Override
	public boolean sketchFullScreen() {
		// TODO Auto-generated method stub
		return true;
	}

	public void draw() 
	{
		
		
		game.drawCounter++;
		
		counter--;
		if(game.window==0 && game.tokens == true)
		{	
			background(0);
			game.board.displayTokens();
			game.processMessages();
		}
		else
		if(game.window==1)
		{
			
			game.processMessages();
		
			background(0);
			game.board.display(255, 40, 4,game.P1,game.P2,game.cardWidth,game.cardHeight);
			game.board.display2(255, 40, 4,game.P1,game.P2,game.cardWidth,game.cardHeight);
			game.board.displayLog();
			rect(300,200,game.cardWidth,game.cardHeight);
			game.goThroughCards();
			game.goThroughEffects();
			
		
			
		} 
		else if(game.window==2)
		{
			game.processMessages();
			
			background(0);
			game.board.display(255, 40, 4,game.P1,game.P2,game.cardWidth,game.cardHeight);
			game.board.display2(255, 40, 4,game.P1,game.P2,game.cardWidth,game.cardHeight);
			game.board.displayLog();
			rect(300,200,game.cardWidth,game.cardHeight);
			game.goThroughCards();
			game.goThroughEffects();
			
		
		}
	//image(loadImage("scry.png"),100,200,100,50);
	}

	//////////////////////////////////////////////////////////////////////////////////////////////
	
	public void keyPressed() 
		{
		switch (key) 
			{	
			case 't':
				if (game.tokens == false)
					{
					game.tokens = true; 
					game.window=0;
					}
				else
					{
					game.tokens = false;
					game.window=2;
					}
			break;
		
			case 's':
				if(game.board.stack1.cardOnStack==true)
				game.board.stack1.cardOnStack=false;
				else game.board.stack1.cardOnStack=true;
				break;
			
			case 'd':
				if (game.Cards.isEmpty() == false) 
					{
					Card c = game.Cards.get((int) random(game.Cards.size()));
					c.isDead = true;
					   PVector acc=null;
					    PVector vel=null;
					  for(int i=0;i<c.se.size();i++)
				    	{
				    	
				    	SparkEdge e=c.se.get(i);
				    c.sparkTime=15;
				    	e.changeSparkType('c',0,0,0,6,vel,acc);
				    	}
					}
			break;
			case 'p':
				if(game.ActivePlayer==game.P1)
					game.ActivePlayer=game.P2;
				else 					game.ActivePlayer=game.P1;

					
				break;
			case 'g':
				if(game.GameType=='S') {game.GameType='M';game.window=2;}
				else {game.GameType='S';game.window=1;}
				
				if(game.P1.isLocal==false)
					game.P1.isLocal=true;
				else game.P1.isLocal=false;
				
				if(game.P2.isLocal==false)
					game.P2.isLocal=true;
				else game.P2.isLocal=false;
				break;
					
			
	
			case 'l' :
				if(game.lines.length>game.log && game.lines[game.log]!="Utworzono serwer" && game.lines[game.log]!="Zamknieto serwer") 
					game.udp.send( game.lines[game.log], "localhost",600  );
				if(game.lines[game.log]=="Zamknieto serwer") 
					{
					game.Cards.clear(); 
					game.tokens=true; 
					}
				game.log++;
				if(game.log>=game.lines.length) game.log=0;
			break;
		}
	}


	public void mousePressed() 
		{
		if (mouseButton == RIGHT) 
			{
			int id = 0;
			for (int i = 0; i < game.Cards.size(); i++)
				if (game.Cards.get(i).id >= id)
					id = game.Cards.get(i).id + 1;
				if(game.T[0]==null) 
					{
					game.T[0]=new PVector(mouseX,mouseY);
					}
				else
					if(game.T[1]==null) 
					{
						game.T[1]=new PVector(mouseX,mouseY);
					}
				else
					if(game.T[2]==null) 
						{
						game.T[2]=new PVector(mouseX,mouseY);}
					else
						if(game.T[3]==null) 
							{
							game.T[3]=new PVector(mouseX,mouseY); 
							game.Cards.add(new Card((int)game.T[0].x,(int)game.T[0].y,(int)game.T[1].x,(int)game.T[1].y,(int)game.T[2].x,(int)game.T[2].y,(int)game.T[3].x,(int)game.T[3].y,id,id,1,this,game,1,1));
							game.T[0]=null;
							game.T[1]=null;
							game.T[2]=null;
							game.T[3]=null;
							}
				}
		if (mouseButton == LEFT) 
		{	
			//game.Effects.add(new Effect(this,game,Type.FIRESHIELD,100,1));
			//game.Effects.add(new Effect(this,game,Type.FIRE2,74,1));
			/*
			game.Effects.add(new Effect(this,game,Type.DEATH,300,0));
			Card c = game.Cards.get(0);
			c.isDead = true;
			c.deadCounter=300;
			//c.sparkTime=15;
			game.removeById(0);
			
			*/
			
			//game.Effects.add(new Effect(this,game,Type.FIRE,48,1));
		//	game.Effects.add(new Effect(this,game,Type.BOOST,50,1));
		//	game.Effects.add(new Effect(this,game,Type.REDUCTION,50,2));
			 //game.Effects.add(new Effect(this, game, Type.DAMAGE, 40, 0));
			// Effect e= game.Effects.get(game.Effects.size()-1);
			//	e.q=3;
			//game.Effects.add(new Effect(this, game, 200,new PVector(0.5f,0.5f), new PVector(0.001f,0.001f), 30, 0, 255, 0, "TEXT"));
			
			//game.Effects.add(new Effect(this,game,Type.BOLT,40,0,game.cardWidth,game.cardHeight,game.Cards));	
			/*if(v==null)v=new PVector(mouseX,mouseY);
			else
			{
				u=new PVector(mouseX,mouseY);
				game.Effects.add(new Effect(this,game, Type.SPEAR, 60,new PVector(v.x,v.y),new PVector(u.x, u.y)));
				v=null;
				u=null;
			}
			
			if(v==null)v=new PVector(mouseX,mouseY);
			else
			{
				u=new PVector(mouseX,mouseY);
				game.Effects.add(new Effect(this,game, Type.ARROW, 60,new PVector(v.x,v.y),new PVector(u.x, u.y)));
				v=null;
				u=null;
			}
			*/
			game.Effects.add(new Effect(this,game,Type.SCRY,200,game.board.lib1.position,new PVector(0,0),true));
			game.Effects.add(new Effect(this, game, 200, new PVector(game.board.lib1.position.x,game.board.lib1.position.y-0.05f), new PVector(0, 0),35, 0, 0, 255, "1",true));
			 //game.Effects.add(new Effect(this,game,Type.FIRE,30,1,game.cardWidth,game.cardHeight,game.Cards));	
		
		}
		
	}
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	public static void main(String args[]) {
		PApplet.main(new String[] { "--present", "MTGPackage.Main" });
	}
	
	public void receive(byte[] data, String ip, int port) 
	{
	data = subset(data, 0, data.length);
	String message = new String(data);
	synchronized (game.mutex) 
		{
		game.Msgs.add(message);
		println(message);
		}
	}
}
