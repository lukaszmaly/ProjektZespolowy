#pragma once
#include <iostream>
#include <string>
#include "opencv2/core/core.hpp"
#include "SFML/Network.hpp"
#include <fstream>
#include <string>
#include "settings.h"
using namespace std;
using namespace sf;
using namespace cv;

class Server
{
private:
	int port;
	bool showLog;
	int interval;
	int time;
	UdpSocket soc;
	IpAddress client;
public:
	Server();
	void NextPhase();

	void VisualEffect(string id,int player,int creature);
	void PutPermanentOnLibrary(int id);
		void Scry(int id,int value);
	void Write(const char tab[100]);
	void Markers(int w,int h);
	int GetInterval();
	void SetInterval(int value);
	void AddMana(int id,Color color,int count=1);
	void SubMana(int id,Color color,int count=1);
	void Played(int id);
	void SubMana(int id,int white,int blue,int black,int red,int green);
	void SubLife(int id,int count=1);
	void AddLife(int id,int count=1);
	void Dead(int id);
	void StackColor(int id,State state);
	void DrawCard(int id,int value);
	void Block(int id,int idb,int gracz,Point a, Point b,Point c,Point d,bool taped,int id2,int att,int def);
	~Server();
	void Update();
	void Attack(int id,int idb,int gracz,Point a, Point b,Point c,Point d,bool taped,int att,int def);
	void Cost(int id,int white,int blue,int black,int red,int green);
	void Init(string ip,int port,int interval,bool showLog);
	void SendNewCard(int id,int idb,int gracz,Point a, Point b,Point c,Point d,bool taped);
	void UpdateCard(int id,int idb,int gracz,Point a, Point b,Point c,Point d,bool taped,int att,int def);
	void AddPlayer(int id,const char name[]);
};

