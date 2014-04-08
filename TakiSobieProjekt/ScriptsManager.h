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
	int targetPlayerId;
	MarkerDetector MDetector;

	ScriptsManager(void)
	{
		canUseMarker=true;
		target=Point(-1,-1);
		targetId=-1;
		targetPlayerId=-1;
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
		game.SubLife(id,n);
	}
	void AddLife(int id,int n,Game &game)
	{
		game.AddLife(id,n);
	}
	void DrawCard(int id,int n,Game &game)
	{
		game.server.DrawCard(id,n);
	}
	void ScryCard(int id,int n,Game &game)
	{
		game.server.Scry(id,n);
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
		if(p.y<350) targetPlayerId=1;
		else targetPlayerId=2;
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

		for(unsigned int i=0;i<stos.size();i++)
		{
			if((stos[i].cardBase.type==INSTANT || stos[i].cardBase.type==ENCHANTMENT || stos[i].cardBase.type==SORCERY) && stos[i].cardBase.id!=game.lastId 
				&& game.CanPay(stos[i].owner,stos[i].cardBase.whiteCost,stos[i].cardBase.blueCost,stos[i].cardBase.blackCost,stos[i].cardBase.redCost,stos[i].cardBase.greenCost,stos[i].cardBase.lessCost))
			{

				game.Pay(stos[i].owner,stos[i].cardBase.whiteCost,stos[i].cardBase.blueCost,stos[i].cardBase.blackCost,stos[i].cardBase.redCost,stos[i].cardBase.greenCost,stos[i].cardBase.lessCost);
				vector<pair<int,int>> t =stos[i].cardBase.enterAbilities;
				for(int j=0;j<t.size();j++)
				{
					game.stack.push_back(Spell(stos[i].cardBase.id,t[j].first,stos[i].owner,targetId,targetId,t[j].second));
				}

				game.lastId=stos[i].cardBase.id;
				canUseMarker=true;
				targetId=-1;
			}
		}

		for(unsigned int i=0;i<game.stack.size();i++)
		{
			Resolve(game.stack[i],cards,game);
			game.stack.erase(game.stack.begin()+i);
			i=-1;
		}
	}

	void Resolve(Spell s,vector<Card> &cards,Game &game)
	{
		switch(s.baseId)
		{
		case 0:
			AddLife(s.targetPlayer,s.value,game);
			break;
		case 1:
			SubLife(s.targetPlayer,s.value,game);
			break;
		case 2:
			AddDamage(s.targetCreature,cards,s.value);
			break;
		case 3:
			SubLife(s.targetPlayer,s.value,game);
			break;
		case 4:
			AddEOT(s.targetCreature,cards,s.value,s.value);
			break;

		case 5:
			AddEOT(s.targetCreature,cards,(-1)*s.value,(-1)*s.value);
			break;
		case 6:
			DestroyCreature(s.targetCreature,cards);
			break;
		case 7:
			AddEOT(s.targetCreature,cards,s.value,0);
			break;
		case 8:
			AddEOT(s.targetCreature,cards,0,s.value);
			break;
		case 9:
			AddLifelink(s.targetCreature,cards,true);
			break;
		case 10:
			AddLifelink(s.targetCreature,cards,false);
			break;
		case 11:
			AddFirstStrike(s.targetCreature,cards,true);
			break;
		case 12:
			AddFirstStrike(s.targetCreature,cards,false);
			break;
		case 13:
			AddFlying(s.targetCreature,cards,true);
			break;
		case 14:
			AddFlying(s.targetCreature,cards,false);
			break;
		case 15:
			AddTrample(s.targetCreature,cards,true);
			break;
		case 16:
			AddTrample(s.targetCreature,cards,false);
			break;
		case 17:
			AddHexproof(s.targetCreature,cards,true);
			break;
		case 18:
			AddHexproof(s.targetCreature,cards,false);
			break;
		case 19:
			PutCounter(s.targetCreature,cards,s.value,s.value);
			break;
		case 20:
			PutCounter(s.targetCreature,cards,s.value,s.value);
			break;
		case 21:
			DrawCard(s.owner,s.value,game);
			break;
		case 22:
			CantAttack(s.targetCreature,cards);
			break;
		case 23:
			CantBlock(s.targetCreature,cards);
			break;
		case 24:
			AddLife(s.owner,s.value,game);
			break;
		case 25:
			SubLife(s.owner,s.value,game);
			break;
		case 26:
			GiveDrawACardWhenAttack(s.targetCreature,cards);
			break;
		case 27:
			GivePutCounterWhenAttack(s.targetCreature,cards);
			break;
		case 28:
			TapCard(s.targetCreature,cards,true);
			break;
		case 29:
			TapCard(s.targetCreature,cards,false);
			break;
		case 30:
			ScryCard(s.owner,s.value,game);
			break;
		}
	}

	void TapCard(int id,vector<Card>&cards,bool permanent)
	{
		for(unsigned int i=0;i<cards.size();i++)
		{
			if(cards[i].id==id)
			{
				if(permanent)	cards[i].hasCantUntap=true;
				else cards[i].canUntap=false;
			}
		}

	}
	void GivePutCounterWhenAttack(int id,vector<Card>&cards)
	{

	}

	void GiveDrawACardWhenAttack(int id,vector<Card>&cards)
	{

	}

	void CantBlock(int id,vector<Card>&cards)
	{
		for(unsigned int i=0;i<cards.size();i++)
		{
			if(cards[i].id==id)
			{
				cards[i].hasCantBlock=true;
			}
		}
	}
	void CantAttack(int id,vector<Card>&cards)
	{
		for(unsigned int i=0;i<cards.size();i++)
		{
			if(cards[i].id==id)
			{
				cards[i].hasCantAttack=true;
			}
		}
	}
	void PutCounter(int id,vector<Card>&cards,int att,int def)
	{
		for(unsigned int i=0;i<cards.size();i++)
		{
			if(cards[i].id==id)
			{
				cards[i].Add(att,def);
			}
		}
	}
	void AddHexproof(int id,vector<Card>&cards,bool permanent)
	{
		for(unsigned int i=0;i<cards.size();i++)
		{
			if(cards[i].id==id)
			{
				if(permanent) cards[i].hasHexproof=true;
				else	cards[i].hasHexproofEOT=true;
			}
		}
	}
	void AddTrample(int id,vector<Card>&cards,bool permanent)
	{
		for(unsigned int i=0;i<cards.size();i++)
		{
			if(cards[i].id==id)
			{
				if(permanent) cards[i].hasTrample=true;
				else	cards[i].hasTrampleEOT=true;
			}
		}
	}
	void AddFlying(int id,vector<Card>&cards,bool permanent)
	{
		for(unsigned int i=0;i<cards.size();i++)
		{
			if(cards[i].id==id)
			{
				if(permanent) cards[i].hasFlying=true;
				else cards[i].hasFlyingEOT=true;
			}
		}
	}
	void AddLifelink(int id,vector<Card>&cards,bool permanent)
	{
		for(unsigned int i=0;i<cards.size();i++)
		{
			if(cards[i].id==id)
			{
				if(permanent) cards[i].hasLifelink=true;
				else	cards[i].hasLifelinkEOT=true;
			}
		}
	}
	void AddFirstStrike(int id,vector<Card>&cards,bool permanent)
	{
		for(unsigned int i=0;i<cards.size();i++)
		{
			if(cards[i].id==id)
			{
				if(permanent) cards[i].hasFirstStrike=true;
				else	cards[i].hasFirstStrikeEOT=true;
			}
		}
	}
	~ScriptsManager(void);
};

