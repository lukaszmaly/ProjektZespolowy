  PShape wycinek;
  PImage img;
  int frame_num = 1;
  float stopnie = 0;
  int zmiana_faz = 1;

void setup()
{
  size(500,500);
}

void draw()
{
 zmiana_faz +=1;
 if (zmiana_faz >=600) zmiana_faz = 1;
 if(zmiana_faz<120)
    obraz(1,200,200);
      else if(zmiana_faz <240)
    obraz(2,200,200);
      else if(zmiana_faz <360)
    obraz(3,200,200);
      else if(zmiana_faz <480)
    obraz(4,200,200);
      else if(zmiana_faz <600)
    obraz(5,200,200);
}

    
  void obraz(int nr_fazy ,int x, int y)
  {
   //pushMatrix();
    switch (nr_fazy)
    {
    case 1:
    img = loadImage("kolo_f1.png");
    break;
    
    case 2:
    img = loadImage("kolo_f2.png");
    break;
    
    case 3:
    img = loadImage("kolo_f3.png");
    break;
    
    case 4:
    img = loadImage("kolo_f4.png");
    break;
      
    case 5:
    img = loadImage("kolo_f5.png");
    break;
    }
        
     frame_num += 1;
     if(frame_num>600) frame_num=1;
     stopnie = (float) (0.6 * frame_num);
    
    translate(x+100,y+100);
    rotate(radians(stopnie));
    translate(-(x+100),-(y+100));
    image(img,x,y);
    
    //popMatrix();
    
    }
  


