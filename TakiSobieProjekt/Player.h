#pragma once
#include <string>
#include "opencv2\imgproc\imgproc.hpp"
#include "opencv2/core/core.hpp"
#include "opencv2/features2d/features2d.hpp"
#include "opencv2/highgui/highgui.hpp"
#include "opencv2/nonfree/nonfree.hpp"
#include <iostream>
#include <vector>
using namespace std;
using namespace cv;
class Player
{
public:
	Player(void);
	void Init(string name,int markerId);
	Player(string name,int markerId);
	~Player(void);
	Player &getCurrentPlayer();
	int hp;
	string name;
	int mana;
	int markerId;
	int cardsOnHand;
	int cardsInLib;
	int cardsInTomb;
	bool active;
	void Update();
	void Draw();

	  int operator==(const Player &p) {return  markerId==p.markerId;}
};

