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
#include "CardB.h"
#include "Game.h"
#include "Player.h"
#define MIN_D1 1.37
#define MAX_D1 1.45
#define M_PI 3.14159265358979323846
#define MIN_D2 0.67
#define MAX_D2 0.73
using namespace std;
using namespace cv;
class Card
{
public:
	bool wantFight;
	void Fight(Card &op);
	CardB cardBase;
	static int ID;
	int id;
	bool dead;
	Point old;
	bool ready;
	int att;
	bool error;
	bool nowa;
	int def;
	int topIndex;
	Player owner;
	int cardId;
	int count;
	int timer;
	bool taped;
	string name;
	void prepareToBlock();
	Point a,b,c,d;
	Mat img;
	float angle;
	static const int TTL=10;
	int ttl;
	void die();
	bool fresh;
	void Clear();
	bool attack;
	Point enemy;
	bool block;
	void setCardBase(CardB &card);
	void Compare(Mat &img1,Mat &img2,float tab[3]);
	Card(Point a, Point b, Point c,Point d,Mat &img,vector<CardB>& bkarty,Game &game,bool temp);
	Card(void);
	void prepareToAttack();
	float getAngle();
	int maxC(int a,int b,int c);
	int minC(int a,int b,int c);
	string Wynik(int b,int g,int r,int h, int s,int v);
	void Draw(Mat &img1,vector<CardB>&bkarty,Game &game);
	void Update(Point a,Point b,Point c,Point d,Mat &img,vector<CardB>& bkarty,Game &game,bool temp);
	Point2f getCenter();
	static void fastImg(string name,float s)
	{
		Mat img(200,200,CV_64F);
		char cad[100];
		sprintf(cad,"%f",s);
		putText(img,cad, Point(10,10),FONT_HERSHEY_SIMPLEX, 0.5,  Scalar(200,200,200),2);
		imshow(name,img);

	}

	static float getangle( cv::Point pt1, cv::Point pt2, cv::Point pt0 ) {
		float dx1 = pt1.x - pt0.x;
		float dy1 = pt1.y - pt0.y;
		float dx2 = pt2.x - pt0.x;
		float dy2 = pt2.y - pt0.y;
		return (dx1*dx2 + dy1*dy2)/sqrt((dx1*dx1 + dy1*dy1)*(dx2*dx2 + dy2*dy2));
	}

	static Point2f getCenter(Point a,Point b,Point c,Point d);

	bool Check(Card k);
	void Tap(Game &game);
	void Untap(Game &game);
	int MinusCount(int a,int b,int c);
	static void Prepare(vector<Point> &square,Mat &img);
	static bool Valid(Point a,Point b,Point c,Point d)
	{
		float a1=Distance(a,b)/Distance(b,c);
		float a2=Distance(b,c)/Distance(c,d);
		if(((a1>=MIN_D1 && a1<=MAX_D1) || (a1>=MIN_D2 && a1<=MAX_D2)) && ((a2>=MIN_D1 && a2<=MAX_D1) || (a2>=MIN_D2 && a2<=MAX_D2)))
		{
			return true;
		}
		return false;


	}
	static float Distance(Point a,Point b)
	{
		return std::sqrtf(((b.x-a.x)*(b.x-a.x)+(b.y-a.y)*(b.y-a.y)));
	}
	~Card(void);
};

