#include "Card.h"

Card::Card(void)
{
}

Card::~Card(void)
{
}

void Card::Prepare(vector<Point>&square,Mat &img)
{
	bool f1=false;
	bool f2=false;

	float ab = Distance(square[0],square[1]);
	float bc = Distance(square[1],square[2]);

	//Poprawienie bokow
	if(ab>bc) 
	{
		f1=true;
		//cout<<"Poprawianie bokow"<<endl; 
		Point ta,tb,tc,td;
		ta=square[0];
		tb=square[1];
		tc=square[2];
		td=square[3];
		square[0]=tb;
		square[1]=tc;
		square[2]=td;
		square[3]=ta;
		float a=atan2f(( square[0].y - square[1].y ),( square[0].x - square[1].x ) ) * 180 / M_PI + 180;


	}

	Mat tmp;
	Mat t;
	img.copyTo(t);
	tmp.cols=200;
	tmp.rows=400;
	Point2f c1[4] = {square[0],square[1],square[2],square[3]};
	Point2f c2[4] = {Point2f(0,0), Point2f(251,0), Point2f(251,356),Point2f(0,356)};
	Mat mmat(3,3,CV_32FC1);
	mmat=getAffineTransform(c1,c2);
	warpAffine(t,tmp,mmat,Size(251,356));

	int tab1[3]={0,0,0};
	int tab2[3]={0,0,0};
	int tab3[3]={0,0,0};
	int width=tmp.cols;
	int channels=tmp.channels();
	int height=tmp.rows;

	for(int y=10;y<height/2-10;y++)
	{
		for(int x=0;x<width;x++)
		{
			tab1[0]+=tmp.data[channels*(width*y + x)];
			tab1[1]+=tmp.data[channels*(width*y + x) +1];
			tab1[2]+=tmp.data[channels*(width*y + x) +2];
		}
	}

	for(int y=height/2+10;y<height-10;y++)
	{
		for(int x=0;x<width;x++)
		{
			tab2[0]+=tmp.data[channels*(width*y + x)];
			tab2[1]+=tmp.data[channels*(width*y + x) +1];
			tab2[2]+=tmp.data[channels*(width*y + x) +2];
		}
	}
	tab3[0]=tab2[0]-tab1[0];
	tab3[1]=tab2[1]-tab1[1];
	tab3[2]=tab2[2]-tab1[2];

	//jeœli r jest mniejsze od 2 to góra kart jest dobrze okreslona
	int r=0;
	if(tab3[0]<0) r++;
	if(tab3[1]<0) r++;
	if(tab3[2]<0) r++;
	if(r>=2)
	{
		f2=true;
		//cout<<"Poprawianie kolwjnoaci"<<endl; 
		swap(square[0],square[2]);
		swap(square[1],square[3]);
	}



	//////Koniec poprawiania boków
	//////Poprawianie kolejnoœci punktów

	float a=atan2f(( square[0].y - square[1].y ),( square[0].x - square[1].x ) ) * 180 / M_PI + 180;
	fastImg("a",a);
	if(a>90) 
	{ 
		//cout<<"Zamieniam kolejnoœæ punktow"<<endl;
		swap(square[0],square[1]);
		swap(square[2],square[3]);
	}


	//Koniec poprawiania kolejnoœci punktów


}

float Card::getAngle()
{
	return atan2f(( a.y - b.y ),( a.x - b.x ) ) * 180 / M_PI + 180;
}

void Card::Compare(Mat &img1,Mat &img2,float tab[3])
{
	if(img1.data && img2.data)
	{
		int width=img1.cols;
		int height=img1.rows;
		int n=width*height;
		int channels=img1.channels();
		long int red=0,green=0,blue=0;
		long int red2=0,green2=0,blue2=0;
		unsigned int wsk=0;

		for(int y=0;y<height;y++)
		{
			for(int x=0;x<width;x++)
			{
				wsk=channels*(width*y + x);
				blue=img1.data[wsk]-img2.data[wsk];
				blue2+=blue*blue;
				green=img1.data[wsk+1]-img2.data[wsk+1];
				green2+=green*green;
				red=img1.data[wsk+2]-img2.data[wsk +2];
				red2+=red*red;
			}
		}
		tab[2]=red2/(float)n;
		tab[1]=green2/(float)n;
		tab[0]=blue2/(float)n;
	}
}

Card::Card(Point a, Point b, Point c,Point d,Mat &img,vector<CardB>& bkarty,Game &game)
{
	owner=game.getCurrentPlayer();
	taped=false;
	name="none";
	this->a=a;
	this->b=b;
	this->c=c;
	this->d=d;
	ttl=TTL;
	timer=0;
	att=-1;
	def=-1;
	ready=false;
	dead=false;
	id=ID++;
	cout<<"Utworzenie karty id="<<id<<endl;
	Update(a,b,c,d,img,bkarty,game);
}

bool Card::Check(Card k)
{
	if((a.x<=k.a.x && a.y<=k.a.y) && (b.x>=k.b.x && b.y<=k.b.y) && (c.x>=k.c.x && c.y>=k.c.y) && (d.x<=k.d.x && d.y>=k.d.y))
	{
		return true;
	}
	return false;
}

Point2f Card::getCenter()
{
	Point2f t(0,0);
	t.x=t.x+a.x+b.x+c.x+d.x;
	t.y=t.y+a.y+b.y+c.y+d.y;
	t.x/=(float)4;
	t.y/=(float)4;
	return t;
}

Point2f Card::getCenter(Point a,Point b,Point c,Point d)
{
	Point2f t(0,0);
	t.x=t.x+a.x+b.x+c.x+d.x;
	t.y=t.y+a.y+b.y+c.y+d.y;
	t.x/=(float)4;
	t.y/=(float)4;
	return t;
}

void Card::Update(Point a,Point b,Point c,Point d,Mat &img,vector<CardB>& bkarty,Game &game)
{
	this->a=a;
	this->b=b;
	this->c=c;
	this->d=d;
	ttl=TTL;
	bool taptemp=false;
	if(getAngle()>45) taptemp=true;
	if(taptemp!=taped)
	{
		taped=taptemp;
		if(taped==false)
		{
			Untap(game);
		}
		else
		{
			Tap(game);
		}
	}

	Mat tmp;
	Mat t;
	img.copyTo(t);
	tmp.cols=251;
	tmp.rows=356;
	Point2f c1[4] = {a,b,c,d};
	Point2f c2[4] = {Point2f(0,0), Point2f(251,0), Point2f(251,356),Point2f(0,356)};
	Mat mmat(3,3,CV_32FC1);
	mmat=getAffineTransform(c1,c2);
	warpAffine(t,tmp,mmat,Size(251,356));



	float tm[3];
	float min=256*256;
	cardId=-1;
	for(unsigned int i=0;i<bkarty.size();i++)
	{
		Compare(tmp,bkarty[i].img,tm);
		float t1=tm[0]+tm[1]+tm[2];
		if(t1<min) { cardId=i;min=t1;}
	}
	imshow("Karta",tmp);
	if(waitKey(30)==122)
	{
		string name;
		int id;
		cout<<"Wpisz nazwe karty:"<<endl;
		cin>>name;
		cin >>id;
		cout<<"Dodano nowa karte"<<endl;;
		imshow("Kartadodana"+name,tmp);
		imwrite( "C:/umk/"+name+".jpg", tmp );
		bkarty.push_back(CardB(tmp,id,name,Red,CREATURE));
	}

}
int Card::maxC(int a,int b,int c)
{
	int m=max(a,max(b,c));
	if(m==a) return 1;
	if(m==b) return 2;
	return 3;
}
int Card::minC(int a,int b,int c)
{
	int m=min(a,min(b,c));
	if(m==a) return 1;
	if(m==b) return 2;
	return 3;
}

void Card::setCardBase(CardB &card)
{
	this->cardBase=card;
}

void Card::Tap(Game &game)
{
	cout<<"Tapnieto"<<endl;
	if(this->cardBase.type=LAND)
	{
		game.getCurrentPlayer().mana++;
	}
}

void Card::Untap(Game &game)
{
	cout<<"Odtapowano"<<endl;
	if(this->cardBase.type=LAND)
	{
		game.getCurrentPlayer().mana--;
	}
}
string Card::Wynik(int b,int g,int r,int h, int s,int v)
{
	int max1 = maxC(b,g,r);
	int max2 = maxC(h,s,v);
	int min1 = minC(b,g,r);
	int min2 = minC(h,s,v);
	if(max1==3 && max2==3 && min1==1 && min2==1) return "czerwony";
	if(max1==2 && max2==3 && min1==3 && min2==2) return "czarny";
	if(max1==1 && max2==3 && min1==3 && min2==2) return "niebieski";
	//if(max1==2 && max2==3 && abs(b-r)<10) return "zielony";
	//if(max1==2 && max2==3 && (abs(g-b)<20 || abs(g-r)<20 )) return "bialy";
	return "false";
}
void Card::die()
{
	dead=true;
}
void Card::Fight(Card &op)
{
	int t1=def-op.att;
	int t2=op.def-att;
	if(t1<=0) die();
	if(t2<=0) op.die();
}

void Card::Draw(Mat img,vector<CardB>&bkarty)
{
	if(--ttl>0)
	{
		char cad[100];
		char cad1[100];

		line(img,a,b,Scalar(0,0,255),2);
		line(img,b,c,Scalar(0,0,255),2);
		line(img,c,d,Scalar(0,0,255),2);
		line(img,d,a,Scalar(0,0,255),2);

		if(cardId!=-1)
		{
			sprintf(cad,"%s",bkarty[cardId].name.c_str());
		}
		else
		{
			sprintf(cad,"none");
		}

		if(taped==true)
			sprintf(cad1,"Karta tapnieta(%s)",owner.name.c_str());
		else
			sprintf(cad1,"Karta odtapowana(%s)",owner.name.c_str());
		putText(img,"a", Point(a.x,a.y),FONT_HERSHEY_SIMPLEX, 0.5,  Scalar(0,0,255),2);
		putText(img,"b", Point(b.x,b.y),FONT_HERSHEY_SIMPLEX, 0.5,  Scalar(0,0,255),2);
		putText(img,"c", Point(c.x,c.y),FONT_HERSHEY_SIMPLEX, 0.5,  Scalar(0,0,255),2);
		putText(img,"d", Point(d.x,d.y),FONT_HERSHEY_SIMPLEX, 0.5,  Scalar(0,0,255),2);
		putText(img,cad, getCenter(),FONT_HERSHEY_SIMPLEX, 0.5,  Scalar(0,0,255),2);		
		putText(img,cad1, getCenter()+Point2f(0,20),FONT_HERSHEY_SIMPLEX, 0.5,Scalar(0,0,255),2);		
	}
}