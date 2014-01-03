#include "CardB.h"


CardB::CardB(void)
{
}


CardB::~CardB(void)
{
}



CardB::CardB(Mat &img,int id,string name,Color color,Type t,int att,int def,int koszt)
{
	this->att=att;
	this->def=def;
	this->koszt=koszt;
	this->type=t;
	this->color = color;
	this->id = id;
	this->name = name;
	this->img = img;
	if(!img.data)
	{
		cout<<"Blad podczas ³adowania pliku: "<<endl;
	}
}
void CardB::Update()
{

	Mat img = imread(name);
	if(!img.data)
	{
		cout<<"Blad podczas ³adowania pliku: "<<name<<endl;
	}
}

