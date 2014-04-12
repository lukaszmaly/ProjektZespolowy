#pragma once
#include "opencv2\imgproc\imgproc.hpp"
#include <opencv2\core\core.hpp>
#include <opencv2\highgui\highgui.hpp>
#include <opencv2\photo\photo.hpp>
#include <opencv2\features2d\features2d.hpp>
#include <opencv2\nonfree\features2d.hpp>
#include <opencv2\objdetect\objdetect.hpp>
#include <opencv2\calib3d\calib3d.hpp>
#include <iostream>
#include <cmath>
#include "Game.h"
#include "Card.h"
#include "settings.h"
using namespace std;
using namespace cv;
class MainServer
{
public:
	
	vector<string> Msg;


	MainServer()
	{
		Msg.push_back("| ADD 0 7 1 0 641 329 757 340 739 501 623 492 |");
		Msg.push_back("| UPDATE 0 7 1 0 640 329 758 342 739 502 622 491 -1 -1 |");
		Msg.push_back("| UPDATE 0 7 1 0 640 329 758 341 739 501 623 492 -1 -1 |");
		Msg.push_back("| UPDATE 0 7 1 0 640 329 758 341 739 501 622 490 -1 -1 |");
		Msg.push_back("UPDATE 0 7 1 0 640 329 758 341 739 502 622 490 -1 -1 |");
		Msg.push_back("| UPDATE 0 7 1 0 640 329 758 341 740 501 622 490 -1 -1 |");

	}

	vector<string> &split(string &s, char delim, vector<string> &elems) {
   stringstream ss(s);
    string item;
    while (std::getline(ss, item, delim)) {
        elems.push_back(item);
    }
    return elems;
}


vector<string> split(string &s, char delim) {
    vector<string> elems;
    split(s, delim, elems);
    return elems;
}

	void Read(vector<Card>&cards,vector<CardB>&bcards,Game &game)
	{
		vector<string> msg;
		if(Msg.size()!=0)
		{
			msg=split(Msg[0],' ');
		}
		if(msg.size()==0) return;
		if(msg[1].compare("ADD")==0)
		{
			
			AddCard(atoi(msg[2].c_str()),atoi(msg[3].c_str()),Point(atoi(msg[6].c_str()),atoi(msg[7].c_str())),Point(atoi(msg[8].c_str()),atoi(msg[9].c_str())),Point(atoi(msg[10].c_str()),atoi(msg[11].c_str())),Point(atoi(msg[12].c_str()),atoi(msg[13].c_str())),cards,bcards,game);

		}
		else if(msg[1].compare("UPDATE")==0)
		{
			UpdateCard(atoi(msg[2].c_str()),Point(atoi(msg[6].c_str()),atoi(msg[7].c_str())),Point(atoi(msg[8].c_str()),atoi(msg[9].c_str())),Point(atoi(msg[10].c_str()),atoi(msg[11].c_str())),Point(atoi(msg[12].c_str()),atoi(msg[13].c_str())),cards,game,atoi(msg[14].c_str()),atoi(msg[15].c_str()));

		}

		Msg.erase(Msg.begin());

	}
	~MainServer(void);


	
	void AddCard(int id,int baseId,Point a,Point b,Point c,Point d,vector<Card>&cards,vector<CardB>&bcards,Game &game)
	{
		int owner = 1;
		if(game.playerIdInMultiplayerMode==1)	owner=2;
		Card t=Card(a,b,c,d,bcards,game,owner,baseId);
		t.id=id;
		Card::ID++;
		cards.push_back(t);
		game.server.SendNewCard(t.id,t.cardBase.id,t.owner,t.a,t.b,t.c,t.d,t.taped);
	}

	void UpdateCard(int id,Point a,Point b,Point c,Point d,vector<Card>&cards,Game &game,int att,int def)
	{
		for(unsigned int i=0;i<cards.size();i++)
		{
			if(cards[i].id==id)
			{

				cards[i].Update(a,b,c,d,att,def,game);
					cards[i].attack=false;
					cards[i].block=false;
				game.server.UpdateCard(cards[i].id,cards[i].cardBase.id,cards[i].owner,a,b,c,d,cards[i].taped,att,def);
			}
		}
	}

	void Attack(int id,Point a,Point b,Point c,Point d,vector<Card>&cards,Game &game,int att,int def)
	{
		for(unsigned int i=0;i<cards.size();i++)
		{
			if(cards[i].id==id)
			{
				cards[i].Update(a,b,c,d,att,def,game);
				cards[i].attack=true;
				game.server.Attack(cards[i].id,cards[i].cardBase.id,cards[i].owner,a,b,c,d,cards[i].taped,att,def);
			}
		}
	}

	void Effect(string effect,int player,int creature,Game &game)
	{
		game.server.VisualEffect(effect,player,creature);		
	}

	void Block(int id,Point a,Point b,Point c,Point d,vector<Card>&cards,Game &game,int att,int def,int blockId)
	{
		for(unsigned int i=0;i<cards.size();i++)
		{
			if(cards[i].id==id)
			{
					cards[i].block=true;
					cards[i].blocking=blockId;
				cards[i].Update(a,b,c,d,att,def,game);
				game.server.Block(cards[i].id,cards[i].cardBase.id,cards[i].owner,a,b,c,d,cards[i].taped,blockId,att,def);
			}
		}
	}
	void Dead(int id,vector<Card>&cards,Game &game)
	{
		for(unsigned int i=0;i<cards.size();i++)
		{
			if(cards[i].id==id)
			{
				cards[i].die();
				game.server.Dead(id);
			}
		}
	}
};

