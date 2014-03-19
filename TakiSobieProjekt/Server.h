#pragma once
#include <iostream>
#include <string>
#include "opencv2/core/core.hpp"
#include "SFML/Network.hpp"
#include <fstream>
using namespace std;
using namespace sf;
using namespace cv;

class Server
{
private:
	int port;
	int ile;
	bool showLog;
	int interval;
	int time;
	UdpSocket soc;
	IpAddress client;
public:
	Server();
	void NextPhase();
	void Write(const char tab[100]);
	void Markers(int w,int h);
	int GetInterval();
	void AddMana(int id,int count=1);
		void SubMana(int id,int count=1);
		void SubLife(int id,int count=1);
	void Dead(int id);
	void Block(int id,int idb,int gracz,Point a, Point b,Point c,Point d,bool taped,int id2);
	~Server();
	void Update();
	void Attack(int id,int idb,int gracz,Point a, Point b,Point c,Point d,bool taped);
	void Cost(int owner,int cost);
	void Init(string ip,int port,int interval,bool showLog);
	void SendNewCard(int id,int idb,int gracz,Point a, Point b,Point c,Point d,bool taped);
	void UpdateCard(int id,int idb,int gracz,Point a, Point b,Point c,Point d,bool taped);
	void AddPlayer(int id,const char name[]);
};

