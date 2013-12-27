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
	INSTANT = 1,
	SORCERY = 2,
	ENCHANTMENT = 3,
	LAND = 4
};

class CardB
{
public:
	int id;
	string name;
	Color color;
	Mat img;
	Type type;
	CardB(Mat &img1,int id,string name,Color color,Type t);
	void Update();
	CardB(void);
	~CardB(void);
};

