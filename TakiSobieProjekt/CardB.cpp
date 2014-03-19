#include "CardB.h"


CardB::CardB(void)
{
}


CardB::~CardB(void)
{
}

string CardB::GetTypeToString()
{
	switch(type)
	{
	case CREATURE:
		return "Creature";
		break;
	case ENCHANTMENT:
		return "Enchantment";
		break;
	case SORCERY:
		return "Sorcery";
		break;
	case INSTANT:
		return "Instant";
		break;
	case ARTIFACT:
		return "Artifact";
		break;
	}
}

void CardB::PrintStats()
{
	cout<<"----"<<endl;
	cout<<"Id: "<< id<<endl;
	cout<<"Nazwa: "<<name<<endl;
	cout<<"Typ: "<<GetTypeToString()<<endl;
	if(blueCost>0) cout<<"Blue: "<<blueCost<<endl;
	if(greenCost>0) cout<<"Green: "<<greenCost<<endl;
	if(blackCost>0) cout<<"Black: "<<blackCost<<endl;
	if(whiteCost>0) cout<<"White: "<<whiteCost<<endl;
	if(redCost>0) cout<<"Red: "<<redCost<<endl;
	if(lessCost>0) cout<<"Colorless: "<<lessCost<<endl;
	cout<<"Static abilities: ";
	if(hasHexproof) cout<<"Hexproof";
	if(hasDeatchtuch) cout<<"Deatchtuch";
	if(hasLifelink) cout<<"Lifelink";
	if(hasDefender) cout<<"Defender";
	if(hasFlying) cout<<"Flying";
	if(hasReach) cout<<"Reach";
	if(hasHaste) cout<<"Haste";
	if(hasFirstStrike) cout<<"FirstStrike";
	cout<<endl;
	cout<<"Enter battlefield abb: "<<enterAbilities.size()<<endl;
	cout<<"Upkeep  abb: "<<upkeepAbilities.size()<<endl;
	cout<<"Passive abb: "<<passiveAbilities.size()<<endl;
}


void CardB::Init(int id,string name,int type,int att,int def,int redCost,int whiteCost,int greenCost,int blackCost,int blueCost,int lessCost,bool hasDefender,bool hasLifelink,bool hasDeatchtuch,bool hasHaste,bool hasFlying,bool hasReach,bool hasFirstStrike,bool hasHexproof,vector<pair<int,int>> enterList,vector<pair<int,int>> upkeepList,vector<pair<int,int>> passiveList)
{

	this->id=id;
	this->name=name;

	switch(type)
	{
	case 0:
		this->type=ENCHANTMENT;
		break;
	case 1:
		this->type=INSTANT;
		break;
	case 2:
		this->type=SORCERY;
		break;
	case 3:
		this->type=CREATURE;
		break;
	case 4:
		this->type=LAND;
		break;
	case 5:
		this->type= ARTIFACT;
		break;
	}

	this->att=att;
	this->def=def;
	this->redCost=redCost;
	this->whiteCost=whiteCost;
	this->greenCost=greenCost;
	this->blueCost=blueCost;
	this->blackCost=blackCost;
	this->lessCost=lessCost;
	this->hasHexproof=hasHexproof;
	this->hasHaste= hasHaste;
	this->hasDefender=hasDefender;
	this->hasDeatchtuch=hasDeatchtuch;
	this->hasFlying=hasFlying;
	this->hasReach=hasReach;
	this->hasFirstStrike=hasFirstStrike;
	this->hasLifelink=hasLifelink;
	this->enterAbilities=enterList;
	this->upkeepAbilities=upkeepList;
	this->passiveAbilities=passiveList;
}

CardB::CardB(Mat &img)
{
	this->img = img;
	if(!this->img.data)
	{
		cout<<"File does not exist!"<<endl;
	}
}

