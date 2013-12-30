#pragma once
#include "Player.h"

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
	int aPlayer;

	Player player1;
	Player player2;
	Phase phase;
	void Update()
	{
		if(waitKey(20)==116) //t
		{
			cout<<"Kolejna faza"<<endl;
		}
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
	Game(string player1s,int player1Id,string player2s,int player2Id)
	{
	phase=PIERWSZY;
		player1.Init(player1s,player1Id);
		player2.Init(player2s,player2Id);
		aPlayer=player1.markerId;
	}
	~Game()
	{
	}
	
};
