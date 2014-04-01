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
	int lastId;

	ScriptsManager(void)
	{
		canUseMarker=true;
		target=Point(-1,-1);
		targetId=-1;
		lastId=-1;
	}
	void AddEOT(int id,vector<Card> &cards,int attack,int defense)
	{
		for(unsigned int i=0;i<cards.size();i++)
		{
			if(cards[i].id==id)
			{
				cards[i].AddEOT(attack,defense);
				return;
			}
		}
	}


	

	void DestroyCreature(int id,vector<Card> &cards)
	{
		for(unsigned int i=0;i<cards.size();i++)
		{
			if(cards[i].id==id)
			{
				cards[i].die();
				return;
			}
		}
	}
	void AddDamage(int id,vector<Card> &cards,int value)
	{
		for(unsigned int i=0;i<cards.size();i++)
		{
			if(cards[i].id==id)
			{
				cards[i].Damage(value);
				return;
			}
		}
	}

	void SubLife(int id,int n,Game &game)
	{
		
	}

	void DrawCard(int id,int n,Game &game)
	{

	}

	void ScryCard(int id,int n,Game &game)
	{

	}

	void PutPermanentOnDeck(int id,vector<Card> &cards,Game &game)
	{
		for(unsigned int i=0;i<cards.size();i++)
		{
			if(cards[i].id==id)
			{
				game.server.PutPermanentOnLibrary(id);
				cards.erase(cards.begin()+i);
				return;
			}
		}
	}

	int GetCardId(Point p,vector<Card> &cards,Game &game)
	{
		int index =-1;
		int distance = 10000000;
		for(unsigned int i=0;i<cards.size();i++)
		{
			int d = game.distance(cards[i].getCenter(),p);
			if(d<distance && !cards[i].cardBase.hasHexproof)
			{
				distance = d;
				index = cards[i].id;
			}
				
			}
		
		return index;
	}

	void Upkeep(Game &game,vector<Card> &cards)
	{

		for(unsigned int i=0;i<cards.size();i++)
		{
			for(unsigned int j =0; j<cards[i].cardBase.upkeepAbilities.size();j++)
			{
				//triggery
			}
		}


	}

	void Update(Mat &img,Game &game,vector<Card> &cards,vector<Card> &stos)
	{
		char cad6[100];
		sprintf(cad6,"Aktualna cel:%d (%d,%d)",targetId,target.x,target.y);
		putText(img,cad6, Point(10,100),FONT_HERSHEY_SIMPLEX, 0.5,  Scalar(0,0,255),2);
		sprintf(cad6,"Aktualna faza %s",game.getCurrentPhase().c_str());
		putText(img,cad6, Point(10,130),FONT_HERSHEY_SIMPLEX, 0.5,  Scalar(0,0,255),2);

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






		for(unsigned int i=0;i<stos.size();i++)
		{
			if(stos[i].cardBase.type==INSTANT && stos[i].cardBase.id!=lastId 
				&& stos[i].owner.mana.Pay(stos[i].cardBase.whiteCost,stos[i].cardBase.blueCost,stos[i].cardBase.blackCost,stos[i].cardBase.redCost,stos[i].cardBase.greenCost,stos[i].cardBase.lessCost))
			{
				if(Resolve(stos[i],cards,game)==true)
				{
					lastId=stos[i].cardBase.id;
				}
				canUseMarker=true;
				targetId=-1;
			}
		}
	}

	bool Resolve(Card &card,vector<Card> &cards,Game &game)
	{
		//sprawdz czy karta bêdzie targetowa³a i uzale¿nij od tego dalsz¹ czêœæ programu
		vector<pair<int,int>> ab = card.cardBase.enterAbilities;
		if((ab.size())!=0)
		{
			for(int i=0;i<ab.size();i++)
			{
				if(ab[i].first==2 && targetId!=-1) 
				{
					
					AddDamage(targetId,cards,ab[i].second);
					game.server.VisualEffect("BOLT",-1,targetId);
					return true;
				}
				else if(ab[i].first==4 && targetId!=-1) 
				{
					AddEOT(targetId,cards,ab[i].second,ab[i].second);
					return true;
				}
				else if(ab[i].first==6  && targetId!=-1)
				{
					DestroyCreature(targetId,cards);
					return true;
				}
				else
				{
					return false;
				}
			}
		}


		return false;

	}

	~ScriptsManager(void);
};

