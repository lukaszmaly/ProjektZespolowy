#include "Game.h"

bool Game::CheckCardsProp()
{
	return checkCardsProp;
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
}

void Game::nextPhase()
{
	server.NextPhase();
	zmiana=true;
	switch(phase)
	{
	case PIERWSZY:
		phase=ATAK;
		break;
	case ATAK:
		phase=OBRONA;
		break;
	case OBRONA:
		phase=WYMIANA;
		break;
	case WYMIANA:
		phase=DRUGI;
		break;
	case DRUGI:
		phase=UPKEEP;
		break;
	case UPKEEP:
		phase = PIERWSZY;
		break;
	}
}

void Game::Draw()
{
	//player1.Draw();
	//player2.Draw();
	imshow("DiffCard",diff);

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
		server.NextPhase();
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

Phase Game::GetPhase()
{
	return phase;
}


int Game::GetPlayer(Player& player)
{
	if(player.markerId == player1.markerId) return 1;
	return 2;
}

Game::Game(string player1s,int player1Id,string player2s,int player2Id,string ip,int port,int w,int h,int interval,bool showLog)
{
	SetTargetMode(false);
	firsCardPoint = Point(550,250);
	firstCardHeight = 350;
	firstCardWidth = 250;
	diff =Mat(251,356,CV_8UC3);
	showCardArea=false;
	bgrMode=true;
	firstCardChecked=false;
	checkCardsProp=false;
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

		if(markers[i].id==player1.idmarkera)
		{
			if(player1.angle==-1)
			{
				player1.angle = getangle(markers[i][0],markers[i][1],markers[i].getCenter());

			}
			else
			{
				if(abs(player1.angle-getangle(markers[i][0],markers[i][1],markers[i].getCenter()))>80)
				{
					player1.agree=true;
					player1.oldangle=player1.angle;
				}

			}

		}
		if(markers[i].id==player2.idmarkera)
		{
			if(player2.angle==-1)
			{
				player2.angle = getangle(markers[i][0],markers[i][1],markers[i].getCenter());
			}
			else
			{
				if(abs(player2.angle-getangle(markers[i][0],markers[i][1],markers[i].getCenter()))>80)
				{
					player2.agree=true;
					player1.oldangle=player1.angle;
				}
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

		if(player1.markerId == markers[i].id && aPlayer != player1.markerId) 
		{
			aPlayer = markers[i].id;
			break;
		}

		if(player2.markerId == markers[i].id && aPlayer != player2.markerId)
		{
			aPlayer=markers[i].id;
			break;
		}
	}
}