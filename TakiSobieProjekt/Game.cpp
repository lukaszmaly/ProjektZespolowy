#include "Game.h"


void Game::setFaza(int i)
{
	switch(i)
	{
	case 0:
		phase=PIERWSZY;
		break;
	case 1:
		phase=ATAK;
		break;
	case 2:
		phase=OBRONA;
		break;
	case 3:
		phase=WYMIANA;
		break;
	case 4:
		phase=DRUGI;
		break;
	case 5:
		phase=UPKEEP;
		break;
	}
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
	player1.Draw();
	player2.Draw();
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

Game::Game(string player1s,int player1Id,string player2s,int player2Id,string ip,int port,int w,int h,int interval)
{
	gameWidth = w;
	gameHeight = h;
	zmiana = false;
	phase = PIERWSZY;
	t=false;
	server.Init(ip,port,interval);
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