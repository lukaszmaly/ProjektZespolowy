package MTGPackage;
import processing.core.*;

public class Phrases {

      PShape wycinek;
	  PImage img,img1,img2,img3,img4,img5;
	  int frame_num = 1;
	  float stopnie = 0;
	  int zmiana_faz = 1;
	  PApplet parent;
	  int poz_x,poz_y;
	  int aktualna_faza=1;

	  Phrases(PApplet p)
	  {
		  parent = p;
		    img1 = parent.loadImage("kolo_f1.png");
		    img2=  parent.loadImage("kolo_f2.png");
		    img3 = parent.loadImage("kolo_f3.png");
		    img4 = parent.loadImage("kolo_f4.png");
		    img5 = parent.loadImage("kolo_f5.png");
		    poz_x = 0;
		    poz_y = 300;
		
	  }
	  
	void rysuj()
	{	 
	 obraz(aktualna_faza,poz_x,poz_y);
	}
	
	void zmien_faze()
	{
		aktualna_faza += 1;
		if(aktualna_faza == 6) aktualna_faza = 1;
	}
	void zmien_faze(int n)
	{
		aktualna_faza += 1;
		if(aktualna_faza == 6) aktualna_faza = 1;
	}

	    
	  void obraz(int nr_fazy ,int x, int y)
	  {
	    parent.pushMatrix();
	    switch (nr_fazy)
	    {
	    case 1:
	    img = img1;
	    break;
	    
	    case 2:
	    img = img2;
	    break;
	    
	    case 3:
	    img = img3;
	    break;
	    
	    case 4:
	    img = img4;
	    break;
	      
	    case 5:
	    img = img5;
	    break;
	    }
	        
	     frame_num += 1;
	     if(frame_num>600) frame_num=1;
	     stopnie = (float) (0.6 * frame_num);
	    
	    parent.translate(x+113,y+83);
	    parent.rotate(parent.radians(stopnie));
	    parent.translate(-(x+100),-(y+100));
	    parent.image(img,x,y);
	   
	    parent.popMatrix();
	    
	    }
	
}
