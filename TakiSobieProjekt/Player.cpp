#include "Player.h"


Player::Player(string name,int markerId)
{
	this->markerId=markerId;
	this->name=name;
	angleDiff=0;
	hp=20;
	cardsInLib=60;
	cardsInTomb=0;
	cardsOnHand=7;
	
}



void Player::Init(string name,int markerId)
{
	oldangle=-1;
	agree=false;
	angle=-1;
	this->markerId=markerId;
	this->name=name;
	hp=20;
	cardsInLib=60;
	cardsInTomb=0;
	cardsOnHand=7;

}
void Player::Draw()
{
	img = Scalar(0,0,0);
	char cad[100];
	char cad1[100];
	sprintf(cad,"Gracz: %s",name.c_str());
	sprintf(cad1,"Ilosc zyc: %d",this->hp);

	putText(img,cad, Point(10,10),FONT_HERSHEY_SIMPLEX, 0.5,  Scalar(200,200,200),2);
	putText(img,cad1, Point(10,30),FONT_HERSHEY_SIMPLEX, 0.5,  Scalar(200,200,200),2);

	sprintf(cad1,"Diff: %d",abs(this->angleDiff));
	putText(img,cad1, Point(10,50),FONT_HERSHEY_SIMPLEX, 0.5,  Scalar(200,200,200),2);

	sprintf(cad1,"OldAngle: %d",this->oldangle);
	putText(img,cad1, Point(10,70),FONT_HERSHEY_SIMPLEX, 0.5,  Scalar(200,200,200),2);

	sprintf(cad1,"Biala: %d",this->mana.white);
	putText(img,cad1, Point(10,90),FONT_HERSHEY_SIMPLEX, 0.5,  Scalar(200,200,200),2);

	sprintf(cad1,"Zielona: %d",this->mana.green);
	putText(img,cad1, Point(10,120),FONT_HERSHEY_SIMPLEX, 0.5,  Scalar(200,200,200),2);
	sprintf(cad1,"NExt phase: %s",this->agree ? "true":"false");
	putText(img,cad1, Point(10,150),FONT_HERSHEY_SIMPLEX, 0.5,  Scalar(200,200,200),2);
	imshow(name,img);

}

Player::Player(void)
{
	img =Mat(200,200,CV_64F);
}


Player::~Player(void)
{
}
