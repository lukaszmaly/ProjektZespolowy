package MTGPackage;
import processing.core.*;

public class Spark 
{
	PApplet parent;

	//rodzaj czasteczki, predkosc, polozenie, przyspieszenie,kolor, rozmiar, czas zycia
	char type;
	int size;
	PVector v,loc,a;
	float life;
	int r,g,b;

	Spark(PVector l,PApplet p, PVector a,PVector v,int life)
	{
		parent=p;
		this.v=new PVector(parent.random(0.2f)-0.1f,parent.random(0.2f)-0.1f);
		this.a=new PVector(parent.random(-5f,5f),parent.random(-5,5));		
		loc = l.get();	
		this.life = life;
		r=0;
		g=0;
		b=0;
	}

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

		switch(this.type)
		{
			case 'e': 
				parent.strokeWeight(2);
			
		  		if(parent.millis()%2==0)
		  		{
		  			parent.stroke(r-10,g-10,b-10,3);
		  			parent.fill(r-10,g-10,b-10,3);
		  			parent.ellipse(loc.x, loc.y,size*20, size*20);
		  		}
			  
		  		parent.stroke(r,g,b,life*5);
		  		parent.fill(r,g,b,life*5);
		  		parent.ellipse(loc.x, loc.y,size, size); 

	  
		  		break;
		  case 'p': parent.strokeWeight((int)(size*0.6));
		  			parent.point(loc.x,loc.y); break;
		  case 'c': parent.line(loc.x-9, loc.y, loc.x+9, loc.y);
		  			parent.line(loc.x, loc.y-9, loc.x, loc.y+9);
		  			break;
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
