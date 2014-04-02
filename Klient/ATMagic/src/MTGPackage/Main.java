package MTGPackage;

import hypermedia.net.UDP;
import processing.core.*;

import java.nio.file.attribute.AttributeView;
import java.util.*;

import javax.management.monitor.Monitor;

import MTGPackage.Effect.Type;

public class Main extends PApplet {

	Game game=new Game(this);
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
			game.board.displayLog();
			game.goThroughCards();
			game.goThroughEffects();			
		}
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
					game.window=1;
					}
			break;
		
			case 'd':
				if (game.Cards.isEmpty() == false) 
					{
					Card c = game.Cards.get((int) random(game.Cards.size()));
					c.isDead = true;
					}
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
			int id = 1;
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
							game.Cards.add(new Card((int)game.T[0].x,(int)game.T[0].y,(int)game.T[1].x,(int)game.T[1].y,(int)game.T[2].x,(int)game.T[2].y,(int)game.T[3].x,(int)game.T[3].y,id,id,0,this,game));
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
		}
	}
}
