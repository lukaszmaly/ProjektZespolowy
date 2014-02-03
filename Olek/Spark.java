package MTGPackage;


import processing.core.*;


public class Spark {
	
	PApplet parent;
	
	
	//rodzaj czasteczki, predkosc, polozenie, przyspieszenie i czas trwania
	 char type='e';
	 PVector v;
	 PVector loc;
	 PVector a;
	 float life;

	 //Konstruktor z lokalizacja	
	Spark(PVector l,PApplet p)
	{
		parent =p;
		 a = new PVector(parent.random(0.2f)-0.1f,parent.random(0.2f)-0.1f);
		    v = new PVector(parent.random(-5f,5f),parent.random(-5,5));
		    loc = l.get();
		    life = 30f;
	}
	
	//update polozenia i wyswietlanie
	public void run() 
	{
		update();
	    display();	
	}
	
	void update() 
	{
	    v.add(a);
	    loc.add(v);
	    life -= 1.0;
	}

	  
	  void display() 
	  {
		  
		  parent.stroke(0,0,255,life*5);
		  parent.strokeWeight(1);
		  parent.fill(0,255,0,life*5);
		  switch(this.type)
		  {
		  case 'e': parent.ellipse(loc.x, loc.y, parent.random(10), parent.random(10)); break;
		  case 'p': parent.point(loc.x,loc.y); break;
		  case 'c': parent.line(loc.x-5, loc.y, loc.x+5, loc.y);
		  			parent.line(loc.x, loc.y-5, loc.x, loc.y+5);
		  }
	   
	  }
	  boolean isDead() 
	  	{
		if (life < 0.0) 
			{
		      return true;
		    } 
			else 
			{
		      return false;
		    }
		}
}
