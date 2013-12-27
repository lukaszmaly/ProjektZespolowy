#include "Card.h"


Card::Card(void)
{
}


Card::~Card(void)
{
}


void Card::Compare(Mat &img1,Mat &img2,float tab[3])
{
	if(img1.data && img2.data)
	{
	int width=img1.cols;
	int channels=img1.channels();
	int height=img1.rows;
	long int red=0,green=0,blue=0;
	long int red2=0,green2=0,blue2=0;
	int fred=0,fgreen=0,fblue=0;
	int n=width*height;
	for(int y=0;y<height;y++)
	{
		for(int x=0;x<width;x++)
		{
			blue=img1.data[channels*(width*y + x)]-img2.data[channels*(width*y + x)];
			blue2+=blue*blue;
			green=img1.data[channels*(width*y + x) +1]-img2.data[channels*(width*y + x) +1];
			green2+=green*green;
			red=img1.data[channels*(width*y + x) +2]-img2.data[channels*(width*y + x) +2];
			red2+=red*red;
		}
	}
	tab[2]=fred=red2/n;
	tab[1]=fgreen=green2/n;
	tab[0]=fblue=blue2/n;
	/*cout<<"Fred = "<<fred<<endl;
	cout<<"Fgreen = "<<fgreen<<endl;
	cout<<"Gblue = "<<fblue<<endl;*/
	}

}

Card::Card(Point a, Point b, Point c,Point d,bool check)
{
	taped=false;
	name="none";
	this->a=a;
	this->b=b;
	this->c=c;
	this->d=d;
	ttl=TTL;
	timer=0;
	if(check==false)
	{
		id=ID++;
		cout<<"Utworzenie karty "<<id<<endl;
		cout<<"Dane karty:"<<endl;
		float a1=Distance(a,b)/Distance(b,c);
		float a2=Distance(b,c)/Distance(c,d);
		float a3=Distance(c,d)/Distance(d,a);
		float a4=Distance(d,a)/Distance(a,b);
		cout<<"AB/BC:"<<a1<<endl;
		cout<<"BC/CD:"<<a2<<endl;
		cout<<"CD/DA:"<<a3<<endl;
		cout<<"DA/AB:"<<a4<<endl;

	}
}

bool Card::Check(Card k)
{
	if((a.x<=k.a.x && a.y<=k.a.y) &&
		(b.x>=k.b.x && b.y<=k.b.y) &&
		(c.x>=k.c.x && c.y>=k.c.y) &&
		(d.x<=k.d.x && d.y>=k.d.y))
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

void Card::Update(Point a,Point b,Point c,Point d)
{
	this->a=a;
	this->b=b;
	this->c=c;
	this->d=d;
	ttl=TTL;
}
int Card::maxC(int a,int b,int c)
{
	int m=max(a,max(b,c));
	if(m==a) return 1;
	if(m==b) return 2;
	if(m==c) return 3;
}
int Card::minC(int a,int b,int c)
{
	int m=min(a,min(b,c));
	if(m==a) return 1;
	if(m==b) return 2;
	if(m==c) return 3;
}

void Card::setCardBase(CardB &card)
{
	this->cardBase=card;
}

void Card::Tap(Player &player)
{
	if(cardBase.type==LAND)
	{
		player.mana++;
	}
}

void Card::Untap(Player &player)
{
		if(cardBase.type==LAND)
	{
		player.mana--;
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

void Card::Draw(Mat img,vector<CardB>&bkarty,bool first)
{
	dzielna=1;
	if(--ttl>0)
	{
		char cad[100];
		char cad1[100];
		Mat tmp;
		Mat t;
		img.copyTo(t);
		tmp.cols=200;
		tmp.rows=400;
		Point2f c1[4] = {a,b,c,d};
		Point2f c2[4] = {Point2f(0,0), Point2f(251,0), Point2f(251,356),Point2f(0,356)};
		Mat mmat(3,3,CV_32FC1);
		mmat=getAffineTransform(c1,c2);

		warpAffine(t,tmp,mmat,Size(251,356));
		line(img,a,b,Scalar(0,0,255),2);
		line(img,b,c,Scalar(0,0,255),2);
		line(img,c,d,Scalar(0,0,255),2);
		line(img,d,a,Scalar(0,0,255),2);



		float tm[3];
		float min=256*256;
		int min_index=-1;
		for(int i=0;i<bkarty.size();i++)
		{
			Compare(tmp,bkarty[i].img,tm);
			float t1=tm[0]+tm[1]+tm[2];
			if(t1<min) { min_index=i;min=t1;}

		}
		if(min_index!=-1)
		{
			sprintf(cad,"%s",bkarty[min_index].name.c_str());
		}
		else
		{
			sprintf(cad,"none");
		}
		sprintf(cad1,"Tapnieta: %b",taped);
		putText(img,"a", Point(a.x,a.y),FONT_HERSHEY_SIMPLEX, 0.5,  Scalar(0,0,255),2);
		putText(img,"b", Point(b.x,b.y),FONT_HERSHEY_SIMPLEX, 0.5,  Scalar(0,0,255),2);
		putText(img,"c", Point(c.x,c.y),FONT_HERSHEY_SIMPLEX, 0.5,  Scalar(0,0,255),2);
		putText(img,"d", Point(d.x,d.y),FONT_HERSHEY_SIMPLEX, 0.5,  Scalar(0,0,255),2);
		putText(img,cad, getCenter(),FONT_HERSHEY_SIMPLEX, 0.5,  Scalar(0,0,255),2);		
		putText(img,cad1, getCenter()+Point2f(0,20),FONT_HERSHEY_SIMPLEX, 0.5,  Scalar(0,0,255),2);		

		namedWindow("Karta", CV_WINDOW_AUTOSIZE );

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
}