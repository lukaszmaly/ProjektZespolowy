package MTGPackage;
import processing.core.*;


public class Effect 
{
	public enum Type{START, BOLT, SPARKLES, FRAME}
	
	PApplet parent;
	int life;
	Type type;
	
	Effect(PApplet p)
	{
		parent=p;
		this.life=0;		
	}
}

