#pragma once
#include "opencv2\imgproc\imgproc.hpp"
#include "opencv2/core/core.hpp"
#include "opencv2/features2d/features2d.hpp"
#include "opencv2/highgui/highgui.hpp"
#include "opencv2/nonfree/nonfree.hpp"
#include <iostream>
#include <vector>
using namespace std;
using namespace cv;
enum Color{
	Red = 0,
	Black = 1,
	Land = 2
};
enum Type{
	CREATURE = 0,
	LAND = 1,
	INSTANT = 2,
};

class CardB
{
public:
	int id;
	string name;
	Color color;
	Mat img;
	int att,def;
	int koszt;
	Type type;
	int ableToTarget;
	CardB(Mat &img1,int id,string name,Color color,Type t,int att,int def,int koszt,int ableToTarget=0);
	void Update();
	void OnUntap();

	CardB(void);
	~CardB(void);
};

