package MTGPackage;
import processing.core.*;

public class Player {
	PApplet parent;
	int id,life,manaPool;
	int w=0,b=0,u=0,r=0,g=0;
	String name;
	boolean isLocal=true;	
	Player(PApplet p, int id,String name)
	{
		parent=p;
		this.name=name;
		this.life=20;
		
		this.id=id;
		

	}
	
	void addMana(char c)
	{
		switch (c) {
		case 'B': this.b++; 
			
			break;
		case 'R': this.r++; 
		
		break;
		case 'G': this.g++; 
		
		break;
		case 'U': this.u++; 
		
		break;
		case 'W': this.w++; 
		
		break;

		default:
			break;
		}
		
	}
	 void subtractMana(int w, int u, int b,int r , int g)
	{
	this.w-=w;
	this.u-=u;
	this.b-=b;
	this.g-=g;
	this.r-=r;
		}
	void subtractMana(char c)
	{
		switch (c) {
		case 'B': this.b--; 
			
			break;
		case 'R': this.r--; 
		
		break;
		case 'G': this.g--; 
		
		break;
		case 'U': this.u--; 
		
		break;
		case 'W': this.w--; 
		
		break;

		default:
			break;
		}
		
	}

}
