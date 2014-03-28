package MTGPackage;
import processing.core.*;

public class Player {
	PApplet parent;
	int id,life,manaPool;
	
	Player(PApplet p, int id)
	{
		parent=p;
		this.life=20;
		this.manaPool=0;
		this.id=id;
		
	}

}
