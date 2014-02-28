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
		//this.v=new PVector(parent.random(0.2f)-0.1f,parent.random(-0.2f)+0.1f);
		if(v!=null) this.v=v.get(); else this.v=new PVector(parent.random(-1,1), parent.random(-1,1));
		if(a!=null) this.a=a.get(); else this.a=new PVector(parent.random(-0.05f,0.05f),parent.random(-0.5f,0.5f));
		//this.a=new PVector(parent.random(0,5f),parent.random(0,5));		
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
			case 'a':
				parent.strokeWeight(1);
				parent.fill(r,g,b,2);
				parent.stroke(r,g,b,(int)(life*0.3));
				parent.ellipse(loc.x, loc.y,size, size);
				parent.ellipse(loc.x, loc.y,size*2, size*2);
				parent.ellipse(loc.x, loc.y,size*3, size*3);
				break;
			case 'e': 
				parent.strokeWeight(2);
		  		parent.stroke(r,g,b,life*5);		  		
		  		parent.noFill();
		  		parent.ellipse(loc.x, loc.y,size, size);
		  		parent.stroke(r,g,b,life*4);
		  		parent.ellipse(loc.x-10, loc.y-10,size-2, size-2);
		  		parent.stroke(r,g,b,life*3);
		  		parent.ellipse(loc.x-20, loc.y-20,size-4, size-4);
		  		parent.stroke(r,g,b,life*2);
		  		parent.ellipse(loc.x-30, loc.y-30,size-6, size-6);
		  		parent.stroke(r,g,b,life*1);
		  		parent.ellipse(loc.x-40, loc.y-40,size-8, size-8);
		  		break;
		  		
			case 'p': 
				parent.strokeWeight((int)(size*0.6));
		  		parent.fill(r,g,b,life*5);
		  		parent.point(loc.x,loc.y); 
		  		break;
		  			
		  case 'c': 
			  	parent.strokeWeight(1);
		  		parent.stroke(r,g,b,life);
		  		parent.fill(r,g,b,life);
			  	parent.line(loc.x-(int)(0.10*life), loc.y, loc.x+(int)(0.10*life), loc.y);
		  		parent.line(loc.x, loc.y-(int)(0.10*life), loc.x, loc.y+(int)(0.10*life));
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
