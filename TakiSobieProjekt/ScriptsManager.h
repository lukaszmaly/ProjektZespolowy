#pragma once
#include "Game.h"
#include "Card.h"
#include "CardB.h"
#include <vector>
#include "aruco.h"
#include "opencv2/core/core.hpp"
#include "marker.h"
#include "cvdrawingutils.h"
#include <cmath>
using namespace aruco;
using namespace std;
class ScriptsManager
{
public:
	Point target;
	bool canUseMarker;
	int targetId;
	MarkerDetector MDetector;


	ScriptsManager(void)
	{
		canUseMarker=true;
		target=Point(-1,-1);
		targetId=-1;
	}

	void DestroyCreature(int id,vector<Card> &cards)
	{
		for(unsigned int i=0;i<cards.size();i++)
		{
			if(cards[i].id==id)
			{
				cards.erase(cards.begin()+i);
				return;
			}
		}
	}
	void AddDamage(int id, int n)
	{

	}

	void SubLife(int id,int n)
	{

	}

	void DrawCard(int id,int n)
	{

	}



	int GetCardId(Point p,vector<Card> &cards,Game &game)
	{
		for(unsigned int i=0;i<cards.size();i++)
		{
			if(game.distance(cards[i].getCenter(),p)<20)
			{
				return cards[i].id;
			}
		}
		return -1;
	}
	void Update(Mat &img,Game &game,vector<Card> &cards,vector<Card> &stos)
	{
					char cad6[100];
	sprintf(cad6,"Aktualna cel:%d (%d,%d)",targetId,target.x,target.y);
	putText(img,cad6, Point(10,100),FONT_HERSHEY_SIMPLEX, 0.5,  Scalar(0,0,255),2);
		MDetector.setMinMaxSize(0.01f);
		vector<Marker> markers;
		MDetector.detect(img,markers);
		for(unsigned int i=0;i<markers.size();i++)
		{
			if(markers[i].id==TARGETMARKER)
			{
				if(canUseMarker==false) {break;}
				target=markers[i].getCenter();
				targetId=GetCardId(target,cards,game);
				if(targetId!=-1)
				{
					canUseMarker=false;
				}
				break;
			}
		}



		if(stos.size()==0) return;




	

		if(targetId==-1) return;
	

		for(unsigned int i=0;i<stos.size();i++)
		{
			if(stos[i].owner.mana>=stos[i].cardBase.koszt)
			{
				stos[i].owner.mana-=stos[i].cardBase.koszt;
				DestroyCreature(targetId,cards);
				canUseMarker=true;
				targetId=-1;
			}
		}
	}



	~ScriptsManager(void);
};

