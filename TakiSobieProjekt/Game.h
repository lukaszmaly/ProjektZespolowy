#pragma once
#include "Player.h"
#include "Server.h"
#include <string>
#include "aruco.h"
#include "opencv2/imgproc/imgproc.hpp"
#include "opencv2/core/core.hpp"
#include "cvdrawingutils.h"
#include "settings.h"
#include <fstream>
using namespace std;
using namespace cv;
using namespace aruco;
#define ACTION 428
#define TARGETMARKER 985
#define M_PI 3.14159265358979323846
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
	State stackState;
	int gameWidth;
	int gameHeight;
	bool zmiana;
	Point action;
	bool checkCardsProp;
	bool bgrMode;
	bool targetMode;
public:
	Mat diff;
	void ChangeStackState(int id,State state);
	int lastId;
	static int Distance(Point a,Point b)
	{
		return std::sqrtf(((b.x-a.x)*(b.x-a.x)+(b.y-a.y)*(b.y-a.y)));
	}
	
	bool IsTargetMode();
	void SetTargetMode(bool value);
	Point firsCardPoint;
	int firstCardWidth;
	int firstCardHeight;
	bool firstCardChecked;
		bool showCardArea;
	bool IsBgrMode();
	bool CheckCardsProp();
Point target;
bool beAbleMarker;
float targetAngle;
float targetOldAngle;
	void StopMarker();
	void MakeDiffImage(Mat &img1,Point a,Point b,Point c,Point d);
		static float getangle( cv::Point pt1, cv::Point pt2, cv::Point pt0 ) {
	return atan2f( (pt1.y - pt2.y ),( pt1.x - pt2.x ) ) * 180 / M_PI + 180;
	}
	bool oneAttack;

	Point a,b,c,d;
	bool t;
	Player & GetPlayer(int id);
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
	int GetCurrentPlayer();
	Player &getCurrentPlayer();
	Game(string player1s,int player1Id,string player2s,int player2Id,string ip,int port,int w,int h,int interval,bool showLog);
	~Game();


	void Clear();
	void AddMana(int id,Color color);
	void SubMana(int id,Color color);
	void Pay(int id,int white, int blue,int black,int red,int green,int colorless);
	bool CanPay(int id,int white, int blue,int black,int red,int green,int colorless);
	void SubLife(int id,int value);
	void AddLife(int id,int value);
	bool IsMana(int id,Color color);

	string lastString;
	void GHDefense(int attacker,int defencer)
	{
		string log = "DEFENSE "+to_string((_Longlong)attacker) + " " + to_string((_Longlong)attacker);
		Write(log);
	}
	void GHPlay(int owner,int id,string name,int targetCreature,int targetPlayer)
	{
		string log = "PLAY "+to_string((_Longlong)owner) + " " +to_string((_Longlong)id) +" "+ name +" " +to_string((_Longlong)targetCreature)+ " " +to_string((_Longlong)targetPlayer);
		Write(log);
	}
	void GHAtack(int id)
	{
			string log = "ATTACK "+to_string((_Longlong)id);
		Write(log);
	}
	void GHTap(int id)
	{
			string log = "TAP "+to_string((_Longlong)id);
		Write(log);
	}
	void GHUntap(int id)
	{
			string log = "UNTAP "+to_string((_Longlong)id);
		Write(log);
	}
	void GHSubLife(int owner,int value)
	{
		string log = "SUBLIFE "+to_string((_Longlong)owner)+" "+to_string((_Longlong)value);
		Write(log);
	
	}
	void GHAddLife(int owner,int value)
	{
		string log = "ADDLIFE "+to_string((_Longlong)owner)+" "+to_string((_Longlong)value);
		Write(log);
	
	}
	void GHNewGame(string name1,string name2)
	{
		string log = "NEWGAME "+ name1 + " "+ name2;

			Write(log);

	}
	void GHDie(int id)
	{
	string log = "DIE "+to_string((_Longlong)id);
		Write(log);
	

	}
	void Write(string log)
	{
				fstream plik;
	plik.open( "log1.txt", std::ios::in | std::ios::out | std::ios::app);
	plik << log<<endl;
	plik.close();
	}

};
