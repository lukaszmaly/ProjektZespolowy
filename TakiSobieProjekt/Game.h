#pragma once
#include "Player.h"
#include "Server.h"



enum Phase {
	PIERWSZY = 0,
	ATAK = 1,
	OBRONA = 2,
	WYMIANA = 3,
	DRUGI = 4
};


class Game
{
public:
Server server;

	int aPlayer;
	Point action;
	bool zmiana;
	Player player1;

	Player player2;
	Phase phase;
	int gameWidth;
	int gameHeight;


	void nextPhase()
	{
		zmiana=true;
	
		switch(phase)
		{
		case PIERWSZY:
				cout<<"Faza ataku"<<endl;
			phase=ATAK;
			break;

		case ATAK:
			cout<<"Faza obrony"<<endl;
			phase=OBRONA;
			break;

		case OBRONA:
			cout<<"Faza wymiany obrazen"<<endl;
			phase=WYMIANA;
			break;

		case WYMIANA:
			cout<<"Faza druga"<<endl;
			phase=DRUGI;
			break;

		case DRUGI:
			cout<<"Faza pierwsza"<<endl;
			phase=PIERWSZY;
			break;

		}
	}

	void Update()
	{
		
	
	





	}
	void Draw()
	{
		player1.Draw();
		player2.Draw();
	}

	Player &getCurrentPlayer()
	{
		if(aPlayer==player1.markerId) return player1;
		return player2;
	}
	Game(string player1s,int player1Id,string player2s,int player2Id,string ip,int port,int w,int h)
	{
		gameWidth=w;
		gameHeight=h;
		zmiana=false;
		phase=PIERWSZY;
		player1.Init(player1s,player1Id);
		player2.Init(player2s,player2Id);
		aPlayer=player1.markerId;
		server.Init(ip,port);
	}
	~Game()
	{
	}

};
