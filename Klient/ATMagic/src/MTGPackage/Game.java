package MTGPackage;
import processing.core.*;

import java.util.*;
public class Game {

	PApplet parent;
	//ArrayList<Player> Players;
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
	
	boolean tokens=true;
	
	Game(PApplet p)
	{
		parent=p;
		//Players=new ArrayList<Player>();
		Cards=new ArrayList<Card>();
		Effects=new ArrayList<Effect>();
		
		board=new Board(p);
		fazy=new Phrases(p);
		
		P1=new Player(p,0,"PlayerOne");
		P2=new Player(p,1,"PlayerTwo");
		
		cardWidth=113;
		cardHeight=162;
		
		String[] lines = parent.loadStrings("log.txt");
		
		edge1=parent.loadImage("LG.png"); 
		edge2=parent.loadImage("PG.png"); 
		edge3=parent.loadImage("PD.png"); 
		edge4=parent.loadImage("LD.png");
		skull = parent.loadImage("skull.jpg");
		upArrow=parent.loadImage("arrows_up.png");
		bolt=parent.loadImage("lightning-single1.png");
		
		calibr = parent.createFont("Arial", 50, true);
		f = parent.createFont("Arial", 12, true);
	}
	
	
}
