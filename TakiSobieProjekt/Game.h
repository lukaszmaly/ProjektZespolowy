#pragma once
#include "Player.h"
#include "Server.h"
#include <string>
#include "aruco.h"
#include "opencv2/imgproc/imgproc.hpp"
#include "opencv2/core/core.hpp"
#include "cvdrawingutils.h"
using namespace std;
using namespace cv;
using namespace aruco;
#define ACTION 428
enum Phase {
	PIERWSZY = 0,
	ATAK = 1,
	OBRONA = 2,
	WYMIANA = 3,
	DRUGI = 4,
	UPKEEP = 5
};

class Game
{
private:
	int aPlayer;
	Phase phase;
	int gameWidth;
	int gameHeight;
	bool zmiana;
	Point action;
	
public:
	bool oneAttack;
	int GetPlayer(Player &player);
	Point a,b,c,d;
	bool t;
	Player player1;
	Player player2;
	Phase GetPhase();
	Server server;
	vector<Marker> markers;
	int GetGameWidth();
	int GetGameHeight();
	int distance(Point a,Point b);
	void CheckMarkers(Mat &img);
	void setFaza(int i);
	void setPlayer(int i);
	string getCurrentPhase();
	void nextPhase();
	void Update();
	void Draw();
	Player &getCurrentPlayer();
	Game(string player1s,int player1Id,string player2s,int player2Id,string ip,int port,int w,int h,int interval,bool showLog);
	~Game();
};
