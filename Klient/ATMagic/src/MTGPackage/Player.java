package MTGPackage;
import processing.core.*;

public class Player {
	PApplet parent;
	int id,life,manaPool;
	int w,b,u,r,g;
	
	Player(PApplet p, int id)
	{
		parent=p;
		this.life=20;
		this.manaPool=0;
		this.id=id;
		w=b=u=r=g=0;
		
	}
	
	void addMana(char c)
	{
		switch (c) {
		case 'b': this.b++; 
			
			break;
		case 'r': this.r++; 
		
		break;
		case 'g': this.g++; 
		
		break;
		case 'u': this.u++; 
		
		break;
		case 'w': this.w++; 
		
		break;

		default:
			break;
		}
		
	}
	
	void subtractMana(char c)
	{
		switch (c) {
		case 'b': this.b++; 
			
			break;
		case 'r': this.r++; 
		
		break;
		case 'g': this.g++; 
		
		break;
		case 'u': this.u++; 
		
		break;
		case 'w': this.w++; 
		
		break;

		default:
			break;
		}
		
	}

}
