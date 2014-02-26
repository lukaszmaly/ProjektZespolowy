#pragma once
#include "Player.h"
#include "Server.h"
#include <string>
using namespace std;


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
	Point cardScaner;

	void setFaza(int i)
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
		}
	}

	void setPlayer(int i)
	{
		aPlayer= player2.markerId;
		if(i==0) aPlayer= player1.markerId;

	}

	string getCurrentPhase()
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
		}
	}
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
