#include "Game.h"

bool Game::IsMana(int id,Color color)
{
	if(id==1)
	{
		return this->player1.mana.IsMana(color);
	}
	else
	{
		return this->player2.mana.IsMana(color);
	}
	return false;
}
void Game::AddMana(int id,Color color)
{
	if(id==1)
	{
		this->player1.mana.AddMana(color);
	}
	else
	{
		this->player2.mana.AddMana(color);
	}
	this->server.AddMana(id,color);
}
void Game::Clear()
{
	this->lastId=-1;
		playedLand=false;
}
void Game::SubMana(int id,Color color)
{
	if(id==1)
	{
		this->player1.mana.SubMana(color);
	}
	else
	{
		this->player2.mana.SubMana(color);
	}
	this->server.SubMana(id,color);
}
void Game::AddLife(int id,int value)
{
	
	if(id==1)
	{
		this->player1.hp+=value;
	}
	else
	{
		this->player2.hp+=value;
	}
	this->server.AddLife(id,value);
	GHAddLife(id,value);
}
void Game::SubLife(int id,int value)
{
	if(id==1)
	{
		this->player1.hp-=value;
	}
	else
	{
		this->player2.hp-=value;
	}
	this->server.SubLife(id,value);
	GHSubLife(id,value);
}
bool Game::CanPay(int id,int white, int blue,int black,int red,int green,int colorless)
{
	if(id==1)
	{
		return this->player1.mana.CanPay(white,blue,black,red,green,colorless);
	}
	else
	{
		return this->player2.mana.CanPay(white,blue,black,red,green,colorless);
	}
	return false;
}
void Game::Pay(int id,int white, int blue,int black,int red,int green,int colorless)
{
	int w,u,b,r,g;
	int aw,au,ab,ar,ag;
	if(id==1)
	{
		this->player1.mana.CopyMana(w,u,b,r,g);
		if(this->player1.mana.CanPay(white,blue,black,red,green,colorless)==true)
		{
			this->player1.mana.Pay(white,blue,black,red,green,colorless);
			this->player1.mana.CopyMana(aw,au,ab,ar,ag);
			this->server.SubMana(id,white,blue,black,red,green);
			this->ChangeStackState(1,NEUTRAL);
		}
	}
	else
	{
		this->player2.mana.CopyMana(w,u,b,r,g);
		if(this->player2.mana.CanPay(white,blue,black,red,green,colorless)==true)
		{
			this->player2.mana.Pay(white,blue,black,red,green,colorless);
			this->player2.mana.CopyMana(aw,au,ab,ar,ag);
			this->server.SubMana(id,white,blue,black,red,green);
				this->ChangeStackState(2,NEUTRAL);
		}
	}
}


bool Game::CheckCardsProp()
{
	return checkCardsProp;
}
Player & Game::GetPlayer(int id)
{
	if(id==1)
	{
		return this->player1;
	}
	return this->player2;
}
void Game::setFaza(int i)
{

	switch(i)
	{
	case 0:
		oneAttack=true;
		phase=PIERWSZY;

		break;
	case 1:
		oneAttack=false;
		phase=ATAK;
		break;
	case 2:
		oneAttack=false;
		phase=OBRONA;
		break;
	case 3:
		oneAttack=false;
		phase=WYMIANA;
		break;
	case 4:
		oneAttack=true;
		phase=DRUGI;
		break;
	case 5:
		phase=UPKEEP;
		break;
	}
}
bool Game::IsBgrMode()
{
	return bgrMode;
}

bool Game::IsTargetMode()
{
	return targetMode;
}
void Game::SetTargetMode(bool value)
{
	targetMode=value;
}


void Game::MakeDiffImage(Mat &img1,Point a,Point b,Point c,Point d)
{
	if(!img1.data || !diff.data) return;
	Mat img;
	Mat after;

	Mat t;
	img1.copyTo(t);
	img.cols=251;
	img.rows=356;
	Point2f c1[4] = {a,b,c,d};
	Point2f c2[4] = {Point2f(0,0), Point2f(251,0), Point2f(251,356),Point2f(0,356)};
	Mat mmat(3,3,CV_32FC1);
	mmat=getAffineTransform(c1,c2);
	warpAffine(t,img,mmat,Size(251,356));
	/*cvtColor(img,img,CV_BGR2HSV);
	cvtColor(diff,diff,CV_BGR2HSV);*/

	imshow("SprawdzanaKarta",img);

	img.copyTo(after);
	int width=diff.cols;
	int height=diff.rows;
	int n=width*height;
	int channels=diff.channels();
	long int red=0,green=0,blue=0;
	long int red2=0,green2=0,blue2=0;
	unsigned int wsk=0;
	float bb=0,g=0,r=0;
	for(int y=0;y<height;y++)
	{
		for(int x=0;x<width;x++)
		{
			wsk=channels*(width*y + x);
			blue+=diff.data[wsk]-img.data[wsk];
			green+=diff.data[wsk+1]-img.data[wsk+1];
			red+=diff.data[wsk+2]-img.data[wsk+2];
		}
	}
	r=red/(float)n;
	g=green/(float)n;
	bb=blue/(float)n;

	for(int y=0;y<height;y++)
	{
		for(int x=0;x<width;x++)
		{
			wsk=channels*(width*y + x);
			after.data[wsk]+=(int)bb;
			after.data[wsk+1]+=(int)g;
			after.data[wsk+2]+=(int)r;
		}
	}


	imshow("AfterImprove",after);
}

string Game::getCurrentPhase()
{
	switch(phase)
	{
	case PIERWSZY:
		return "Faza pierwsza";
		break;
	case ATAK:
		return "Faza ataku";
		break;
	case OBRONA:

		return "Faza obrony";
		break;
	case WYMIANA:
		return "Faza wymiany obrazen";
		break;
	case DRUGI:
		return "Faza druga";
		break;
	case UPKEEP:
		return "Faza upkeepu";
		break;
	}
	return "INCORRECT";
}

void Game::nextPhase()
{

	zmiana=true;
	switch(phase)
	{
	case PIERWSZY:
		phase=ATAK;
		server.NextPhase();
		this->oneAttack=false;
		break;
	case ATAK:
		server.NextPhase();
		this->oneAttack=false;
		phase=OBRONA;
		break;
	case OBRONA:
		server.NextPhase();
		this->oneAttack=false;
		phase=WYMIANA;
		break;
	case WYMIANA:
		server.NextPhase();
		phase=DRUGI;
		this->oneAttack=false;
		break;
	case DRUGI:
		
		phase=UPKEEP;
		break;
	case UPKEEP:
		server.NextPhase();
		phase = PIERWSZY;
		break;
	}
}

void Game::Draw(Mat &frame)
{
	player1.Draw(frame);
	player2.Draw(frame);
	//imshow("DiffCard",diff);

}

void Game::setPlayer(int i)
{
	aPlayer = player2.markerId;
	if(i==0) aPlayer = player1.markerId;

}

int Game::GetGameHeight()
{
	return gameHeight;
}

int Game::GetGameWidth()
{
	return gameWidth;
}

void Game::Update()
{
	if(player1.agree==true && player2.agree==true)
	{
		player1.agree=false; 
		player2.agree=false;
		nextPhase();
	}

	if(beAbleMarker==false)
	{
		if(abs(targetAngle-targetOldAngle)>45)
		{
			beAbleMarker=true;
			targetOldAngle = targetAngle;
		}
	}
	server.Update();

}

Game::~Game()
{
}

Player& Game::getCurrentPlayer()
{
	if(aPlayer == player1.markerId) return player1;
	return player2;
}

int Game::GetCurrentPlayer()
{
	if(aPlayer == player1.markerId) return 1;
	return 2;
}



Phase Game::GetPhase()
{
	return phase;
}


void Game::ChangeStackState(int id,State state)
{
	
		this->server.StackColor(id,state);
	

}
Game::Game(string player1s,int player1Id,string player2s,int player2Id,string ip,int port,int w,int h,int interval,bool showLog)
{
		oneLandEachTurn = false;
	 playedLand = false;
	stackState=NEUTRAL;
	SetTargetMode(false);
	lastId=-1;
	firsCardPoint = Point(550,250);
	firstCardHeight = 350;
	firstCardWidth = 250;
	diff =Mat(251,356,CV_8UC3);
	showCardArea=false;
	bgrMode=true;
	firstCardChecked=false;
	checkCardsProp=true;
	CanResolve = true;
	beAbleMarker=true;
	gameWidth = w;
	oneAttack=false;
	gameHeight = h;
	zmiana = false;
	phase = PIERWSZY;
	t=false;
	server.Init(ip,port,interval,showLog);
	player1.Init(player1s,player1Id);
	player2.Init(player2s,player2Id);
	aPlayer = player1.markerId;
	server.AddPlayer(1,player1s.c_str());
	server.AddPlayer(2,player2s.c_str());
	server.ActivePlayer(1);
}

int Game::distance(Point a,Point b)
{
	return std::sqrtf(((b.x-a.x)*(b.x-a.x)+(b.y-a.y)*(b.y-a.y)));
}

void Game::CheckMarkers(Mat &frame)
{
	MarkerDetector MDetector;
	MDetector.setMinMaxSize(0.01f);
	markers.clear();
	MDetector.detect(frame,markers);

	for(int i = 0; i < markers.size(); i++) 
	{

		if(markers[i].id==player1.markerId)
		{


			player1.angle =markers[i].getAngle();
			if(player1.oldangle<0) { player1.oldangle=player1.angle;}

			player1.angleDiff=player1.angle-player1.oldangle;
			if(player1.angleDiff>100)
			{
				player1.angleDiff=360-player1.angleDiff;
			}
			if(player1.angleDiff<-100) 
			{
				player1.angleDiff=-360-player1.angleDiff;
			}


			if(abs(player1.angleDiff)>80)
			{
				player1.agree=true;
				player1.oldangle=player1.angle;
			}
		}

		if(markers[i].id==player2.markerId)
		{


			player2.angle =markers[i].getAngle();
			if(player2.oldangle<0) { player2.oldangle=player2.angle;}

			player2.angleDiff=player2.angle-player2.oldangle;
			if(player2.angleDiff>100)
			{
				player2.angleDiff=360-player2.angleDiff;
			}
			if(player2.angleDiff<-100) 
			{
				player2.angleDiff=-360-player2.angleDiff;
			}

			if(abs(player2.angleDiff)>80)
			{
				player2.agree=true;
				player2.oldangle=player2.angle;
			}

		}





		if(markers[i].id == TARGETMARKER)
		{
			continue;
		}

		markers[i].draw(frame,Scalar(0,0,255));

		if(markers[i].id == ACTION)
		{
			if(action.x == -1) 
			{ 
				action = markers[i].getCenter();
			}
			else
			{
				if(distance(action,markers[i].getCenter()) >= 100)
				{
					action = markers[i].getCenter();
					nextPhase();
				}
			}
		}

		//if(player1.markerId == markers[i].id && aPlayer != player1.markerId) 
		//{
		//	aPlayer = markers[i].id;
		//	break;
		//}

		/*	if(player2.markerId == markers[i].id && aPlayer != player2.markerId)
		{
		aPlayer=markers[i].id;
		break;
		}*/
	}
}