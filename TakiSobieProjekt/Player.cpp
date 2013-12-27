#include "Player.h"


Player::Player(string name)
{
	this->name=name;
	hp=20;
	cardsInLib=60;
	cardsInTomb=0;
	cardsOnHand=7;
	mana=0;
}

void Player::Draw()
{
	Mat img(200,200,CV_64F);
	char cad[100];
	char cad1[100];
	sprintf(cad,"Gracz: %s",name.c_str());
	sprintf(cad1,"Ilosc zyc: %d",this->hp);

	putText(img,cad, Point(10,10),FONT_HERSHEY_SIMPLEX, 0.5,  Scalar(200,200,200),2);
	putText(img,cad1, Point(10,30),FONT_HERSHEY_SIMPLEX, 0.5,  Scalar(200,200,200),2);

	sprintf(cad1,"Karty(dlon): %d",this->cardsOnHand);
	putText(img,cad1, Point(10,50),FONT_HERSHEY_SIMPLEX, 0.5,  Scalar(200,200,200),2);

	sprintf(cad1,"Karty(cmentarz): %d",this->cardsInTomb);
	putText(img,cad1, Point(10,70),FONT_HERSHEY_SIMPLEX, 0.5,  Scalar(200,200,200),2);

	sprintf(cad1,"Karty(biblioteka): %d",this->cardsInLib);
	putText(img,cad1, Point(10,90),FONT_HERSHEY_SIMPLEX, 0.5,  Scalar(200,200,200),2);

	sprintf(cad1,"Mana: %d",this->mana);
	putText(img,cad1, Point(10,120),FONT_HERSHEY_SIMPLEX, 0.5,  Scalar(200,200,200),2);

	imshow(name,img);

}

Player::Player(void)
{
}


Player::~Player(void)
{
}
