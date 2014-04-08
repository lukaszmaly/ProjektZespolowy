#include "Card.h"

Card::Card(void)
{

}

Card::~Card(void)
{

}
void Card::Damage(int value)
{
	this->defEOT-=value;
	if(GetDefense()<=0) this->die();
}
void Card::SetTarget(int id)
{
	target = id;
}

int Card::GetTarget()
{
	return target;
}
void Card::Prepare(vector<Point>&square,Mat &img)
{
	if(square[1].y>square[3].y) swap(square[1],square[3]);
	float ab = Distance(square[0],square[1]);
	float bc = Distance(square[1],square[2]);

	//Poprawienie bokow.
	if(ab>bc) 
	{
		Point ta,tb,tc,td;
		ta=square[0];
		tb=square[1];
		tc=square[2];
		td=square[3];
		square[0]=tb;
		square[1]=tc;
		square[2]=td;
		square[3]=ta;
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
	cv::warpAffine(t,tmp,mmat,Size(251,356));

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
		swap(square[0],square[2]);
		swap(square[1],square[3]);
	}

	//////Koniec poprawiania boków
	//////Poprawianie kolejnoœci punktów
	Point2f center = getCenter(square[0],square[1],square[2],square[3]);
	Point2f first;
	Point2f second;
	Point2f tab[4]={square[0],square[1],square[2],square[3]};
	int tabb[4]={0,1,2,3};
	for(int i=0;i<4;i++)
	{
		for(int j=i+1;j<4;j++)
		{
			if(tab[j].y<tab[i].y)//if(tab[j].y<tab[i].y+5)  5 to wskaznik tolerancji(bledy pokazuja siê gdy 2 górne rogi s¹ idealnie równoleg³e.
				//Nie do konca jeszcze zrobione
			{
				int te= tabb[i];
				tabb[i]=tabb[j];
				tabb[j]=te;
				Point2f t= tab[j];
				tab[j]=tab[i];
				tab[i]=t;
			}
		}
	}
	first = square[tabb[0]];
	second = square[(tabb[0]+1)%4];
	//second = square[tabb[1]];
	// stara wersja square[tabb[1]]; <-- w tej wersji sortowanie jest wymagane
	//sortowanie jest aktualnie niepotrzebne, ale nie wiem czy wystêpuje nadal b³¹d, dlatego dla pewnoœci zostawiam

	float a = atan2f(( first.y - center.y ),( first.x - center.x ) ) * 180 / M_PI + 180;
	float aa = atan2f(( second.y - center.y ),( second.x - center.x ) ) * 180 / M_PI + 180;

	if(a > aa) 
	{ 
		swap(square[0],square[1]);
		swap(square[2],square[3]);
	}
}

float Card::getAngle()
{
	return atan2f(( a.y - getCenter().y ),( a.x - getCenter().x ) ) * 180 / M_PI + 180;
}

void Card::Compare(Mat &img1,Mat &img2,float tab[3],Game &game)
{
	if(img1.data && img2.data)
	{
		if(game.IsBgrMode()==false)
		{
			cvtColor(img1,img1,COLOR_BGR2HSV);
			cvtColor(img2,img2,COLOR_BGR2HSV);
		}
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


void Card::Unlock()
{
	id=ID++;
}

int Card::GetAttack()
{
	return (att+attEOT+additionalAttack);
}
int Card::GetDefense()
{
	return (def+defEOT+additionalDefense);
}

void Card::AddEOT(int attack,int defense)
{
	attEOT+=attack;
	defEOT+=defense;
	if(GetDefense()<=0) die();
}
void Card::Add(int attack,int defense)
{
	additionalAttack+=attack;
	additionalDefense+=defense;
	if(GetDefense()<=0) die();
}
Card::Card(Point a, Point b, Point c,Point d,Mat &img,vector<CardB>& bkarty,Game &game,bool temp=false)
{
	attEOT=defEOT=0;
	additionalAttack=additionalDefense=0;
	area=0;
	canUntap=true;
	target=-1;
	error=false;
	nowa=true;
	sendTime=0;
	id=-1;
	blocking=-1;
	owner=game.GetCurrentPlayer();
	taped=false;
	attack=block=false;
	this->a=a;
	old=Point(-1,-1);
	this->b=b;
	this->c=c;
	this->d=d;
	ttl=TTL;
	hasCantAttack=hasCantBlock=false;
	att=-1;
	def=-1;
	dead=false;
	Update(a,b,c,d,img,bkarty,game,temp);
	cardBase.type==LAND ? gaveMana=false : gaveMana=true;
	old=getCenter();
}
bool Card::TrySend(Game &game)
{
	sendTime++;
	if(sendTime>=game.server.GetInterval())
	{
		sendTime=0;
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

bool Card::TapUntap()
{
	bool taptemp = true;
	float t = getAngle();
	if((t > 180 && t < 270) || (t > 0 && t < 90))	taptemp = false;
	if(taptemp != taped) return true;
	return false;
}

void Card::Update(Point a,Point b,Point c,Point d,Mat &img,vector<CardB>& bkarty,Game &game,bool temp=false)
{

	if(temp == false)
	{
		this->a=a;
		this->b=b;
		this->c=c;
		this->d=d;

		if(dead==false)ttl=TTL;


		if(TapUntap()==true)
		{
			if(taped==true)
			{
				Untap(game);
			}
			else
			{
				Tap(game);
			}
		}

		if(game.GetPhase()==PIERWSZY || game.GetPhase()==DRUGI) {old=getCenter(); enemy=Point(-1,-1);}
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
		Compare(tmp,bkarty[i].img,tm,game);
		float t1=tm[0]+tm[1]+tm[2];
		if(t1<min) { cardId=i;min=t1;}
	}
	if(cardId!=-1)
		setCardBase(bkarty[cardId]);
	//imshow("Karta",tmp);
	if(waitKey(10)==122)
	{
		string name;

		cout<<"Wpisz nazwe karty:"<<endl;
		cout<<"Nazwa | Id | koszt | Typ(0-Creature, 1-land) | Atak | Obrna |"<<endl;
		//cin>>name >>id>>koszt>>typ>>att>>def;
		cin>>name;
		//if(typ==0) typek=CREATURE; else typek=LAND;
		cout<<"Dodano nowa karte"<<endl;;
		imshow("Kartadodana"+name,tmp);
		imwrite( "C:/umk/"+name+".jpg", tmp );
		//bkarty.push_back(CardB(tmp,id,name,Red,typek,att,def,koszt));
	}
	else if(nowa==true && temp==false)
	{
		nowa=false;
		att=cardBase.att;
		def=cardBase.def;

		/*if(game.getCurrentPlayer().mana<cardBase.koszt)
		{
		nowa=true;
		error=true;
		}
		else
		{
		game.getCurrentPlayer().mana-=cardBase.koszt;
		nowa=false;
		error=false;
		}*/

	}
}

void Card::setCardBase(CardB &card)
{
	this->cardBase=card;
}

void Card::Tap(Game &game)
{
	cout<<"Tapnieto"<<endl;
	taped=true;
	if(this->cardBase.type==LAND && canUntap==true)
	{
		game.AddMana(this->owner,this->cardBase.landColor);
	}
	game.GHTap(this->id);
}

void Card::Untap(Game &game)
{
	cout<<"Odtapowano"<<endl;
	taped=false;
	if(this->cardBase.type=LAND)
	{
		if(game.IsMana(this->owner,this->cardBase.landColor) && canUntap==true)
		{
			game.SubMana(this->owner,this->cardBase.landColor);
		}
		else
		{
			canUntap=false;
		}
	}
		game.GHTap(this->id);
}

void Card::die()
{
	dead=true;
}

void Card::GiveLifeToPlayer(int value,Game &game)
{
	game.AddLife(owner,value);
}

void Card::Fight(Card &op,Game &game)
{
	op.canUntap=false;
	game.GHAtack(op.id);
	game.GHDefense(op.id,this->id);

	if((this->cardBase.hasFirstStrike && op.cardBase.hasFirstStrike) || (!this->cardBase.hasFirstStrike && !op.cardBase.hasFirstStrike))
	{
		Damage(op.GetAttack());
		op.Damage(GetAttack());
		if(this->cardBase.hasLifelink)	this->GiveLifeToPlayer(this->att,game);
		if(op.cardBase.hasLifelink)	op.GiveLifeToPlayer(op.att,game);
		if(GetDefense()<=0 || op.cardBase.hasDeatchtuch) die();
		if(op.GetDefense()<=0 || this->cardBase.hasDeatchtuch) op.die();
	}
	else if(this->cardBase.hasFirstStrike)
	{
		op.Damage(GetAttack());
		if(this->cardBase.hasLifelink)	this->GiveLifeToPlayer(this->att,game);
		if(op.GetDefense()<=0 || this->cardBase.hasDeatchtuch) 
		{
			op.die(); 
		}
		else
		{
			Damage(op.GetAttack());
			if(op.cardBase.hasLifelink)	op.GiveLifeToPlayer(op.att,game);
			if(GetDefense()<=0 || op.cardBase.hasDeatchtuch) die();
		}
	}
	else if(op.cardBase.hasFirstStrike)
	{
		Damage(op.GetAttack());
		if(op.cardBase.hasLifelink)	op.GiveLifeToPlayer(op.att,game);
		if(GetDefense()<=0 || op.cardBase.hasDeatchtuch) 
		{
			die(); 
		}
		else
		{
			op.Damage(GetAttack());
			if(this->cardBase.hasLifelink)	this->GiveLifeToPlayer(this->att,game);
			if(op.GetDefense()<=0 || this->cardBase.hasDeatchtuch) die();
		}
	}

	Clear();
	op.Clear();
}

void Card::Draw(Mat &img1,vector<CardB>&bkarty,Game &game)
{
	if(--ttl>=0)
	{
		char cad[100];
		char cad1[100];
		char cad2[100];
		line(img1,a,b,Scalar(0,0,255),2);
		line(img1,b,c,Scalar(0,0,255),2);
		line(img1,c,d,Scalar(0,0,255),2);
		line(img1,d,a,Scalar(0,0,255),2);
		putText(img1,"a", Point(a.x,a.y),FONT_HERSHEY_SIMPLEX, 0.5,  Scalar(0,0,255),2);
		putText(img1,"b", Point(b.x,b.y),FONT_HERSHEY_SIMPLEX, 0.5,  Scalar(0,0,255),2);
		putText(img1,"c", Point(c.x,c.y),FONT_HERSHEY_SIMPLEX, 0.5,  Scalar(0,0,255),2);
		putText(img1,"d", Point(d.x,d.y),FONT_HERSHEY_SIMPLEX, 0.5,  Scalar(0,0,255),2);



		sprintf(cad2,"ID:%d",id);
		putText(img1,cad2, Point(d.x,d.y)+Point(0,20),FONT_HERSHEY_SIMPLEX, 0.5,  Scalar(0,0,255),2);



		if(cardId!=-1)
		{
			sprintf(cad,"%s",bkarty[cardId].name.c_str());
		}
		else
		{
			sprintf(cad,"none");
		}

		if(taped==true)
			sprintf(cad1,"Taped(%s)",game.GetPlayer(this->owner).name.c_str());
		else
			sprintf(cad1,"Untaped(%s)",game.GetPlayer(this->owner).name.c_str());



		putText(img1,cad, a+Point(10,15),FONT_HERSHEY_SIMPLEX, 0.5,  Scalar(0,0,255),2);		
		putText(img1,cad1, a+Point(10,30),FONT_HERSHEY_SIMPLEX, 0.5,Scalar(0,0,255),2);		
		if(att!=-1)
		{
			char cad3[100];
			sprintf(cad3,"Att: %d",GetAttack());
			putText(img1,cad3, d-Point(0,20),FONT_HERSHEY_SIMPLEX, 0.5,  Scalar(0,0,255),2);	
		}

		if(def!=-1)
		{
			char cad4[100];
			sprintf(cad4,"Def: %d",GetDefense());
			putText(img1,cad4, c-Point(0,20),FONT_HERSHEY_SIMPLEX, 0.5,  Scalar(0,0,255),2);	
		}


		if(game.GetPhase()==ATAK || (game.GetPhase()==OBRONA && attack==true))
		{
			if(Distance(getCenter(),old)>50 && attack==false)
			{
				putText(img1,"Tapnij zeby zaatakowac",getCenter()-Point2f(0,50),FONT_HERSHEY_SIMPLEX, 0.5,  Scalar(0,200,200),4);
				line(img1,old,getCenter(),Scalar(255,0,0),3);
			}

			if(attack==true) {
				line(img1,old,getCenter(),Scalar(255,0,0),3); putText(img1,"Atakuje",getCenter()+Point2f(0,50),FONT_HERSHEY_SIMPLEX, 0.5,  Scalar(0,0,255),2);	



			}
		}

		if(game.GetPhase()==OBRONA)
		{
			if(block==true)
			{
				line(img1,old,getCenter(),Scalar(255,0,0),3);
				putText(img1,"Bronie",getCenter()+Point2f(0,50),FONT_HERSHEY_SIMPLEX, 0.5,  Scalar(0,0,255),2);	
				if(enemy.x!=-1) {
					line(img1,enemy,getCenter(),Scalar(255,0,200),3);
				}
			}
		}
		//if(cardBase.koszt>owner.mana && cardBase.type!=LAND)
		//{
		//	line(img1,a,c,Scalar(255,0,0),3);
		//	char cad4[100];
		////	sprintf(cad4,"Brakuje: %d",cardBase.koszt-owner.mana);
		//	putText(img1,cad4, d+Point(0,20),FONT_HERSHEY_SIMPLEX, 0.5,  Scalar(0,0,255),2);	

		//}
		if(dead==true)
		{
			line(img1,a,c,Scalar(255,0,0),3);
			line(img1,b,d,Scalar(255,0,0),3);
		}
	}
}

void Card::NewRound(int player)
{
	if(this->owner==player)
	{
		this->canUntap=true;
	}
	this->attack=false;
	this->block=false;
	def=cardBase.def+this->additionalAttack;
	att=cardBase.att+this->additionalDefense;
	defEOT=0;
	attEOT=0;
}

void Card::Clear()
{
	attack=false;
	block=false;
	old=getCenter();
	enemy=Point(-1,-1);
	blocking=-1;
}

bool Card::CanBlock(Card card)
{
	if(this->canUntap==false) return false;
	if(this->hasCantBlock==false && card.cardBase.hasFlying==true && (this->cardBase.hasFlying==true || this->cardBase.hasReach==true))
	{
		return true;
	}
	return false;
}
bool Card::CanAttack()
{
	if(this->cardBase.hasDefender==true || this->hasCantAttack==true) return false;

	return true;
}